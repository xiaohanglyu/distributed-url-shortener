package com.lynlyu.shortlink.common.util;

/**
 * Utility for converting between Base10 (Long) and Base62 (String).
 * Used for generating compact, URL-safe short codes.
 */
public class Base62Converter {

    private static final String CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = CHARACTERS.length();

    /**
     * Encodes a long ID into a Base62 string.
     */
    public static String encode(long input) {
        StringBuilder sb = new StringBuilder();
        if (input == 0) {
            return String.valueOf(CHARACTERS.charAt(0));
        }
        while (input > 0) {
            sb.append(CHARACTERS.charAt((int) (input % BASE)));
            input /= BASE;
        }
        return sb.reverse().toString();
    }

    /**
     * Decodes a Base62 string back into a long ID.
     */
    public static long decode(String input) {
        long res = 0;
        for (char c : input.toCharArray()) {
            res = res * BASE + CHARACTERS.indexOf(c);
        }
        return res;
    }
}