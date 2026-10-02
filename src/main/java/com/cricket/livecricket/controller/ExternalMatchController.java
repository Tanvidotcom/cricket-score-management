
package com.cricket.livecricket.controller;

import com.cricket.livecricket.service.CricketApiService;
import tools.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/external")
public class ExternalMatchController {

    private final CricketApiService cricketApiService;

    public ExternalMatchController(
            CricketApiService cricketApiService) {
        this.cricketApiService = cricketApiService;
    }

    @GetMapping("/matches")
    public JsonNode getExternalMatches() {
        try {
            return cricketApiService.fetchCurrentMatches();

        } catch (IllegalStateException e) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    e.getMessage()
            );

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Unable to fetch matches from the cricket API."
            );
        }
    }
}