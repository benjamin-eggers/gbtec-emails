package de.beg.gbtec.emails.converter;

import de.beg.gbtec.emails.http.dto.CreateEmailRequest;
import de.beg.gbtec.emails.http.dto.UpdateEmailRequest;
import de.beg.gbtec.emails.model.Email;
import de.beg.gbtec.emails.model.EmailStatus;
import de.beg.gbtec.emails.model.Recipient;
import de.beg.gbtec.emails.repository.dto.EmailEntity;
import de.beg.gbtec.emails.repository.dto.RecipientJsonb;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EmailConverterTest {

    @Test
    void toEmails_shouldConvertPageOfEntitiesToListOfEmails() {
        // Given
        RecipientJsonb recipientJsonb = new RecipientJsonb();
        recipientJsonb.setNiceName("Test User");
        recipientJsonb.setEmail("test@example.com");

        EmailEntity entity = new EmailEntity();
        entity.setId(1L);
        entity.setFrom("sender@example.com");
        entity.setTo(List.of(recipientJsonb));
        entity.setCc(List.of());
        entity.setBcc(List.of());
        entity.setSubject("Test Subject");
        entity.setBody("Test Body");
        entity.setState(EmailStatus.DRAFT);

        Page<EmailEntity> page = new PageImpl<>(List.of(entity));

        // When
        List<Email> result = EmailConverter.toEmails(page);

        // Then
        assertThat(result).hasSize(1);
        Email email = result.getFirst();
        assertThat(email.id()).isEqualTo(1L);
        assertThat(email.from()).isEqualTo("sender@example.com");
        assertThat(email.to()).hasSize(1);
        assertThat(email.to().getFirst().niceName()).isEqualTo("Test User");
        assertThat(email.to().getFirst().email()).isEqualTo("test@example.com");
        assertThat(email.cc()).isEmpty();
        assertThat(email.bcc()).isEmpty();
        assertThat(email.subject()).isEqualTo("Test Subject");
        assertThat(email.body()).isEqualTo("Test Body");
        assertThat(email.state()).isEqualTo(EmailStatus.DRAFT);
    }

    @Test
    void toEmail_shouldConvertEntityToEmail() {
        // Given
        RecipientJsonb recipientJsonb = new RecipientJsonb();
        recipientJsonb.setNiceName("Test User");
        recipientJsonb.setEmail("test@example.com");

        EmailEntity entity = new EmailEntity();
        entity.setId(1L);
        entity.setFrom("sender@example.com");
        entity.setTo(List.of(recipientJsonb));
        entity.setCc(List.of());
        entity.setBcc(List.of());
        entity.setSubject("Test Subject");
        entity.setBody("Test Body");
        entity.setState(EmailStatus.DRAFT);

        // When
        Email result = EmailConverter.toEmail(entity);

        // Then
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.from()).isEqualTo("sender@example.com");
        assertThat(result.to()).hasSize(1);
        assertThat(result.to().getFirst().niceName()).isEqualTo("Test User");
        assertThat(result.to().getFirst().email()).isEqualTo("test@example.com");
        assertThat(result.cc()).isEmpty();
        assertThat(result.bcc()).isEmpty();
        assertThat(result.subject()).isEqualTo("Test Subject");
        assertThat(result.body()).isEqualTo("Test Body");
        assertThat(result.state()).isEqualTo(EmailStatus.DRAFT);
    }

    @Test
    void mapRecipients_shouldConvertListOfRecipientJsonbToListOfRecipient() {
        // Given
        RecipientJsonb recipientJsonb1 = new RecipientJsonb();
        recipientJsonb1.setNiceName("Test User 1");
        recipientJsonb1.setEmail("test1@example.com");

        RecipientJsonb recipientJsonb2 = new RecipientJsonb();
        recipientJsonb2.setNiceName("Test User 2");
        recipientJsonb2.setEmail("test2@example.com");

        List<RecipientJsonb> recipientJsonbs = List.of(recipientJsonb1, recipientJsonb2);

        // When
        List<Recipient> result = EmailConverter.mapRecipients(recipientJsonbs);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).niceName()).isEqualTo("Test User 1");
        assertThat(result.get(0).email()).isEqualTo("test1@example.com");
        assertThat(result.get(1).niceName()).isEqualTo("Test User 2");
        assertThat(result.get(1).email()).isEqualTo("test2@example.com");
    }

    @Test
    void toRecipient_shouldConvertRecipientJsonbToRecipient() {
        // Given
        RecipientJsonb recipientJsonb = new RecipientJsonb();
        recipientJsonb.setNiceName("Test User");
        recipientJsonb.setEmail("test@example.com");

        // When
        Recipient result = EmailConverter.toRecipient(recipientJsonb);

        // Then
        assertThat(result.niceName()).isEqualTo("Test User");
        assertThat(result.email()).isEqualTo("test@example.com");
    }

    @Test
    void toEmailEntity_shouldConvertCreateEmailRequestToEmailEntity() {
        // Given
        Recipient recipient = Recipient.builder()
                .niceName("Test User")
                .email("test@example.com")
                .build();

        CreateEmailRequest request = new CreateEmailRequest(
                "sender@example.com",
                List.of(recipient),
                List.of(),
                List.of(),
                "Test Subject",
                "Test Body",
                EmailStatus.DRAFT
        );

        // When
        EmailEntity result = EmailConverter.toEmailEntity(request);

        // Then
        assertThat(result.getFrom()).isEqualTo("sender@example.com");
        assertThat(result.getTo()).hasSize(1);
        assertThat(result.getTo().getFirst().getNiceName()).isEqualTo("Test User");
        assertThat(result.getTo().getFirst().getEmail()).isEqualTo("test@example.com");
        assertThat(result.getCc()).isEmpty();
        assertThat(result.getBcc()).isEmpty();
        assertThat(result.getSubject()).isEqualTo("Test Subject");
        assertThat(result.getBody()).isEqualTo("Test Body");
        assertThat(result.getState()).isEqualTo(EmailStatus.DRAFT);
    }

    @Test
    void toRecipientJsonb_listVersion_shouldConvertListOfRecipientToListOfRecipientJsonb() {
        // Given
        Recipient recipient1 = Recipient.builder()
                .niceName("Test User 1")
                .email("test1@example.com")
                .build();

        Recipient recipient2 = Recipient.builder()
                .niceName("Test User 2")
                .email("test2@example.com")
                .build();

        List<Recipient> recipients = List.of(recipient1, recipient2);

        // When
        List<RecipientJsonb> result = EmailConverter.toRecipientJsonb(recipients);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getNiceName()).isEqualTo("Test User 1");
        assertThat(result.get(0).getEmail()).isEqualTo("test1@example.com");
        assertThat(result.get(1).getNiceName()).isEqualTo("Test User 2");
        assertThat(result.get(1).getEmail()).isEqualTo("test2@example.com");
    }

    @Test
    void toRecipientJsonb_singleVersion_shouldConvertRecipientToRecipientJsonb() {
        // Given
        Recipient recipient = Recipient.builder()
                .niceName("Test User")
                .email("test@example.com")
                .build();

        // When
        RecipientJsonb result = EmailConverter.toRecipientJsonb(recipient);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNiceName()).isEqualTo("Test User");
        assertThat(result.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void toRecipientJsonb_singleVersion_shouldReturnNullWhenBothFieldsAreBlank() {
        // Given
        Recipient recipient = Recipient.builder()
                .niceName("")
                .email("")
                .build();

        // When
        RecipientJsonb result = EmailConverter.toRecipientJsonb(recipient);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void toRecipientJsonb_singleVersion_shouldReturnNullWhenBothFieldsAreNull() {
        // Given
        Recipient recipient = Recipient.builder()
                .niceName(null)
                .email(null)
                .build();

        // When
        RecipientJsonb result = EmailConverter.toRecipientJsonb(recipient);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void applyEmailUpdate_shouldUpdateEmailEntityWithUpdateEmailRequest() {
        // Given
        EmailEntity entity = new EmailEntity();
        entity.setId(1L);
        entity.setFrom("old-sender@example.com");
        entity.setTo(List.of());
        entity.setCc(List.of());
        entity.setBcc(List.of());
        entity.setSubject("Old Subject");
        entity.setBody("Old Body");
        entity.setState(EmailStatus.DRAFT);

        Recipient recipient = Recipient.builder()
                .niceName("Test User")
                .email("test@example.com")
                .build();

        UpdateEmailRequest request = new UpdateEmailRequest(
                "new-sender@example.com",
                List.of(recipient),
                List.of(),
                List.of(),
                "New Subject",
                "New Body",
                EmailStatus.SENT
        );

        // When
        EmailConverter.applyEmailUpdate(entity, request);

        // Then
        assertThat(entity.getFrom()).isEqualTo("new-sender@example.com");
        assertThat(entity.getTo()).hasSize(1);
        assertThat(entity.getTo().getFirst().getNiceName()).isEqualTo("Test User");
        assertThat(entity.getTo().getFirst().getEmail()).isEqualTo("test@example.com");
        assertThat(entity.getCc()).isEmpty();
        assertThat(entity.getBcc()).isEmpty();
        assertThat(entity.getSubject()).isEqualTo("New Subject");
        assertThat(entity.getBody()).isEqualTo("New Body");
        assertThat(entity.getState()).isEqualTo(EmailStatus.SENT);
    }
}