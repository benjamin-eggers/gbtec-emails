package de.beg.gbtec.emails.http;

import de.beg.gbtec.emails.AbstractIntegrationTest;
import de.beg.gbtec.emails.api.EmailControllerApiClient;
import de.beg.gbtec.emails.http.dto.BulkRequest;
import de.beg.gbtec.emails.http.dto.BulkResponse;
import de.beg.gbtec.emails.http.dto.CreateEmailRequest;
import de.beg.gbtec.emails.http.dto.RequestEntry;
import de.beg.gbtec.emails.model.Email;
import de.beg.gbtec.emails.model.Recipient;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

import static de.beg.gbtec.emails.RandomFactory.*;
import static de.beg.gbtec.emails.model.EmailStatus.DRAFT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

public class EmailControllerBulkTest extends AbstractIntegrationTest {

    @Autowired
    private EmailControllerApiClient apiClient;

    @Test
    void bulkCreateEmails_shouldReturnSuccessfulForEmptyRequest() {
        var response = apiClient.createEmailsBulk(new BulkRequest<>(null))
                .statusCode(MULTI_STATUS.value())
                .extract()
                .body()
                .as(new TypeRef<BulkResponse<Email>>() {
                });

        assertThat(response).isNotNull();
        assertThat(response.results()).isEmpty();
    }

    @Test
    void bulkCreateEmails_shouldReturnBadRequestForTooManyRequests() {
        // It is only allowed to submit a maximum of 50 individual requests
        var requests = Stream.generate(this::getValidCreateEmailRequest)
                .limit(60)
                .map(RequestEntry::of)
                .toList();

        apiClient.createEmailsBulk(new BulkRequest<>(requests))
                .statusCode(BAD_REQUEST.value());
    }

    @Test
    void bulkCreateEmails_shouldReturnBadRequestForInvalidRequest() {
        var requests = List.of(
                RequestEntry.of(
                        CreateEmailRequest.builder().build()
                )
        );
        apiClient.createEmailsBulk(new BulkRequest<>(requests))
                .statusCode(BAD_REQUEST.value());
    }

    @Test
    void bulkCreateEmails_shouldReturnSuccessfulForRequests() {
        var firstValidRequest = getValidCreateEmailRequest();
        var secondValidRequest = getValidCreateEmailRequest();
        var bulkRequest = new BulkRequest<>(
                List.of(
                        RequestEntry.of(firstValidRequest),
                        RequestEntry.of(secondValidRequest)
                )
        );
        var response = apiClient.createEmailsBulk(bulkRequest)
                .statusCode(MULTI_STATUS.value())
                .extract()
                .body()
                .as(new TypeRef<BulkResponse<Email>>() {
                });

        assertThat(response).isNotNull();
        assertThat(response.results()).hasSize(2);

        var firstBulkResponseEntry = response.results().getFirst();
        assertThat(firstBulkResponseEntry.status()).isEqualTo(CREATED.value());
        assertCreatedEmail(firstBulkResponseEntry.data(), firstValidRequest);

        var lastBulkResponseEntry = response.results().getLast();
        assertThat(lastBulkResponseEntry.status()).isEqualTo(CREATED.value());
        assertCreatedEmail(lastBulkResponseEntry.data(), secondValidRequest);
    }

    private void assertCreatedEmail(
            Email firstEmail,
            CreateEmailRequest firstValidRequest
    ) {
        assertThat(firstEmail).isNotNull();
        assertThat(firstEmail.id()).isNotNull();
        assertThat(firstEmail.from()).isEqualTo(firstValidRequest.from());
        String[] toEmails = firstValidRequest.to()
                .stream()
                .map(Recipient::email)
                .toArray(String[]::new);
        assertThat(firstEmail.to())
                .map(Recipient::email)
                .containsExactly(toEmails);
        assertThat(firstEmail.subject()).isEqualTo(firstValidRequest.subject());
        assertThat(firstEmail.body()).isEqualTo(firstValidRequest.body());
        assertThat(firstEmail.state()).isEqualTo(firstValidRequest.state());
    }

    private CreateEmailRequest getValidCreateEmailRequest() {
        return CreateEmailRequest.builder()
                .from(randomEmail())
                .to(List.of(Recipient.builder().email(randomEmail()).build()))
                .subject(randomQuote())
                .body(randomText())
                .state(DRAFT)
                .build();
    }

}
