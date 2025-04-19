package de.beg.gbtec.emails.service;

import de.beg.gbtec.emails.converter.EmailConverter;
import de.beg.gbtec.emails.exception.EmailNotFoundException;
import de.beg.gbtec.emails.exception.UpdateNotAllowedException;
import de.beg.gbtec.emails.http.dto.*;
import de.beg.gbtec.emails.model.Email;
import de.beg.gbtec.emails.model.EmailStatus;
import de.beg.gbtec.emails.model.PagedResponse;
import de.beg.gbtec.emails.repository.EmailRepository;
import de.beg.gbtec.emails.repository.dto.EmailEntity;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Set;

import static de.beg.gbtec.emails.converter.EmailConverter.toEmail;
import static de.beg.gbtec.emails.converter.EmailConverter.toEmailEntity;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@Service
public class EmailService {

    private final EmailRepository emailRepository;
    private final Set<String> knownSpamAddresses;
    private final int knownSpamQueryLimit;

    public EmailService(
            EmailRepository emailRepository,
            @Value("${de.beg.gbtec.emails.known-spam-addresses}") Set<String> knownSpamAddresses,
            @Value("${de.beg.gbtec.emails.known-spam-query-limit:2}") int knownSpamQueryLimit
    ) {
        this.emailRepository = emailRepository;
        this.knownSpamAddresses = knownSpamAddresses;
        this.knownSpamQueryLimit = knownSpamQueryLimit;
    }

    public PagedResponse<Email> getEmails(
            Pageable pageable
    ) {
        Page<EmailEntity> entities = emailRepository.findAll(pageable);
        return PagedResponse.<Email>builder()
                .entries(EmailConverter.toEmails(entities))
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .hasNext(entities.hasNext())
                .build();
    }

    public BulkResponse<Integer, Email> createEmailBulk(
            BulkRequest<CreateEmailRequest> bulkRequest
    ) {
        var response = new LinkedHashMap<Integer, BulkResponseEntry<Email>>();
        for (int i = 0; i < bulkRequest.requests().size(); i++) {
            var createEmailRequest = bulkRequest.requests().get(i);
            try {
                Email email = createEmail(createEmailRequest);
                response.put(i, new BulkResponseEntry<>(OK, email));
            } catch (Exception e) {
                response.put(i, new BulkResponseEntry<>(INTERNAL_SERVER_ERROR, null));
            }
        }

        return new BulkResponse<>(response);
    }

    public Email createEmail(
            CreateEmailRequest request
    ) {
        EmailEntity entity = toEmailEntity(request);
        EmailEntity savedEmail = emailRepository.save(entity);
        return toEmail(savedEmail);
    }

    public Email updateEmail(
            Long id,
            UpdateEmailRequest request
    ) {
        EmailEntity entity = emailRepository.findById(id)
                .orElseThrow(EmailNotFoundException::new);

        assertEmailUpdateAllowed(entity);

        EmailConverter.applyEmailUpdate(entity, request);
        EmailEntity updatedEmail = emailRepository.save(entity);

        return EmailConverter.toEmail(updatedEmail);
    }


    private void assertEmailUpdateAllowed(EmailEntity entity) {
        if (!entity.getState().equals(EmailStatus.DRAFT)) {
            throw new UpdateNotAllowedException();
        }
    }

    public void deleteEmail(Long id) {

    }

    public void markEmailsAsSpam() {
        knownSpamAddresses.forEach(this::markEmailAsSpam);
    }

    @SneakyThrows
    private void markEmailAsSpam(String emailAddress) {
        Slice<EmailEntity> entitySlice = emailRepository.findEmailEntitiesContainingAddress(emailAddress, knownSpamQueryLimit);
        while (!entitySlice.isEmpty() || entitySlice.hasNext()) {
            entitySlice.forEach(emailEntity -> {
                        emailEntity.setState(EmailStatus.SPAM);
                        emailRepository.save(emailEntity);
                    }
            );
            entitySlice = emailRepository.findEmailEntitiesContainingAddress(emailAddress, knownSpamQueryLimit);
        }
    }

}
