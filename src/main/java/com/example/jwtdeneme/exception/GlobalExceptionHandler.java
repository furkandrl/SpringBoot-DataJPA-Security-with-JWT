package com.example.jwtdeneme.exception;

import com.example.jwtdeneme.dto.ErrorObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<ErrorObject> handleException(GenericException exc, WebRequest request){
        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(exc.getHttpStatus().value());
        errorObject.setMessage(exc.getMessage());
        errorObject.setTimestamp(new Date());

        return new ResponseEntity<>(errorObject, exc.getHttpStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<RuntimeException> handleRuntimeException(RuntimeException  exception){

        return new ResponseEntity<>(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
