package ru.itmentor.rest.service.impl;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmentor.common.entity.Role;
import ru.itmentor.common.entity.User;
import ru.itmentor.common.repository.RoleRepository;
import ru.itmentor.common.repository.UserRepository;
import ru.itmentor.rest.dto.CreateUserRequestDto;
import ru.itmentor.rest.dto.CreateUserResponseDto;
import ru.itmentor.rest.dto.UserAuthRequestDto;
import ru.itmentor.rest.dto.UserAuthResponseDto;
import ru.itmentor.rest.mapper.UserMapper;
import ru.itmentor.rest.service.UserService;
import ru.itmentor.rest.util.JwtTokenUtil;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenUtil tokenUtil;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, JwtTokenUtil tokenUtil, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenUtil = tokenUtil;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UsernameNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public User update(Long id, User user) {
        User userId = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            User existingUser = userRepository.findByEmail(user.getEmail())
                    .orElse(null);
            if (existingUser != null && !existingUser.getId().equals(id)) {
                throw new RuntimeException("Email already in use");
            }
        }
        if (user.getFirstname() != null && !user.getFirstname().isEmpty()) {
            userId.setFirstname(user.getFirstname());
        }
        if (user.getLastname() != null && !user.getLastname().isEmpty()) {
            userId.setLastname(user.getLastname());
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            userId.setEmail(user.getEmail());
        }
        if (user.getAge() > 0) {
            userId.setAge(user.getAge());
        }
        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            userId.setUsername(user.getUsername());
        }
        return userRepository.save(userId);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    @Override
    @Transactional
    public CreateUserResponseDto register(CreateUserRequestDto createUserRequestDto) {
        if (userRepository.findByEmail(createUserRequestDto.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists with email: " + createUserRequestDto.getEmail());
        }
        User user = userMapper.map(createUserRequestDto);
        user.setPassword(passwordEncoder.encode(createUserRequestDto.getPassword()));
        Role defaultRole = roleRepository.findByName("ROLE_USER");
        if (defaultRole == null) {
            defaultRole = new Role();
            defaultRole.setName("USER");
            defaultRole = roleRepository.save(defaultRole);
        }
        user.getRoleList().add(defaultRole);
        User userSave = userRepository.save(user);
        return userMapper.mapToResponse(userSave);
    }

    @Override
    public UserAuthResponseDto authenticate(UserAuthRequestDto userAuthRequestDto) {
        User user = userRepository.findByEmail(userAuthRequestDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userAuthRequestDto.getEmail()));

        if (!passwordEncoder.matches(userAuthRequestDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = tokenUtil.generateToken(user.getEmail());
        return new UserAuthResponseDto(token);
    }
}