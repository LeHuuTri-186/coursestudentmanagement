package com.lehuutri.studentmanagement.services.impl;

import org.springframework.stereotype.Service;

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
    public StudentEntity createStudent(StudentEntity student) {
        return this.studentRepository.save(student);
    }
}
