package de.beg.gbtec.emails.http.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.beg.gbtec.emails.model.EmailStatus;
import de.beg.gbtec.emails.model.Recipient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.Builder;

import java.util.List;

import static de.beg.gbtec.emails.http.dto.Constants.*;

@Builder
public record CreateEmailRequest(
        @JsonProperty(API_PROPERTY_FROM) @Email String from,
        @JsonProperty(API_PROPERTY_TO) List<@Valid Recipient> to,
        @JsonProperty(API_PROPERTY_CC) List<@Valid Recipient> cc,
        @JsonProperty(API_PROPERTY_BCC) List<@Valid Recipient> bcc,
        @JsonProperty(API_PROPERTY_SUBJECT) String subject,
        @JsonProperty(API_PROPERTY_BODY) String body,
        @JsonProperty(API_PROPERTY_STATE) EmailStatus state
) {

    public CreateEmailRequest {
        to = to != null ? List.copyOf(to) : List.of();
        cc = cc != null ? List.copyOf(cc) : List.of();
        bcc = bcc != null ? List.copyOf(bcc) : List.of();
    }

}
