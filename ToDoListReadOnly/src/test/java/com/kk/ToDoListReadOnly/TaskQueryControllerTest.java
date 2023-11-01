package com.kk.ToDoListReadOnly;

import com.kk.ToDoListReadOnly.controller.TaskQueryController;
import com.kk.ToDoListReadOnly.entities.Status;
import com.kk.ToDoListReadOnly.entities.Task;
import com.kk.ToDoListReadOnly.repository.TaskRepository;
import com.kk.ToDoListReadOnly.service.impl.ToDoListServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskQueryController.class)
public class TaskQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ToDoListServiceImpl toDoListServiceImpl;

    @Mock
    TaskRepository taskRepository;

    @InjectMocks
    ToDoListServiceImpl toDoListService;

    @Test
    void shouldProcessKafkaEvent() {
        String event = "{\"type\":\"TaskCreated\",\"task\":{\"id\":2,\"name\":\"task-1\",\"description\":\"Sample task-1\",\"startDate\":[2023,8,20],\"endDate\":[2023,8,21],\"status\":\"PENDING\",\"totalEfforts\":10.0}}";
        when(taskRepository.save(Mockito.any())).thenReturn(getMockedData().get(0));
        toDoListService.processTaskEvent(event);
    }

    @Test
    void shouldReturnListOfTasks() throws Exception {
        List<Task> taskList = getMockedData();
        Page<Task> taskList1 = new PageImpl<>(taskList);
        when(toDoListServiceImpl.getAllTasks(PageRequest.of(0, 10, Sort.Direction.DESC, "totalEfforts"))).thenReturn(taskList1);
        mockMvc.perform(MockMvcRequestBuilders.get("/todolist/api/v1/readonly/user/list/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(3))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andDo(print());
    }

    @Test
    void shouldReturnFilteredListOfTasks() throws Exception {
        List<Task> taskList = getMockedData();
        Page<Task> taskList1 = new PageImpl<>(taskList);
        when(toDoListServiceImpl.getAllTasks(PageRequest.of(0, 10, Sort.Direction.DESC, "totalEfforts"))).thenReturn(taskList1);
        mockMvc.perform(MockMvcRequestBuilders.get("/todolist/api/v1/readonly/user/list/all?filterBy=task-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(1))
                .andExpect(jsonPath("$.content[0].name").value("task-1"))
                .andDo(print());
    }

    @Test
    void shouldReturnTaskByTaskName() throws Exception {
        List<Task> taskByName = getMockedData().stream().filter(t -> t.getName().equals("task-1")).collect(Collectors.toList());
        when(toDoListServiceImpl.getByTaskName(Mockito.anyString())).thenReturn(taskByName.get(0));
        mockMvc.perform(MockMvcRequestBuilders.get("/todolist/api/v1/readonly/user/list/{taskName}", "task-1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andDo(print());
    }

    List<Task> getMockedData() {
        return new ArrayList<>(
                Arrays.asList(new Task(1L,"user-1", "task-1", "sample task - 1", LocalDate.of(2023, 8, 20), LocalDate.of(2023, 8, 22), Status.PENDING, 10),
                        new Task(2L, "user-2","task-2", "sample task - 2", LocalDate.of(2023, 8, 20), LocalDate.of(2023, 8, 22), Status.PENDING, 30),
                        new Task(3L, "user-3","task-3", "sample task - 3", LocalDate.of(2023, 8, 20), LocalDate.of(2023, 8, 22), Status.COMPLETED, 20)));

    }
}
