package com.kk.ToDoListWriteOnly.service;


import com.kk.ToDoListWriteOnly.entities.Status;
import com.kk.ToDoListWriteOnly.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;


public interface ToDoListService {

    Task addTask(Task task);

    Task updateTask(Long taskId, Status taskStatus);
}
