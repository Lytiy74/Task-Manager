package ua.azaika.taskmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.azaika.taskmanager.dto.UserPostRequestDTO;
import ua.azaika.taskmanager.dto.UserResponseDTO;
import ua.azaika.taskmanager.mapper.UserMapper;
import ua.azaika.taskmanager.model.User;
import ua.azaika.taskmanager.service.UserService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserResponseDTO> post(@RequestBody UserPostRequestDTO userPostRequestDTO) {
        User inputUser = userMapper.toUser(userPostRequestDTO);
        User savedUser = userService.save(inputUser);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(uri).body(userMapper.toResponseDTO(savedUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Integer id) {
        User user = userService.findById(id);
        if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(userMapper.toResponseDTO(user));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAll(
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String email) {

        if (userName != null) {
            User user = userService.findByUserName(userName);
            if (user == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(List.of(userMapper.toResponseDTO(user)));
        }

        if (email != null) {
            User user = userService.findByEmail(email);
            if (user == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(List.of(userMapper.toResponseDTO(user)));
        }

        List<UserResponseDTO> responseDTOS = userService.getAll().stream()
                .map(userMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(responseDTOS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> put(@PathVariable Integer id, @RequestBody UserPostRequestDTO userPostRequestDTO) {
        User inputUser = userMapper.toUser(userPostRequestDTO);
        User updatedUser = userService.update(id, inputUser);
        if (updatedUser == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(userMapper.toResponseDTO(updatedUser));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> patch(@PathVariable Integer id, @RequestBody UserPostRequestDTO userPostRequestDTO) {
        User inputUser = userMapper.toUser(userPostRequestDTO);
        User updatedUser = userService.partialUpdate(id, inputUser);
        if (updatedUser == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(userMapper.toResponseDTO(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
