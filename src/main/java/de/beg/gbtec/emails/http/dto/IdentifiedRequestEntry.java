package de.beg.gbtec.emails.http.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record IdentifiedRequestEntry<T>(
        @NotNull Long id,
        @Valid T data
) implements BulkRequestEntry<T> {
}
