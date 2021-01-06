package com.zemeso.springboot.thymeleafdemo.controller;

import com.zemeso.springboot.thymeleafdemo.dto.DepartmentDTO;
import com.zemeso.springboot.thymeleafdemo.dto.EmployeeDTO;
import com.zemeso.springboot.thymeleafdemo.service.DepartmentService;
import com.zemeso.springboot.thymeleafdemo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/list")
    public String listEmployees(HttpServletRequest request, Model theModel){

        int page = 0;
        int size = 5;

        if (request.getParameter("page") != null &&
                !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }

        if (request.getParameter("size") != null
                && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }

        Page<EmployeeDTO> employeeList =
                employeeService.findAll(page,size);

        theModel.addAttribute("employees", employeeList);
        return "list-employees";
    }

    @GetMapping("/byDeptId")
    public String getEmployesByDept(@RequestParam("deptId") int theId,
                                    Model theModel){
        List<EmployeeDTO> employeeDTOList =
                employeeService.findEmpsByDept(theId);

        theModel.addAttribute("employees", employeeDTOList);
        return "list-dept-employees";

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
