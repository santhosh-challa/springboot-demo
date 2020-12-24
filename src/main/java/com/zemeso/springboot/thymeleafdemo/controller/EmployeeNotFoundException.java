package com.zemeso.springboot.thymeleafdemo.controller;

public class EmployeeNotFoundException extends RuntimeException{


    public EmployeeNotFoundException(String message) {
        super(message);
    }

}
