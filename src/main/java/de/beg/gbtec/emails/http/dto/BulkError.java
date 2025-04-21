package de.beg.gbtec.emails.http.dto;

import org.springframework.http.HttpStatus;

public record BulkError<T>(
        int statusCode,
        String message
) implements BulkResponseEntry<T> {

    public static <T> BulkError<T> badRequest(String message) {
        return new BulkError<>(HttpStatus.BAD_REQUEST.value(), message);
    }

    public static <T> BulkError<T> internalServerError(String message) {
        return new BulkError<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }
}
