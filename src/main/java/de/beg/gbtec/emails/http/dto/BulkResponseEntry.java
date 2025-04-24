package de.beg.gbtec.emails.http.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.lang.Nullable;

import static org.springframework.http.HttpStatus.*;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record BulkResponseEntry<T>(
        int status,
        @Nullable Long id,
        @Nullable T data,
        @Nullable String message
) {
    public static <T> BulkResponseEntry<T> created(Long id, T data) {
        return new BulkResponseEntry<>(CREATED.value(), id, data, null);
    }

    public static <T> BulkResponseEntry<T> internalServerError(String message) {
        return new BulkResponseEntry<>(INTERNAL_SERVER_ERROR.value(), null, null, message);
    }

    public static <T> BulkResponseEntry<T> internalServerError(Long id, String message) {
        return new BulkResponseEntry<>(INTERNAL_SERVER_ERROR.value(), id, null, message);
    }

    public static <T> BulkResponseEntry<T> ok(Long id) {
        return new BulkResponseEntry<>(OK.value(), id, null, null);
    }

    public static <T> BulkResponseEntry<T> ok(Long id, T data) {
        return new BulkResponseEntry<>(OK.value(), id, data, null);
    }

    public static <T> BulkResponseEntry<T> notFound(Long id, String message) {
        return new BulkResponseEntry<>(NOT_FOUND.value(), id, null, message);
    }
}
