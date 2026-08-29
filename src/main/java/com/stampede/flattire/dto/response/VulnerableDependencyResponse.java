package com.stampede.flattire.dto.response;

import java.util.List;

public record VulnerableDependencyResponse(
        String dependency,
        int vulnerabilityCount,
        String highestSeverity,
        double highestCvssScore,
        List<VulnerabilityDetailResponse> vulnerabilities
) {
}