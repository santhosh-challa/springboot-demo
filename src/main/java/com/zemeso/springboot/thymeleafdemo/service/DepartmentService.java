package com.zemeso.springboot.thymeleafdemo.service;

import com.zemeso.springboot.thymeleafdemo.dto.DepartmentDTO;
import org.springframework.data.domain.Page;

import java.util.List;


public interface DepartmentService {

	Page<DepartmentDTO> findAll(int page, int size);

	List<DepartmentDTO> findAll();
	
	DepartmentDTO findById(int theId);
	
	DepartmentDTO save(DepartmentDTO theEmployee);
	
	void deleteById(int theId);
	
}
