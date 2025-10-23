package ua.azaika.taskmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.azaika.taskmanager.model.User;
import ua.azaika.taskmanager.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public User update(Integer id, User updates) {
        return userRepository.update(id, updates);
    }

    @Override
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }
}
