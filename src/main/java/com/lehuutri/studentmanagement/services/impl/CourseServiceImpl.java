package com.lehuutri.studentmanagement.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

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
    public CourseEntity saveCourse(CourseEntity course) {
        return this.courseRepository.save(course);
    }

    @Override
    public Iterable<CourseEntity> findAllCourse() {
        return courseRepository.findAll();
    }

    @Override
    public Optional<CourseEntity> findOneCourse(String courseId) {
        return this.courseRepository.findById(courseId);
    }

    @Override
    public void deleteById(String courseId) {
        if (findOneCourse(courseId).isPresent()) {
            this.courseRepository.deleteById(courseId);
        }
    }

    @Override
    public List<CourseEntity> findByYear(Integer year) {
        if (year == null) {
            return StreamSupport.stream(findAllCourse().spliterator(), false).toList();
        }
        return this.courseRepository.findByYear(year);
    }

    @Override
    public List<CourseEntity> findByNameIsLike(String name) {
        if (name == null) {
            return StreamSupport.stream(findAllCourse().spliterator(), false).toList();
        }
        return this.courseRepository.findByNameIsLike(name + "%");
    }
}
