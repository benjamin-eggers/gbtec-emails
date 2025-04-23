package de.beg.gbtec.emails.converter;

import de.beg.gbtec.emails.http.dto.CreateEmailRequest;
import de.beg.gbtec.emails.http.dto.UpdateEmailRequest;
import de.beg.gbtec.emails.model.Email;
import de.beg.gbtec.emails.model.Recipient;
import de.beg.gbtec.emails.repository.dto.EmailEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.trimToNull;

public class EmailConverter {

    private EmailConverter() {
    }

    public static List<Email> toEmails(
            List<EmailEntity> entities
    ) {
        return entities.stream()
                .map(EmailConverter::toEmail)
                .toList();
    }

    public static Email toEmail(
            EmailEntity entity
    ) {
        return Email.builder()
                .id(entity.getId())
                .from(entity.getFrom())
                .to(mapRecipients(entity.getTo()))
                .cc(mapRecipients(entity.getCc()))
                .bcc(mapRecipients(entity.getBcc()))
                .subject(entity.getSubject())
                .body(entity.getBody())
                .state(entity.getState())
                .build();
    }

    private static List<Recipient> mapRecipients(
            List<String> recipients
    ) {
        return Optional.ofNullable(recipients)
                .orElseGet(List::of)
                .stream()
                .map(recipient -> Recipient.builder().email(recipient).build())
                .toList();
    }


    public static EmailEntity toEmailEntity(
            CreateEmailRequest request
    ) {
        var entity = new EmailEntity();
        entity.setFrom(trimToNull(request.from()));
        entity.setTo(unwrapRecipients(request.to()));
        entity.setCc(unwrapRecipients(request.cc()));
        entity.setBcc(unwrapRecipients(request.bcc()));
        entity.setSubject(trimToNull(request.subject()));
        entity.setBody(trimToNull(request.body()));
        entity.setState(request.state());
        return entity;
    }

    private static List<String> unwrapRecipients(
            List<Recipient> recipients
    ) {
        return recipients.stream()
                .map(Recipient::email)
                .filter(StringUtils::isNotBlank)
                .toList();
    }


    public static void applyEmailUpdate(
            EmailEntity entity,
            UpdateEmailRequest request
    ) {
        entity.setFrom(trimToNull(request.from()));
        entity.setTo(unwrapRecipients(request.to()));
        entity.setCc(unwrapRecipients(request.cc()));
        entity.setBcc(unwrapRecipients(request.bcc()));
        entity.setSubject(trimToNull(request.subject()));
        entity.setBody(trimToNull(request.body()));
        entity.setState(request.state());
    }
}
