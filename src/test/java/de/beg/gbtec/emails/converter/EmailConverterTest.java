package de.beg.gbtec.emails.converter;

import de.beg.gbtec.emails.http.dto.CreateEmailRequest;
import de.beg.gbtec.emails.http.dto.UpdateEmailRequest;
import de.beg.gbtec.emails.model.Email;
import de.beg.gbtec.emails.model.EmailStatus;
import de.beg.gbtec.emails.model.Recipient;
import de.beg.gbtec.emails.repository.dto.EmailEntity;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EmailConverterTest {

    @Test
    void toEmails_shouldConvertPageOfEmailEntitiesToListOfEmails() {
        // Given
        EmailEntity entity1 = createEmailEntity(1L, "sender1@example.com", List.of("recipient1@example.com"),
                List.of("cc1@example.com"), List.of("bcc1@example.com"), "Subject 1", "Body 1", EmailStatus.DRAFT);

        EmailEntity entity2 = createEmailEntity(2L, "sender2@example.com", List.of("recipient2@example.com"),
                List.of("cc2@example.com"), List.of("bcc2@example.com"), "Subject 2", "Body 2", EmailStatus.SENT);

        List<EmailEntity> list = List.of(entity1, entity2);

        // When
        List<Email> emails = EmailConverter.toEmails(list);

        // Then
        assertThat(emails).hasSize(2);

        Email email1 = emails.getFirst();
        assertThat(email1.id()).isEqualTo(1L);
        assertThat(email1.from()).isEqualTo("sender1@example.com");
        assertThat(email1.to()).hasSize(1);
        assertThat(email1.to().getFirst().email()).isEqualTo("recipient1@example.com");
        assertThat(email1.cc()).hasSize(1);
        assertThat(email1.cc().getFirst().email()).isEqualTo("cc1@example.com");
        assertThat(email1.bcc()).hasSize(1);
        assertThat(email1.bcc().getFirst().email()).isEqualTo("bcc1@example.com");
        assertThat(email1.subject()).isEqualTo("Subject 1");
        assertThat(email1.body()).isEqualTo("Body 1");
        assertThat(email1.state()).isEqualTo(EmailStatus.DRAFT);

        Email email2 = emails.get(1);
        assertThat(email2.id()).isEqualTo(2L);
        assertThat(email2.from()).isEqualTo("sender2@example.com");
        assertThat(email2.to()).hasSize(1);
        assertThat(email2.to().getFirst().email()).isEqualTo("recipient2@example.com");
        assertThat(email2.cc()).hasSize(1);
        assertThat(email2.cc().getFirst().email()).isEqualTo("cc2@example.com");
        assertThat(email2.bcc()).hasSize(1);
        assertThat(email2.bcc().getFirst().email()).isEqualTo("bcc2@example.com");
        assertThat(email2.subject()).isEqualTo("Subject 2");
        assertThat(email2.body()).isEqualTo("Body 2");
        assertThat(email2.state()).isEqualTo(EmailStatus.SENT);
    }

    @Test
    void toEmails_shouldHandleEmptyPage() {
        // When
        List<Email> emails = EmailConverter.toEmails(Collections.emptyList());

        // Then
        assertThat(emails).isEmpty();
    }

    @Test
    void toEmail_shouldConvertEmailEntityToEmail() {
        // Given
        EmailEntity entity = createEmailEntity(1L, "sender@example.com", List.of("recipient@example.com"),
                List.of("cc@example.com"), List.of("bcc@example.com"), "Subject", "Body", EmailStatus.DRAFT);

        // When
        Email email = EmailConverter.toEmail(entity);

        // Then
        assertThat(email.id()).isEqualTo(1L);
        assertThat(email.from()).isEqualTo("sender@example.com");
        assertThat(email.to()).hasSize(1);
        assertThat(email.to().getFirst().email()).isEqualTo("recipient@example.com");
        assertThat(email.cc()).hasSize(1);
        assertThat(email.cc().getFirst().email()).isEqualTo("cc@example.com");
        assertThat(email.bcc()).hasSize(1);
        assertThat(email.bcc().getFirst().email()).isEqualTo("bcc@example.com");
        assertThat(email.subject()).isEqualTo("Subject");
        assertThat(email.body()).isEqualTo("Body");
        assertThat(email.state()).isEqualTo(EmailStatus.DRAFT);
    }

    @Test
    void toEmail_shouldHandleEmptyRecipientLists() {
        // Given
        EmailEntity entity = createEmailEntity(1L, "sender@example.com", Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(), "Subject", "Body", EmailStatus.DRAFT);

        // When
        Email email = EmailConverter.toEmail(entity);

        // Then
        assertThat(email.id()).isEqualTo(1L);
        assertThat(email.from()).isEqualTo("sender@example.com");
        assertThat(email.to()).isEmpty();
        assertThat(email.cc()).isEmpty();
        assertThat(email.bcc()).isEmpty();
        assertThat(email.subject()).isEqualTo("Subject");
        assertThat(email.body()).isEqualTo("Body");
        assertThat(email.state()).isEqualTo(EmailStatus.DRAFT);
    }

    @Test
    void toEmailEntity_shouldConvertCreateEmailRequestToEmailEntity() {
        // Given
        CreateEmailRequest request = CreateEmailRequest.builder()
                .from("sender@example.com")
                .to(List.of(Recipient.builder().email("recipient@example.com").build()))
                .cc(List.of(Recipient.builder().email("cc@example.com").build()))
                .bcc(List.of(Recipient.builder().email("bcc@example.com").build()))
                .subject("Subject")
                .body("Body")
                .state(EmailStatus.DRAFT)
                .build();

        // When
        EmailEntity entity = EmailConverter.toEmailEntity(request);

        // Then
        assertThat(entity.getId()).isNull();
        assertThat(entity.getFrom()).isEqualTo("sender@example.com");
        assertThat(entity.getTo()).hasSize(1);
        assertThat(entity.getTo().getFirst()).isEqualTo("recipient@example.com");
        assertThat(entity.getCc()).hasSize(1);
        assertThat(entity.getCc().getFirst()).isEqualTo("cc@example.com");
        assertThat(entity.getBcc()).hasSize(1);
        assertThat(entity.getBcc().getFirst()).isEqualTo("bcc@example.com");
        assertThat(entity.getSubject()).isEqualTo("Subject");
        assertThat(entity.getBody()).isEqualTo("Body");
        assertThat(entity.getState()).isEqualTo(EmailStatus.DRAFT);
    }

    @Test
    void toEmailEntity_shouldTrimNullValues() {
        // Given
        CreateEmailRequest request = CreateEmailRequest.builder()
                .from("  ")
                .to(List.of(Recipient.builder().email("recipient@example.com").build()))
                .cc(List.of())
                .bcc(List.of())
                .subject("")
                .body(null)
                .state(EmailStatus.DRAFT)
                .build();

        // When
        EmailEntity entity = EmailConverter.toEmailEntity(request);

        // Then
        assertThat(entity.getFrom()).isNull();
        assertThat(entity.getTo()).hasSize(1);
        assertThat(entity.getCc()).isNull();
        assertThat(entity.getBcc()).isNull();
        assertThat(entity.getSubject()).isNull();
        assertThat(entity.getBody()).isNull();
        assertThat(entity.getState()).isEqualTo(EmailStatus.DRAFT);
    }

    @Test
    void toEmailEntity_shouldFilterBlankRecipients() {
        // Given
        CreateEmailRequest request = CreateEmailRequest.builder()
                .from("sender@example.com")
                .to(List.of(
                        Recipient.builder().email("recipient@example.com").build(),
                        Recipient.builder().email("  ").build(),
                        Recipient.builder().email("").build()
                ))
                .cc(List.of())
                .bcc(List.of())
                .subject("Subject")
                .body("Body")
                .state(EmailStatus.DRAFT)
                .build();

        // When
        EmailEntity entity = EmailConverter.toEmailEntity(request);

        // Then
        assertThat(entity.getTo()).hasSize(1);
        assertThat(entity.getTo().getFirst()).isEqualTo("recipient@example.com");
    }

    @Test
    void toEmailEntity_shouldHandleAllEmailStatuses() {
        // Test for DRAFT
        CreateEmailRequest draftRequest = CreateEmailRequest.builder()
                .from("sender@example.com")
                .to(List.of(Recipient.builder().email("recipient@example.com").build()))
                .state(EmailStatus.DRAFT)
                .build();

        EmailEntity draftEntity = EmailConverter.toEmailEntity(draftRequest);
        assertThat(draftEntity.getState()).isEqualTo(EmailStatus.DRAFT);

        // Test for SENT
        CreateEmailRequest sentRequest = CreateEmailRequest.builder()
                .from("sender@example.com")
                .to(List.of(Recipient.builder().email("recipient@example.com").build()))
                .state(EmailStatus.SENT)
                .build();

        EmailEntity sentEntity = EmailConverter.toEmailEntity(sentRequest);
        assertThat(sentEntity.getState()).isEqualTo(EmailStatus.SENT);

        // Test for DELETED
        CreateEmailRequest deletedRequest = CreateEmailRequest.builder()
                .from("sender@example.com")
                .to(List.of(Recipient.builder().email("recipient@example.com").build()))
                .state(EmailStatus.DELETED)
                .build();

        EmailEntity deletedEntity = EmailConverter.toEmailEntity(deletedRequest);
        assertThat(deletedEntity.getState()).isEqualTo(EmailStatus.DELETED);

        // Test for SPAM
        CreateEmailRequest spamRequest = CreateEmailRequest.builder()
                .from("sender@example.com")
                .to(List.of(Recipient.builder().email("recipient@example.com").build()))
                .state(EmailStatus.SPAM)
                .build();

        EmailEntity spamEntity = EmailConverter.toEmailEntity(spamRequest);
        assertThat(spamEntity.getState()).isEqualTo(EmailStatus.SPAM);
    }

    @Test
    void applyEmailUpdate_shouldUpdateEmailEntityFromUpdateEmailRequest() {
        // Given
        EmailEntity entity = createEmailEntity(1L, "old-sender@example.com", List.of("old-recipient@example.com"),
                List.of("old-cc@example.com"), List.of("old-bcc@example.com"), "Old Subject", "Old Body", EmailStatus.DRAFT);

        UpdateEmailRequest request = new UpdateEmailRequest(
                "new-sender@example.com",
                List.of(Recipient.builder().email("new-recipient@example.com").build()),
                List.of(Recipient.builder().email("new-cc@example.com").build()),
                List.of(Recipient.builder().email("new-bcc@example.com").build()),
                "New Subject",
                "New Body",
                EmailStatus.SENT
        );

        // When
        EmailConverter.applyEmailUpdate(entity, request);

        // Then
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getFrom()).isEqualTo("new-sender@example.com");
        assertThat(entity.getTo()).hasSize(1);
        assertThat(entity.getTo().getFirst()).isEqualTo("new-recipient@example.com");
        assertThat(entity.getCc()).hasSize(1);
        assertThat(entity.getCc().getFirst()).isEqualTo("new-cc@example.com");
        assertThat(entity.getBcc()).hasSize(1);
        assertThat(entity.getBcc().getFirst()).isEqualTo("new-bcc@example.com");
        assertThat(entity.getSubject()).isEqualTo("New Subject");
        assertThat(entity.getBody()).isEqualTo("New Body");
        assertThat(entity.getState()).isEqualTo(EmailStatus.SENT);
    }

    @Test
    void applyEmailUpdate_shouldTrimNullValues() {
        // Given
        EmailEntity entity = createEmailEntity(1L, "sender@example.com", List.of("recipient@example.com"),
                List.of("cc@example.com"), List.of("bcc@example.com"), "Subject", "Body", EmailStatus.DRAFT);

        UpdateEmailRequest request = new UpdateEmailRequest(
                "  ",
                List.of(Recipient.builder().email("new-recipient@example.com").build()),
                List.of(),
                List.of(),
                "",
                null,
                EmailStatus.SENT
        );

        // When
        EmailConverter.applyEmailUpdate(entity, request);

        // Then
        assertThat(entity.getFrom()).isNull();
        assertThat(entity.getTo()).hasSize(1);
        assertThat(entity.getCc()).isNull();
        assertThat(entity.getBcc()).isNull();
        assertThat(entity.getSubject()).isNull();
        assertThat(entity.getBody()).isNull();
        assertThat(entity.getState()).isEqualTo(EmailStatus.SENT);
    }

    @Test
    void applyEmailUpdate_shouldFilterBlankRecipients() {
        // Given
        EmailEntity entity = createEmailEntity(1L, "sender@example.com", List.of("recipient@example.com"),
                List.of("cc@example.com"), List.of("bcc@example.com"), "Subject", "Body", EmailStatus.DRAFT);

        UpdateEmailRequest request = new UpdateEmailRequest(
                "sender@example.com",
                List.of(
                        Recipient.builder().email("new-recipient@example.com").build(),
                        Recipient.builder().email("  ").build(),
                        Recipient.builder().email("").build()
                ),
                List.of(),
                List.of(),
                "Subject",
                "Body",
                EmailStatus.DRAFT
        );

        // When
        EmailConverter.applyEmailUpdate(entity, request);

        // Then
        assertThat(entity.getTo()).hasSize(1);
        assertThat(entity.getTo().getFirst()).isEqualTo("new-recipient@example.com");
    }

    @Test
    void applyEmailUpdate_shouldHandleAllEmailStatuses() {
        // Test for DRAFT
        EmailEntity draftEntity = createEmailEntity(1L, "sender@example.com", List.of("recipient@example.com"),
                Collections.emptyList(), Collections.emptyList(), "Subject", "Body", EmailStatus.SENT);

        UpdateEmailRequest draftRequest = new UpdateEmailRequest(
                "sender@example.com",
                List.of(Recipient.builder().email("recipient@example.com").build()),
                List.of(),
                List.of(),
                "Subject",
                "Body",
                EmailStatus.DRAFT
        );

        EmailConverter.applyEmailUpdate(draftEntity, draftRequest);
        assertThat(draftEntity.getState()).isEqualTo(EmailStatus.DRAFT);

        // Test for SENT
        EmailEntity sentEntity = createEmailEntity(2L, "sender@example.com", List.of("recipient@example.com"),
                Collections.emptyList(), Collections.emptyList(), "Subject", "Body", EmailStatus.DRAFT);

        UpdateEmailRequest sentRequest = new UpdateEmailRequest(
                "sender@example.com",
                List.of(Recipient.builder().email("recipient@example.com").build()),
                List.of(),
                List.of(),
                "Subject",
                "Body",
                EmailStatus.SENT
        );

        EmailConverter.applyEmailUpdate(sentEntity, sentRequest);
        assertThat(sentEntity.getState()).isEqualTo(EmailStatus.SENT);

        // Test for DELETED
        EmailEntity deletedEntity = createEmailEntity(3L, "sender@example.com", List.of("recipient@example.com"),
                Collections.emptyList(), Collections.emptyList(), "Subject", "Body", EmailStatus.DRAFT);

        UpdateEmailRequest deletedRequest = new UpdateEmailRequest(
                "sender@example.com",
                List.of(Recipient.builder().email("recipient@example.com").build()),
                List.of(),
                List.of(),
                "Subject",
                "Body",
                EmailStatus.DELETED
        );

        EmailConverter.applyEmailUpdate(deletedEntity, deletedRequest);
        assertThat(deletedEntity.getState()).isEqualTo(EmailStatus.DELETED);

        // Test for SPAM
        EmailEntity spamEntity = createEmailEntity(4L, "sender@example.com", List.of("recipient@example.com"),
                Collections.emptyList(), Collections.emptyList(), "Subject", "Body", EmailStatus.DRAFT);

        UpdateEmailRequest spamRequest = new UpdateEmailRequest(
                "sender@example.com",
                List.of(Recipient.builder().email("recipient@example.com").build()),
                List.of(),
                List.of(),
                "Subject",
                "Body",
                EmailStatus.SPAM
        );

        EmailConverter.applyEmailUpdate(spamEntity, spamRequest);
        assertThat(spamEntity.getState()).isEqualTo(EmailStatus.SPAM);
    }

    private EmailEntity createEmailEntity(
            Long id,
            String from,
            List<String> to,
            List<String> cc,
            List<String> bcc,
            String subject,
            String body,
            EmailStatus state
    ) {
        var entity = new EmailEntity();
        entity.setId(id);
        entity.setFrom(from);
        entity.setTo(to);
        entity.setCc(cc);
        entity.setBcc(bcc);
        entity.setSubject(subject);
        entity.setBody(body);
        entity.setState(state);
        return entity;
    }
}