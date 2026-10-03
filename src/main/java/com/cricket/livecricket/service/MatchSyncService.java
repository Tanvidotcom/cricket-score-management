
package com.cricket.livecricket.service;

import com.cricket.livecricket.client.CricketApiClient;
import com.cricket.livecricket.entity.Match;
import com.cricket.livecricket.entity.ScoreSnapshot;
import com.cricket.livecricket.repository.MatchRepository;
import com.cricket.livecricket.repository.ScoreSnapshotRepository;

import tools.jackson.databind.JsonNode;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MatchSyncService {

    private final CricketApiClient cricketApiClient;
    private final MatchRepository matchRepository;
    private final ScoreSnapshotRepository scoreSnapshotRepository;

    public MatchSyncService(
            CricketApiClient cricketApiClient,
            MatchRepository matchRepository,
            ScoreSnapshotRepository scoreSnapshotRepository) {
        this.cricketApiClient = cricketApiClient;
        this.matchRepository = matchRepository;
        this.scoreSnapshotRepository = scoreSnapshotRepository;
    }

    @Transactional
    public int syncMatches() {

        JsonNode response = cricketApiClient.fetchCurrentMatches();
        JsonNode matchesData = response.path("data");

        if (response.isArray()) {
            matchesData = response;
        }

        if (!matchesData.isArray()) {
            throw new IllegalStateException(
                    "The API response does not contain a matches array.");
        }

        int savedCount = 0;

        for (JsonNode matchData : matchesData) {

            String externalId = matchData.path("id").asText("");

            if (externalId.isBlank()) {
                continue;
            }

            Match match = matchRepository
                    .findByExternalMatchId(externalId)
                    .orElseGet(Match::new);

            match.setExternalMatchId(externalId);

            String title = matchData.path("name").asText("");
            if (!title.isBlank()) {
                match.setMatchTitle(title);
            }

            String type = matchData.path("matchType").asText("");
            if (!type.isBlank()) {
                match.setMatchType(type);
            }

            boolean matchStarted = matchData.path("matchStarted").asBoolean(false);
boolean matchEnded = matchData.path("matchEnded").asBoolean(false);

String status;

if (matchEnded) {
    status = "COMPLETED";
} else if (matchStarted) {
    status = "LIVE";
} else {
    status = "UPCOMING";
}

match.setStatus(status);

            String venue = matchData.path("venue").asText("");
            if (!venue.isBlank()) {
                match.setVenue(venue);
            }

            String date = matchData.path("date").asText("");
            if (!date.isBlank()) {
                match.setMatchDate(date);
            }

            JsonNode teams = matchData.path("teams");

            if (teams.isArray() && teams.size() >= 1) {
                match.setTeam1Name(teams.get(0).asText(""));
            }

            if (teams.isArray() && teams.size() >= 2) {
                match.setTeam2Name(teams.get(1).asText(""));
            }

            JsonNode scores = matchData.path("score");

            List<ScoreSnapshot> snapshots = new ArrayList<>();

            if (scores.isArray()) {

                for (int i = 0; i < scores.size(); i++) {

                    JsonNode scoreData = scores.get(i);

                    int runs = scoreData.path("r").asInt(0);
                    int wickets = scoreData.path("w").asInt(0);
                    String overs = scoreData.path("o").asText("0");
                    String inning = scoreData.path("inning").asText("");

                    String battingTeam =
                            getBattingTeam(inning, teams, i);

                    ScoreSnapshot snapshot = new ScoreSnapshot();

                    snapshot.setInningsNumber(i + 1);
                    snapshot.setBattingTeam(battingTeam);
                    snapshot.setRuns(runs);
                    snapshot.setWickets(wickets);
                    snapshot.setOvers(overs);
                    snapshot.setRunRate(calculateRunRate(runs, overs));

                    if (i == 1 && scores.size() > 1) {
                        int firstInningsRuns =
                                scores.get(0).path("r").asInt(0);
                        snapshot.setTarget(firstInningsRuns + 1);
                    }

                    snapshot.setRawData(scoreData.toString());
                    snapshots.add(snapshot);
                }

                updateTeamScores(match, scores, teams);
            }

            match.setRawData(matchData.toString());

            // Save the match first to ensure it has a database ID.
            Match savedMatch = matchRepository.save(match);

            // Save only new or changed innings scores.
            for (ScoreSnapshot snapshot : snapshots) {

                snapshot.setMatch(savedMatch);

                var latestSnapshot =
                        scoreSnapshotRepository
                                .findFirstByMatch_IdAndInningsNumberOrderByFetchedAtDesc(
                                        savedMatch.getId(),
                                        snapshot.getInningsNumber()
                                );

                boolean scoreChanged = latestSnapshot.isEmpty()
                        || !isSameScore(
                                latestSnapshot.get(),
                                snapshot
                        );

                if (scoreChanged) {
                    scoreSnapshotRepository.save(snapshot);
                }
            }

            savedCount++;
        }

        return savedCount;
    }

    private void updateTeamScores(
            Match match,
            JsonNode scores,
            JsonNode teams) {

        String team1 = match.getTeam1Name();
        String team2 = match.getTeam2Name();

        String team1Score = null;
        String team2Score = null;

        for (int i = 0; i < scores.size(); i++) {

            JsonNode score = scores.get(i);
            String inning = score.path("inning").asText("");

            String formattedScore = formatScore(score);

            if (containsTeamName(inning, team1)) {
                team1Score = formattedScore;
            } else if (containsTeamName(inning, team2)) {
                team2Score = formattedScore;
            } else if (i == 0 && team1Score == null) {
                team1Score = formattedScore;
            } else if (i == 1 && team2Score == null) {
                team2Score = formattedScore;
            }
        }

        if (team1Score != null) {
            match.setTeam1Score(team1Score);
        }

        if (team2Score != null) {
            match.setTeam2Score(team2Score);
        }
    }

    private boolean containsTeamName(String inning, String teamName) {
        return teamName != null
                && !teamName.isBlank()
                && inning.toLowerCase()
                        .contains(teamName.toLowerCase());
    }

    private boolean isSameScore(
            ScoreSnapshot oldScore,
            ScoreSnapshot newScore) {

        return Objects.equals(oldScore.getBattingTeam(),
                              newScore.getBattingTeam())
                && Objects.equals(oldScore.getRuns(),
                                  newScore.getRuns())
                && Objects.equals(oldScore.getWickets(),
                                  newScore.getWickets())
                && Objects.equals(oldScore.getOvers(),
                                  newScore.getOvers())
                && Objects.equals(oldScore.getTarget(),
                                  newScore.getTarget());
    }

    private String formatScore(JsonNode scoreData) {

        int runs = scoreData.path("r").asInt(0);
        int wickets = scoreData.path("w").asInt(0);
        String overs = scoreData.path("o").asText("0");

        return runs + "/" + wickets + " (" + overs + " ov)";
    }

    private String getBattingTeam(
            String inning,
            JsonNode teams,
            int index) {

        if (teams.isArray()) {
            for (JsonNode team : teams) {
                String teamName = team.asText("");

                if (!teamName.isBlank()
                        && inning.toLowerCase().contains(
                                teamName.toLowerCase())) {
                    return teamName;
                }
            }

            if (index < teams.size()) {
                return teams.get(index).asText("");
            }
        }

        return inning;
    }

    private BigDecimal calculateRunRate(int runs, String oversText) {

        try {
            String[] parts = oversText.split("\\.");

            int completedOvers = Integer.parseInt(parts[0]);
            int balls = parts.length > 1
                    ? Integer.parseInt(parts[1])
                    : 0;

            if (balls < 0 || balls > 5) {
                return BigDecimal.ZERO;
            }

            BigDecimal totalOvers =
                    BigDecimal.valueOf(completedOvers)
                            .add(BigDecimal.valueOf(balls)
                                    .divide(BigDecimal.valueOf(6), 6,
                                            RoundingMode.HALF_UP));

            if (totalOvers.compareTo(BigDecimal.ZERO) == 0) {
                return BigDecimal.ZERO;
            }

            return BigDecimal.valueOf(runs)
                    .divide(totalOvers, 2, RoundingMode.HALF_UP);

        } catch (NumberFormatException ex) {
            return BigDecimal.ZERO;
        }
    }
}