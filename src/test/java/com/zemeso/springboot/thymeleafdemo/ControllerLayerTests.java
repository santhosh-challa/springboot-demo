package com.zemeso.springboot.thymeleafdemo;

import com.zemeso.springboot.thymeleafdemo.dao.EmployeeRepository;
import com.zemeso.springboot.thymeleafdemo.dto.DepartmentDTO;
import com.zemeso.springboot.thymeleafdemo.dto.EmployeeDTO;
import com.zemeso.springboot.thymeleafdemo.entity.Department;
import com.zemeso.springboot.thymeleafdemo.entity.Employee;
import com.zemeso.springboot.thymeleafdemo.exceptions.EmployeeNotFoundException;
import com.zemeso.springboot.thymeleafdemo.service.EmployeeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ControllerLayerTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private EmployeeRepository repository;

    @BeforeAll
    private void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void listEmployeesTest() throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(
                "/employees/list")).andExpect(status().isOk())
                .andExpect(view().name("list-employees"))
                .andReturn();
        verify(repository, times(1))
                .findAllByOrderByLastNameAsc();

    }

    @Test
    void addEmployeeTest() throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(
                "/employees/showFormForAdd"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee-form"))
                .andReturn();
    }

    @Test
    void saveEmployeeTest() throws Exception {

        EmployeeDTO emp = new EmployeeDTO(1,
                "Santhosh", "Challa",
                "Santhosh.challa@gmail.com");
        DepartmentDTO dept = new DepartmentDTO(1, "IT");
        emp.setDepartment(dept);

        // add emp object to model as flash attribute
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(
                "/employees/save").flashAttr("employee", emp))
                .andDo(print())
                .andExpect(view()
                        .name("redirect:/employees/list"))
                .andReturn();
    }

    @Test
    void updateEmployeeTest() throws Exception {

        Employee emp = new Employee(1,
                "Santhosh123457", "Challa",
                "Santhosh.challa@gmail.com");
        Department dept = new Department(1, "IT");
        emp.setDepartment(dept);
        when(repository.findById(1)).thenReturn(java.util.Optional.of(emp));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(
                "/employees/showFormForUpdate?employeeId="
                        + emp.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("employee-form"))
                .andReturn();
    }

    @Test
    void deleteEmployeeTest() throws Exception {

        Employee emp = new Employee(1,
                "Santhosh123457", "Challa",
                "Santhosh.challa@gmail.com");

        when(repository.findById(emp.getId()))
                .thenReturn(java.util.Optional.of(emp));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(
                "/employees/delete?employeeId=" + emp.getId()))
                //.andExpect(status().isOk())
                .andExpect(view().name(
                        "redirect:/employees/list"))
                .andReturn();

        verify(repository, times(1))
                .deleteById(emp.getId());

    }

    @Test
    void invalidEmployeeUpdateThrowsException() throws Exception {
        int exceptionParam = 1000000;

        when(repository.findById(exceptionParam)).
                thenThrow(new EmployeeNotFoundException("employee not found."));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/employees/showFormForUpdate" +
                        "?employeeId=" + exceptionParam))
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResolvedException
                        () instanceof EmployeeNotFoundException))
                .andExpect(result -> assertEquals("employee not found.",
                        result.getResolvedException().getMessage())).andExpect(view().name(
                "error-notfound"));

        verify(repository, times(1)).findById(exceptionParam);
    }
}
