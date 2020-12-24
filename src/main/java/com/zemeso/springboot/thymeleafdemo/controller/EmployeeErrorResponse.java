package com.zemeso.springboot.thymeleafdemo.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeErrorResponse {
    private int status;
    private String message;
    private long timeStamp;
}
