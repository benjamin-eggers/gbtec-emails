package de.beg.gbtec.emails.model;

import lombok.Builder;

@Builder
public record Recipient(
        String niceName,
        String email
) {
}
