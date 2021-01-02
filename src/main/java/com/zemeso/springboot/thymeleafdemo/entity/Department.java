package com.zemeso.springboot.thymeleafdemo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "dept")
public class Department {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "dept_name")
    private String deptName;

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY, cascade =
            CascadeType.ALL)
    private List<Employee> employees;

    public void addEmployee(Employee emp){
        if(employees == null){
            employees = new ArrayList<>();
        }

        employees.add(emp);
        emp.setDepartment(this);
    }

    public void removeEmployee(Employee emp){
        employees.remove(emp);
        emp.setDepartment(null);
    }

    public Department(int id, String deptName) {
        this.id = id;
        this.deptName = deptName;
    }
}
