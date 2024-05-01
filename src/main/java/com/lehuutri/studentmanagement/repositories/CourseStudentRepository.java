package com.lehuutri.studentmanagement.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.lehuutri.studentmanagement.domain.entities.CourseEntity;
import com.lehuutri.studentmanagement.domain.entities.CourseStudentEntity;

import jakarta.transaction.Transactional;


@Repository
public interface CourseStudentRepository extends CrudRepository<CourseStudentEntity, Long> {
    List<CourseStudentEntity> findByCourse(CourseEntity course);

    @Query(value = "SELECT count(*) from course_student c_s where c_s.course_id = ?1 and c_s.student_id = ?2", nativeQuery = true)
    Long countByCourseAndStudentIds(String courseId, String studentId);

    @Transactional
    @Modifying
    @Query(value = "DELETE from course_student c_s where c_s.course_id = ?1 and c_s.student_id = ?2", nativeQuery = true)
    void deleteByCourseAndStudentIds(String courseId, String studentId);

    @Query(value = "SELECT * from course_student c_s where c_s.course_id = ?1 and c_s.student_id = ?2", nativeQuery = true)
    Optional<CourseStudentEntity> findByCourseAndStudentIds(String courseId, String studentId);

    @Transactional
    @Modifying
    @Query(value = "DELETE from course_student c_s where c_s.student_id = ?1", nativeQuery = true)
    void deleteByStudent(String studentId);
}
