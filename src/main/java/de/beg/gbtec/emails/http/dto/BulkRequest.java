package de.beg.gbtec.emails.http.dto;

import java.util.List;

public record BulkRequest<T>(
        List<T> requests
) {

    public BulkRequest {
        requests = requests == null ? List.of() : List.copyOf(requests);
    }
}
