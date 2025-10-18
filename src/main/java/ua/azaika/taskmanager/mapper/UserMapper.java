package ua.azaika.taskmanager.mapper;

import org.mapstruct.Mapper;
import ua.azaika.taskmanager.dto.UserPostRequestDTO;
import ua.azaika.taskmanager.dto.UserResponseDTO;
import ua.azaika.taskmanager.model.User;

import static org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface UserMapper {
    User toUser(UserPostRequestDTO userPostRequestDTO);

    UserResponseDTO toResponseDTO(User user);
}
