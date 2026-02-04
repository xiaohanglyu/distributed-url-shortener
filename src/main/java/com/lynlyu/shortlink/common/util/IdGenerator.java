package com.lynlyu.shortlink.common.util;

import com.lynlyu.shortlink.repository.mapper.IdGeneratorMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Distributed ID Generator using Segment Mode.
 * Ensures high performance and thread safety for 100M DAU.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class IdGenerator {

    private final IdGeneratorMapper idGeneratorMapper;

    private AtomicLong currentId = new AtomicLong(0);
    private long maxId = 0;
    private final String BIZ_TYPE = "short_link";

    /**
     * Generates the next unique ID.
     * Uses synchronized block only when the segment is exhausted.
     */
    public long nextId() {
        if (currentId.get() >= maxId) {
            synchronized (this) {
                if (currentId.get() >= maxId) {
                    reload();
                }
            }
        }
        return currentId.getAndIncrement();
    }

    private void reload() {
        // 1. Fetch next segment from DB: UPDATE max_id = max_id + step
        // 2. We will implement this in IdGeneratorMapper
        log.info("ID segment exhausted. Fetching next segment from database...");

        // This is a simplified logic. In production, we'd use a separate DTO.
        idGeneratorMapper.updateNextSegment(BIZ_TYPE);
        var segment = idGeneratorMapper.selectByBizType(BIZ_TYPE);

        this.maxId = segment.getMaxId();
        this.currentId.set(maxId - segment.getStep());
    }
}