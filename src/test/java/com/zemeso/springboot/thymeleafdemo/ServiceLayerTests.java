package com.zemeso.springboot.thymeleafdemo;

import com.zemeso.springboot.thymeleafdemo.dao.DepartmentRepository;
import com.zemeso.springboot.thymeleafdemo.dto.DepartmentDTO;
import com.zemeso.springboot.thymeleafdemo.entity.Department;
import com.zemeso.springboot.thymeleafdemo.exceptions.EmployeeErrorResponse;
import com.zemeso.springboot.thymeleafdemo.exceptions.EmployeeNotFoundException;
import com.zemeso.springboot.thymeleafdemo.dao.EmployeeRepository;
import com.zemeso.springboot.thymeleafdemo.dto.EmployeeDTO;
import com.zemeso.springboot.thymeleafdemo.entity.Employee;
import com.zemeso.springboot.thymeleafdemo.service.DepartmentService;
import com.zemeso.springboot.thymeleafdemo.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class ServiceLayerTests {

    @Autowired
    private EmployeeService employeeService;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentService departmentService;

    @MockBean
    private DepartmentRepository departmentRepository;

    @Test
    void findAllEmployeeTest() {
        List<Employee> employeeList = new ArrayList<>();
        Employee emp1 = new Employee(1, "santhosh",
                "challa", "santhosh.chall@gmail.com");
        Employee emp2 = new Employee(2, "Anil",
                "dheram", "anil.chall@gmail.com");
        Department dept = new Department(1, "IT");
        emp1.setDepartment(dept);
        emp2.setDepartment(dept);
        employeeList.add(emp1);
        employeeList.add(emp2);

        Pageable pageable = PageRequest.of(0, 5);
        Page<Employee> employeePages = new PageImpl<>(employeeList, pageable,
                employeeList.size());

        when(employeeRepository.findAll(PageRequest.of(0, 5)))
                .thenReturn(employeePages);
        assertEquals(2,
                employeeService.findAll(0, 5).getTotalElements());
    }

    @Test
    void employeeFindByIdTest() {
        int theId = 1;
        Employee emp = new Employee(1,
                "Santhosh", "Challa",
                "Santhosh.challa@gmail.com");
        Department dept = new Department(1, "IT");
        emp.setDepartment(dept);
        EmployeeDTO empDTO = new EmployeeDTO(1,
                "Santhosh", "Challa",
                "Santhosh.challa@gmail.com");
        DepartmentDTO deptDTO = new DepartmentDTO(1, "IT");
        empDTO.setDepartment(deptDTO);

        when(employeeRepository.findById(theId)).thenReturn(Optional.of(emp));
        EmployeeDTO employeeDTO = employeeService.findById(theId);

        assertEquals(emp.getId(),
                employeeDTO.getId());
        assertEquals(emp.getEmail(),
                employeeDTO.getEmail());
        assertEquals(emp.getFirstName(),
                employeeDTO.getFirstName());
        assertEquals(emp.getLastName(),
                employeeDTO.getLastName());
        assertEquals(emp.getDepartment().getDeptName(),
                employeeDTO.getDepartment().getDeptName());
    }

    @Test()
    void employeeFindByIdTestThrowsEmployeeNotFoundException() {

        int exceptionParam = 1000000;

        when(employeeRepository.findById(exceptionParam)).
                thenThrow(new EmployeeNotFoundException("Did not find " +
                        "employee id - " + exceptionParam));
        try {
            employeeService.findById(exceptionParam);
        } catch (EmployeeNotFoundException ex) {
            assertEquals("Did not find " +
                    "employee id - " + exceptionParam, ex.getMessage());
        }
    }


    @Test
    void saveEmployeeTest() {

        EmployeeDTO emp = new EmployeeDTO(1,
                "Santhosh", "Challa",
                "Santhosh.challa@gmail.com");
        DepartmentDTO dept = new DepartmentDTO(1, "IT");
        emp.setDepartment(dept);
        assertEquals(employeeService.save(emp), emp);
    }

    @Test
    void deleteEmployeeTest() {

        Employee emp = new Employee(1,
                "Santhosh", "Challa",
                "Santhosh.challa@gmail.com");
        employeeService.deleteById(1);
        verify(employeeRepository, times(1)).deleteById(1);
    }

    @Test
    void employeeErrorResponseTest() {

        EmployeeErrorResponse error = new
                EmployeeErrorResponse(404, "sample"
                , 1234);
        assertEquals("sample", error.getMessage());
        assertEquals(404, error.getStatus());
        assertEquals(1234, error.getTimeStamp());
        error.setMessage("error");
        error.setStatus(400);
        error.setTimeStamp(12345);
        assertEquals("error", error.getMessage());
        assertEquals(400, error.getStatus());
        assertEquals(12345, error.getTimeStamp());
    }

    @Test
    void findAllDepartmentsTest() {
        List<Department> departmentList = new ArrayList<>();

        Department dept1 = new Department(1, "IT");
        Department dept2 = new Department(2, "Admin");
        departmentList.add(dept1);
        departmentList.add(dept2);

        Pageable pageable = PageRequest.of(0, 5);
        Page<Department> deptPages = new PageImpl<>(departmentList,
                pageable,
                departmentList.size());

        when(departmentRepository.findAll(PageRequest.of(0, 5)))
                .thenReturn(deptPages);
        assertEquals(2,
                departmentService.findAll(0, 5).getTotalElements());
    }

    @Test()
    void departmentFindByIdTestThrowsEmployeeNotFoundException() {

        int exceptionParam = 1000000;

        when(departmentRepository.findById(exceptionParam)).
                thenThrow(new EmployeeNotFoundException
                        ("Did not find Department id - "+ exceptionParam));
        try {
            departmentService.findById(exceptionParam);
        } catch (EmployeeNotFoundException ex) {
            assertEquals("Did not find Department id - "
                    + exceptionParam, ex.getMessage());
        }
    }

    @Test
    void saveDepartmentTest() {

        DepartmentDTO dept = new DepartmentDTO(1, "IT");
        assertEquals(departmentService.save(dept), dept);
    }

    @Test
    void deleteDepartmentTest() {

        Department dept = new Department(1, "IT");
        departmentService.deleteById(1);
        verify(departmentRepository, times(1))
                .deleteById(1);
    }
}
