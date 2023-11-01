package com.kk.ToDoListWriteOnly.service.impl;

import com.kk.ToDoListWriteOnly.entities.Status;
import com.kk.ToDoListWriteOnly.entities.Task;
import com.kk.ToDoListWriteOnly.events.TaskEvent;
import com.kk.ToDoListWriteOnly.repository.TaskRepository;
import com.kk.ToDoListWriteOnly.service.ToDoListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ToDoListServiceImpl implements ToDoListService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private KafkaTemplate<String, TaskEvent> kafkaTemplate;

    @Override
    public Task addTask(Task task) {
            task = taskRepository.save(task);
            TaskEvent event = new TaskEvent("TaskCreated", task);
            kafkaTemplate.send("tasks", event);
            return task;
    }

    @Override
    public Task updateTask(Long taskId, Status taskStatus) {
        Optional<Task> taskObj = taskRepository.findById(taskId);
        if (taskObj.isPresent()) {
            Task updatedTask = taskObj.get();
            updatedTask.setStatus(taskStatus);
            Task task = taskRepository.save(updatedTask);
            TaskEvent event = new TaskEvent("TaskCreated", task);
            kafkaTemplate.send("tasks", event);
            return task;
        }
        return null;
    }
}
