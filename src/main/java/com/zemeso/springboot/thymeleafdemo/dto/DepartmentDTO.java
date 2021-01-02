package com.zemeso.springboot.thymeleafdemo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class DepartmentDTO {

    private int id;
    private String deptName;
    private EmployeeDTO employee;

    public DepartmentDTO(int id, String deptName) {
        this.id = id;
        this.deptName = deptName;
    }
}
