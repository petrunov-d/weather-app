package com.dpetrunov.weatherapp.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdviceErrorHandler {

    @ResponseBody
    @ExceptionHandler(value = HttpClientErrorException.class)
    public final ResponseEntity<String> handleHttpClientErrorException(HttpClientErrorException exception) {

        return new ResponseEntity<>(exception.getMessage(), exception.getStatusCode());
    }

    @ResponseBody
    @ExceptionHandler(value = HttpServerErrorException.class)
    public final ResponseEntity<String> handleHttpServerErrorException(HttpClientErrorException exception) {

        return new ResponseEntity<>(exception.getMessage(), exception.getStatusCode());
    }
}
