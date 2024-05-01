package com.lehuutri.studentmanagement.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.lehuutri.studentmanagement.domain.entities.StudentEntity;

@Repository
public interface StudentRepository extends CrudRepository<StudentEntity, String> {
    @Query(value = "SELECT s.* from student s where s.student_id in ( SELECT c_s.student_id from course_student c_s where c_s.course_id = ?1)", nativeQuery = true)
    Iterable<StudentEntity> findStudentInCourse(String courseId);

    @Query(value = "SELECT s.* from student s where s.student_id not in ( SELECT c_s.student_id from course_student c_s where c_s.course_id = ?1)", nativeQuery = true)
    Iterable<StudentEntity> findStudentNotInCourse(String courseId);

    List<StudentEntity> findByNameIsLike(String searchVal);
}
