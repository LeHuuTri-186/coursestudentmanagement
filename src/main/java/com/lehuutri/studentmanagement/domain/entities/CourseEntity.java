package com.lehuutri.studentmanagement.domain.entities;

import java.io.Serializable;
import java.util.List;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
@Table(name = "Course")
public class CourseEntity implements Serializable {
    @Id
    private String courseId;

    @NotBlank
    private String name;

    @NotBlank
    private String lecturer;

    @NotNull
    private Integer year;

    private String note;

    @OneToMany(mappedBy = "course")
    @Fetch(FetchMode.SELECT)
    private List<CourseStudentEntity> students;
}
