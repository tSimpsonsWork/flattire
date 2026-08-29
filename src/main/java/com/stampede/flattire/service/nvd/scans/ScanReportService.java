package com.stampede.flattire.service.nvd.scans;

import com.stampede.flattire.dto.response.ScanStatusResponse;
import com.stampede.flattire.dto.response.ScanSummaryResponse;
import com.stampede.flattire.dto.response.VulnerabilityDetailResponse;
import com.stampede.flattire.dto.response.VulnerableDependencyResponse;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@Service
public class ScanReportService {

    private static final Path REPORT_PATH =
            Path.of("target", "dependency-check-report.json");

    private final ObjectMapper objectMapper;

    public ScanReportService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    public ScanStatusResponse getStatus() {

        if (!Files.exists(REPORT_PATH)) {

            return new ScanStatusResponse(
                    false,
                    REPORT_PATH.toString(),
                    null
            );
        }

        try {

            Instant lastModified =
                    Files.getLastModifiedTime(REPORT_PATH)
                            .toInstant();

            return new ScanStatusResponse(
                    true,
                    REPORT_PATH.toString(),
                    lastModified
            );

        } catch (IOException e) {

            throw new IllegalStateException(
                    "Unable to read scan report metadata",
                    e
            );
        }
    }


    public ScanSummaryResponse getSummary() {

        if (!Files.exists(REPORT_PATH)) {
            return emptySummary();
        }

        try {

            JsonNode report =
                    readReport();

            List<JsonNode> dependencies =
                    getDependencies(report);

            List<JsonNode> vulnerableDependencies =
                    dependencies.stream()
                            .filter(this::hasVulnerabilities)
                            .toList();

            List<JsonNode> vulnerabilities =
                    vulnerableDependencies.stream()
                            .flatMap(this::vulnerabilityStream)
                            .filter(this::isValidVulnerability)
                            .toList();

            double highestCvss =
                    vulnerabilities.stream()
                            .mapToDouble(this::extractCvssScore)
                            .filter(score -> score > 0.0)
                            .max()
                            .orElse(0.0);

            return new ScanSummaryResponse(
                    dependencies.size(),
                    vulnerableDependencies.size(),
                    vulnerabilities.size(),
                    severityFromScore(highestCvss)
            );

        } catch (Exception e) {

            throw new IllegalStateException(
                    "Unable to parse Dependency-Check report",
                    e
            );
        }
    }


    public List<VulnerableDependencyResponse> getVulnerableDependencies() {

        if (!Files.exists(REPORT_PATH)) {
            return List.of();
        }

        try {

            JsonNode report =
                    readReport();

            return getDependencies(report)
                    .stream()
                    .filter(this::hasVulnerabilities)
                    .map(this::mapVulnerableDependency)
                    .sorted(
                            java.util.Comparator.comparingDouble(
                                    VulnerableDependencyResponse::highestCvssScore
                            ).reversed()
                    )
                    .toList();

        } catch (Exception e) {

            throw new IllegalStateException(
                    "Unable to parse vulnerable dependencies",
                    e
            );
        }
    }


    private VulnerableDependencyResponse mapVulnerableDependency(
            JsonNode dependency
    ) {

        List<VulnerabilityDetailResponse> vulnerabilities =
                vulnerabilityStream(dependency)
                        .filter(this::isValidVulnerability)
                        .map(this::mapVulnerability)
                        .sorted(
                                java.util.Comparator.comparingDouble(
                                        VulnerabilityDetailResponse::cvssScore
                                ).reversed()
                        )
                        .toList();

        double highestCvss =
                vulnerabilities.stream()
                        .mapToDouble(
                                VulnerabilityDetailResponse::cvssScore
                        )
                        .max()
                        .orElse(0.0);

        return new VulnerableDependencyResponse(
                extractDependencyName(dependency),
                vulnerabilities.size(),
                severityFromScore(highestCvss),
                highestCvss,
                vulnerabilities
        );
    }


    private VulnerabilityDetailResponse mapVulnerability(
            JsonNode vulnerability
    ) {

        double score =
                extractCvssScore(vulnerability);

        return new VulnerabilityDetailResponse(
                vulnerability
                        .path("name")
                        .asText("Unknown vulnerability"),

                severityFromScore(score),

                score,

                vulnerability
                        .path("description")
                        .asText("No description available")
        );
    }


    private String extractDependencyName(
            JsonNode dependency
    ) {

        return Stream.of(
                        dependency.path("fileName"),
                        dependency.path("filePath")
                )
                .filter(node ->
                        !node.isMissingNode()
                )
                .filter(node ->
                        !node.isNull()
                )
                .map(JsonNode::asText)
                .filter(value ->
                        !value.isBlank()
                )
                .findFirst()
                .orElse("Unknown dependency");
    }


    private JsonNode readReport() {

        return objectMapper.readTree(
                REPORT_PATH.toFile()
        );
    }


    private List<JsonNode> getDependencies(
            JsonNode report
    ) {

        return report
                .path("dependencies")
                .valueStream()
                .toList();
    }


    private boolean hasVulnerabilities(
            JsonNode dependency
    ) {

        JsonNode vulnerabilities =
                dependency.path("vulnerabilities");

        return vulnerabilities.isArray()
                && !vulnerabilities.isEmpty();
    }


    private Stream<JsonNode> vulnerabilityStream(
            JsonNode dependency
    ) {

        return dependency
                .path("vulnerabilities")
                .valueStream();
    }


    private boolean isValidVulnerability(
            JsonNode vulnerability
    ) {

        return !vulnerability.isMissingNode()
                && !vulnerability.isNull();
    }


    private double extractCvssScore(
            JsonNode vulnerability
    ) {

        return List.<Function<JsonNode, Optional<Double>>>of(

                        node -> extractScore(
                                node,
                                "cvssv4",
                                "baseScore"
                        ),

                        node -> extractScore(
                                node,
                                "cvssv3",
                                "baseScore"
                        ),

                        node -> extractScore(
                                node,
                                "cvssv2",
                                "score"
                        )
                )
                .stream()
                .map(extractor ->
                        extractor.apply(vulnerability)
                )
                .flatMap(Optional::stream)
                .findFirst()
                .orElse(0.0);
    }


    private Optional<Double> extractScore(
            JsonNode vulnerability,
            String cvssNodeName,
            String scoreField
    ) {

        return Optional.of(
                        vulnerability.path(cvssNodeName)
                )
                .filter(node ->
                        !node.isMissingNode()
                )
                .filter(node ->
                        !node.isNull()
                )
                .map(node ->
                        node.path(scoreField)
                )
                .filter(node ->
                        !node.isMissingNode()
                )
                .filter(node ->
                        !node.isNull()
                )
                .map(JsonNode::asDouble);
    }


    private String severityFromScore(
            double score
    ) {

        return Map.of(
                        9.0, "CRITICAL",
                        7.0, "HIGH",
                        4.0, "MEDIUM",
                        0.1, "LOW"
                )
                .entrySet()
                .stream()
                .filter(entry ->
                        score >= entry.getKey()
                )
                .max(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .orElse("NONE");
    }


    private ScanSummaryResponse emptySummary() {

        return new ScanSummaryResponse(
                0,
                0,
                0,
                "NONE"
        );
    }
}