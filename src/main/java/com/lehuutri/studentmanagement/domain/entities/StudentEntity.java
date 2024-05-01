package com.lehuutri.studentmanagement.domain.entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    @NotNull
    private LocalDate birthday;

    private String note;

    @OneToMany(mappedBy = "student")
    private List<CourseStudentEntity> courses;
}
