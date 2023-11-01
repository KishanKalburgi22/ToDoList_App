package com.kk.UserService.service.impl;

import com.kk.UserService.entities.Task;
import com.kk.UserService.entities.User;
import com.kk.UserService.external.services.TaskReadOnlyService;
import com.kk.UserService.repository.UserRepository;
import com.kk.UserService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskReadOnlyService taskReadOnlyService;

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Page<User> getAllUsers(int pageNo, int pageSize, String filterBy, Sort.Direction sortDir) {
        Page<Task> taskList = taskReadOnlyService.getAllTasks(pageNo, pageSize, filterBy, sortDir);
        Map<String, List<Task>> groupedTasks = taskList.getContent().stream().collect(groupingBy(Task::getUserId));
        List<User> userList = new ArrayList<>();
       for(String key : groupedTasks.keySet()) {
           User user = userRepository.findById(key).orElse(null);
           if (user != null) {
               user.setTasks(groupedTasks.get(key));
               userList.add(user);
           }
       }
        return new PageImpl<>(userList,taskList.getPageable(),taskList.getTotalElements());
    }

    @Override
    public Page<User> getUserById(String userId,int pageNo, int pageSize) {
        User user = userRepository.findById(userId).orElse(null);
        Page<Task> tasks = taskReadOnlyService.getByUserId(userId,pageNo,pageSize);
        if (user != null)
            user.setTasks(tasks.getContent());
        return new PageImpl<>(Collections.singletonList(user), tasks.getPageable(), tasks.getTotalElements());
    }
}
