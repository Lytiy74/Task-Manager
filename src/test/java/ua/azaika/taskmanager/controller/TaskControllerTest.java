package ua.azaika.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.azaika.taskmanager.dto.TaskPostRequestDTO;
import ua.azaika.taskmanager.dto.TaskResponseDTO;
import ua.azaika.taskmanager.dto.UserResponseDTO;
import ua.azaika.taskmanager.mapper.TaskMapper;
import ua.azaika.taskmanager.model.Task;
import ua.azaika.taskmanager.model.User;
import ua.azaika.taskmanager.service.TaskService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private TaskMapper taskMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenTask_whenPost_ShouldReturnResponseStatusCreated() throws Exception {
        //given
        User user1 = User.builder()
                .id(1)
                .userName("User1")
                .email("user1@test.com")
                .password("pass1")
                .build();

        Task taskInput = Task.builder()
                .title("Task1")
                .description("Description1")
                .build();

        Task savedTask = Task.builder()
                .id(1)
                .title("Task1")
                .description("Description1")
                .users(List.of(user1))
                .build();

        TaskPostRequestDTO postRequestDTO = new TaskPostRequestDTO(
                "Task1",
                "Description1",
                List.of(1)
        );

        UserResponseDTO userResponseDTO = new UserResponseDTO(1, "User1", "user1@test.com");
        TaskResponseDTO responseDTO = new TaskResponseDTO(
                1,
                "Task1",
                "Description1",
                List.of(userResponseDTO)
        );

        Mockito.when(taskMapper.toTask(Mockito.any(TaskPostRequestDTO.class))).thenReturn(taskInput);
        Mockito.when(taskService.save(Mockito.any(Task.class), Mockito.anyList())).thenReturn(savedTask);
        Mockito.when(taskMapper.toResponseDTO(Mockito.any(Task.class))).thenReturn(responseDTO);

        //when-then
        mvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void givenTask_whenPost_ShouldReturnResponseBodyTask() throws Exception {
        //given
        User user1 = User.builder()
                .id(1)
                .userName("User1")
                .email("user1@test.com")
                .password("pass1")
                .build();

        Task taskInput = Task.builder()
                .title("Task1")
                .description("Description1")
                .build();

        Task savedTask = Task.builder()
                .id(1)
                .title("Task1")
                .description("Description1")
                .users(List.of(user1))
                .build();

        TaskPostRequestDTO postRequestDTO = new TaskPostRequestDTO(
                "Task1",
                "Description1",
                List.of(1)
        );

        UserResponseDTO userResponseDTO = new UserResponseDTO(1, "User1", "user1@test.com");
        TaskResponseDTO responseDTO = new TaskResponseDTO(
                1,
                "Task1",
                "Description1",
                List.of(userResponseDTO)
        );

        Mockito.when(taskMapper.toTask(Mockito.any(TaskPostRequestDTO.class))).thenReturn(taskInput);
        Mockito.when(taskService.save(Mockito.any(Task.class), Mockito.anyList())).thenReturn(savedTask);
        Mockito.when(taskMapper.toResponseDTO(Mockito.any(Task.class))).thenReturn(responseDTO);

        //when-then
        mvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequestDTO)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Task1"))
                .andExpect(jsonPath("$.description").value("Description1"))
                .andExpect(jsonPath("$.users[0].id").value(1))
                .andExpect(jsonPath("$.users[0].userName").value("User1"));
    }

    @Test
    void givenTaskId_whenGetById_ShouldReturnTask() throws Exception {
        //given
        User user1 = User.builder()
                .id(1)
                .userName("User1")
                .email("user1@test.com")
                .password("pass1")
                .build();

        Task task = Task.builder()
                .id(1)
                .title("Task1")
                .description("Description1")
                .users(List.of(user1))
                .build();

        UserResponseDTO userResponseDTO = new UserResponseDTO(1, "User1", "user1@test.com");
        TaskResponseDTO responseDTO = new TaskResponseDTO(
                1,
                "Task1",
                "Description1",
                List.of(userResponseDTO)
        );

        Mockito.when(taskService.findById(1)).thenReturn(task);
        Mockito.when(taskMapper.toResponseDTO(task)).thenReturn(responseDTO);

        //when-then
        mvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Task1"))
                .andExpect(jsonPath("$.description").value("Description1"))
                .andExpect(jsonPath("$.users[0].id").value(1))
                .andExpect(jsonPath("$.users[0].userName").value("User1"));
    }

    @Test
    void givenNonExistingTaskId_whenGetById_ShouldReturnNotFound() throws Exception {
        //given
        Mockito.when(taskService.findById(999)).thenReturn(null);

        //when-then
        mvc.perform(get("/api/tasks/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetAll_ShouldReturnAllTasks() throws Exception {
        //given
        User user1 = User.builder()
                .id(1)
                .userName("User1")
                .email("user1@test.com")
                .password("pass1")
                .build();

        User user2 = User.builder()
                .id(2)
                .userName("User2")
                .email("user2@test.com")
                .password("pass2")
                .build();

        Task task1 = Task.builder()
                .id(1)
                .title("Task1")
                .description("Description1")
                .users(List.of(user1))
                .build();

        Task task2 = Task.builder()
                .id(2)
                .title("Task2")
                .description("Description2")
                .users(List.of(user2))
                .build();

        UserResponseDTO userResponseDTO1 = new UserResponseDTO(1, "User1", "user1@test.com");
        UserResponseDTO userResponseDTO2 = new UserResponseDTO(2, "User2", "user2@test.com");

        TaskResponseDTO responseDTO1 = new TaskResponseDTO(
                1,
                "Task1",
                "Description1",
                List.of(userResponseDTO1)
        );
        TaskResponseDTO responseDTO2 = new TaskResponseDTO(
                2,
                "Task2",
                "Description2",
                List.of(userResponseDTO2)
        );

        Mockito.when(taskService.getAll()).thenReturn(List.of(task1, task2));
        Mockito.when(taskMapper.toResponseDTO(task1)).thenReturn(responseDTO1);
        Mockito.when(taskMapper.toResponseDTO(task2)).thenReturn(responseDTO2);

        //when-then
        mvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Task1"))
                .andExpect(jsonPath("$[0].users[0].userName").value("User1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Task2"))
                .andExpect(jsonPath("$[1].users[0].userName").value("User2"));
    }

    @Test
    void givenEmptyTaskList_whenGetAll_ShouldReturnNotFound() throws Exception {
        //given
        Mockito.when(taskService.getAll()).thenReturn(List.of());

        //when-then
        mvc.perform(get("/api/tasks"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenTaskIdAndUpdatedData_whenPut_ShouldReturnUpdatedTask() throws Exception {
        //given
        User user1 = User.builder()
                .id(1)
                .userName("User1")
                .email("user1@test.com")
                .password("pass1")
                .build();

        TaskPostRequestDTO putRequestDTO = new TaskPostRequestDTO(
                "UpdatedTask",
                "UpdatedDescription",
                List.of(1)
        );

        Task inputTask = Task.builder()
                .title("UpdatedTask")
                .description("UpdatedDescription")
                .build();

        Task updatedTask = Task.builder()
                .id(1)
                .title("UpdatedTask")
                .description("UpdatedDescription")
                .users(List.of(user1))
                .build();

        UserResponseDTO userResponseDTO = new UserResponseDTO(1, "User1", "user1@test.com");
        TaskResponseDTO responseDTO = new TaskResponseDTO(
                1,
                "UpdatedTask",
                "UpdatedDescription",
                List.of(userResponseDTO)
        );

        Mockito.when(taskMapper.toTask(Mockito.any(TaskPostRequestDTO.class))).thenReturn(inputTask);
        Mockito.when(taskService.update(Mockito.eq(1), Mockito.any(Task.class))).thenReturn(updatedTask);
        Mockito.when(taskMapper.toResponseDTO(updatedTask)).thenReturn(responseDTO);

        //when-then
        mvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(putRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("UpdatedTask"))
                .andExpect(jsonPath("$.description").value("UpdatedDescription"))
                .andExpect(jsonPath("$.users[0].id").value(1));
    }

    @Test
    void givenTaskId_whenDelete_ShouldReturnNoContent() throws Exception {
        //given
        Mockito.doNothing().when(taskService).deleteById(1);

        //when-then
        mvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(taskService, Mockito.times(1)).deleteById(1);
    }
}