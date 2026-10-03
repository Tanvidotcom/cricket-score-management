
package com.cricket.livecricket.controller;

import com.cricket.livecricket.entity.Team;
import com.cricket.livecricket.repository.PlayerRepository;
import com.cricket.livecricket.repository.TeamRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://localhost:5174"
})
public class TeamController {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    public TeamController(
            TeamRepository teamRepository,
            PlayerRepository playerRepository) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
    }

    public record TeamRequest(
            @NotBlank String name,
            String shortName,
            String country
    ) {}

    @GetMapping
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @GetMapping("/{id}")
    public Team getTeam(@PathVariable Long id) {
        return findTeam(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Team createTeam(@Valid @RequestBody TeamRequest request) {
        if (teamRepository.existsByNameIgnoreCase(request.name().trim())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Team already exists");
        }

        Team team = new Team();
        team.setName(request.name().trim());
        team.setShortName(request.shortName());
        team.setCountry(request.country());

        return teamRepository.save(team);
    }

    @PutMapping("/{id}")
    public Team updateTeam(
            @PathVariable Long id,
            @Valid @RequestBody TeamRequest request) {
        Team team = findTeam(id);

        teamRepository.findByNameIgnoreCase(request.name().trim())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT, "Team name already exists");
                });

        team.setName(request.name().trim());
        team.setShortName(request.shortName());
        team.setCountry(request.country());

        return teamRepository.save(team);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeam(@PathVariable Long id) {
        Team team = findTeam(id);

        if (playerRepository.existsByTeam_Id(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Remove or reassign this team's players first");
        }

        teamRepository.delete(team);
    }

    private Team findTeam(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Team not found"));
    }
}