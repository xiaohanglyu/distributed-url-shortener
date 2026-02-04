package com.lynlyu.shortlink.service;

import com.lynlyu.shortlink.domain.dto.ShortLinkCreateReq;
import com.lynlyu.shortlink.domain.dto.ShortLinkResp;

/**
 * Service interface for URL shortening operations.
 * Designed for high-concurrency redirection and scalable ID generation.
 */
public interface UrlService {

    /**
     * Shortens a long URL into a unique short code.
     * @param request DTO containing the original URL
     * @return DTO containing the generated short code and full URL
     */
    ShortLinkResp createShortLink(ShortLinkCreateReq request);

    /**
     * Resolves a short code back to its original long URL.
     * @param shortCode The unique 6-character identifier
     * @return The original long URL string
     */
    String getOriginalUrl(String shortCode);
}