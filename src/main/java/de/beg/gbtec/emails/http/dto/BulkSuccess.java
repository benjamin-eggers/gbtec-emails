package de.beg.gbtec.emails.http.dto;

import org.springframework.http.HttpStatus;

public record BulkSuccess<T>(
        int statusCode,
        T data
) implements BulkResponseEntry<T> {


    public static <T> BulkSuccess<T> ok(T data) {
        return new BulkSuccess<>(HttpStatus.OK.value(), data);
    }
}
