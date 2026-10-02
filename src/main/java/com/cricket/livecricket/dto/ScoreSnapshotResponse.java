
package com.cricket.livecricket.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ScoreSnapshotResponse(
        Long id,
        Integer inningsNumber,
        String battingTeam,
        Integer runs,
        Integer wickets,
        String overs,
        BigDecimal runRate,
        Integer target,
        LocalDateTime fetchedAt
) {
}