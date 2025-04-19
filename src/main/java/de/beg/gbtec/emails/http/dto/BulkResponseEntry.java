package de.beg.gbtec.emails.http.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record BulkResponseEntry<V>(
        HttpStatus status,
        V data,
        String message
) {
}
