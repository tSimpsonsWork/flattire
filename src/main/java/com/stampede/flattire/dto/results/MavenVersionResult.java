package com.stampede.flattire.dto.results;

public record MavenVersionResult(
        String groupId,
        String artifactId,
        String version
) {

}