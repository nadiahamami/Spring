package com.vermeg.exceptions;

import com.vermeg.payload.responses.ApiResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestControllerAdvice
public class FileUploadExceptionAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ApiResponse<Object> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        String message = "File too large!";
        return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), message, null);
    }
}
