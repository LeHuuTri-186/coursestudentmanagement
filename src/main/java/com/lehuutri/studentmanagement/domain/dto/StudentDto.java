package com.lehuutri.studentmanagement.domain.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentDto {
    private String studentId;
    private String name;
    private String address;
    private LocalDate birthday;
    private String note;
    private List<CourseDto> courses;
}
