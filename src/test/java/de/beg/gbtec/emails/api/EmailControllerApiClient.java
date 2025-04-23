package de.beg.gbtec.emails.api;

import de.beg.gbtec.emails.http.dto.CreateEmailRequest;
import de.beg.gbtec.emails.http.dto.UpdateEmailRequest;
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


    public ValidatableResponse deleteEmail(long id) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/emails/{id}", id)
                .then();
    }
}
