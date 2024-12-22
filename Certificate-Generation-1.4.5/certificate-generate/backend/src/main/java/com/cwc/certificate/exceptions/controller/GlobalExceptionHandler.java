package com.cwc.certificate.exceptions.controller;

import com.cwc.certificate.exceptions.model.*;
import com.cwc.certificate.exceptions.response.ErrorResponse;
import com.cwc.certificate.validations.APIResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(RateLimitExceededException.class)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ResponseEntity<ErrorResponse> rateLimitExceededException(RateLimitExceededException ex, HttpServletRequest request) {
        log.info("Rate limit exceeded");
        return  ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS.value())
                .body(ErrorResponse.builder()
                        .errorID(UUID.randomUUID())
                        .statusCode(HttpStatus.TOO_MANY_REQUESTS.value())
                        .timestamp(LocalDateTime.now(Clock.systemUTC()))
                        .message("Rate limit exceeded")
                        .description(ex.getMessage())
                        .path(request.getRequestURI())
                        .build());
    }


    @ExceptionHandler(DuplicateDataFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> duplicateDataFound(DuplicateDataFound ex, HttpServletRequest request) {
        log.info("Duplicate key found update your data ! {} ");
        return  ResponseEntity.status(HttpStatus.NOT_FOUND.value())
                .body(ErrorResponse.builder()
                        .errorID(UUID.randomUUID())
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .timestamp(LocalDateTime.now(Clock.systemUTC()))
                        .message("Duplicate key found update your data")
                        .description(ex.getMessage())
                        .path(request.getRequestURI())
                        .build());
    }

    @ExceptionHandler(CandidateNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> candidateNotFoundException(CandidateNotFoundException ex, HttpServletRequest request) {
        log.info("Candidate not found with this Id! {} ");
        return  ResponseEntity.status(HttpStatus.NOT_FOUND.value())
                .body(ErrorResponse.builder()
                        .errorID(UUID.randomUUID())
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .timestamp(LocalDateTime.now(Clock.systemUTC()))
                        .message("Candidate Not Found")
                        .description(ex.getMessage())
                        .path(request.getRequestURI())
                        .build());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        log.info("Resource not found with this Id!");
        return  ResponseEntity.status(HttpStatus.NOT_FOUND.value())
                .body(ErrorResponse.builder()
                        .errorID(UUID.randomUUID())
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .timestamp(LocalDateTime.now(Clock.systemUTC()))
                        .message("Resource Not Found")
                        .description(ex.getMessage())
                        .path(request.getRequestURI())
                        .build());
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyFoundException(EmailAlreadyExistException ex, HttpServletRequest request) {
        log.info("Email already exists try with another one!");
        return  ResponseEntity.status(HttpStatus.NOT_FOUND.value())
                .body(ErrorResponse.builder()
                        .errorID(UUID.randomUUID())
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .timestamp(LocalDateTime.now(Clock.systemUTC()))
                        .message("Email already exists try with another one")
                        .description(ex.getMessage())
                        .path(request.getRequestURI())
                        .build());
    }


    @ExceptionHandler(GoogleScriptNotVaildException.class)
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<ErrorResponse> handleGoogleScriptNotVaildException(GoogleScriptNotVaildException ex, HttpServletRequest request) {
         log.info("Google Script Url is empty or not valid url or invalid url format!");
        return  ResponseEntity.status(HttpStatus.NOT_FOUND.value())
                .body(ErrorResponse.builder()
                        .errorID(UUID.randomUUID())
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .timestamp(LocalDateTime.now(Clock.systemUTC()))
                        .message("Google Script Url is empty or not valid url or invalid url format")
                        .description(ex.getMessage())
                        .path(request.getRequestURI())
                        .build());
    }


    @ExceptionHandler(GoogleScriptNotFoundException.class)
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<ErrorResponse> handleGoogleScriptNotFoundException(GoogleScriptNotFoundException ex, HttpServletRequest request) {
        log.info("Google Script Url is empty or not valid url or invalid url format!");
        return  ResponseEntity.status(HttpStatus.NOT_FOUND.value())
                .body(ErrorResponse.builder()
                        .errorID(UUID.randomUUID())
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .timestamp(LocalDateTime.now(Clock.systemUTC()))
                        .message("Google Script Url is empty or not valid url or invalid url format")
                        .description(ex.getMessage())
                        .path(request.getRequestURI())
                        .build());
    }

    @ExceptionHandler(DocumentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleDocumentNotFoundException(DocumentNotFoundException ex, HttpServletRequest request) {
        log.info("Document not found with this Id!");
        return  ResponseEntity.status(HttpStatus.NOT_FOUND.value())
                .body(ErrorResponse.builder()
                        .errorID(UUID.randomUUID())
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .timestamp(LocalDateTime.now(Clock.systemUTC()))
                        .message("Document Not Found")
                        .description(ex.getMessage())
                        .path(request.getRequestURI())
                        .build());
    }

    @ExceptionHandler(CompanyNotFoundException.class)
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<ErrorResponse> handleCompanyNotFoundException(CompanyNotFoundException ex, HttpServletRequest request) {
        Map<String, Object> responseValues = new HashMap<>();
        responseValues.put("Error Id", UUID.randomUUID());
        responseValues.put("statusCode", HttpStatus.FOUND.value());
        responseValues.put("timestamp",LocalDateTime.now(Clock.systemUTC()));
        responseValues.put("message", "Company not found with this name");
        responseValues.put("description", ex.getMessage());
        responseValues.put("path", request.getRequestURI());
        log.info("Company not found with this name!");
        ErrorResponse response = buildApiResponse(responseValues);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    private ErrorResponse buildApiResponse(Map<String, Object> responseValues) {
        ErrorResponse response = new ErrorResponse();
        response.setErrorID((UUID) responseValues.get("Error Id"));
        response.setStatusCode((Integer) responseValues.get("statusCode"));
        response.setTimestamp((LocalDateTime) responseValues.get("timestamp"));
        response.setMessage((String) responseValues.get("message"));
        response.setDescription((String) responseValues.get("description"));
        response.setPath((String) responseValues.get("path"));
        return response;
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        APIResponse apiResponse = APIResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Invalid Request")
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<Void>> handleException(Exception e) {
        APIResponse apiResponse = APIResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Internal server error")
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
