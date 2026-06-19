package com.ssafy.tripbaton.domain.relay.batch;

import com.ssafy.tripbaton.domain.relay.repository.RelayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class RelayStatusBatch {

    private static final int STALE_DAYS = 7;
    private static final int ARCHIVED_DAYS = 30;

    private final RelayRepository relayRepository;

    // 매일 새벽 2시 실행
    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void updateRelayStatuses() {
        LocalDateTime now = LocalDateTime.now();

        int staleCount = relayRepository.updateActiveToStale(now.minusDays(STALE_DAYS));
        log.info("[Batch] ACTIVE → STALE 전환: {}건", staleCount);

        int archivedCount = relayRepository.updateStaleToArchived(now.minusDays(ARCHIVED_DAYS));
        log.info("[Batch] STALE → ARCHIVED 전환: {}건", archivedCount);
    }
}
