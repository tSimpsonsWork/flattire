package com.stampede.flattire.dto.response;

import java.time.Instant;

public record ScanStatusResponse(
        boolean reportAvailable,
        String reportPath,
        Instant lastModified
) {
}