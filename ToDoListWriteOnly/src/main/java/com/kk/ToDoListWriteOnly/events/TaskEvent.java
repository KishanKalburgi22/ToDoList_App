package com.kk.ToDoListWriteOnly.events;

import com.kk.ToDoListWriteOnly.entities.Task;

public class TaskEvent {

    private String type;
    private Task task;

    public TaskEvent(String type, Task task) {
        this.type = type;
        this.task = task;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
