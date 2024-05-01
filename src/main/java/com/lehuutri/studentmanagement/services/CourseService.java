package com.lehuutri.studentmanagement.services;

import java.util.List;
import java.util.Optional;

import com.lehuutri.studentmanagement.domain.entities.CourseEntity;

public interface CourseService {
    CourseEntity saveCourse(final CourseEntity course);

    Iterable<CourseEntity> findAllCourse();

    Optional<CourseEntity> findOneCourse(String courseId);

    void deleteById(String courseId);

    List<CourseEntity> findByYear(Integer year);

    List<CourseEntity> findByNameIsLike(String name);
}
