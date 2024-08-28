package ru.itmentor.rest.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.common.entity.User;
import ru.itmentor.rest.dto.CreateUserRequestDto;
import ru.itmentor.rest.dto.CreateUserResponseDto;
import ru.itmentor.rest.dto.UserDto;
import ru.itmentor.rest.mapper.UserMapper;
import ru.itmentor.rest.security.CurrentUser;
import ru.itmentor.rest.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminEndpoint {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll(@AuthenticationPrincipal CurrentUser currentUser) {
        List<User> users = userService.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<UserDto> userDtos = users.stream()
                .filter(user -> user.getRoles().stream().anyMatch(role -> role.getName().equals("USER")))
                .filter(user -> user.getId().equals(currentUser.getUser().getId()))
                .map(userMapper::mapToDto)
                .collect(Collectors.toList());
        if (!userDtos.isEmpty()) {
            return ResponseEntity.ok(userDtos);
        }
        List<UserDto> adminDtos = users.stream()
                .map(userMapper::mapToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(adminDtos);
    }

    @PostMapping
    public ResponseEntity<CreateUserResponseDto> create(@RequestBody CreateUserRequestDto createUserRequestDto) {
        if (userService.existsByUsername(createUserRequestDto.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        User user = userMapper.map(createUserRequestDto);
        userService.save(user);
        return ResponseEntity.ok(userMapper.mapToResponse(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable("id") Long id, @RequestBody User user) {
        User updatedUser = userService.update(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}