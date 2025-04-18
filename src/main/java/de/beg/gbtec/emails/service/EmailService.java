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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static de.beg.gbtec.emails.converter.EmailConverter.toEmail;
import static de.beg.gbtec.emails.converter.EmailConverter.toEmailEntity;

@Service
public class EmailService {

    private final EmailRepository emailRepository;

    public EmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
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
}
