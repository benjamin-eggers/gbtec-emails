package de.beg.gbtec.emails.service;

import de.beg.gbtec.emails.http.dto.*;
import de.beg.gbtec.emails.model.Email;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
public class BulkRequestHandler {

    public static final String UPDATE_FAILED_MESSAGE = "Could not update the email. Please try again later";
    public static final String CREATE_FAILED_MESSAGE = "Could not store the email. Please try again later";
    public static final String DELETE_FAILED_MESSAGE = "Could not delete the email. Please try again later";
    private final EmailService emailService;

    public BulkRequestHandler(
            EmailService emailService
    ) {
        this.emailService = emailService;
    }

    public BulkResponse<Email> createEmailBulk(
            BulkRequest<RequestEntry<CreateEmailRequest>> bulkRequest
    ) {
        var result = new LinkedList<BulkResponseEntry<Email>>();
        for (int i = 0; i < bulkRequest.requests().size(); i++) {
            var entry = bulkRequest.requests().get(i);
            try {
                Email email = emailService.createEmail(entry.data());
                result.add(BulkSuccess.ok(email));
            } catch (Exception e) {
                result.add(BulkError.internalServerError(CREATE_FAILED_MESSAGE));
            }
        }

        return BulkResponse.of(result);
    }

    public BulkResponse<Email> updateEmailBulk(
            BulkRequest<IdentifiedRequestEntry<UpdateEmailRequest>> bulkRequest
    ) {
        var result = new LinkedList<BulkResponseEntry<Email>>();
        for (int i = 0; i < bulkRequest.requests().size(); i++) {
            var entry = bulkRequest.requests().get(i);
            try {
                Email email = emailService.updateEmail(entry.id(), entry.data());
                result.add(BulkSuccess.ok(email));

            } catch (Exception e) {
                result.add(BulkError.internalServerError(UPDATE_FAILED_MESSAGE));
            }
        }

        return BulkResponse.of(result);
    }

    public BulkResponse<Long> deleteEmailBulk(
            BulkRequest<IdentifiedRequestEntry<Void>> bulkRequest
    ) {
        var result = new LinkedList<BulkResponseEntry<Long>>();
        for (int i = 0; i < bulkRequest.requests().size(); i++) {
            var entry = bulkRequest.requests().get(i);
            Long id = entry.id();
            try {
                emailService.deleteEmail(id);
                result.add(BulkSuccess.ok(id));
            } catch (Exception e) {
                result.add(BulkError.internalServerError(DELETE_FAILED_MESSAGE));
            }
        }
        return BulkResponse.of(result);
    }

}
