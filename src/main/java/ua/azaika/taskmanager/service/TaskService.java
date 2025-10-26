
package ua.azaika.taskmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.azaika.taskmanager.model.Task;
import ua.azaika.taskmanager.model.User;
import ua.azaika.taskmanager.repository.TaskRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    public Task save(Task task, List<Integer> usersIds) {
        List<User> users = usersIds.stream().map(userService::findById).toList();
        task.setUsers(users);
        return taskRepository.save(task);
    }

    public List<Task> getAll() {
        return taskRepository.getAll();
    }

    public Task findById(Integer id) {
        return taskRepository.getById(id);
    }

    public void deleteById(Integer id) {
        taskRepository.deleteById(id);
    }

    public Task update(Integer id, Task updates) {
        return taskRepository.update(id, updates);
    }
}