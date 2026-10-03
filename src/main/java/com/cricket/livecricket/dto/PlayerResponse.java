
package com.cricket.livecricket.dto;

public record PlayerResponse(
        Long id,
        String name,
        String role,
        String battingStyle,
        String bowlingStyle,
        Integer jerseyNumber,
        Long teamId,
        String teamName
) {
}