package com.kk.ToDoListWriteOnly;

import com.kk.ToDoListWriteOnly.controller.TaskCommandController;
import com.kk.ToDoListWriteOnly.entities.Status;
import com.kk.ToDoListWriteOnly.entities.Task;
import com.kk.ToDoListWriteOnly.repository.TaskRepository;
import com.kk.ToDoListWriteOnly.service.impl.ToDoListServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskCommandController.class)
public class TaskCommandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ToDoListServiceImpl toDoListServiceImpl;

    @MockBean
    TaskRepository taskRepository;


    @Test
    void shouldAddTask() throws Exception {
        Task task = new Task(1L, "user-1","task-1", "sample task - 1", LocalDate.of(2023, 8, 20), LocalDate.of(2023, 8, 22), Status.PENDING, 10);
        when(taskRepository.save(Mockito.any())).thenReturn(task);
        when(toDoListServiceImpl.addTask(Mockito.any())).thenReturn(task);
        mockMvc.perform(MockMvcRequestBuilders.post("/todolist/api/v1/writeonly/user/add-list")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"name\": \"task-1\",\n" +
                        "    \"description\": \"Sample task-1\",\n" +
                        "    \"startDate\": \"2023-08-20T12:34:56\",\n" +
                        "    \"endDate\": \"2023-08-21T12:34:56\", \n" +
                        "    \"status\": \"PENDING\",\n" +
                        "    \"totalEfforts\": 10\n" +
                        "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("task-1"))
                .andDo(print());
    }

    @Test
    void shouldUpdateTask() throws Exception {
        List<Task> taskByName = getMockedData().stream().filter(t -> t.getName().equals("task-1")).collect(Collectors.toList());
        Optional<Task> taskById = Optional.of(taskByName.get(0));
        when(taskRepository.save(Mockito.any())).thenReturn(taskByName.get(0));
        when(taskRepository.findById(Mockito.anyLong())).thenReturn(taskById);
        when(toDoListServiceImpl.updateTask(Mockito.anyLong(),Mockito.any())).thenReturn(taskByName.get(0));
        mockMvc.perform(MockMvcRequestBuilders.put("/todolist/api/v1/writeonly/user/update/{taskId}/{taskStatus}", 1, Status.COMPLETED))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("COMPLETED"))
                .andDo(print());
    }

    List<Task> getMockedData() {
        return new ArrayList<>(
                Arrays.asList(new Task(1L, "user-1", "task-1", "sample task - 1", LocalDate.of(2023, 8, 20), LocalDate.of(2023, 8, 22), Status.COMPLETED, 10),
                        new Task(2L, "user-2", "task-2", "sample task - 2", LocalDate.of(2023, 8, 20), LocalDate.of(2023, 8, 22), Status.PENDING, 30),
                        new Task(3L, "user-3","task-3", "sample task - 3", LocalDate.of(2023, 8, 20), LocalDate.of(2023, 8, 22), Status.COMPLETED, 20)));

    }
}
