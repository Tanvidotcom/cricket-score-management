package com.cricket.livecricket.repository;

import com.cricket.livecricket.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {

    Optional<Match> findByExternalMatchId(String externalMatchId);

    List<Match> findByStatusIgnoreCase(String status);

    List<Match> findAllByOrderByLastUpdatedAtDesc();

    List<Match> findByStatusIgnoreCaseOrderByLastUpdatedAtDesc(String status);
}