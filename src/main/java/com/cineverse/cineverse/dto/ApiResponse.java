package com.cineverse.cineverse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    private boolean success;
    private String message;
    private Object data;

    public static ApiResponse success(Object data, String message) {
        return ApiResponse.builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static ApiResponse success(Object data) {
        return ApiResponse.builder()
                .success(true)
                .message("Success")
                .data(data)
                .build();
    }

    public static ApiResponse failure(String message) {
        return ApiResponse.builder()
                .success(false)
                .message(message)
                .data(null)
                .build();
    }
}