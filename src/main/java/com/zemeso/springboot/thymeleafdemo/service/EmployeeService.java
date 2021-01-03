package com.zemeso.springboot.thymeleafdemo.service;

import com.zemeso.springboot.thymeleafdemo.dto.EmployeeDTO;
import org.springframework.data.domain.Page;


public interface EmployeeService {

	Page<EmployeeDTO> findAll(int page, int size);
	
	EmployeeDTO findById(int theId);
	
	EmployeeDTO save(EmployeeDTO theEmployee);
	
	void deleteById(int theId);
	
}
