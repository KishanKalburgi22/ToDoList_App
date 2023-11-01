package com.kk.UserService.external.services;

import com.kk.UserService.entities.Task;
import com.kk.UserService.utils.AppConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "TODOLIST-READONLY")
public interface TaskReadOnlyService {

    @GetMapping(value = "/todolist/api/v1/readonly/user/list/userid/{userId}", produces = "application/json")
    Page<Task> getByUserId(@PathVariable String userId, @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                           @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize);


    @GetMapping(value = "/todolist/api/v1/readonly/user/list/all", produces = "application/json")
    Page<Task> getAllTasks(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                           @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                           @RequestParam(value = "filterBy", defaultValue = AppConstants.DEFAULT_FILTER_BY, required = false) String filterBy,
                           @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) Sort.Direction sortDir);
}
