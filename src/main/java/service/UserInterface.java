package service;

import models.User;

import java.util.List;

public interface UserInterface {
    List<User> getAllUsers();

    List<User> getUserByMail(String username);

    List<User> getUserByEmail(String username);

    User updateUsersRole(User user);

    User getSpecificUser(String username);

    boolean updateUserPassword(User passwordRequest);
}
