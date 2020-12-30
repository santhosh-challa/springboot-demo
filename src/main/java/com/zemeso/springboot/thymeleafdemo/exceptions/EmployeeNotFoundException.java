package com.zemeso.springboot.thymeleafdemo.exceptions;

public class EmployeeNotFoundException extends RuntimeException{


    public EmployeeNotFoundException(String message) {
        super(message);
    }

}
