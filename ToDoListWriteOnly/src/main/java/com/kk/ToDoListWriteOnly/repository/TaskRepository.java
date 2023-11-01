package com.kk.ToDoListWriteOnly.repository;

import com.kk.ToDoListWriteOnly.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
}
