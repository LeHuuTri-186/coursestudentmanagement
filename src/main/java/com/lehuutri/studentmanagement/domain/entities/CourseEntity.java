package com.lehuutri.studentmanagement.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
    private String id;
    private String name;
    private String lecture;
    private Integer year;
    private String note;
}
