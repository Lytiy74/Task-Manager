package ua.azaika.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.azaika.taskmanager.dto.UserPostRequestDTO;
import ua.azaika.taskmanager.dto.UserResponseDTO;
import ua.azaika.taskmanager.mapper.UserMapper;
import ua.azaika.taskmanager.model.User;
import ua.azaika.taskmanager.service.UserService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserMapper userMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void givenUser_whenPost_ShouldReturnResponseStatusCreated() throws Exception {
        //given
        User savedUserEntity = User.builder()
                .id(1)
                .userName("User1")
                .email("testMail@god.com")
                .password("password1")
                .build();

        UserPostRequestDTO postRequestDTO = new UserPostRequestDTO(
                savedUserEntity.getUserName(),
                savedUserEntity.getEmail(),
                "password1"
        );

        UserResponseDTO responseDTO = new UserResponseDTO(
                "1",
                savedUserEntity.getUserName(),
                savedUserEntity.getEmail()
        );
        Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(savedUserEntity);
        Mockito.when(userMapper.toUser(Mockito.any(UserPostRequestDTO.class))).thenReturn(savedUserEntity);
        Mockito.when(userMapper.toResponseDTO(Mockito.any(User.class))).thenReturn(responseDTO);

        //when-then
        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequestDTO)))
                .andExpect(status().isCreated());

    }

    @Test
    void givenUser_whenPost_ShouldReturnResponseBodyUser() throws Exception {
        //given
        User savedUserEntity = User.builder()
                .id(1)
                .userName("User1")
                .email("testMail@god.com")
                .password("password1")
                .build();

        UserPostRequestDTO postRequestDTO = new UserPostRequestDTO(
                savedUserEntity.getUserName(),
                savedUserEntity.getEmail(),
                "password1"
        );

        UserResponseDTO responseDTO = new UserResponseDTO(
                "1",
                savedUserEntity.getUserName(),
                savedUserEntity.getEmail()
        );
        Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(savedUserEntity);
        Mockito.when(userMapper.toUser(Mockito.any(UserPostRequestDTO.class))).thenReturn(savedUserEntity);
        Mockito.when(userMapper.toResponseDTO(Mockito.any(User.class))).thenReturn(responseDTO);

        //when-then
        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequestDTO)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(responseDTO)));

    }

    @Test
    void givenUserId_whenGetById_ShouldReturnUser() throws Exception {
        //given
        User user = User.builder()
                .id(1)
                .userName("User1")
                .email("testMail@god.com")
                .password("password1")
                .build();

        UserResponseDTO responseDTO = new UserResponseDTO(
                "1",
                user.getUserName(),
                user.getEmail()
        );

        Mockito.when(userService.findById(1)).thenReturn(user);
        Mockito.when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

        //when-then
        mvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.userName").value("User1"))
                .andExpect(jsonPath("$.email").value("testMail@god.com"));
    }

    @Test
    void givenNonExistingUserId_whenGetById_ShouldReturnNotFound() throws Exception {
        //given
        Mockito.when(userService.findById(999)).thenReturn(null);

        //when-then
        mvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetAll_ShouldReturnAllUsers() throws Exception {
        //given
        User user1 = User.builder()
                .id(1)
                .userName("User1")
                .email("user1@test.com")
                .password("password1")
                .build();

        User user2 = User.builder()
                .id(2)
                .userName("User2")
                .email("user2@test.com")
                .password("password2")
                .build();

        UserResponseDTO responseDTO1 = new UserResponseDTO("1", "User1", "user1@test.com");
        UserResponseDTO responseDTO2 = new UserResponseDTO("2", "User2", "user2@test.com");

        Mockito.when(userService.getAll()).thenReturn(List.of(user1, user2));
        Mockito.when(userMapper.toResponseDTO(user1)).thenReturn(responseDTO1);
        Mockito.when(userMapper.toResponseDTO(user2)).thenReturn(responseDTO2);

        //when-then
        mvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].userName").value("User1"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].userName").value("User2"));
    }

    @Test
    void givenUserIdAndUpdatedData_whenPut_ShouldReturnUpdatedUser() throws Exception {
        //given
        UserPostRequestDTO putRequestDTO = new UserPostRequestDTO(
                "UpdatedUser",
                "updated@test.com",
                "newPassword"
        );

        User inputUser = User.builder()
                .userName("UpdatedUser")
                .email("updated@test.com")
                .password("newPassword")
                .build();

        User updatedUser = User.builder()
                .id(1)
                .userName("UpdatedUser")
                .email("updated@test.com")
                .password("newPassword")
                .build();

        UserResponseDTO responseDTO = new UserResponseDTO(
                "1",
                "UpdatedUser",
                "updated@test.com"
        );

        Mockito.when(userMapper.toUser(Mockito.any(UserPostRequestDTO.class))).thenReturn(inputUser);
        Mockito.when(userService.update(Mockito.eq(1), Mockito.any(User.class))).thenReturn(updatedUser);
        Mockito.when(userMapper.toResponseDTO(updatedUser)).thenReturn(responseDTO);

        //when-then
        mvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(putRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.userName").value("UpdatedUser"))
                .andExpect(jsonPath("$.email").value("updated@test.com"));
    }

    @Test
    void givenNonExistingUserId_whenPut_ShouldReturnNotFound() throws Exception {
        //given
        UserPostRequestDTO putRequestDTO = new UserPostRequestDTO(
                "UpdatedUser",
                "updated@test.com",
                "newPassword"
        );

        User inputUser = User.builder()
                .userName("UpdatedUser")
                .email("updated@test.com")
                .password("newPassword")
                .build();

        Mockito.when(userMapper.toUser(Mockito.any(UserPostRequestDTO.class))).thenReturn(inputUser);
        Mockito.when(userService.update(Mockito.eq(999), Mockito.any(User.class))).thenReturn(null);

        //when-then
        mvc.perform(put("/api/users/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(putRequestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenUserId_whenDelete_ShouldReturnNoContent() throws Exception {
        //given
        Mockito.doNothing().when(userService).deleteById(1);

        //when-then
        mvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(userService, Mockito.times(1)).deleteById(1);
    }

}