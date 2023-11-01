package com.kk.ToDoListReadOnly.controller;

import com.kk.ToDoListReadOnly.entities.Task;
import com.kk.ToDoListReadOnly.repository.TaskRepository;
import com.kk.ToDoListReadOnly.service.ToDoListService;
import com.kk.ToDoListReadOnly.service.impl.ToDoListServiceImpl;
import com.kk.ToDoListReadOnly.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.support.PageableUtils;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/todolist/api/v1/readonly/user")
public class TaskQueryController {

    @Autowired
    ToDoListServiceImpl toDoListService;

    @GetMapping(value = "/list/all", produces = "application/json")
    public Page<Task> getAllTasks(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                  @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                  @RequestParam(value = "filterBy", defaultValue = AppConstants.DEFAULT_FILTER_BY, required = false) String filterBy,
                                  @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) Sort.Direction sortDir) {
        Page<Task> taskList = toDoListService.getAllTasks(PageRequest.of(pageNo, pageSize, sortDir, "totalEfforts"));
        List<Task> filteredList = null;
        if (!filterBy.isEmpty()) {
            filteredList = taskList.getContent().stream().filter(t -> t.toString().contains(filterBy)).collect(Collectors.toList());
            return new PageImpl<>(filteredList, taskList.getPageable(), filteredList.size());
        }

        return taskList;
    }

    @GetMapping(value = "/list/{taskName}", produces = "application/json")
    public Task getByTaskName(@PathVariable String taskName) {
        return toDoListService.getByTaskName(taskName);
    }

    @GetMapping(value = "/list/userid/{userId}", produces = "application/json")
    public Page<Task> getByUserId(@PathVariable String userId, @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                  @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize) {
        return toDoListService.getByUserId(userId, pageNo, pageSize);
    }
}
