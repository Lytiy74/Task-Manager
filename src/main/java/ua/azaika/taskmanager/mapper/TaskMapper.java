package ua.azaika.taskmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ua.azaika.taskmanager.dto.TaskPostRequestDTO;
import ua.azaika.taskmanager.dto.TaskResponseDTO;
import ua.azaika.taskmanager.model.Task;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = UserMapper.class)
public interface TaskMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "users", ignore = true)
    Task toTask(TaskPostRequestDTO taskPostRequestDTO);

    TaskResponseDTO toResponseDTO(Task task);
}
