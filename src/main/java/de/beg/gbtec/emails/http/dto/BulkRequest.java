package de.beg.gbtec.emails.http.dto;

import java.util.List;

public record BulkRequest<T>(
        List<T> requests
) {
}
