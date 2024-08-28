package ru.itmentor.mvc.service;

import ru.itmentor.common.entity.User;

import java.util.List;

public interface UserService {

    User findByEmail(String email);

    void saveUser(User user);

    User findUserById(Long id);

    List<User> getListUsers();

    User findUserByName(String name);

    void removeUserById(Long id);

    void updateUser(User user);
}
