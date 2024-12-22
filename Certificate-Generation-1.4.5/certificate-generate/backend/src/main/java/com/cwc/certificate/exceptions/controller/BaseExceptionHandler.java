package com.cwc.certificate.exceptions.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * Rest controller adviser for validate rest endpoints.
 *
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/03/17
 */

@RestControllerAdvice
public class BaseExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * Override method for handle rest endpoint resource validations.
     *
     * @param   ex the method argument not valid exception
     * @param   headers the http headers
     * @param   status the http status
     * @param   request the web request
     *
     * @return the response entity
     */

}
