package com.lehuutri.studentmanagement.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.lehuutri.studentmanagement.domain.entities.CourseEntity;
import com.lehuutri.studentmanagement.domain.entities.StudentEntity;
import com.lehuutri.studentmanagement.repositories.StudentRepository;
import com.lehuutri.studentmanagement.services.StudentService;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImpl(final StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public StudentEntity saveStudent(StudentEntity student) {
        return this.studentRepository.save(student);
    }

    @Override
    public Optional<StudentEntity> findOneStudent(String id) {
        return this.studentRepository.findById(id);
    }

    @Override
    public Iterable<StudentEntity> findAllStudent() {
        return this.studentRepository.findAll();
    }

    @Override
    public boolean deleteStudent(String id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);

            return true;
        }

        return false;
    }

    @Override
    public List<StudentEntity> findStudentInCourse(CourseEntity course) {
        return StreamSupport.stream(studentRepository.findStudentInCourse(course.getCourseId()).spliterator(), false).toList();
    }

    @Override
    public List<StudentEntity> findStudentNotInCourse(CourseEntity course) {
        return StreamSupport.stream(studentRepository.findStudentNotInCourse(course.getCourseId()).spliterator(), false).toList();
    }

    @Override
    public List<StudentEntity> findStudentByName(String name) {
        if (name == null || name.strip().length() == 0) {
            return StreamSupport.stream(findAllStudent().spliterator(), false).toList();
        }
        return this.studentRepository.findByNameIsLike(name + "%");
    }
}
