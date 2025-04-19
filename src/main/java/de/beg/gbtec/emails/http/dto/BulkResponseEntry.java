package de.beg.gbtec.emails.http.dto;

import org.springframework.http.HttpStatus;

public record BulkResponseEntry<V>(
        HttpStatus status,
        V data
) {
}
