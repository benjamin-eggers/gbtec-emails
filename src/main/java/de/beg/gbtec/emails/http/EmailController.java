package de.beg.gbtec.emails.http;

import de.beg.gbtec.emails.model.Email;
import de.beg.gbtec.emails.service.EmailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/emails")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping
    public ResponseEntity<Page<Email>> getEmails(@NonNull Pageable pageable) {
        Page<Email> emails = emailService.getEmails(pageable);
        return ResponseEntity.ok(emails);
    }

}
