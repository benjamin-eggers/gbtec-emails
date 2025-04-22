package de.beg.gbtec.emails.service;

import de.beg.gbtec.emails.exception.EmailNotFoundException;
import de.beg.gbtec.emails.http.dto.*;
import de.beg.gbtec.emails.model.Email;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BulkRequestHandler {

    public static final String UPDATE_FAILED_MESSAGE = "Could not update the email. Please try again later";
    public static final String CREATE_FAILED_MESSAGE = "Could not store the email. Please try again later";
    public static final String DELETE_FAILED_MESSAGE = "Could not delete the email. Please try again later";
    public static final String NOT_FOUND_MESSAGE = "Email does not exist";
    private final EmailService emailService;

    public BulkRequestHandler(
            EmailService emailService
    ) {
        this.emailService = emailService;
    }

    public BulkResponse<Email> createEmailBulk(
            BulkRequest<RequestEntry<CreateEmailRequest>> bulkRequest
    ) {
        var result = new ArrayList<BulkResponseEntry<Email>>();
        for (RequestEntry<CreateEmailRequest> entry : bulkRequest.requests()) {
            try {
                Email email = emailService.createEmail(entry.data());
                result.add(BulkSuccess.created(email.id(), email));
            } catch (Exception e) {
                result.add(BulkError.internalServerError(CREATE_FAILED_MESSAGE));
            }
        }

        return BulkResponse.of(result);
    }

    public BulkResponse<Email> updateEmailBulk(
            BulkRequest<IdentifiedRequestEntry<UpdateEmailRequest>> bulkRequest
    ) {
        var result = new ArrayList<BulkResponseEntry<Email>>();
        for (IdentifiedRequestEntry<UpdateEmailRequest> entry : bulkRequest.requests()) {
            Long emailId = entry.id();
            try {
                Email email = emailService.updateEmail(emailId, entry.data());
                result.add(BulkSuccess.ok(emailId, email));
            } catch (EmailNotFoundException e) {
                result.add(BulkError.notFound(emailId, NOT_FOUND_MESSAGE));
            } catch (Exception e) {
                result.add(BulkError.internalServerError(emailId, UPDATE_FAILED_MESSAGE));
            }
        }

        return BulkResponse.of(result);
    }

    public BulkResponse<Long> deleteEmailBulk(
            BulkRequest<IdentifiedRequestEntry<Void>> bulkRequest
    ) {
        var result = new ArrayList<BulkResponseEntry<Long>>();
        for (IdentifiedRequestEntry<Void> entry : bulkRequest.requests()) {
            Long emailId = entry.id();
            try {
                emailService.deleteEmail(emailId);
                result.add(BulkSuccess.ok(emailId));
            } catch (EmailNotFoundException e) {
                result.add(BulkError.notFound(emailId, NOT_FOUND_MESSAGE));
            } catch (Exception e) {
                result.add(BulkError.internalServerError(emailId, DELETE_FAILED_MESSAGE));
            }
        }

        return BulkResponse.of(result);
    }

}
