package com.lehuutri.studentmanagement.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.lehuutri.studentmanagement.domain.dto.StudentDto;
import com.lehuutri.studentmanagement.domain.entities.StudentEntity;
import com.lehuutri.studentmanagement.mappers.Mapper;

@Component
public class StudentMapperImpl implements Mapper<StudentEntity, StudentDto> {

    private final ModelMapper modelMapper;

    public StudentMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public StudentDto mapTo(StudentEntity studentEntity) {
        return this.modelMapper.map(studentEntity, StudentDto.class);
    }

    @Override
    public StudentEntity mapFrom(StudentDto studentDto) {
        return this.modelMapper.map(studentDto, StudentEntity.class);
    }
    
}
