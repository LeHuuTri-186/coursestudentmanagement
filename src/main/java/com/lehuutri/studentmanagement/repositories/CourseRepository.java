package com.lehuutri.studentmanagement.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.lehuutri.studentmanagement.domain.entities.CourseEntity;



@Repository
public interface CourseRepository extends CrudRepository<CourseEntity, String> {
    List<CourseEntity> findByYear(Integer year);
    List<CourseEntity> findByNameIsLike(String name);
}
