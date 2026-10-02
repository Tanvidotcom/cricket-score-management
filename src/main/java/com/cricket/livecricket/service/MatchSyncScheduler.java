
package com.cricket.livecricket.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MatchSyncScheduler {

    private static final Logger logger =
            LoggerFactory.getLogger(MatchSyncScheduler.class);

    private final MatchSyncService matchSyncService;

    public MatchSyncScheduler(MatchSyncService matchSyncService) {
        this.matchSyncService = matchSyncService;
    }

    @Scheduled(
        fixedDelayString = "${cricket.api.refresh-interval:1800000}"
    )
    public void synchronizeMatchesAutomatically() {

        try {
            int count = matchSyncService.syncMatches();

            logger.info(
                    "Automatic match synchronization completed. Matches processed: {}",
                    count
            );

        } catch (Exception exception) {
            logger.error(
                    "Automatic match synchronization failed: {}",
                    exception.getMessage(),
                    exception
            );
        }
    }
}