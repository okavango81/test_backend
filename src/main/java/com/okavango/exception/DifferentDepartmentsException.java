package com.okavango.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DifferentDepartmentsException extends RuntimeException
{
    public DifferentDepartmentsException(String message){
        super(message);
    }
}
