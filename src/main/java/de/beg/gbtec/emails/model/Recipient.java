package de.beg.gbtec.emails.model;

import jakarta.validation.constraints.Email;
import lombok.Builder;

@Builder
public record Recipient(
        String niceName,
        @Email String email
) {
}
