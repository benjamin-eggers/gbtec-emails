package de.beg.gbtec.emails.service;

import de.beg.gbtec.emails.model.Email;
import de.beg.gbtec.emails.model.EmailEntity;
import de.beg.gbtec.emails.repository.EmailRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final EmailRepository emailRepository;

    public EmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    public Page<Email> getEmails(Pageable pageable) {
        return emailRepository.findAll(pageable).map(this::toEmail);
    }

    private Email toEmail(EmailEntity entity) {
        return Email.builder()
                .emailId(entity.getId())
                .emailFrom(entity.getFrom())
                .emailTo(entity.getTo())
                .emailCc(entity.getCc())
                .emailBcc(entity.getBcc())
                .emailSubject(entity.getSubject())
                .emailBody(entity.getBody())
                .state(entity.getState())
                .build();
    }
}
