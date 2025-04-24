package de.beg.gbtec.emails.model;

import lombok.Builder;

import java.util.List;

@Builder
public record PagedResponse<T>(
        List<T> entries,
        long page,
        long size,
        boolean hasNext
) {
}
