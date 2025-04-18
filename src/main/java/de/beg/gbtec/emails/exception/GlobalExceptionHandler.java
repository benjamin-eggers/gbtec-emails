package de.beg.gbtec.emails.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // TODO
    // - vernünftige Response
    // - EmailNotFoundException handlen
    // - bean validation catchbar?
    // - Generischer catch

    @ExceptionHandler(UpdateNotAllowedException.class)
    public ResponseEntity<String> handleException(
            UpdateNotAllowedException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(FORBIDDEN)
                .body("Nope, Chuck Testa");
    }
}
