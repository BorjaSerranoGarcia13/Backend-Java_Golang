package com.ecommerce.response;

public record ErrorResponse(String message, int status, long timestamp) {
    public ErrorResponse {
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }

        if (status < 100 || status > 599) {
            throw new IllegalArgumentException("Invalid HTTP status code");
        }

        if (timestamp < 0) {
            throw new IllegalArgumentException("Timestamp cannot be negative");
        }

    }
}
