package com.fsntest.restapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ApiResponse {
    private Date timestamp;
    private int status;
    private HttpStatus error;
    private String message;
    private String path;
    private Map<String, Object> data;

    public ApiResponse(int code, HttpStatus httpStatus, String message, String path){
        this.timestamp = new Date();
        this.status = code;
        this.error = httpStatus;
        this.message = message;
        this.path = path;
        this.data = new HashMap<>();
    }

    public ApiResponse(HttpStatus httpStatus, String path){
        this.timestamp = new Date();
        this.status = httpStatus.value();
        this.error = httpStatus;
        this.message = httpStatus.getReasonPhrase();
        this.path = path;
        this.data = new HashMap<>();
    }

}
