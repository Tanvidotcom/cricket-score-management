
package com.cricket.livecricket.client;

import tools.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class CricketApiClient {

    private final RestClient restClient;

    @Value("${cricket.api.key:}")
    private String apiKey;

    public CricketApiClient(
            @Value("${cricket.api.base-url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public JsonNode fetchCurrentMatches() {

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "Cricket API key is not configured."
            );
        }

        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/currentMatches")
                            .queryParam("apikey", apiKey)
                            .queryParam("offset", "0")
                            .build())
                    .retrieve()
                    .body(JsonNode.class);

        } catch (RestClientException e) {
            throw new RuntimeException(
                    "Failed to fetch matches from cricket API.", e
            );
        }
    }
}