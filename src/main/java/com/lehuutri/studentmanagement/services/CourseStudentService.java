package com.lehuutri.studentmanagement.services;

import java.util.List;
import java.util.Optional;

import com.lehuutri.studentmanagement.domain.entities.CourseEntity;
import com.lehuutri.studentmanagement.domain.entities.CourseStudentEntity;
import com.lehuutri.studentmanagement.domain.entities.StudentEntity;

public interface CourseStudentService {
    CourseStudentEntity saveCourseStudent(CourseStudentEntity entity);

    List<CourseStudentEntity> findByCourse(CourseEntity course);

    void deleteByCourseAndStudent(CourseEntity course, StudentEntity student);

    Optional<CourseStudentEntity> findByCourseAndStudent(CourseEntity course, StudentEntity student);

    void deleteByStudent(StudentEntity student);
}
