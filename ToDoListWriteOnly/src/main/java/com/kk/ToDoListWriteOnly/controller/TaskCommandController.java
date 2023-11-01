package com.kk.ToDoListWriteOnly.controller;

import com.kk.ToDoListWriteOnly.entities.Status;
import com.kk.ToDoListWriteOnly.entities.Task;
import com.kk.ToDoListWriteOnly.service.impl.ToDoListServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/todolist/api/v1/writeonly/user")
public class TaskCommandController {

    @Autowired
    ToDoListServiceImpl toDoListService;

    private Logger LOG = LoggerFactory.getLogger(TaskCommandController.class);

    @PostMapping("/add-list")
    public ResponseEntity addTask(@RequestBody Task task) {
            Task newTask = toDoListService.addTask(task);
            return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }

    @PutMapping("/update/{taskId}/{taskStatus}")
    public ResponseEntity updateTask(@PathVariable Long taskId, @PathVariable Status taskStatus) {
            Task updateTask = toDoListService.updateTask(taskId, taskStatus);
            return ResponseEntity.ok().body(updateTask);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>("Not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
