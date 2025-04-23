package de.beg.gbtec.emails.http;

import de.beg.gbtec.emails.AbstractIntegrationTest;
import de.beg.gbtec.emails.api.EmailControllerApiClient;
import de.beg.gbtec.emails.http.dto.*;
import de.beg.gbtec.emails.model.Email;
import de.beg.gbtec.emails.model.Recipient;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static de.beg.gbtec.emails.RandomFactory.*;
import static de.beg.gbtec.emails.model.EmailStatus.DRAFT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.MULTI_STATUS;

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
        BulkResponseEntry<Email> firstEntry = response.results().getFirst();
        assertThat(firstEntry.status()).isEqualTo(CREATED.value());
        Email firstEmail = firstEntry.data();
        assertThat(firstEmail).isNotNull();
        BulkResponseEntry<Email> secondEntry = response.results().getLast();

    }

    private static CreateEmailRequest getValidCreateEmailRequest() {
        return CreateEmailRequest.builder()
                .from(randomEmail())
                .to(List.of(Recipient.builder().email(randomEmail()).build()))
                .subject(randomQuote())
                .body(randomText())
                .state(DRAFT)
                .build();
    }

}
