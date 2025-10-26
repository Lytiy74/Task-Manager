package ua.azaika.taskmanager.dto;

public record UserResponseDTO(
        Integer id,
        String userName,
        String email
) {
}
