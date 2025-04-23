package de.beg.gbtec.emails.http.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record IdentifiedRequestEntry<T>(
        @NotNull Long id,
        @Valid T data
) implements BulkRequestEntry<T> {

    public static <T> IdentifiedRequestEntry<T> of(Long id, T data) {
        return new IdentifiedRequestEntry<>(id, data);
    }

}
