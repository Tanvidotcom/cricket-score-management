
package com.cricket.livecricket.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "score_snapshots")
public class ScoreSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    private Integer inningsNumber;
    private String battingTeam;
    private Integer runs;
    private Integer wickets;
    private String overs;
    private BigDecimal runRate;
    private Integer target;

    private LocalDateTime fetchedAt;

    @Column(columnDefinition = "LONGTEXT")
    private String rawData;

    public ScoreSnapshot() {
    }

    @PrePersist
    public void setFetchedAt() {
        if (fetchedAt == null) {
            fetchedAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Integer getInningsNumber() {
        return inningsNumber;
    }

    public void setInningsNumber(Integer inningsNumber) {
        this.inningsNumber = inningsNumber;
    }

    public String getBattingTeam() {
        return battingTeam;
    }

    public void setBattingTeam(String battingTeam) {
        this.battingTeam = battingTeam;
    }

    public Integer getRuns() {
        return runs;
    }

    public void setRuns(Integer runs) {
        this.runs = runs;
    }

    public Integer getWickets() {
        return wickets;
    }

    public void setWickets(Integer wickets) {
        this.wickets = wickets;
    }

    public String getOvers() {
        return overs;
    }

    public void setOvers(String overs) {
        this.overs = overs;
    }

    public BigDecimal getRunRate() {
        return runRate;
    }

    public void setRunRate(BigDecimal runRate) {
        this.runRate = runRate;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    public LocalDateTime getFetchedAt() {
        return fetchedAt;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }
}