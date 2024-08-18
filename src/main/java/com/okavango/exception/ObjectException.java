package com.okavango.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ObjectException
{
    private String path;
    private String method;
    private Integer status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime moment;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> errors = new ArrayList<>();

    public ObjectException(String path, String method, Integer status, String message)
    {
        this.path = path;
        this.method = method;
        this.status = status;
        moment = LocalDateTime.now();
        this.message = message;
    }

    public ObjectException(String path, String method, Integer status, String message, List<String> errors)
    {
        this.path = path;
        this.method = method;
        this.status = status;
        moment = LocalDateTime.now();
        this.message = message;
        this.errors = errors;
    }
}
