package de.beg.gbtec.emails.http.dto;

import lombok.Builder;

import java.util.Map;

@Builder
public record BulkResponse<K, V>(
        Map<K, BulkResponseEntry<V>> entries
) {
}
