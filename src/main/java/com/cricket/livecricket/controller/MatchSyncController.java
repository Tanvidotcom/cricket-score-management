
package com.cricket.livecricket.controller;

import com.cricket.livecricket.service.MatchSyncService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/matches")
public class MatchSyncController {

    private final MatchSyncService matchSyncService;

    public MatchSyncController(MatchSyncService matchSyncService) {
        this.matchSyncService = matchSyncService;
    }

    @PostMapping("/sync")
    public Map<String, Object> syncMatches() {

        int count = matchSyncService.syncMatches();

        return Map.of(
                "message", "Match synchronization completed",
                "matchesProcessed", count
        );
    }
}