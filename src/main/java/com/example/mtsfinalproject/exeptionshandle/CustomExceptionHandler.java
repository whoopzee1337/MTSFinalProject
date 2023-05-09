package com.example.mtsfinalproject.exeptionshandle;

import com.example.mtsfinalproject.constants.ErrorEnum;
import com.example.mtsfinalproject.wrappers.ErrorWrap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.TimeoutException;


@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorWrap> handleResponseStatusException(ResponseStatusException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        ErrorEnum errorCode = ErrorEnum.valueOf(ex.getReason());
        errorResponse.setCode(errorCode.name()).setMessage(errorCode.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorWrap(errorResponse));
    }

//    @ExceptionHandler(TimeoutException.class)
//    public ResponseEntity<ErrorWrap> handleTimeoutException(TimeoutException ex){
//        ErrorResponse errorResponse = new ErrorResponse();
//        errorResponse.setCode(ErrorEnum.TIMEOUT.name()).setMessage(ErrorEnum.TIMEOUT.getMessage());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorWrap(errorResponse));
//    }
}
