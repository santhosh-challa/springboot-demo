package com.zemeso.springboot.thymeleafdemo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.zemeso.springboot.thymeleafdemo.dto.DepartmentDTO;
import com.zemeso.springboot.thymeleafdemo.entity.Department;
import com.zemeso.springboot.thymeleafdemo.exceptions.EmployeeNotFoundException;
import com.zemeso.springboot.thymeleafdemo.dao.EmployeeRepository;
import com.zemeso.springboot.thymeleafdemo.dto.EmployeeDTO;
import com.zemeso.springboot.thymeleafdemo.entity.Employee;

import com.zemeso.springboot.thymeleafdemo.service.EmployeeService;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private ModelMapper myMapper;

    private EmployeeRepository employeeRepository;

    @PostConstruct
    private void initMapper() {

        Converter<Employee, EmployeeDTO> myConverter = new Converter<Employee, EmployeeDTO>() {
            public EmployeeDTO convert(MappingContext<Employee, EmployeeDTO> context) {

                Employee source = context.getSource();
                EmployeeDTO destination = context.getDestination();
                destination.setFirstName(source.getFirstName());
                destination.setLastName(source.getLastName());
                destination.setEmail(source.getEmail());
                destination.setId(source.getId());
                destination.getDepartment().setDeptName(source.getDepartment().getDeptName());
                destination.getDepartment().setId(source.getDepartment().getId());

                return destination;
            }
        };

        Converter<EmployeeDTO, Employee> myRevConverter = new Converter<EmployeeDTO, Employee>() {
            public Employee convert(MappingContext<EmployeeDTO, Employee> context) {

                EmployeeDTO source = context.getSource();
                Employee destination = context.getDestination();
                destination.setFirstName(source.getFirstName());
                destination.setLastName(source.getLastName());
                destination.setEmail(source.getEmail());
                destination.setId(source.getId());
                destination.getDepartment().setDeptName(source.getDepartment().getDeptName());
                destination.getDepartment().setId(source.getDepartment().getId());

                return destination;
            }
        };

        myMapper.addConverter(myConverter);
        myMapper.addConverter(myRevConverter);
    }

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository theEmployeeRepository) {
        employeeRepository = theEmployeeRepository;
    }

    @Override
    public List<EmployeeDTO> findAll() {
        List<Employee> employeeList = employeeRepository
                .findAllByOrderByLastNameAsc();

        List<EmployeeDTO> employeeDTOList = new ArrayList<>();

        for (Employee emp : employeeList) {
            employeeDTOList.add(convertToDTO(emp));
        }
        return employeeDTOList;
    }

    @Override
    public EmployeeDTO findById(int theId) {
        Optional<Employee> result = employeeRepository.findById(theId);

        EmployeeDTO theEmployee = null;

        if (result.isPresent()) {
            theEmployee = convertToDTO(result.get());
        } else {
            // we didn't find the employee
            throw new EmployeeNotFoundException("Did not find employee id - " + theId);
        }
        return theEmployee;
    }

    @Override
    public EmployeeDTO save(EmployeeDTO theEmployee) {

        Employee emp = convertToEntity(theEmployee);
        System.out.println("value of the dept id saving: " + theEmployee.getDepartment().getId());
        employeeRepository.save(emp);
        return theEmployee;
    }

    @Override
    public void deleteById(int theId) {

        employeeRepository.deleteById(theId);
    }

    private EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setDepartment(new DepartmentDTO());
        myMapper.map(employee, employeeDTO);
        return employeeDTO;
    }

    private Employee convertToEntity(EmployeeDTO employeeDTO) {
        Employee emp = new Employee();
        emp.setDepartment(new Department());
        myMapper.map(employeeDTO, emp);
        return emp;
    }

}






