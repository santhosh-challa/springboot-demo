package com.zemeso.springboot.thymeleafdemo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.zemeso.springboot.thymeleafdemo.controller.EmployeeNotFoundException;
import com.zemeso.springboot.thymeleafdemo.dao.EmployeeRepository;
import com.zemeso.springboot.thymeleafdemo.dto.EmployeeDTO;
import com.zemeso.springboot.thymeleafdemo.entity.Employee;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private ModelMapper myMapper;

    private EmployeeRepository employeeRepository;

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

        employeeRepository.save(convertToEntity(theEmployee));
        return theEmployee;
    }

    @Override
    public void deleteById(int theId) {
        employeeRepository.deleteById(theId);
    }

    private EmployeeDTO convertToDTO(Employee employee) {
        return myMapper.map(employee, EmployeeDTO.class);
    }

    private Employee convertToEntity(EmployeeDTO employeeDTO) {
        return myMapper.map(employeeDTO, Employee.class);
    }

}






