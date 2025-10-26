package ua.azaika.taskmanager.dto;

import java.util.List;

public record TaskResponseDTO(
        Integer id,
        String title,
        String description,
        List<UserResponseDTO> users
) {
}
