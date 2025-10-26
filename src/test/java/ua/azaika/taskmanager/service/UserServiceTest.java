
package ua.azaika.taskmanager.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ua.azaika.taskmanager.model.User;
import ua.azaika.taskmanager.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    void givenUserEntity_whenSave_ShouldReturnSavedUser() {
        //given
        User userToSave = User.builder()
                .userName("User1")
                .email("testMail@god.com")
                .password("password1")
                .build();

        User savedUser = User.builder()
                .id(1)
                .userName("User1")
                .email("testMail@god.com")
                .password("password1")
                .build();

        Mockito.when(userRepository.save(userToSave)).thenReturn(savedUser);

        //when
        User result = userService.save(userToSave);

        //then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(1);
        Assertions.assertThat(result.getUserName()).isEqualTo("User1");
        Mockito.verify(userRepository, Mockito.times(1)).save(userToSave);
    }

    @Test
    void givenUserEntity_whenSave_ShouldAssignId() {
        //given
        User userToSave = User.builder()
                .userName("User1")
                .email("testMail@god.com")
                .password("password1")
                .build();

        User savedUser = User.builder()
                .id(1)
                .userName("User1")
                .email("testMail@god.com")
                .password("password1")
                .build();

        Mockito.when(userRepository.save(userToSave)).thenReturn(savedUser);

        //when
        User result = userService.save(userToSave);

        //then
        Assertions.assertThat(result.getId()).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(1);
    }

    @Test
    void givenPredefinedUserList_whenGetAll_ShouldReturnListOfPredefinedUsers() {
        //given
        List<User> predefinedUsers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = User.builder()
                    .id(i + 1)
                    .userName("User" + i)
                    .email("testMail" + i + "@god.com")
                    .password("password" + i)
                    .build();
            predefinedUsers.add(user);
        }

        Mockito.when(userRepository.getAll()).thenReturn(predefinedUsers);

        //when
        List<User> savedUsers = userService.getAll();

        //then
        Assertions.assertThat(savedUsers)
                .hasSize(10)
                .containsAll(predefinedUsers);
        Mockito.verify(userRepository, Mockito.times(1)).getAll();
    }

    @Test
    void givenIdOfUser_whenFindById_ShouldReturnUserWithThisId() {
        //given
        User user = User.builder()
                .id(1)
                .userName("user1")
                .email("testMail@god.com")
                .password("password")
                .build();

        Mockito.when(userRepository.findById(1)).thenReturn(user);

        //when
        User foundUser = userService.findById(1);

        //then
        Assertions.assertThat(foundUser).isNotNull();
        Assertions.assertThat(foundUser.getId()).isEqualTo(1);
        Assertions.assertThat(foundUser.getUserName()).isEqualTo("user1");
        Mockito.verify(userRepository, Mockito.times(1)).findById(1);
    }

    @Test
    void givenUserInDB_whenUpdateByNewUser_ShouldReturnUpdatedUser() {
        //given
        User updatesUser = User.builder()
                .userName("NewUserName")
                .email("newEmail@god.com")
                .password("newPassword")
                .build();

        User updatedUser = User.builder()
                .id(1)
                .userName("NewUserName")
                .email("newEmail@god.com")
                .password("newPassword")
                .build();

        Mockito.when(userRepository.update(1, updatesUser)).thenReturn(updatedUser);

        //when
        User result = userService.update(1, updatesUser);

        //then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(1);
        Assertions.assertThat(result.getUserName()).isEqualTo("NewUserName");
        Assertions.assertThat(result.getEmail()).isEqualTo("newEmail@god.com");
        Mockito.verify(userRepository, Mockito.times(1)).update(1, updatesUser);
    }

    @Test
    void givenUserInDB_whenDeleteById_ShouldCallRepositoryDelete() {
        //given
        Integer userId = 1;
        Mockito.doNothing().when(userRepository).deleteById(userId);

        //when
        userService.deleteById(userId);

        //then
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(userId);
    }
}