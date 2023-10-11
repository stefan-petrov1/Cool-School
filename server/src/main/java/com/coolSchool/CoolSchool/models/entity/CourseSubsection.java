package com.coolSchool.CoolSchool.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "course_subsections")
public class CourseSubsection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotNull(message = "The title of the courseSubsection should not be null!")
    private String title;
    @NotNull(message = "The description of the courseSubsection should not be null!")
    private String description;
    @ManyToOne
    @JoinColumn(name = "course_id")
    @NotNull(message = "The course of the courseSubsection should not be null!")
    private Course course;
    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;
}
