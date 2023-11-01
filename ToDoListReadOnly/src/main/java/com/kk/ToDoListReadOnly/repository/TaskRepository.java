package com.kk.ToDoListReadOnly.repository;

import com.kk.ToDoListReadOnly.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Task findByName(String name);

    List<Task> findAllByUserId(String userId);

    Page<Task> findByUserIdContaining(String userId, Pageable pageable);

    Page<Task> findAll(Pageable pageable);

}
