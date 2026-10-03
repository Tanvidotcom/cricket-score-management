package com.cricket.livecricket.repository;

import com.cricket.livecricket.entity.Player;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    @EntityGraph(attributePaths = {"team"})
    List<Player> findAll();

    @EntityGraph(attributePaths = {"team"})
    List<Player> findByTeam_Id(Long teamId);

    boolean existsByTeam_Id(Long teamId);
}