package com.zemeso.springboot.thymeleafdemo.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class EmployeeDTO {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private DepartmentDTO department;

    public EmployeeDTO(int id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
