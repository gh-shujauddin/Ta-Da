package com.qadri.tada.error;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ApiError {

    private LocalDateTime timeStamp;
    private String error;
    private Integer statusCode;

    public ApiError() {
        this.timeStamp = LocalDateTime.now();
    }

    public ApiError(String error,  Integer statusCode) {
        this();
        this.error = error;
        this.statusCode = statusCode;
    }
}