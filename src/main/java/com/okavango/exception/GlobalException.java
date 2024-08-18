package com.okavango.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.UnexpectedTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalException
{
    // captura o parametro passado na requisicao
    private String resourceRequest(String uri){
        Integer index = uri.lastIndexOf("/");
        String resource = uri.substring(index +1);
        return resource;
    } // *************************************************


    // recurso não encontrado
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ObjectException handlerNoSuchElementException(NoSuchElementException ex, HttpServletRequest request){

        String message = String.format("recurso %s não encontrado", resourceRequest(request.getRequestURI()));
        return new ObjectException(request.getRequestURI(), request.getMethod(), HttpStatus.NOT_FOUND.value(),message);
    } // *************************************************


    // passagem incorreta de parâmetros
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ObjectException handlerHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request)
    {
        String message = String.format("passagem incorreta de parâmetros");
        return new ObjectException(request.getRequestURI(), request.getMethod(), HttpStatus.BAD_REQUEST.value(), message);
    } // *************************************************


    // id de departamento não encontrado
    @ExceptionHandler(DepartmentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ObjectException handlerIllegalArgumentException(DepartmentNotFoundException ex, HttpServletRequest request){

        String message = String.format("id de departamento não encontrado", resourceRequest(request.getRequestURI()));
        return new ObjectException(request.getRequestURI(), request.getMethod(), HttpStatus.NOT_FOUND.value(),message);
    }// *************************************************

    // id de pessoa não encontrado
    @ExceptionHandler(PersonNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ObjectException handlerPersonNotFoundException(PersonNotFoundException ex, HttpServletRequest request){

        String message = String.format("id de pessoa não encontrado", resourceRequest(request.getRequestURI()));
        return new ObjectException(request.getRequestURI(), request.getMethod(), HttpStatus.NOT_FOUND.value(),message);
    }// *************************************************

    // departamentos diferentes
    @ExceptionHandler(DifferentDepartmentsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ObjectException handlerDifferentDepartmentsException(DifferentDepartmentsException ex, HttpServletRequest request)
    {

        String message = String.format("o departameto da tarefa e o departamento da pessoa são diferentes", resourceRequest(request.getRequestURI()));
        return new ObjectException(request.getRequestURI(), request.getMethod(), HttpStatus.NOT_FOUND.value(), message);
    }// *************************************************


    // validação de campos
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ObjectException handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request){

        List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(error -> String.format("%s: %s", error.getField(),error.getDefaultMessage())).collect(Collectors.toList());
        String message = "incapaz de processar o conteúdo contido na instrução";
        return  new ObjectException(request.getRequestURI(), request.getMethod(), HttpStatus.UNPROCESSABLE_ENTITY.value(),message, errors);
    }// *************************************************


}
