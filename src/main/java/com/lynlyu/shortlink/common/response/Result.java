package com.lynlyu.shortlink.common.response;

import lombok.Data;

/**
 * Global standard response wrapper for API consistency.
 * * @param <T> The type of the data payload
 */
@Data
public class Result<T> {
    private int code;
    private String message;
    private T data;

    /**
     * Success response factory method.
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("Success");
        result.setData(data);
        return result;
    }

    /**
     * Failure response factory method.
     */
    public static <T> Result<T> fail(int code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}