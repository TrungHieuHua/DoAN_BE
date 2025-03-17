package com.shose.shoseshop.exception;

import lombok.Data;


@Data
public class AppException extends RuntimeException{
    private ErrorResponse errorResponse;

    public AppException(ErrorResponse error)
    {
        super(error.getMessage());
        this.errorResponse = error;
    }
}
