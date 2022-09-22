package com.vermeg.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailAlreadyUsedException extends Exception{
    public EmailAlreadyUsedException()
    {
        super();
    }

    public EmailAlreadyUsedException(String message)
    {
        super(message);
    }

    public EmailAlreadyUsedException(String message, Throwable cause)
    {
        super(message, cause);
    }
}