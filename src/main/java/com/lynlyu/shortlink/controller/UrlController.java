package com.lynlyu.shortlink.controller;

import com.lynlyu.shortlink.common.response.Result;
import com.lynlyu.shortlink.domain.dto.ShortLinkCreateReq;
import com.lynlyu.shortlink.domain.dto.ShortLinkResp;
import com.lynlyu.shortlink.service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * REST Controller for URL shortening operations.
 * Handles link creation and high-concurrency redirection.
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    /**
     * Creates a short URL for the given long URL.
     * * @param request The request DTO containing the original long URL
     * @return A Result containing the short link details
     */
    @PostMapping("/shorten")
    public Result<ShortLinkResp> shorten(@RequestBody ShortLinkCreateReq request) {
        ShortLinkResp response = urlService.createShortLink(request);
        return Result.success(response);
    }

    /**
     * Redirects short code to the original long URL.
     * Implements HTTP 302 redirection for analytics tracking.
     * * @param shortCode The unique short identifier
     * @param response The HTTP servlet response
     * @throws IOException If redirection fails
     */
    @GetMapping("/{shortCode}")
    public void redirect(@PathVariable String shortCode, HttpServletResponse response) throws IOException {
        String longUrl = urlService.getOriginalUrl(shortCode);
        if (longUrl != null) {
            // Use 302 (Found) to ensure every click passes through the server for analytics
            response.sendRedirect(longUrl);
        } else {
            // Return 404 if the short code is invalid or expired
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}