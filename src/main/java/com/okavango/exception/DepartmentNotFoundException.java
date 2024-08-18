package com.okavango.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DepartmentNotFoundException extends RuntimeException
{
    public DepartmentNotFoundException(String message){
        super(message);
    }
}
