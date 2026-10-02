
package com.cricket.livecricket.dto;

import java.time.LocalDateTime;

public record MatchResponse(
        Long id,
        String externalMatchId,
        String matchTitle,
        String matchType,
        String status,
        String venue,
        String matchDate,
        String team1Name,
        String team2Name,
        String team1Score,
        String team2Score,
        LocalDateTime lastUpdatedAt
) {
}