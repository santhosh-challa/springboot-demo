package com.zemeso.springboot.thymeleafdemo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomerExceptionHandler {
    // Add an exception handler for CustomerNotFoundException

    @ExceptionHandler
    public String handleException(EmployeeNotFoundException exc, Model theModel) {

        // create CustomerErrorResponse

        EmployeeErrorResponse error = new EmployeeErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                exc.getMessage(),
                System.currentTimeMillis());

        theModel.addAttribute("error",error);

        // return ResponseEntity

        return "error-notfound";
    }


    // Add another exception handler ... to catch any exception (catch all)

    @ExceptionHandler
    public String handleException(Exception exc, Model theModel) {

        // create CustomerErrorResponse

        EmployeeErrorResponse error = new EmployeeErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                exc.getMessage(),
                System.currentTimeMillis());

        theModel.addAttribute("error",error);

        return "error-notfound";
    }
}
