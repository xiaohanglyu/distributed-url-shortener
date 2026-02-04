package com.lynlyu.shortlink.service.impl;

import com.lynlyu.shortlink.common.util.Base62Converter;
import com.lynlyu.shortlink.common.util.HashUtil;
import com.lynlyu.shortlink.common.util.IdGenerator;
import com.lynlyu.shortlink.domain.dto.ShortLinkCreateReq;
import com.lynlyu.shortlink.domain.dto.ShortLinkResp;
import com.lynlyu.shortlink.domain.entity.ShortLinkEntity;
import com.lynlyu.shortlink.repository.mapper.ShortLinkMapper;
import com.lynlyu.shortlink.service.UrlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * Production-ready implementation of UrlService.
 * Handles distributed ID generation, base62 encoding, and dual-layer caching (Redis + MySQL).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {

    private final StringRedisTemplate redisTemplate;
    private final ShortLinkMapper shortLinkMapper;
    private final IdGenerator idGenerator;

    // --- Redis Key Constants ---
    /** Prefix for ShortCode to LongURL mapping (Redirection) */
    private static final String REDIS_KEY_MAP = "sl:map:";

    /** Prefix for LongURL Fingerprint to ShortCode mapping (Idempotency) */
    private static final String REDIS_KEY_FINGERPRINT = "sl:fp:";

    // --- Default Config Constants ---
    /** Default expiration if user doesn't provide one (30 days) */
    private static final long DEFAULT_EXPIRE_DAYS = 30L;

    @Value("${app.short-link.domain:https://lyn.ly/}")
    private String domain;

    @Override
    public ShortLinkResp createShortLink(ShortLinkCreateReq request) {
        log.info("Creating short link for original URL: {}", request.getLongUrl());

        // 1. Generate Fingerprint (Idempotency)
        String fingerprint = HashUtil.murmurHash32(request.getLongUrl());
        String fingerprintKey = REDIS_KEY_FINGERPRINT + fingerprint;

        // 2. Idempotency Check: Prevent duplicate generation
        String cachedShortCode = redisTemplate.opsForValue().get(fingerprintKey);
        if (cachedShortCode != null) {
            log.info("Fingerprint hit! Returning existing code: {}", cachedShortCode);
            return buildResponse(cachedShortCode, null); // expirationTime can be null here or fetched if needed
        }

        // 3. Handle Expiration Time
        LocalDateTime expirationTime = request.getExpirationTime();
        if (expirationTime == null) {
            expirationTime = LocalDateTime.now().plusDays(DEFAULT_EXPIRE_DAYS);
        }

        // 4. Generate Distributed ID and Short Code
        long distributedId = idGenerator.nextId();
        String shortCode = Base62Converter.encode(HashUtil.shuffle(distributedId));

        // 5. Persist to MySQL
        ShortLinkEntity entity = ShortLinkEntity.builder()
                .id(distributedId)
                .shortCode(shortCode)
                .longUrl(request.getLongUrl())
                .createTime(LocalDateTime.now())
                .expireTime(expirationTime)
                .build();
        shortLinkMapper.insert(entity);

        // 6. Cache in Redis with Dynamic TTL
        long ttlInSeconds = Duration.between(LocalDateTime.now(), expirationTime).getSeconds();
        if (ttlInSeconds > 0) {
            // Forward Mapping for redirection
            redisTemplate.opsForValue().set(
                    REDIS_KEY_MAP + shortCode,
                    request.getLongUrl(),
                    ttlInSeconds,
                    TimeUnit.SECONDS
            );
            // Reverse Mapping for fingerprinting
            redisTemplate.opsForValue().set(
                    fingerprintKey,
                    shortCode,
                    ttlInSeconds,
                    TimeUnit.SECONDS
            );
        }

        return buildResponse(shortCode, expirationTime);
    }

    /**
     * Helper method to build the response DTO
     */
    private ShortLinkResp buildResponse(String shortCode, LocalDateTime expirationTime) {
        return ShortLinkResp.builder()
                .shortCode(shortCode)
                .shortUrl(domain + shortCode)
                .expirationTime(expirationTime)
                .build();
    }

    @Override
    public String getOriginalUrl(String shortCode) {
        // Step 1: Query Redis (Tier 1 Cache) for O(1) retrieval speed
        String cachedUrl = redisTemplate.opsForValue().get(REDIS_KEY_MAP + shortCode);
        if (cachedUrl != null) {
            log.debug("Cache Hit for shortCode: {}", shortCode);
            return cachedUrl;
        }

        // Step 2: Query MySQL (Tier 2 Storage) if cache misses
        log.warn("Cache Miss for shortCode: {}. Accessing database...", shortCode);
        String dbLongUrl = shortLinkMapper.selectLongUrlByCode(shortCode);

        // Step 3: Populate cache if found to protect database from subsequent hits
        if (dbLongUrl != null) {
            redisTemplate.opsForValue().set(
                    REDIS_KEY_MAP + shortCode,
                    dbLongUrl,
                    DEFAULT_EXPIRE_DAYS,
                    TimeUnit.DAYS
            );
        }

        return dbLongUrl;
    }
}