package com.kk.ToDoListWriteOnly.validators;

import com.kk.ToDoListWriteOnly.annotations.ValidDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.kk.ToDoListWriteOnly.entities.Task;

public class DatesValidator implements ConstraintValidator<ValidDate, Task> {
    @Override
    public boolean isValid(Task task, ConstraintValidatorContext constraintValidatorContext) {
        if (task.getStartDate() != null && task.getEndDate() != null) {
            return task.getStartDate().isBefore(task.getEndDate());
        }
        return false;
    }
}