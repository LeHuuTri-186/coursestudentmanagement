package com.lehuutri.studentmanagement.domain.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "Course")
public class CourseEntity {
    @Id
    private String courseId;
    private String name;
    private String lecture;
    private Integer year;
    private String note;

    @ManyToMany
    @JoinTable(name = "Course_Student",
        joinColumns = @JoinColumn(name = "courseId"),
        inverseJoinColumns = @JoinColumn(name = "studentId"))
    private List<StudentEntity> students;
}
