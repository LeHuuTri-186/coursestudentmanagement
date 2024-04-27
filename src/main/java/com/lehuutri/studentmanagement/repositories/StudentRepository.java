package com.lehuutri.studentmanagement.repositories;

import org.springframework.data.repository.CrudRepository;

import com.lehuutri.studentmanagement.domain.entities.StudentEntity;

public interface StudentRepository extends CrudRepository<StudentEntity, String> {
    
}
