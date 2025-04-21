package de.beg.gbtec.emails.http.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BulkError<T>(
        int statusCode,
        Long id,
        String message
) implements BulkResponseEntry<T> {

    public static <T> BulkError<T> internalServerError(String message) {
        return new BulkError<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, message);
    }

    public static <T> BulkError<T> internalServerError(Long id, String message) {
        return new BulkError<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), id, message);
    }
}
