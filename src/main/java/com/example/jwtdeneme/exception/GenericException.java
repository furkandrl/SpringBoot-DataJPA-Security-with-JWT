package com.example.jwtdeneme.exception;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


import java.util.Date;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class GenericException extends RuntimeException {
    private HttpStatus httpStatus;
    private String message;
    private Date timestamp;
}

