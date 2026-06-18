package by.psu.exception;

import jakarta.validation.ConstraintViolationException;
import org.jspecify.annotations.NonNull;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class CustomControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ApplicationException.class)
    public ProblemDetail handleApplicationException(@NonNull ApplicationException e) {
        var problemDetail = ProblemDetail.forStatusAndDetail(e.getHttpStatus(), e.getMessage());
        problemDetail.setTitle(e.getCode());
        return problemDetail;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(@NonNull ConstraintViolationException e) {
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT, e.getMessage());
        var errorFields = e.getConstraintViolations().stream()
                .collect(Collectors.toMap(cv -> cv.getPropertyPath().toString(),
                        cv -> (Object) cv.getMessage()));
        problemDetail.setProperty("errorFields", errorFields);
        return problemDetail;
    }

    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                               HttpStatusCode status, WebRequest request) {
        var problemDetail = ex.getBody();
        var errorFields = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, fe -> (Object) fe.getDefaultMessage()));
        problemDetail.setProperty("errorFields", errorFields);
        problemDetail.setStatus(HttpStatus.UNPROCESSABLE_CONTENT);
        problemDetail.setDetail("Ошибка валидации");
        problemDetail.setTitle("VLD-001");
        return ResponseEntity.of(problemDetail).build();
    }
}
