package com.lehuutri.studentmanagement.domain.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseDto {
    private String courseId;
    private String name;
    private String lecture;
    private Integer year;
    private String note;
    private List<StudentDto> students;
}
