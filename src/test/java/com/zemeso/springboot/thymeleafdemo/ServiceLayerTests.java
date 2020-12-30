package com.zemeso.springboot.thymeleafdemo;

import com.zemeso.springboot.thymeleafdemo.exceptions.EmployeeErrorResponse;
import com.zemeso.springboot.thymeleafdemo.exceptions.EmployeeNotFoundException;
import com.zemeso.springboot.thymeleafdemo.dao.EmployeeRepository;
import com.zemeso.springboot.thymeleafdemo.dto.EmployeeDTO;
import com.zemeso.springboot.thymeleafdemo.entity.Employee;
import com.zemeso.springboot.thymeleafdemo.service.EmployeeService;
import org.junit.jupiter.api.Assertions;
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

    @Test()
    void findByIdTestThrowsEmployeeNotFoundException() {

        Assertions.assertThrows(EmployeeNotFoundException.class, () -> {
            int theId =1;
            // when(repository.findById(theId)).thenThrow
            // (EmployeeNotFoundException.class);
            service.findById(theId);
        });
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

    @Test
    void employeeErrorResponseTest(){

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
}
