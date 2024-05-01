package com.lehuutri.studentmanagement.services;

import java.util.List;
import java.util.Optional;

import com.lehuutri.studentmanagement.domain.entities.CourseEntity;
import com.lehuutri.studentmanagement.domain.entities.StudentEntity;

public interface StudentService {
    StudentEntity saveStudent(StudentEntity student);

    Optional<StudentEntity> findOneStudent(final String id);

    Iterable<StudentEntity> findAllStudent();

    boolean deleteStudent(String id);

    List<StudentEntity> findStudentInCourse(CourseEntity course);

    List<StudentEntity> findStudentNotInCourse(CourseEntity course);

    List<StudentEntity> findStudentByName(String name);
}
