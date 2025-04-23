package de.beg.gbtec.emails.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UpdateNotAllowedException.class)
    public ResponseEntity<Problem> handleUpdateNotAllowedException(
            HttpServletRequest request
    ) {
        var problem = Problem.builder()
                .title("Update forbidden")
                .message("Updating the email is forbidden because it is not in status DRAFT")
                .status(FORBIDDEN)
                .location(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(FORBIDDEN)
                .body(problem);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<Problem> handleEmailNotFoundException(
            HttpServletRequest request
    ) {
        var problem = Problem.builder()
                .title("Not found")
                .message("Email with the given id does not exist")
                .status(NOT_FOUND)
                .location(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(NOT_FOUND)
                .body(problem);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Problem> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e,
            HttpServletRequest request
    ) {
        var validationErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .filter(fieldError -> fieldError.getCode() != null)
                .map(fieldError -> Map.entry(fieldError.getField(), fieldError.getCode()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        var problem = Problem.builder()
                .title("Validation Error")
                .message("The given request violates the validation requirements")
                .status(BAD_REQUEST)
                .location(request.getRequestURI())
                .detail(validationErrors)
                .build();

        return ResponseEntity.status(BAD_REQUEST)
                .body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Problem> handleException(
            Exception e,
            HttpServletRequest request
    ) {
        int exceptionHashCode = e.hashCode();
        log.error("[{}] Unexpected error occurred", exceptionHashCode, e);

        var problem = Problem.builder()
                .title("Internal Server Error")
                .message("An unexpected error occurred. Please try again later. If the issue persists please give the following code as reference: " + exceptionHashCode)
                .status(INTERNAL_SERVER_ERROR)
                .location(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(problem);
    }

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Problem(
            String title,
            String message,
            HttpStatus status,
            String location,
            @Nullable Object detail
    ) {
    }

}
