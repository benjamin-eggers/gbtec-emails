package de.beg.gbtec.emails.http.dto;

import jakarta.validation.Valid;

public record RequestEntry<T>(
        @Valid T data
) implements BulkRequestEntry<T> {

    public static <T> RequestEntry<T> of(T data) {
        return new RequestEntry<>(data);
    }
}
