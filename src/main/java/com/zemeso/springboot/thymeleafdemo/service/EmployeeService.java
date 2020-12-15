package com.zemeso.springboot.thymeleafdemo.service;

import java.util.List;

import com.zemeso.springboot.thymeleafdemo.dto.EmployeeDTO;


public interface EmployeeService {

	public List<EmployeeDTO> findAll();
	
	public EmployeeDTO findById(int theId);
	
	public EmployeeDTO save(EmployeeDTO theEmployee);
	
	public void deleteById(int theId);
	
}
