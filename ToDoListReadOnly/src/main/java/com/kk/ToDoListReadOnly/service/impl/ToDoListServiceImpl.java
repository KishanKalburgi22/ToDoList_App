package com.kk.ToDoListReadOnly.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kk.ToDoListReadOnly.entities.Task;
import com.kk.ToDoListReadOnly.events.TaskEvent;
import com.kk.ToDoListReadOnly.repository.TaskRepository;
import com.kk.ToDoListReadOnly.service.ToDoListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToDoListServiceImpl implements ToDoListService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public Page<Task> getAllTasks(PageRequest pageRequest) {
        return taskRepository.findAll(pageRequest);
    }

    @Override
    public Task getByTaskName(String taskName) {
        return taskRepository.findByName(taskName);
    }

    @Override
    public Page<Task> getByUserId(String userId,int page, int  size) {
        Pageable paging = PageRequest.of(page, size, Sort.Direction.DESC,"totalEfforts");
        return taskRepository.findByUserIdContaining(userId,paging);
    }

    @KafkaListener(topics = "tasks", groupId = "tasks_group")
    public void processTaskEvent(String event) {

        System.out.println("Getting event " + event);

        TaskEvent taskEvent = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            taskEvent = objectMapper.readValue(event, TaskEvent.class);

            System.out.println(taskEvent);

            switch (taskEvent.getType()) {
                case "TaskCreated":

                    taskRepository.save(taskEvent.getTask());
                    break;

                default:
                    break;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
