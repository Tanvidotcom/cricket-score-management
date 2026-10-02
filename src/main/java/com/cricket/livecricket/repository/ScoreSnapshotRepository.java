
package com.cricket.livecricket.repository;

import com.cricket.livecricket.entity.ScoreSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScoreSnapshotRepository
        extends JpaRepository<ScoreSnapshot, Long> {

    List<ScoreSnapshot> findByMatch_IdOrderByFetchedAtDesc(Long matchId);

    Optional<ScoreSnapshot>
    findFirstByMatch_IdAndInningsNumberOrderByFetchedAtDesc(
            Long matchId,
            Integer inningsNumber
    );
}