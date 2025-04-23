package de.beg.gbtec.emails.http.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import java.util.List;

public record BulkRequest<S extends BulkRequestEntry<?>>(
        @Size(max = 100) List<@Valid S> requests
) {

    public BulkRequest {
        requests = requests == null ? List.of() : List.copyOf(requests);
    }
}
