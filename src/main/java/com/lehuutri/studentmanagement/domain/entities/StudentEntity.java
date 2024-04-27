package com.lehuutri.studentmanagement.domain.entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "Student")
public class StudentEntity {
    @Id
    private String studentId;
    private String name;
    private String address;
    private LocalDate birthday;
    private String note;
    @ManyToMany(mappedBy = "students")
    private List<CourseEntity> courses;
}
