package com.lynlyu.shortlink.common.util;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;

/**
 * Utility for hashing and ID shuffling.
 * Optimized for performance in high-concurrency environments.
 */
public class HashUtil {

    // A random prime number to shuffle the ID space
    private static final long OFFSET = 1000000000L;
    private static final long MULTIPLIER = 48271L;

    /**
     * Shuffles the ID to prevent predictable/sequential short codes.
     * This ensures 1001 and 1002 result in very different Base62 strings.
     */
    public static long shuffle(long id) {
        return (id ^ OFFSET) * MULTIPLIER % 0xFFFFFFFFL;
    }

    /**
     * Generates a 32-bit MurmurHash fingerprint for a string.
     * Ideal for URL deduplication (idempotency) due to its speed.
     */
    public static String murmurHash32(String input) {
        return Hashing.murmur3_32_fixed()
                .hashString(input, StandardCharsets.UTF_8)
                .toString();
    }
}