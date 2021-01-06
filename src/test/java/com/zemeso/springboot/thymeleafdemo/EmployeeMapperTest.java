package com.zemeso.springboot.thymeleafdemo;

import com.zemeso.springboot.thymeleafdemo.dto.DepartmentDTO;
import com.zemeso.springboot.thymeleafdemo.dto.EmployeeDTO;
import com.zemeso.springboot.thymeleafdemo.entity.Department;
import com.zemeso.springboot.thymeleafdemo.entity.Employee;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmployeeMapperTest {
    private ModelMapper modelMapper;

    @BeforeAll
    void setup() {
        modelMapper = new ModelMapper();

        Converter<Employee, EmployeeDTO> myConverter = context -> {

            Employee source = context.getSource();
            EmployeeDTO destination = context.getDestination();
            destination.setFirstName(source.getFirstName());
            destination.setLastName(source.getLastName());
            destination.setEmail(source.getEmail());
            destination.setId(source.getId());
            destination.getDepartment().setDeptName(source.getDepartment().getDeptName());
            destination.getDepartment().setId(source.getDepartment().getId());

            return destination;
        };

        Converter<EmployeeDTO, Employee> myRevConverter = context -> {

            EmployeeDTO source = context.getSource();
            Employee destination = context.getDestination();
            destination.setFirstName(source.getFirstName());
            destination.setLastName(source.getLastName());
            destination.setEmail(source.getEmail());
            destination.setId(source.getId());
            destination.getDepartment().setDeptName(source.getDepartment().getDeptName());
            destination.getDepartment().setId(source.getDepartment().getId());

            return destination;
        };

        modelMapper.addConverter(myConverter);
        modelMapper.addConverter(myRevConverter);
    }

    @Test
    void checkIfMappedObjectsAreValid() {

        Employee emp = new Employee(1, "Santhosh", "Challa", "santhosh" +
                ".challa@gmail.com");
        Department dept = new Department(1, "IT");
        emp.setDepartment(dept);

        EmployeeDTO employeeDTO = new EmployeeDTO();
        DepartmentDTO departmentDTO = new DepartmentDTO();
        employeeDTO.setDepartment(departmentDTO);

        modelMapper.map(emp, employeeDTO);

        assertEquals(employeeDTO.getId(), emp.getId());
        assertEquals(employeeDTO.getFirstName(), emp.getFirstName());
        assertEquals(employeeDTO.getLastName(), emp.getLastName());
        assertEquals(employeeDTO.getEmail(), emp.getEmail());
        assertEquals(employeeDTO.getDepartment().getId(),
                emp.getDepartment().getId());
        assertEquals(employeeDTO.getDepartment().getDeptName(),
                emp.getDepartment().getDeptName());
    }
}
