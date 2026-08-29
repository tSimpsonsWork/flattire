package com.stampede.flattire.dto.results;

public record MavenSearchResult(
        String groupId,
        String artifactId,
        String latestVersion
) {
}
