
package ua.azaika.taskmanager.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ua.azaika.taskmanager.model.Task;
import ua.azaika.taskmanager.model.User;
import ua.azaika.taskmanager.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @MockitoBean
    private TaskRepository taskRepository;

    @MockitoBean
    private UserService userService;

    @Test
    void givenTaskWithUsers_whenSave_ShouldReturnSavedTask() {
        //given
        User user1 = User.builder().id(1).userName("User1").email("user1@test.com").password("pass1").build();
        User user2 = User.builder().id(2).userName("User2").email("user2@test.com").password("pass2").build();

        Task taskToSave = Task.builder()
                .title("Task1")
                .description("Description1")
                .build();

        Task savedTask = Task.builder()
                .id(1)
                .title("Task1")
                .description("Description1")
                .users(List.of(user1, user2))
                .build();

        List<Integer> usersIds = List.of(1, 2);

        Mockito.when(userService.findById(1)).thenReturn(user1);
        Mockito.when(userService.findById(2)).thenReturn(user2);
        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(savedTask);

        //when
        Task result = taskService.save(taskToSave, usersIds);

        //then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(1);
        Assertions.assertThat(result.getTitle()).isEqualTo("Task1");
        Assertions.assertThat(result.getUsers()).hasSize(2);
        Mockito.verify(taskRepository, Mockito.times(1)).save(Mockito.any(Task.class));
    }

    @Test
    void givenTaskList_whenGetAll_ShouldReturnAllTasks() {
        //given
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Task task = Task.builder()
                    .id(i + 1)
                    .title("Task" + i)
                    .description("Description" + i)
                    .build();
            tasks.add(task);
        }

        Mockito.when(taskRepository.getAll()).thenReturn(tasks);

        //when
        List<Task> result = taskService.getAll();

        //then
        Assertions.assertThat(result).hasSize(5);
        Assertions.assertThat(result).containsAll(tasks);
        Mockito.verify(taskRepository, Mockito.times(1)).getAll();
    }

    @Test
    void givenTaskId_whenFindById_ShouldReturnTask() {
        //given
        Task task = Task.builder()
                .id(1)
                .title("Task1")
                .description("Description1")
                .build();

        Mockito.when(taskRepository.getById(1)).thenReturn(task);

        //when
        Task result = taskService.findById(1);

        //then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(1);
        Assertions.assertThat(result.getTitle()).isEqualTo("Task1");
        Mockito.verify(taskRepository, Mockito.times(1)).getById(1);
    }

    @Test
    void givenTaskId_whenDeleteById_ShouldCallRepositoryDelete() {
        //given
        Integer taskId = 1;
        Mockito.doNothing().when(taskRepository).deleteById(taskId);

        //when
        taskService.deleteById(taskId);

        //then
        Mockito.verify(taskRepository, Mockito.times(1)).deleteById(taskId);
    }

    @Test
    void givenTaskIdAndUpdates_whenUpdate_ShouldReturnUpdatedTask() {
        //given
        Task updates = Task.builder()
                .title("UpdatedTask")
                .description("UpdatedDescription")
                .build();

        Task updatedTask = Task.builder()
                .id(1)
                .title("UpdatedTask")
                .description("UpdatedDescription")
                .build();

        Mockito.when(taskRepository.update(1, updates)).thenReturn(updatedTask);

        //when
        Task result = taskService.update(1, updates);

        //then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(1);
        Assertions.assertThat(result.getTitle()).isEqualTo("UpdatedTask");
        Assertions.assertThat(result.getDescription()).isEqualTo("UpdatedDescription");
        Mockito.verify(taskRepository, Mockito.times(1)).update(1, updates);
    }
}