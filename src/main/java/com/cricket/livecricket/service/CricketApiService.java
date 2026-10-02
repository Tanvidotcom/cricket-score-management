
package com.cricket.livecricket.service;

import com.cricket.livecricket.client.CricketApiClient;
import tools.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

@Service
public class CricketApiService {

    private final CricketApiClient cricketApiClient;

    public CricketApiService(CricketApiClient cricketApiClient) {
        this.cricketApiClient = cricketApiClient;
    }

    public JsonNode fetchCurrentMatches() {
        return cricketApiClient.fetchCurrentMatches();
    }
}