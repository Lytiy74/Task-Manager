package ua.azaika.taskmanager.dto;

public record UserPostRequestDTO(
        String userName,
        String email,
        String password
) {
}
