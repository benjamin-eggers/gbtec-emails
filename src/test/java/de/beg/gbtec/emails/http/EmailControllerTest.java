package de.beg.gbtec.emails.http;

import de.beg.gbtec.emails.AbstractIntegrationTest;
import de.beg.gbtec.emails.api.EmailControllerApiClient;
import de.beg.gbtec.emails.http.dto.CreateEmailRequest;
import de.beg.gbtec.emails.http.dto.UpdateEmailRequest;
import de.beg.gbtec.emails.model.Email;
import de.beg.gbtec.emails.model.PagedResponse;
import de.beg.gbtec.emails.model.Recipient;
import de.beg.gbtec.emails.repository.EmailRepository;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static de.beg.gbtec.emails.RandomFactory.*;
import static de.beg.gbtec.emails.model.EmailStatus.DRAFT;
import static de.beg.gbtec.emails.model.EmailStatus.SENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

class EmailControllerTest extends AbstractIntegrationTest {

    @Autowired
    private EmailControllerApiClient apiClient;

    @Autowired
    private EmailRepository emailRepository;

    @Value("${spring.data.web.pageable.max-page-size}")
    private long maxPageSize;

    private final TypeRef<PagedResponse<Email>> pagedResponseTypeRef = new TypeRef<>() {
    };

    @BeforeEach
    void setUp() {
        emailRepository.deleteAll();
    }

    @Test
    void getEmails_shouldReturnEmptyResponse() {
        PagedResponse<Email> emailPagedResponse = apiClient.getEmails()
                .statusCode(OK.value())
                .extract()
                .body()
                .as(pagedResponseTypeRef);

        assertThat(emailPagedResponse).isNotNull();
        assertThat(emailPagedResponse.entries()).isEmpty();
        assertThat(emailPagedResponse.page()).isEqualTo(0);
        assertThat(emailPagedResponse.size()).isStrictlyBetween(0L, maxPageSize);
        assertThat(emailPagedResponse.hasNext()).isFalse();
    }

    @Test
    void createEmail_shouldCreateEmail() {
        var from = randomEmail();
        var firstRecipient = randomEmail();
        var secondRecipient = randomEmail();
        var firstCc = randomEmail();
        var secondCc = randomEmail();
        var firstBcc = randomEmail();
        var secondBcc = randomEmail();
        var subject = randomQuote();
        var body = randomText();

        var request = CreateEmailRequest.builder()
                .from(from)
                .to(toRecipients(firstRecipient, secondRecipient))
                .cc(toRecipients(firstCc, secondCc))
                .bcc(toRecipients(firstBcc, secondBcc))
                .subject(subject)
                .body(body)
                .state(DRAFT)
                .build();

        var responseBody = apiClient.createEmail(request)
                .statusCode(CREATED.value())
                .extract()
                .body()
                .as(Email.class);

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.id()).isNotNull();
        assertThat(responseBody.from()).isEqualTo(from);
        assertThat(responseBody.to())
                .map(Recipient::email)
                .containsExactly(firstRecipient, secondRecipient);
        assertThat(responseBody.cc())
                .map(Recipient::email)
                .containsExactly(firstCc, secondCc);
        assertThat(responseBody.bcc())
                .map(Recipient::email)
                .containsExactly(firstBcc, secondBcc);
        assertThat(responseBody.subject()).isEqualTo(subject);
        assertThat(responseBody.body()).isEqualTo(body);
        assertThat(responseBody.state()).isEqualTo(DRAFT);
    }

    @Test
    void updateEmail_shouldUpdateEmail() {
        var from = randomEmail();
        var initialRecipient = randomEmail();

        var createdEmail = apiClient.createEmail(
                        CreateEmailRequest.builder()
                                .from(from)
                                .to(toRecipients(initialRecipient))
                                .state(DRAFT)
                                .build()
                )
                .statusCode(CREATED.value())
                .extract()
                .body()
                .as(Email.class);
        assertThat(createdEmail).isNotNull();
        assertThat(createdEmail.id()).isNotNull();
        assertThat(createdEmail.from()).isEqualTo(from);
        assertThat(createdEmail.to())
                .map(Recipient::email)
                .containsExactly(initialRecipient);
        assertThat(createdEmail.cc()).isEmpty();
        assertThat(createdEmail.bcc()).isEmpty();
        assertThat(createdEmail.subject()).isNull();
        assertThat(createdEmail.body()).isNull();
        assertThat(createdEmail.state()).isEqualTo(DRAFT);

        var updatedRecipient = randomEmail();
        var subject = randomQuote();
        var body = randomText();
        var updateRequest = UpdateEmailRequest.builder()
                .from(from)
                .to(toRecipients(updatedRecipient))
                .subject(subject)
                .body(body)
                .state(SENT)
                .build();

        var updatedEmail = apiClient.updateEmail(createdEmail.id(), updateRequest)
                .statusCode(OK.value())
                .extract()
                .body()
                .as(Email.class);

        assertThat(updatedEmail).isNotNull();
        assertThat(updatedEmail.id()).isEqualTo(createdEmail.id());
        assertThat(updatedEmail.from()).isEqualTo(from);
        assertThat(updatedEmail.to())
                .map(Recipient::email)
                .containsExactly(updatedRecipient);
        assertThat(updatedEmail.cc()).isEmpty();
        assertThat(updatedEmail.bcc()).isEmpty();
        assertThat(updatedEmail.subject()).isEqualTo(subject);
        assertThat(updatedEmail.body()).isEqualTo(body);
        assertThat(updatedEmail.state()).isEqualTo(SENT);
    }

    @ParameterizedTest
    @MethodSource("getFailingCreateEmailRequests")
    void createEmail_shouldReturnBadRequestDueToValidationErrors(CreateEmailRequest request) {
        apiClient.createEmail(request)
                .statusCode(BAD_REQUEST.value());
    }

    private static List<Recipient> toRecipients(String... emails) {
        return Arrays.stream(emails)
                .map(email ->
                        Recipient.builder()
                                .email(email)
                                .build()
                ).toList();
    }

    public static Stream<Arguments> getFailingCreateEmailRequests() {
        var gibberish = "cheese & wine";
        return Stream.of(
                // Failing because no STATE is given
                Arguments.of(
                        CreateEmailRequest.builder().build()
                ),
                // Failing because of an invalid 'from' email address
                Arguments.of(
                        CreateEmailRequest.builder().from(gibberish).state(SENT).build()
                ),
                // Failing because of an invalid 'to' email address
                Arguments.of(
                        CreateEmailRequest.builder().to(toRecipients(gibberish)).state(SENT).build()
                ),
                // Failing because of an invalid 'cc' email address
                Arguments.of(
                        CreateEmailRequest.builder().cc(toRecipients(gibberish)).state(SENT).build()
                ),
                // Failing because of an invalid 'bcc' email address
                Arguments.of(
                        CreateEmailRequest.builder().bcc(toRecipients(gibberish)).state(SENT).build()
                )
        );
    }


}