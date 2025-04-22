package de.beg.gbtec.emails.http;

import de.beg.gbtec.emails.AbstractIntegrationTest;
import de.beg.gbtec.emails.http.dto.CreateEmailRequest;
import de.beg.gbtec.emails.model.Email;
import de.beg.gbtec.emails.model.PagedResponse;
import de.beg.gbtec.emails.model.Recipient;
import de.beg.gbtec.emails.repository.EmailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static de.beg.gbtec.emails.RandomFactory.*;
import static de.beg.gbtec.emails.model.EmailStatus.DRAFT;
import static de.beg.gbtec.emails.model.EmailStatus.SENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

class EmailControllerTest extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmailRepository emailRepository;

    @BeforeEach
    void setUp() {
        emailRepository.deleteAll();
    }

    @Test
    void getEmails() {
        var typeRef = new ParameterizedTypeReference<PagedResponse<Email>>() {
        };
        var response = restTemplate.exchange("/emails", HttpMethod.GET, null, typeRef);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
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

        var response = restTemplate.postForEntity("/emails", request, Email.class);
        Email responseBody = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(CREATED);
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.id()).isNotNull();
        assertThat(responseBody.from()).isEqualTo(from);
        assertThat(responseBody.to()).map(Recipient::email).containsExactly(firstRecipient, secondRecipient);
        assertThat(responseBody.cc()).map(Recipient::email).containsExactly(firstCc, secondCc);
        assertThat(responseBody.bcc()).map(Recipient::email).containsExactly(firstBcc, secondBcc);
        assertThat(responseBody.subject()).isEqualTo(subject);
        assertThat(responseBody.body()).isEqualTo(body);
        assertThat(responseBody.state()).isEqualTo(DRAFT);
    }

    @ParameterizedTest
    @MethodSource("getFailingCreateEmailRequests")
    void createEmail_shouldReturnBadRequestDueToValidationErrors(CreateEmailRequest request) {
        var response = restTemplate.postForEntity("/emails", request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
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
                // Failing because of invalid 'from' email address
                Arguments.of(
                        CreateEmailRequest.builder().from(gibberish).state(SENT).build()
                ),
                // Failing because of invalid 'to' email address
                Arguments.of(
                        CreateEmailRequest.builder().to(toRecipients(gibberish)).state(SENT).build()
                ),
                // Failing because of invalid 'cc' email address
                Arguments.of(
                        CreateEmailRequest.builder().cc(toRecipients(gibberish)).state(SENT).build()
                ),
                // Failing because of invalid 'bcc' email address
                Arguments.of(
                        CreateEmailRequest.builder().bcc(toRecipients(gibberish)).state(SENT).build()
                )
        );
    }

}