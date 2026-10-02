
package com.cricket.livecricket.controller;

import com.cricket.livecricket.dto.ScoreSnapshotResponse;
import com.cricket.livecricket.entity.ScoreSnapshot;
import com.cricket.livecricket.repository.MatchRepository;
import com.cricket.livecricket.repository.ScoreSnapshotRepository;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@CrossOrigin(origins = "http://localhost:5173")
public class ScoreSnapshotController {

    private final MatchRepository matchRepository;
    private final ScoreSnapshotRepository scoreSnapshotRepository;

    public ScoreSnapshotController(
            MatchRepository matchRepository,
            ScoreSnapshotRepository scoreSnapshotRepository) {
        this.matchRepository = matchRepository;
        this.scoreSnapshotRepository = scoreSnapshotRepository;
    }

    @GetMapping("/{id}/scores")
    public List<ScoreSnapshotResponse> getMatchScores(
            @PathVariable Long id) {

        if (!matchRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Match not found"
            );
        }

        List<ScoreSnapshot> snapshots =
                scoreSnapshotRepository
                        .findByMatch_IdOrderByFetchedAtDesc(id);

        return snapshots.stream()
                .map(snapshot -> new ScoreSnapshotResponse(
                        snapshot.getId(),
                        snapshot.getInningsNumber(),
                        snapshot.getBattingTeam(),
                        snapshot.getRuns(),
                        snapshot.getWickets(),
                        snapshot.getOvers(),
                        snapshot.getRunRate(),
                        snapshot.getTarget(),
                        snapshot.getFetchedAt()
                ))
                .toList();
    }
}