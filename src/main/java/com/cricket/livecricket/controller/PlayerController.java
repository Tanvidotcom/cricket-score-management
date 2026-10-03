
package com.cricket.livecricket.controller;

import com.cricket.livecricket.dto.PlayerResponse;
import com.cricket.livecricket.entity.Player;
import com.cricket.livecricket.entity.Team;
import com.cricket.livecricket.repository.PlayerRepository;
import com.cricket.livecricket.repository.TeamRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/players")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://localhost:5174"
})
public class PlayerController {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public PlayerController(
            PlayerRepository playerRepository,
            TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    public record PlayerRequest(
            @NotBlank String name,
            String role,
            String battingStyle,
            String bowlingStyle,
            Integer jerseyNumber,
            @NotNull Long teamId
    ) {}

    @GetMapping
    public List<PlayerResponse> getAllPlayers() {
        return playerRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public PlayerResponse getPlayer(@PathVariable Long id) {
        return toResponse(findPlayer(id));
    }

    @GetMapping("/team/{teamId}")
    public List<PlayerResponse> getPlayersByTeam(
            @PathVariable Long teamId) {
        if (!teamRepository.existsById(teamId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Team not found");
        }

        return playerRepository.findByTeam_Id(teamId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlayerResponse createPlayer(
            @Valid @RequestBody PlayerRequest request) {
        Player player = new Player();
        applyRequest(player, request);
        return toResponse(playerRepository.save(player));
    }

    @PutMapping("/{id}")
    public PlayerResponse updatePlayer(
            @PathVariable Long id,
            @Valid @RequestBody PlayerRequest request) {
        Player player = findPlayer(id);
        applyRequest(player, request);
        return toResponse(playerRepository.save(player));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlayer(@PathVariable Long id) {
        playerRepository.delete(findPlayer(id));
    }

    private void applyRequest(Player player, PlayerRequest request) {
        Team team = teamRepository.findById(request.teamId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Team not found"));

        player.setName(request.name().trim());
        player.setRole(request.role());
        player.setBattingStyle(request.battingStyle());
        player.setBowlingStyle(request.bowlingStyle());
        player.setJerseyNumber(request.jerseyNumber());
        player.setTeam(team);
    }

    private Player findPlayer(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Player not found"));
    }

    private PlayerResponse toResponse(Player player) {
        return new PlayerResponse(
                player.getId(),
                player.getName(),
                player.getRole(),
                player.getBattingStyle(),
                player.getBowlingStyle(),
                player.getJerseyNumber(),
                player.getTeam().getId(),
                player.getTeam().getName()
        );
    }
}