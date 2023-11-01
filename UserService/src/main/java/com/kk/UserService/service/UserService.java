package com.kk.UserService.service;

import com.kk.UserService.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface UserService {
    User saveUser(User user);

    Page<User> getAllUsers(int pageNo, int pageSize, String filterBy, Sort.Direction sortDir);

    Page<User> getUserById(String userId,int pageNo, int pageSize);
}
