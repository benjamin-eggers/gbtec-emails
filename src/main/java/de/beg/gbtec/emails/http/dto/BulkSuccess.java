package de.beg.gbtec.emails.http.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BulkSuccess<T>(
        int statusCode,
        Long id,
        T data
) implements BulkResponseEntry<T> {

    public static <T> BulkSuccess<T> ok(Long id) {
        return new BulkSuccess<>(OK.value(), id, null);
    }

    public static <T> BulkSuccess<T> ok(Long id, T data) {
        return new BulkSuccess<>(OK.value(), id, data);
    }

    public static <T> BulkSuccess<T> created(Long id, T data) {
        return new BulkSuccess<>(CREATED.value(), id, data);
    }
}
