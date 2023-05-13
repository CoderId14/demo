package com.example.demo.exceptions;


import com.example.demo.dto.response.ApiResponseError;
import com.example.demo.dto.response.ErrorResponse;
import com.example.demo.exceptions.auth.TokenRefreshException;
import com.example.demo.exceptions.auth.UnauthorizedException;
import com.example.demo.exceptions.user.AccountActiveException;
import com.example.demo.exceptions.user.ResourceExistsException;
import com.example.demo.exceptions.user.TokenInvalidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.naming.AuthenticationException;
import java.util.*;

import static org.springframework.http.HttpStatus.*;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(TokenInvalidException.class)
    public ResponseEntity<?> resolveException(TokenInvalidException ex) {
        return new ResponseEntity<>(getBody(BAD_REQUEST, ex, ex.getMessage()), new HttpHeaders(), BAD_REQUEST);
    }

    @ExceptionHandler(AccountActiveException.class)
    public ResponseEntity<?> resolveException(AccountActiveException ex) {
        return new ResponseEntity<>(getBody(BAD_REQUEST, ex, ex.getMessage()), new HttpHeaders(), BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> resolveException(UnauthorizedException ex) {
        return new ResponseEntity<>(getBody(BAD_REQUEST, ex, ex.getMessage()), new HttpHeaders(), BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> resolveException(AuthenticationException ex) {
        return new ResponseEntity<>(getBody(BAD_REQUEST, ex, ex.getMessage()), new HttpHeaders(), BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(BadRequestException ex) {
        return new ResponseEntity<>(getBody(BAD_REQUEST, ex, ex.getMessage()), new HttpHeaders(), BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(getBody(BAD_REQUEST, ex, ex.getMessage()), new HttpHeaders(), BAD_REQUEST);
    }

    @ExceptionHandler(ResourceExistsException.class)
    public ResponseEntity<Object> resolveException(ResourceExistsException ex) {
        return new ResponseEntity<>(getBody(BAD_REQUEST, ex, ex.getMessage()), new HttpHeaders(), BAD_REQUEST);
    }

    @ExceptionHandler(ParameterException.class)
    public ResponseEntity<?> resolveException(ParameterException ex) {
        return new ResponseEntity<>(getBody(BAD_REQUEST, ex, ex.getMessage()), new HttpHeaders(), BAD_REQUEST);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> resolveException(AccessDeniedException ex) {
        return new ResponseEntity<>(getBody(BAD_REQUEST, ex, ex.getMessage()), new HttpHeaders(), BAD_REQUEST);
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> resolveException (MissingServletRequestParameterException ex) {
        return new ResponseEntity<>(getBody(BAD_REQUEST, ex, ex.getMessage()), new HttpHeaders(), BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> resolveException (MethodArgumentTypeMismatchException ex) {
        return new ResponseEntity<>(getBody(BAD_REQUEST, ex, ex.getName() + " invalid"), new HttpHeaders(), BAD_REQUEST);
    }
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> resolveException(BindException ex) {
        log.info("BindException");
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
        ex.getGlobalErrors().forEach(e -> errors.put(e.getObjectName(), e.getDefaultMessage()));

        if (errors.containsKey("createBookRequest")) {
            ApiResponseError responseError = new ApiResponseError();
            List<ErrorResponse> errorResponses = new ArrayList<>();
            errors.forEach((k, v) -> {
                ErrorResponse errorResponse = ErrorResponse.builder()
                        .error(k)
                        .message(v)
                        .build();
                errorResponses.add(errorResponse);
            });
            responseError.setErrors(errorResponses);
            return ResponseEntity.badRequest().body(responseError);
        } else {
            return ResponseEntity.badRequest().body(errors);
        }
    }

    @ExceptionHandler(TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<?> handleTokenRefreshException(TokenRefreshException ex) {
        return new ResponseEntity<>(getBody(FORBIDDEN, ex, ex.getMessage()), new HttpHeaders(), FORBIDDEN);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exception(Exception ex) {
        return new ResponseEntity<>(getBody(INTERNAL_SERVER_ERROR, ex, ex.getMessage()), new HttpHeaders(), INTERNAL_SERVER_ERROR);
    }

    public Map<String, Object> getBody(HttpStatus status, Exception ex, String message) {

        log.error(message, ex);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", message);
        body.put("timestamp", new Date());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("exception", ex.toString());

        Throwable cause = ex.getCause();
        if (cause != null) {
            body.put("exceptionCause", ex.getCause().toString());
        }
        return body;
    }
}
