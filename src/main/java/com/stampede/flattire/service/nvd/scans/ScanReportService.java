package com.stampede.flattire.service.nvd.scans;

import com.stampede.flattire.dto.response.ScanStatusResponse;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

@Service
public class ScanReportService {

    private static final Path REPORT_PATH = Path.of("target", "dependency-check-report.json");

    public ScanStatusResponse getStatus() {
        boolean exists = Files.exists(REPORT_PATH);

        if (!exists) {
            return new ScanStatusResponse(
                    false,
                    REPORT_PATH.toString(),
                    null
            );
        }

        try {
            Instant lastModified =
                    Files.getLastModifiedTime(REPORT_PATH).toInstant();
            return new ScanStatusResponse(
                    true,
                    REPORT_PATH.toString(),
                    lastModified
            );
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Unable to read scan report metadata", e
            );
        }
    }
}