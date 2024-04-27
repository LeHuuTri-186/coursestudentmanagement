package com.lehuutri.studentmanagement.services.impl;

import org.springframework.stereotype.Service;

import com.lehuutri.studentmanagement.domain.entities.CourseEntity;
import com.lehuutri.studentmanagement.repositories.CourseRepository;
import com.lehuutri.studentmanagement.services.CourseService;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;

    public CourseServiceImpl(final CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public CourseEntity createCourse(CourseEntity course) {
        return this.courseRepository.save(course);
    }
}
