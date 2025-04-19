package de.beg.gbtec.emails.http.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.beg.gbtec.emails.model.EmailStatus;
import de.beg.gbtec.emails.model.Recipient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

import java.util.List;

import static de.beg.gbtec.emails.http.dto.Constants.*;

public record UpdateEmailRequest(
        @JsonProperty(API_PROPERTY_FROM) @Email String from,
        @JsonProperty(API_PROPERTY_TO) List<@Valid Recipient> to,
        @JsonProperty(API_PROPERTY_CC) List<@Valid Recipient> cc,
        @JsonProperty(API_PROPERTY_BCC) List<@Valid Recipient> bcc,
        @JsonProperty(API_PROPERTY_SUBJECT) String subject,
        @JsonProperty(API_PROPERTY_BODY) String body,
        @JsonProperty(API_PROPERTY_STATE) EmailStatus state
) {

    public UpdateEmailRequest {
        to = to == null ? List.of() : List.copyOf(to);
        cc = cc == null ? List.of() : List.copyOf(cc);
        bcc = bcc == null ? List.of() : List.copyOf(bcc);
    }

}
