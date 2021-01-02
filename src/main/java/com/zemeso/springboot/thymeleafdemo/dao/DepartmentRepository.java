package com.zemeso.springboot.thymeleafdemo.dao;

import com.zemeso.springboot.thymeleafdemo.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends
        JpaRepository<Department,Integer> {

    // create a method to return dept list order by name
    List<Department> findAllByOrderByDeptNameAsc();
}
