
package com.cricket.livecricket.controller;

import com.cricket.livecricket.dto.MatchResponse;
import com.cricket.livecricket.entity.Match;
import com.cricket.livecricket.repository.MatchRepository;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@CrossOrigin(origins = "http://localhost:5173")
public class MatchController {

    private final MatchRepository matchRepository;

    public MatchController(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @GetMapping
    public List<MatchResponse> getAllMatches() {
        return matchRepository.findAllByOrderByLastUpdatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/live")
public List<MatchResponse> getLiveMatches() {
    return matchRepository
            .findByStatusIgnoreCaseOrderByLastUpdatedAtDesc("LIVE")
            .stream()
            .map(this::toResponse)
            .toList();
}

    @GetMapping("/{id}")
    public MatchResponse getMatchById(@PathVariable Long id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Match not found"));

        return toResponse(match);
    }

    private MatchResponse toResponse(Match match) {
        return new MatchResponse(
                match.getId(),
                match.getExternalMatchId(),
                match.getMatchTitle(),
                match.getMatchType(),
                match.getStatus(),
                match.getVenue(),
                match.getMatchDate(),
                match.getTeam1Name(),
                match.getTeam2Name(),
                match.getTeam1Score(),
                match.getTeam2Score(),
                match.getLastUpdatedAt()
        );
    }
}