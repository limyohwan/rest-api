package com.fsntest.restapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e, HttpServletRequest req){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        ApiResponse apiException = new ApiResponse(badRequest.value(), badRequest, e.getMessage(), req.getRequestURI());
        return new ResponseEntity<>(apiException, badRequest);
    }
}
