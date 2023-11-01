package com.kk.ToDoListWriteOnly.entities;

import com.kk.ToDoListWriteOnly.annotations.ValidDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name="Tasks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ValidDate(message = "Task endDate should be greater than startDate.")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String userId;
    @NotBlank(message = "Task name is required & can't be empty.")
    private String name;
    @NotBlank(message = "Task description is required & can't be empty.")
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    @NotNull(message = "Task status is required & can't be empty.")
    @Enumerated(EnumType.STRING)
    private Status status;
    @Min(value = 1, message = "Task totalEfforts is required & must be greater than 0")
    private double totalEfforts;

   }
