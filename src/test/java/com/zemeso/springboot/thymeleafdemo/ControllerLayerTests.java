package com.zemeso.springboot.thymeleafdemo;

import com.zemeso.springboot.thymeleafdemo.dao.DepartmentRepository;
import com.zemeso.springboot.thymeleafdemo.dao.EmployeeRepository;
import com.zemeso.springboot.thymeleafdemo.dto.DepartmentDTO;
import com.zemeso.springboot.thymeleafdemo.dto.EmployeeDTO;
import com.zemeso.springboot.thymeleafdemo.entity.Department;
import com.zemeso.springboot.thymeleafdemo.entity.Employee;
import com.zemeso.springboot.thymeleafdemo.exceptions.EmployeeNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ControllerLayerTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private DepartmentRepository departmentRepository;

    @BeforeAll
    private void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void listEmployeesTest() throws Exception {

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

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(
                "/employees/list")).andExpect(status().isOk())
                .andExpect(view().name("list-employees"))
                .andReturn();
        verify(employeeRepository, times(1))
                .findAll(PageRequest.of(0,5));

    }

    @Test
    void addEmployeeTest() throws Exception {

        EmployeeDTO emp = new EmployeeDTO(1,
                "Santhosh", "Challa",
                "Santhosh.challa@gmail.com");
        DepartmentDTO dept = new DepartmentDTO(1, "IT");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(
                "/employees/showFormForAdd").flashAttr("employee", emp)
        .flashAttr("department", dept))
                .andExpect(status().isOk())
                .andExpect(view().name("employee-form"))
                .andExpect(model().attributeExists("employee","department"))
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
        when(employeeRepository.findById(1)).thenReturn(java.util.Optional.of(emp));

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

        when(employeeRepository.findById(emp.getId()))
                .thenReturn(java.util.Optional.of(emp));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(
                "/employees/delete?employeeId=" + emp.getId()))
                //.andExpect(status().isOk())
                .andExpect(view().name(
                        "redirect:/employees/list"))
                .andReturn();

        verify(employeeRepository, times(1))
                .deleteById(emp.getId());

    }

    @Test
    void invalidEmployeeUpdateThrowsException() throws Exception {
        int exceptionParam = 1000000;

        when(employeeRepository.findById(exceptionParam)).
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

        verify(employeeRepository, times(1)).findById(exceptionParam);
    }

    @Test
    void listDepartmentsTest() throws Exception {

        List<Department> departmentsList = new ArrayList<>();

        Department dept1 = new Department(1, "IT");
        Department dept2 = new Department(2, "Admin");
        departmentsList.add(dept1);
        departmentsList.add(dept2);

        Pageable pageable = PageRequest.of(0, 5);
        Page<Department> deptPages = new PageImpl<>(departmentsList, pageable,
                departmentsList.size());

        when(departmentRepository.findAll(PageRequest.of(0, 5)))
                .thenReturn(deptPages);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(
                "/departments/list")).andExpect(status().isOk())
                .andExpect(view().name("list-departments"))
                .andReturn();
        verify(departmentRepository, times(1))
                .findAll(PageRequest.of(0,5));

    }

    @Test
    void addDepartmentTest() throws Exception {

        DepartmentDTO dept = new DepartmentDTO(1, "IT");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(
                "/departments/showFormForAdd")
                .flashAttr("department", dept))
                .andExpect(status().isOk())
                .andExpect(view().name("department-form"))
                .andExpect(model().attributeExists("department"))
                .andReturn();
    }

    @Test
    void saveDepartmentTest() throws Exception {

        DepartmentDTO dept = new DepartmentDTO(1, "IT");

        // add dept object to model as flash attribute
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(
                "/departments/save")
                .flashAttr("department", dept))
                .andDo(print())
                .andExpect(view()
                        .name("redirect:/departments/list"))
                .andReturn();
    }

    @Test
    void updateDepartmentTest() throws Exception {

        Department dept = new Department(1, "IT");
        when(departmentRepository.findById(1))
                .thenReturn(java.util.Optional.of(dept));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(
                "/departments/showFormForUpdate?deptId="
                        + dept.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("department-form"))
                .andReturn();

    }

    @Test
    void deleteDepartmentTest() throws Exception {

        Department dept = new Department(1, "IT");
        when(departmentRepository.findById(dept.getId()))
                .thenReturn(java.util.Optional.of(dept));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(
                "/departments/delete?deptId=" + dept.getId()))
                //.andExpect(status().isOk())
                .andExpect(view().name(
                        "redirect:/departments/list"))
                .andReturn();

        verify(departmentRepository, times(1))
                .deleteById(dept.getId());

    }
}
