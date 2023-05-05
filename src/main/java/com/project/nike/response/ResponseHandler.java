package com.project.nike.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {

    public static ResponseEntity<Object> responseEntity(String message, HttpStatus httpStatus, Object responseObj){
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("status", httpStatus);
        response.put("data", responseObj);

        return new ResponseEntity<>(response, httpStatus);
    }

    public static ResponseEntity<Object> errorResponseEntity(String message, HttpStatus httpStatus, Object responseObj){
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("status", httpStatus);
        response.put("data", responseObj);

        return new ResponseEntity<>(response, httpStatus);
    }
}
