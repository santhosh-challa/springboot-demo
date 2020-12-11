package com.zemeso.springboot.thymeleafdemo.dto;

import lombok.Data;

@Data
public class EmployeeDTO {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
}
