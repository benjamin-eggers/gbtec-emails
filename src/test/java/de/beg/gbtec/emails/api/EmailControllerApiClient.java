package de.beg.gbtec.emails.api;

import de.beg.gbtec.emails.http.dto.*;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class EmailControllerApiClient {

    public ValidatableResponse getEmails() {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get("/emails")
                .then();
    }

    public ValidatableResponse getEmail(Long id) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get("/emails/{id}", id)
                .then();
    }

    public ValidatableResponse createEmail(
            CreateEmailRequest request
    ) {
        return given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/emails")
                .then();
    }

    public ValidatableResponse createEmailsBulk(
            BulkRequest<RequestEntry<CreateEmailRequest>> request
    ) {
        return given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/emails/bulk")
                .then();
    }

    public ValidatableResponse updateEmail(
            long id,
            UpdateEmailRequest request
    ) {
        return given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/emails/{id}", id)
                .then();
    }

    public ValidatableResponse updateEmailsBulk(
            BulkRequest<IdentifiedRequestEntry<UpdateEmailRequest>> request
    ) {
        return given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/emails/bulk")
                .then();
    }

    public ValidatableResponse deleteEmail(long id) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/emails/{id}", id)
                .then();
    }

    public ValidatableResponse deleteEmailsBulk(
            BulkRequest<IdentifiedRequestEntry<Void>> request
    ) {
        return given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .delete("/emails/bulk")
                .then();
    }

}
