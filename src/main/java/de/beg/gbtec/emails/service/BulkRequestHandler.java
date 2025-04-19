package de.beg.gbtec.emails.service;

import de.beg.gbtec.emails.http.dto.*;
import de.beg.gbtec.emails.model.Email;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@Service
public class BulkRequestHandler {

    private final EmailService emailService;
    private final Validator validator;

    public BulkRequestHandler(
            EmailService emailService,
            Validator validator
    ) {
        this.emailService = emailService;
        this.validator = validator;
    }

    public BulkResponse<Integer, Email> createEmailBulk(
            BulkRequest<CreateEmailRequest> bulkRequest
    ) {
        var response = new LinkedHashMap<Integer, BulkResponseEntry<Email>>();
        for (int i = 0; i < bulkRequest.requests().size(); i++) {
            var createEmailRequest = bulkRequest.requests().get(i);
            try {
                var constraintViolations = validator.validate(createEmailRequest);
                if (constraintViolations.isEmpty()) {
                    Email email = emailService.createEmail(createEmailRequest);
                    response.put(
                            i,
                            BulkResponseEntry.<Email>builder()
                                    .status(OK)
                                    .data(email)
                                    .build()
                    );
                } else {
                    response.put(
                            i,
                            BulkResponseEntry.<Email>builder()
                                    .status(BAD_REQUEST)
                                    .message("Request is not well formed and violates the constraints")
                                    .build()
                    );
                }
            } catch (Exception e) {
                response.put(
                        i,
                        BulkResponseEntry.<Email>builder()
                                .status(INTERNAL_SERVER_ERROR)
                                .message("Could not store the email. Please try again later")
                                .build()
                );
            }
        }

        return new BulkResponse<>(response);
    }

    public BulkResponse<Long, Email> updateEmailBulk(
            BulkRequest<Map.Entry<Long, UpdateEmailRequest>> bulkRequest
    ) {
        var response = new LinkedHashMap<Long, BulkResponseEntry<Email>>();
        for (int i = 0; i < bulkRequest.requests().size(); i++) {
            var entry = bulkRequest.requests().get(i);
            var emailId = entry.getKey();
            var updateEmailRequest = entry.getValue();
            try {
                var constraintViolations = validator.validate(updateEmailRequest);
                if (constraintViolations.isEmpty()) {
                    Email email = emailService.updateEmail(emailId, updateEmailRequest);
                    response.put(
                            emailId,
                            BulkResponseEntry.<Email>builder()
                                    .status(OK)
                                    .data(email)
                                    .build()
                    );
                } else {
                    response.put(
                            emailId,
                            BulkResponseEntry.<Email>builder()
                                    .status(BAD_REQUEST)
                                    .message("Request is not well formed and violates the constraints")
                                    .build()
                    );
                }
            } catch (Exception e) {
                response.put(
                        emailId,
                        BulkResponseEntry.<Email>builder()
                                .status(INTERNAL_SERVER_ERROR)
                                .message("Could not update the email. Please try again later")
                                .build()
                );
            }
        }

        return new BulkResponse<>(response);
    }
}
