package com.zemeso.springboot.thymeleafdemo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class DepartmentDTO {

    private int id;
    private String deptName;
    private List<EmployeeDTO> employeeDTOs;

    public DepartmentDTO(int id, String deptName) {
        this.id = id;
        this.deptName = deptName;
    }
}
