package com.zemeso.springboot.thymeleafdemo;

import com.zemeso.springboot.thymeleafdemo.dao.DepartmentRepository;
import com.zemeso.springboot.thymeleafdemo.dto.DepartmentDTO;
import com.zemeso.springboot.thymeleafdemo.dto.EmployeeDTO;
import com.zemeso.springboot.thymeleafdemo.entity.Department;
import com.zemeso.springboot.thymeleafdemo.entity.Employee;
import com.zemeso.springboot.thymeleafdemo.service.DepartmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class MiscTests {

    @MockBean
    private static ModelMapper modelMapper;

    @Autowired
    private DepartmentService departmentService;

    @MockBean
    private DepartmentRepository departmentRepository;

    @Test
    void departmentFindByIdTest() {
        int theId = 1;
        List<Employee> employeeList = new ArrayList<>();
        Employee emp1 = new Employee(1, "santhosh",
                "challa", "santhosh.chall@gmail.com");
        Employee emp2 = new Employee(2, "Anil",
                "dheram", "anil.chall@gmail.com");
        employeeList.add(emp1);
        employeeList.add(emp2);

        Department dept = new Department(1, "IT");
        dept.setEmployees(employeeList);

        List<EmployeeDTO> employeeDTOList = new ArrayList<>();
        EmployeeDTO employeeDTO1 = new EmployeeDTO(1, "santhosh",
                "challa", "santhosh.chall@gmail.com");
        EmployeeDTO employeeDTO2 = new EmployeeDTO(2, "Anil",
                "dheram", "anil.chall@gmail.com");
        employeeDTOList.add(employeeDTO1);
        employeeDTOList.add(employeeDTO2);

        DepartmentDTO departmentDTO = new DepartmentDTO(1, "IT");
        departmentDTO.setEmployeeDTOs(employeeDTOList);
        dept.setEmployees(employeeList);
        when(departmentRepository.findById(theId)).thenReturn(Optional.of(dept));
        when(modelMapper.map(any(), any())).thenReturn(departmentDTO);
        DepartmentDTO res = departmentService.findById(theId);
        assertEquals(dept.getId(),
                res.getId());
        assertEquals(dept.getDeptName(),
                res.getDeptName());
        assertEquals(dept.getEmployees().size(),
                res.getEmployeeDTOs().size());
    }
}
