package de.beg.gbtec.emails.http;

import de.beg.gbtec.emails.http.dto.CreateEmailRequest;
import de.beg.gbtec.emails.http.dto.UpdateEmailRequest;
import de.beg.gbtec.emails.model.Email;
import de.beg.gbtec.emails.model.PagedResponse;
import de.beg.gbtec.emails.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emails")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping
    public ResponseEntity<PagedResponse<Email>> getEmails(
            @NonNull Pageable pageable
    ) {
        PagedResponse<Email> emails = emailService.getEmails(pageable);
        return ResponseEntity.ok(emails);
    }

    @PostMapping
    public ResponseEntity<Email> createEmail(
            @Valid @RequestBody CreateEmailRequest request
    ) {
        Email email = emailService.createEmail(request);
        return ResponseEntity.ok(email);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Email> updateEmail(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEmailRequest request
    ) {
        Email email = emailService.updateEmail(id, request);
        return ResponseEntity.ok(email);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmail(
            @PathVariable Long id
    ) {
        emailService.deleteEmail(id);
        return ResponseEntity.ok().build();
    }

}
