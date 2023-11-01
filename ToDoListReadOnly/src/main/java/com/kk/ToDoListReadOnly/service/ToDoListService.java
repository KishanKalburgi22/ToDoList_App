package com.kk.ToDoListReadOnly.service;

import com.kk.ToDoListReadOnly.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ToDoListService {

    Page<Task> getAllTasks(PageRequest pageRequest);

    Task getByTaskName(String taskName);

    Page<Task> getByUserId(String userId, int page, int size);
}
