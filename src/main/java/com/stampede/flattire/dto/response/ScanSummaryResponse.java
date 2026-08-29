package com.stampede.flattire.dto.response;

public record ScanSummaryResponse(
        int totalDependencies,
        int vulnerableDependencies,
        int knownCves,
        String highestSeverity
) {
}