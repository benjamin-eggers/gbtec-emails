package de.beg.gbtec.emails.http;

import de.beg.gbtec.emails.exception.GlobalExceptionHandler.Problem;
import de.beg.gbtec.emails.http.dto.*;
import de.beg.gbtec.emails.model.Email;
import de.beg.gbtec.emails.model.PagedResponse;
import de.beg.gbtec.emails.service.BulkRequestHandler;
import de.beg.gbtec.emails.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import static de.beg.gbtec.emails.util.SwaggerExamples.*;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.MULTI_STATUS;

@RestController
@RequestMapping("/emails")
public class EmailController {

    private final EmailService emailService;
    private final BulkRequestHandler bulkRequestHandler;

    public EmailController(
            EmailService emailService,
            BulkRequestHandler bulkRequestHandler
    ) {
        this.emailService = emailService;
        this.bulkRequestHandler = bulkRequestHandler;
    }

    @Operation(
            summary = """
                    Returns an ordered page containing emails. Default order is 'id' DESC. \
                    If no emails are to be found the response will contain no entries but response body will never be null.
                    """
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Default response if no unexpected error occurred",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = PagedResponse.class)
                                    ),
                            }
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Default response if any unexpect error occurred",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = Problem.class)
                                    ),
                            }
                    ),
            }
    )
    @GetMapping
    public ResponseEntity<PagedResponse<Email>> getEmails(
            @NonNull @PageableDefault(sort = "id", direction = DESC) Pageable pageable
    ) {
        PagedResponse<Email> emails = emailService.getEmails(pageable);
        return ResponseEntity.ok(emails);
    }

    @Operation(
            summary = """
                    Looks up and returns a single email by the given id from the path.
                    """
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "If the email with the given id was found",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = Email.class),
                                            examples = @ExampleObject(value = EMAIL_EXAMPLE)
                                    ),
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "If the email with the given id wasn't found",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = Problem.class),
                                            examples = @ExampleObject(value = PROBLEM_EXAMPLE)
                                    ),
                            }
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Default response if any unexpect error occurred",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = Problem.class),
                                            examples = @ExampleObject(value = PROBLEM_EXAMPLE)
                                    ),
                            }
                    ),
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Email> getEmailById(
            @PathVariable Long id
    ) {
        Email email = emailService.getEmailById(id);
        return ResponseEntity.ok(email);
    }

    @PostMapping
    public ResponseEntity<Email> createEmail(
            @Valid @RequestBody CreateEmailRequest request
    ) {
        Email email = emailService.createEmail(request);
        return ResponseEntity.status(CREATED).body(email);
    }

    @Operation(
            summary = """
                    Offers the possibility to create up to 50 emails in a single request.
                    The response will contain a detailed entry for each request in the same order as the requests.
                    """
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "207",
                            description = "Default response if no unexpected error occurred",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = BulkResponse.class),
                                            examples = @ExampleObject(value = BULK_CREATE_RESPONSE_EXAMPLE)
                                    ),
                            }
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Default response if any unexpect error occurred",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = Problem.class),
                                            examples = @ExampleObject(value = PROBLEM_EXAMPLE)
                                    ),
                            }
                    ),
            }
    )
    @PostMapping("/bulk")
    public ResponseEntity<BulkResponse<Email>> bulkCreateEmails(
            @RequestBody @Valid BulkRequest<RequestEntry<CreateEmailRequest>> request
    ) {
        var response = bulkRequestHandler.createEmailBulk(request);
        return ResponseEntity.status(MULTI_STATUS).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Email> updateEmail(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEmailRequest request
    ) {
        Email email = emailService.updateEmail(id, request);
        return ResponseEntity.ok(email);
    }

    @PutMapping("/bulk")
    public ResponseEntity<BulkResponse<Email>> bulkUpdateEmails(
            @RequestBody @Valid BulkRequest<IdentifiedRequestEntry<UpdateEmailRequest>> request
    ) {
        var response = bulkRequestHandler.updateEmailBulk(request);
        return ResponseEntity.status(MULTI_STATUS).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmail(
            @PathVariable Long id
    ) {
        emailService.deleteEmail(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<BulkResponse<Void>> bulkDeleteEmails(
            @RequestBody @Valid BulkRequest<IdentifiedRequestEntry<Void>> request
    ) {
        var response = bulkRequestHandler.deleteEmailBulk(request);
        return ResponseEntity.status(MULTI_STATUS).body(response);
    }

}
