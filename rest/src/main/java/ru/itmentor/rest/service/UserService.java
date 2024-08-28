package ru.itmentor.rest.service;


import ru.itmentor.common.entity.User;
import ru.itmentor.rest.dto.*;

import java.util.List;

public interface UserService {

    User save(User user);

    List<User> findAll();

    User findById(Long id);

    void deleteById(Long id);

    User update(Long id, User user);

    boolean existsByUsername(String username);

    User findByEmail(String email);

    UserAuthResponseDto authenticate(UserAuthRequestDto userAuthRequestDto);

    CreateUserResponseDto register(CreateUserRequestDto createUserRequestDto);
}
