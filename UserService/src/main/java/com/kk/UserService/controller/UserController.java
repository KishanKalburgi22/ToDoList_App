package com.kk.UserService.controller;

import com.kk.UserService.entities.LoginRequestDTO;
import com.kk.UserService.entities.Status;
import com.kk.UserService.entities.Task;
import com.kk.UserService.entities.User;
import com.kk.UserService.repository.UserRepository;
import com.kk.UserService.service.UserService;
import com.kk.UserService.utils.AppConstants;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);
        User newUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<?> performLogin(@RequestBody LoginRequestDTO loginRequestDTO) {
        User user = userRepository.getUserByEmail(loginRequestDTO.getEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found..!!!");
        }
        if (user.getPassword().equals(loginRequestDTO.getPassword())) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User Authentication Failed..!!!");
    }

    @GetMapping(value = "/{userId}", produces = "application/json")
    @CircuitBreaker(name = "taskServiceBreaker", fallbackMethod = "taskServiceFallback1")
    public ResponseEntity<?> getUserById(@PathVariable String userId, @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                         @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize) {
        Page<User> user = userService.getUserById(userId, pageNo, pageSize);
        if (user != null)
            return ResponseEntity.ok(user);
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!!!");
    }

    @GetMapping(value = "/getAllUsers", produces = "application/json")
    @CircuitBreaker(name = "taskServiceBreaker", fallbackMethod = "taskServiceFallback2")
    public Page<User> getAllUsers(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                  @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                  @RequestParam(value = "filterBy", defaultValue = AppConstants.DEFAULT_FILTER_BY, required = false) String filterBy,
                                  @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) Sort.Direction sortDir) {
        return userService.getAllUsers(pageNo, pageSize, filterBy, sortDir);
    }

    public ResponseEntity<?> taskServiceFallback1(String userId, Exception e) {
        LOG.info("Fallback method is executed as the task service is down - {}", e.getMessage());
        Task task = new Task(0L, "DummyUserId", "DummyTaskName", "Dummy user as the task service is down", LocalDate.now(), LocalDate.now(), Status.PENDING, 0);
        User user = User.builder().userId("DummyuserId").tasks(Arrays.asList(task)).build();
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(user);
    }

    public Page<User> taskServiceFallback2(int pageNo, int pageSize, String filterBy, Sort.Direction sortDir, Exception e) {
        LOG.info("Fallback method is executed as the task service is down - {}", e.getMessage());
        Task task = new Task(0L, "DummyUserId", "DummyTaskName", "Dummy user as the task service is down", LocalDate.now(), LocalDate.now(), Status.PENDING, 0);
        User user = User.builder().userId("DummyuserId").tasks(Arrays.asList(task)).build();
        return new PageImpl<>(Arrays.asList(user));
    }
}
