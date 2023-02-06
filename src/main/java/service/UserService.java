package service;

import lombok.AllArgsConstructor;
import models.User;
import org.springframework.stereotype.Service;
import repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements UserInterface{

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().collect(Collectors.toList());
    }

    @Override
    public List<User> getUserByMail(String username) {
        return null;
    }

    @Override
    public List<User> getUserByEmail(String username) {
        return null;
    }

    @Override
    public User updateUsersRole(User user) {
        return null;
    }

    @Override
    public User getSpecificUser(String username) {
        return null;
    }

    @Override
    public boolean updateUserPassword(User passwordRequest) {
        return false;
    }
}
