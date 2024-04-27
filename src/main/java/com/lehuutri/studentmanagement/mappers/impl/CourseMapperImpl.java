package com.lehuutri.studentmanagement.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.lehuutri.studentmanagement.domain.dto.CourseDto;
import com.lehuutri.studentmanagement.domain.entities.CourseEntity;
import com.lehuutri.studentmanagement.mappers.Mapper;

@Component
public class CourseMapperImpl implements Mapper<CourseEntity, CourseDto> {

    private ModelMapper modelMapper;

    public CourseMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    
    @Override
    public CourseDto mapTo(CourseEntity courseEntity) {
        return this.modelMapper.map(courseEntity, CourseDto.class);
    }

    @Override
    public CourseEntity mapFrom(CourseDto courseDto) {
        return this.modelMapper.map(courseDto, CourseEntity.class);
    }
    
}
