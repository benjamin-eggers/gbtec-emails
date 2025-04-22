package de.beg.gbtec.emails.http.dto;

import jakarta.validation.Valid;

import java.util.List;

public record BulkRequest<S extends BulkRequestEntry<?>>(
        List<@Valid S> requests
) {

    public BulkRequest {
        requests = requests == null ? List.of() : List.copyOf(requests);
    }
}
