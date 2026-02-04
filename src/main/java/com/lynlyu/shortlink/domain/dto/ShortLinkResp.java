package com.lynlyu.shortlink.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for short link creation response.
 * Standardized for high-concurrency API delivery.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkResp {

    /**
     * The unique 6-8 character code (e.g., "aX7zB2")
     */
    private String shortCode;

    /**
     * The full clickable short URL (e.g., "https://t.ly/aX7zB2")
     */
    private String shortUrl;

    /**
     * Optional: Expiration timestamp in ISO format (useful for frontend display)
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expirationTime;
}