package de.beg.gbtec.emails.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

import static de.beg.gbtec.emails.http.dto.Constants.*;

@Builder
public record Email(
        @JsonProperty(API_PROPERTY_ID) Long id,
        @JsonProperty(API_PROPERTY_FROM) String from,
        @JsonProperty(API_PROPERTY_TO) List<Recipient> to,
        @JsonProperty(API_PROPERTY_CC) List<Recipient> cc,
        @JsonProperty(API_PROPERTY_BCC) List<Recipient> bcc,
        @JsonProperty(API_PROPERTY_SUBJECT) String subject,
        @JsonProperty(API_PROPERTY_BODY) String body,
        @JsonProperty(API_PROPERTY_STATE) EmailStatus state
) {
}
