package com.lehuutri.studentmanagement.domain.entities;

import java.time.LocalDate;

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
@Table(name = "Student")
public class StudentEntity {
    @Id
    private String id;
    private String name;
    private String address;
    private LocalDate birthday;
    private String note;
}
