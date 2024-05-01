package com.lehuutri.studentmanagement.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.lehuutri.studentmanagement.domain.entities.CourseEntity;
import com.lehuutri.studentmanagement.domain.entities.CourseStudentEntity;
import com.lehuutri.studentmanagement.domain.entities.StudentEntity;
import com.lehuutri.studentmanagement.repositories.CourseStudentRepository;
import com.lehuutri.studentmanagement.services.CourseStudentService;

@Service
public class CourseStudentServiceImpl implements CourseStudentService {
    private final CourseStudentRepository courseStudentRepository;

    public CourseStudentServiceImpl(final CourseStudentRepository courseStudentRepository) {
        this.courseStudentRepository = courseStudentRepository;
    }

    @Override
    public CourseStudentEntity saveCourseStudent(CourseStudentEntity entity) {
        if (courseStudentRepository.countByCourseAndStudentIds(entity.getCourse().getCourseId(), entity.getStudent().getStudentId()) > 1) {
            return null;
        }

        return courseStudentRepository.save(entity);
    }

    @Override
    public List<CourseStudentEntity> findByCourse(CourseEntity course) {
        return courseStudentRepository.findByCourse(course);
    }

    @Override
    public void deleteByCourseAndStudent(CourseEntity course, StudentEntity student) {
        this.courseStudentRepository.deleteByCourseAndStudentIds(course.getCourseId(), student.getStudentId());
    }

    @Override
    public Optional<CourseStudentEntity> findByCourseAndStudent(CourseEntity course, StudentEntity student) {
        return this.courseStudentRepository.findByCourseAndStudentIds(course.getCourseId(), student.getStudentId());
    }

    @Override
    public void deleteByStudent(StudentEntity student) {
        this.courseStudentRepository.deleteByStudent(student.getStudentId());
    }
}
