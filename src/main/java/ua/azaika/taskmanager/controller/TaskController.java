package ua.azaika.taskmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.azaika.taskmanager.dto.TaskPostRequestDTO;
import ua.azaika.taskmanager.dto.TaskResponseDTO;
import ua.azaika.taskmanager.mapper.TaskMapper;
import ua.azaika.taskmanager.model.Task;
import ua.azaika.taskmanager.service.TaskService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @PostMapping
    public ResponseEntity<TaskResponseDTO> post(@RequestBody TaskPostRequestDTO taskPostRequestDTO) {
        Task task = taskMapper.toTask(taskPostRequestDTO);
        Task savedTask = taskService.save(task, taskPostRequestDTO.usersIds());
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedTask.getId())
                .toUri();
        return ResponseEntity.created(uri).body(taskMapper.toResponseDTO(savedTask));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAll() {
        List<Task> taskList = taskService.getAll();
        if (taskList.isEmpty()) return ResponseEntity.notFound().build();
        List<TaskResponseDTO> responseDTOs = taskList.stream()
                .map(taskMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> findById(@PathVariable Integer id) {
        Task task = taskService.findById(id);
        if (task == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(taskMapper.toResponseDTO(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> update(@PathVariable Integer id, @RequestBody TaskPostRequestDTO taskPostRequestDTO) {
        Task updates = taskMapper.toTask(taskPostRequestDTO);
        Task updatedTask = taskService.update(id, updates);
        return ResponseEntity.ok(taskMapper.toResponseDTO(updatedTask));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        taskService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}