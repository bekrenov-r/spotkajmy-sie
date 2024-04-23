package com.bekrenovr.spotkajmysie.exception;

import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ProblemDetail> handleRuntimeException(RuntimeException ex, WebRequest webRequest){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createProblemDetail(
                        ex, HttpStatus.BAD_REQUEST, ex.getMessage(),
                        null, null, webRequest)
                );
    }

    @Override
    public ResponseEntity<Object> handleHandlerMethodValidationException(HandlerMethodValidationException ex, HttpHeaders headers, HttpStatusCode statusCode, WebRequest webRequest){
        List<FieldError> errors = ex.getAllValidationResults().stream()
                .flatMap(validationResult -> validationResult.getResolvableErrors().stream())
                .map(FieldError.class::cast)
                .toList();
        StringBuilder detail = new StringBuilder("Errors: ");
        for(FieldError error : errors){
            detail.append("[").append(error.getField()).append(" ").append(error.getDefaultMessage()).append("]");
        }
        return ResponseEntity.badRequest().body(createProblemDetail(
                ex, HttpStatus.BAD_REQUEST, detail.toString(), null, null, webRequest
        ));
    }
}
