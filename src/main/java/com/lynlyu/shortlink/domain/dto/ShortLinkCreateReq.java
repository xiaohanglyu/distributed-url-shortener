package com.lynlyu.shortlink.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for creating a new short link.
 * Used as the @RequestBody in the UrlController.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkCreateReq {

    /**
     * The original long URL provided by the user.
     * Must be a valid HTTP/HTTPS URL.
     */
    private String longUrl;

    /**
     * Optional: Specific timestamp when the link expires.
     * If null, the system default (e.g., 30 days from now) will be applied.
     */
    private LocalDateTime expirationTime;

    /**
     * Optional: A custom alias for the short link (e.g., "winter-sale").
     * If provided, the system will use this instead of a generated code.
     */
    private String customAlias;
}