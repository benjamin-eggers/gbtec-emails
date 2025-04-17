package de.beg.gbtec.emails.model;

import lombok.Builder;

import java.util.List;

@Builder
public record Email(
        Long emailId,
        String emailFrom,
        List<Recipient> emailTo,
        List<Recipient> emailCc,
        List<Recipient> emailBcc,
        String emailSubject,
        String emailBody,
        EmailStatus state
) {
}
