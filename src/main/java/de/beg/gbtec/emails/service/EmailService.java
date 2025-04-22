package de.beg.gbtec.emails.service;

import de.beg.gbtec.emails.converter.EmailConverter;
import de.beg.gbtec.emails.exception.EmailNotFoundException;
import de.beg.gbtec.emails.exception.UpdateNotAllowedException;
import de.beg.gbtec.emails.http.dto.CreateEmailRequest;
import de.beg.gbtec.emails.http.dto.UpdateEmailRequest;
import de.beg.gbtec.emails.model.Email;
import de.beg.gbtec.emails.model.EmailStatus;
import de.beg.gbtec.emails.model.PagedResponse;
import de.beg.gbtec.emails.repository.EmailRepository;
import de.beg.gbtec.emails.repository.dto.EmailEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.Set;

import static de.beg.gbtec.emails.converter.EmailConverter.*;
import static de.beg.gbtec.emails.model.EmailStatus.DELETED;

@Service
public class EmailService {

    private final EmailRepository emailRepository;
    private final Set<String> knownSpamAddresses;
    private final int knownSpamQueryLimit;

    public EmailService(
            EmailRepository emailRepository,
            @Value("${de.beg.gbtec.emails.known-spam-addresses}") Set<String> knownSpamAddresses,
            @Value("${de.beg.gbtec.emails.known-spam-query-limit:100}") int knownSpamQueryLimit
    ) {
        this.emailRepository = emailRepository;
        this.knownSpamAddresses = knownSpamAddresses;
        this.knownSpamQueryLimit = knownSpamQueryLimit;
    }

    public PagedResponse<Email> getEmails(
            Pageable pageable
    ) {
        Slice<EmailEntity> entities = emailRepository.findAll(pageable);
        return PagedResponse.<Email>builder()
                .entries(toEmails(entities.getContent()))
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .hasNext(entities.hasNext())
                .build();
    }

    public Email createEmail(
            CreateEmailRequest request
    ) {
        EmailEntity savedEmail = emailRepository.save(toEmailEntity(request));
        return toEmail(savedEmail);
    }

    public Email updateEmail(
            Long id,
            UpdateEmailRequest request
    ) {
        EmailEntity entity = emailRepository.findById(id)
                .orElseThrow(EmailNotFoundException::new);

        assertEmailUpdateAllowed(entity);

        applyEmailUpdate(entity, request);
        EmailEntity updatedEmail = emailRepository.save(entity);

        return EmailConverter.toEmail(updatedEmail);
    }

    public void deleteEmail(
            Long id
    ) {
        EmailEntity emailEntity = emailRepository.findById(id)
                .orElseThrow(EmailNotFoundException::new);
        emailEntity.setState(DELETED);
        emailRepository.save(emailEntity);
    }

    public void markEmailsAsSpam() {
        knownSpamAddresses.forEach(this::markEmailsAsSpam);
    }

    private void markEmailsAsSpam(
            String emailAddress
    ) {
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

    private void assertEmailUpdateAllowed(
            EmailEntity entity
    ) {
        if (!entity.getState().equals(EmailStatus.DRAFT)) {
            throw new UpdateNotAllowedException();
        }
    }

}
