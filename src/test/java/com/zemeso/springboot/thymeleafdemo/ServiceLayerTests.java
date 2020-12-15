package com.zemeso.springboot.thymeleafdemo;

import com.zemeso.springboot.thymeleafdemo.dao.EmployeeRepository;
import com.zemeso.springboot.thymeleafdemo.dto.EmployeeDTO;
import com.zemeso.springboot.thymeleafdemo.entity.Employee;
import com.zemeso.springboot.thymeleafdemo.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
    private EmployeeService service;

    @MockBean
    private EmployeeRepository repository;

    @Test
    void findAllTest() {
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(new Employee(1, "santhosh",
                "challa", "santhosh.chall@gmail.com"));
        employeeList.add(new Employee(2, "Anil",
                "dheram", "anil.chall@gmail.com"));
        when(repository.findAllByOrderByLastNameAsc()).thenReturn(employeeList);
		assertEquals(2,
                service.findAll().size());
    }

	@Test
	void findByIdTest() {
    	int theId =1;
    	Employee emp = new Employee(1,
                "Santhosh","Challa",
                "Santhosh.challa@gmail.com");
		when(repository.findById(theId)).thenReturn(Optional.of(emp));
        EmployeeDTO employeeDTO = service.findById(theId);
		assertEquals(emp.getId(),
                employeeDTO.getId());
        assertEquals(emp.getEmail(),
                employeeDTO.getEmail());
        assertEquals(emp.getFirstName(),
                employeeDTO.getFirstName());
        assertEquals(emp.getLastName(),
                employeeDTO.getLastName());
	}

	@Test
	void saveEmployeeTest(){

        EmployeeDTO emp = new EmployeeDTO(1,
                "Santhosh","Challa",
                "Santhosh.challa@gmail.com");
        assertEquals(service.save(emp),emp);
    }

    @Test
    void deleteEmployeeTest(){

        Employee emp = new Employee(1,
                "Santhosh","Challa",
                "Santhosh.challa@gmail.com");
        service.deleteById(1);
        verify(repository,times(1)).deleteById(1);
    }

}
