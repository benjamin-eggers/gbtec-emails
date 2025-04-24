package de.beg.gbtec.emails.http.dto;

import java.util.List;
import java.util.Objects;

public record BulkResponse<T>(
        List<BulkResponseEntry<T>> results
) {

    public BulkResponse {
        results = Objects.requireNonNullElse(results, List.of());
    }

    public static <T> BulkResponse<T> of(List<BulkResponseEntry<T>> results) {
        return new BulkResponse<>(results);
    }

}
