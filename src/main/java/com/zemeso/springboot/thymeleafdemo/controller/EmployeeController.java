package com.zemeso.springboot.thymeleafdemo.controller;

import com.zemeso.springboot.thymeleafdemo.dto.DepartmentDTO;
import com.zemeso.springboot.thymeleafdemo.dto.EmployeeDTO;
import com.zemeso.springboot.thymeleafdemo.service.DepartmentService;
import com.zemeso.springboot.thymeleafdemo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/list")
    public String listEmployees(Model theModel){

        List<EmployeeDTO> employeeList = employeeService.findAll();

        theModel.addAttribute("employees", employeeList);
        return "list-employees";
    }

    @GetMapping("/showFormForAdd")
    public String addEmployee(Model theModel){
        // create model attribute to bind form data
        EmployeeDTO theEmployee = new EmployeeDTO();
        List<DepartmentDTO> departments =departmentService.findAll();
        DepartmentDTO theDepartment = new DepartmentDTO();

        theModel.addAttribute("employee", theEmployee);
        theModel.addAttribute("departments", departments);
        theModel.addAttribute("department", theDepartment);
        return "employee-form";
    }

    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee")
                                           EmployeeDTO theEmployee){

        employeeService.save(theEmployee);

        // use re-direct to prevent duplicate submissions
        return "redirect:/employees/list";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("employeeId") int theId,
                                    Model theModel) {

        EmployeeDTO theEmployee = employeeService.findById(theId);
        DepartmentDTO theDepartment = new DepartmentDTO();
        theDepartment.setId(theEmployee.getDepartment().getId());
        theDepartment.setDeptName(theEmployee.getDepartment().getDeptName());
        theEmployee.setDepartment(theDepartment);

        List<DepartmentDTO> departments =departmentService.findAll();

        theModel.addAttribute("departments", departments);
        theModel.addAttribute("employee", theEmployee);
        theModel.addAttribute("department", theDepartment);

        return "employee-form";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("employeeId") int theId){
        employeeService.deleteById(theId);

        return "redirect:/employees/list";
    }
}
