package ua.azaika.taskmanager.dto;

import java.util.List;

public record TaskPostRequestDTO(
        String title,
        String description,
        List<Integer> usersIds
) {
}
