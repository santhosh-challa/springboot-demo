package com.zemeso.springboot.thymeleafdemo.service.impl;

import com.zemeso.springboot.thymeleafdemo.dao.DepartmentRepository;
import com.zemeso.springboot.thymeleafdemo.dto.DepartmentDTO;
import com.zemeso.springboot.thymeleafdemo.entity.Department;
import com.zemeso.springboot.thymeleafdemo.exceptions.EmployeeNotFoundException;
import com.zemeso.springboot.thymeleafdemo.service.DepartmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private ModelMapper myMapper;

    private DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository theDepartmentRepository) {
        departmentRepository = theDepartmentRepository;
    }

    @Override
    public Page<DepartmentDTO> findAll(int page, int size) {
        Page<Department> departmentList = departmentRepository
                .findAll(PageRequest.of(page,size));

        return departmentList.map(this::convertToDTO);
    }

    @Override
    public List<DepartmentDTO> findAll() {
        List<Department> departmentList = departmentRepository
                .findAll();

        return departmentList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentDTO findById(int theId) {
        Optional<Department> result = departmentRepository.findById(theId);

        DepartmentDTO theDepartment = null;

        if (result.isPresent()) {
            theDepartment = convertToDTO(result.get());
        } else {
            // we didn't find the Department
            throw new EmployeeNotFoundException("Did not find Department id - " + theId);
        }
        return theDepartment;
    }

    @Override
    public DepartmentDTO save(DepartmentDTO theDepartment) {

        departmentRepository.save(convertToEntity(theDepartment));
        return theDepartment;
    }

    @Override
    public void deleteById(int theId) {

        departmentRepository.deleteById(theId);
    }

    private DepartmentDTO convertToDTO(Department department) {
        return myMapper.map(department, DepartmentDTO.class);
    }

    private Department convertToEntity(DepartmentDTO departmentDTO) {
        return myMapper.map(departmentDTO, Department.class);
    }

}






