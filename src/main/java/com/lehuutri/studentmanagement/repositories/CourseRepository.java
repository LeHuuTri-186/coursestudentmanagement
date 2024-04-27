package com.lehuutri.studentmanagement.repositories;

import org.springframework.data.repository.CrudRepository;

import com.lehuutri.studentmanagement.domain.entities.CourseEntity;

public interface CourseRepository extends CrudRepository<CourseEntity, String> {
    
}
