package com.zemeso.springboot.thymeleafdemo.service;

import com.zemeso.springboot.thymeleafdemo.dto.DepartmentDTO;

import java.util.List;


public interface DepartmentService {

	public List<DepartmentDTO> findAll();
	
	public DepartmentDTO findById(int theId);
	
	public DepartmentDTO save(DepartmentDTO theEmployee);
	
	public void deleteById(int theId);
	
}
