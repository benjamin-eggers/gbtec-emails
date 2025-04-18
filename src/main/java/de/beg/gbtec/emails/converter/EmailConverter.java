package de.beg.gbtec.emails.converter;

import de.beg.gbtec.emails.http.dto.CreateEmailRequest;
import de.beg.gbtec.emails.http.dto.UpdateEmailRequest;
import de.beg.gbtec.emails.model.Email;
import de.beg.gbtec.emails.model.Recipient;
import de.beg.gbtec.emails.repository.dto.EmailEntity;
import de.beg.gbtec.emails.repository.dto.RecipientJsonb;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isAllBlank;
import static org.apache.commons.lang3.StringUtils.trimToNull;

public class EmailConverter {

    private EmailConverter() {
    }

    public static List<Email> toEmails(
            Page<EmailEntity> entities
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

    public static List<Recipient> mapRecipients(
            List<RecipientJsonb> recipients
    ) {
        return recipients.stream()
                .map(EmailConverter::toRecipient)
                .toList();
    }

    public static Recipient toRecipient(
            RecipientJsonb recipientJsonb
    ) {
        return Recipient.builder()
                .niceName(recipientJsonb.getNiceName())
                .email(recipientJsonb.getEmail())
                .build();
    }

    public static EmailEntity toEmailEntity(
            CreateEmailRequest request
    ) {
        var entity = new EmailEntity();
        entity.setFrom(trimToNull(request.from()));
        entity.setTo(toRecipientJsonb(request.to()));
        entity.setCc(toRecipientJsonb(request.cc()));
        entity.setBcc(toRecipientJsonb(request.bcc()));
        entity.setSubject(trimToNull(request.subject()));
        entity.setBody(trimToNull(request.body()));
        entity.setState(request.state());
        return entity;
    }

    public static List<RecipientJsonb> toRecipientJsonb(List<Recipient> recipients) {
        return recipients.stream()
                .map(EmailConverter::toRecipientJsonb)
                .filter(Objects::nonNull)
                .toList();
    }

    @Nullable
    public static RecipientJsonb toRecipientJsonb(Recipient recipient) {
        if (isAllBlank(recipient.email(), recipient.niceName())) {
            return null;
        }
        var recipientJsonb = new RecipientJsonb();
        recipientJsonb.setEmail(trimToNull(recipient.email()));
        recipientJsonb.setNiceName(trimToNull(recipient.niceName()));
        return recipientJsonb;
    }

    public static void applyEmailUpdate(
            EmailEntity entity,
            UpdateEmailRequest request
    ) {
        entity.setFrom(trimToNull(request.from()));
        entity.setTo(toRecipientJsonb(request.to()));
        entity.setCc(toRecipientJsonb(request.cc()));
        entity.setBcc(toRecipientJsonb(request.bcc()));
        entity.setSubject(trimToNull(request.subject()));
        entity.setBody(trimToNull(request.body()));
        entity.setState(request.state());
    }
}
