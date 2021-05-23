package com.kosenko.wikirest.controller;

import com.kosenko.wikirest.entity.error.ErrorResponse;
import com.kosenko.wikirest.entity.error.ErrorValidationResponse;
import com.kosenko.wikirest.exception.EntityExistsException;
import com.kosenko.wikirest.exception.EntityNotFoundException;
import com.kosenko.wikirest.exception.FileUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorValidationResponse handleException(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrorsMap = new HashMap<>();

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        fieldErrors.forEach(fieldError -> fieldErrorsMap.put(fieldError.getField(), fieldError.getDefaultMessage()));

        return new ErrorValidationResponse(fieldErrorsMap);
    }

    @ExceptionHandler(value = {EntityExistsException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(EntityExistsException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponse handleException(EntityNotFoundException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(value = {FileUploadException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(FileUploadException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(value = {MultipartException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(MultipartException ex) {
        return new ErrorResponse("Необходимо выбрать хотя бы один файл.");
    }
}