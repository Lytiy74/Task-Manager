package ua.azaika.taskmanager.service;

import org.springframework.stereotype.Service;
import ua.azaika.taskmanager.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final List<User> users = new ArrayList<>();

    @Override
    public User save(User user) {
        user.setId(users.size() + 1);
        users.add(user);
        return user;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users);
    }

    @Override
    public User findById(Integer id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> findByUserName(String userName) {
        return users.stream()
                .filter(user -> user.getUserName().equals(userName))
                .toList();
    }

    @Override
    public List<User> findByEmail(String email) {
        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .toList();
    }

    @Override
    public User update(Integer id, User updates) {
        User existingUser = findById(id);
        int index = users.indexOf(existingUser);
        updates.setId(id);
        users.set(index, updates);
        return updates;
    }

    @Override
    public User partialUpdate(Integer id, User updates) {
        User existingUser = findById(id);
        if (existingUser == null) return null;
        if (updates.getUserName() != null) existingUser.setUserName(updates.getUserName());
        if (updates.getEmail() != null) existingUser.setEmail(updates.getEmail());
        if (updates.getPassword() != null) existingUser.setPassword(updates.getPassword());
        return existingUser;
    }

    @Override
    public void deleteById(Integer id) {
        users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .ifPresent(users::remove);
    }
}
