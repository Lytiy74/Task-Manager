package ua.azaika.taskmanager.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.azaika.taskmanager.model.User;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService.getAll().clear();
    }

    @Test
    void givenUserEntity_whenSave_ShouldSave() {
        //given
        User userToSave = User.builder()
                .userName("User1")
                .email("testMail@god.com")
                .password("password1")
                .build();

        //when
        userService.save(userToSave);

        //then
        User savedUser = userService.getAll().getFirst();
        Assertions.assertThat(savedUser).isEqualTo(userToSave);
    }

    @Test
    void givenUserEntity_whenSave_ShouldAssignId() {
        //given
        User userToSave = User.builder()
                .userName("User1")
                .email("testMail@god.com")
                .password("password1")
                .build();

        //when
        userService.save(userToSave);

        //then
        Assertions.assertThat(userToSave.getId()).isNotNull();
    }

    @Test
    void givenPredefinedUserList_whenGetAll_ShouldReturnListOfPredefinedUsers() {
        //given
        List<User> predefinedUsers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = User.builder()
                    .userName("User" + i)
                    .email("testMail" + i)
                    .password("password" + i)
                    .build();
            predefinedUsers.add(user);
            userService.save(user);
        }

        //when
        List<User> savedUsers = userService.getAll();

        //then
        Assertions.assertThat(savedUsers)
                .hasSize(10)
                .containsAll(predefinedUsers);
    }

    @Test
    void givenIdOfUser_whenFindById_ShouldReturnUserWithThisId() {
        //given
        User user = User.builder()
                .userName("user1")
                .email("testMail@god.com")
                .password("password")
                .build();
        int id = 1;
        userService.save(user);

        //when
        User foundUser = userService.findById(id);

        //then
        Assertions.assertThat(foundUser).isEqualTo(user);
    }

    @Test
    void givenUserName_whenFindByUserName_ShouldReturnUserWithThisUserName() {
        //given
        User user = User.builder()
                .userName("user1")
                .email("testMail@god.com")
                .password("password")
                .build();
        String userName = user.getUserName();
        userService.save(user);

        //when
        List<User> foundUsers = userService.findByUserName(userName);

        //then
        Assertions.assertThat(foundUsers.getFirst()).isEqualTo(user);
    }

    @Test
    void givenUserEmail_whenFindByEmail_ShouldReturnUserWithThisEmail() {
        //given
        User user = User.builder()
                .userName("user1")
                .email("testMail@god.com")
                .password("password")
                .build();
        String email = user.getEmail();
        userService.save(user);

        //when
        List<User> foundUsers = userService.findByEmail(email);

        //then
        Assertions.assertThat(foundUsers.getFirst()).isEqualTo(user);
    }

    @Test
    void givenUserInDB_whenUpdateByNewUser_ShouldSaveUpdatedUser() {
        //given
        User user = User.builder()
                .userName("user1")
                .email("testMail@god.com")
                .password("password")
                .build();
        int id = userService.save(user).getId();
        User updatesUser = User.builder()
                .userName("NewUserName")
                .email("newEmail@god.com")
                .password("newPassword")
                .build();

        //when
        userService.update(id, updatesUser);

        //then
        User updatedUser = userService.findById(id);
        Assertions.assertThat(updatedUser).isEqualTo(updatesUser);
    }

    @Test
    void givenUserInDB_whenUpdateByNewUser_ShouldReturnUpdatedUser() {
        //given
        User user = User.builder()
                .userName("user1")
                .email("testMail@god.com")
                .password("password")
                .build();
        int id = userService.save(user).getId();
        User updatesUser = User.builder()
                .userName("NewUserName")
                .email("newEmail@god.com")
                .password("newPassword")
                .build();

        //when
        User updated = userService.update(id, updatesUser);

        //then
        Assertions.assertThat(updated).isEqualTo(updatesUser);
    }

    @Test
    void givenUserInDB_whenPartialUpdateByNewUser_ShouldReturnSaveUser() {
        //given
        User user = User.builder()
                .userName("user1")
                .email("testMail@god.com")
                .password("password")
                .build();
        int id = userService.save(user).getId();
        User updatesUser = User.builder()
                .userName("NewUserName")
                .build();

        //when
        User updated = userService.partialUpdate(id, updatesUser);

        //then
        Assertions.assertThat(updated).isEqualTo(user);
        Assertions.assertThat(updated.getUserName()).isEqualTo(updatesUser.getUserName());
        Assertions.assertThat(updated.getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(updated.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    void givenUserInDB_whenDeleteById_ShouldDeleteUserFromDB() {
        //given
        User user = User.builder()
                .userName("user1")
                .email("testMail@god.com")
                .password("password")
                .build();
        int id = userService.save(user).getId();

        //when
        userService.deleteById(id);

        //then
        Assertions.assertThat(userService.findById(id)).isNull();
    }


}
