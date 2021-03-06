package com.zemeso.springboot.thymeleafdemo.controller;

import com.zemeso.springboot.thymeleafdemo.dto.DepartmentDTO;
import com.zemeso.springboot.thymeleafdemo.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/list")
    public String listDepartments(HttpServletRequest request, Model theModel) {

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
        Page<DepartmentDTO> deptList = departmentService.findAll(page, size);

        theModel.addAttribute("departments", deptList);
        return "list-departments";
    }

    @GetMapping("/showFormForAdd")
    public String addDepartment(Model theModel) {
        // create model attribute to bind form data
        DepartmentDTO theDepartment = new DepartmentDTO();

        theModel.addAttribute("department", theDepartment);
        return "department-form";
    }

    @PostMapping("/save")
    public String saveDepartment(@ModelAttribute("department")
                                         DepartmentDTO theDepartment) {

        departmentService.save(theDepartment);

        // use re-direct to prevent duplicate submissions
        return "redirect:/departments/list";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("deptId") int theId,
                                    Model theModel) {

        DepartmentDTO theDepartment = departmentService.findById(theId);
        theModel.addAttribute("department", theDepartment);

        return "department-form";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("deptId") int theId) {
        departmentService.deleteById(theId);

        return "redirect:/departments/list";
    }
}
