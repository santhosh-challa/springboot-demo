package com.zemeso.springboot.thymeleafdemo.service;

import java.util.List;

import com.zemeso.springboot.thymeleafdemo.dto.EmployeeDTO;
import com.zemeso.springboot.thymeleafdemo.entity.Employee;

public interface EmployeeService {

	public List<EmployeeDTO> findAll();
	
	public EmployeeDTO findById(int theId);
	
	public void save(EmployeeDTO theEmployee);
	
	public void deleteById(int theId);
	
}
