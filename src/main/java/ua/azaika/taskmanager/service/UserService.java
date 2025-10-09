package ua.azaika.taskmanager.service;

import ua.azaika.taskmanager.model.User;

import java.util.List;

public interface UserService {
    User save(User user);

    List<User> getAll();

    User findById(Integer id);

    User findByUserName(String userName);

    User findByEmail(String email);

    User update(Integer id, User user);

    User partialUpdate(Integer id, User user);

    void deleteById(Integer id);

}
