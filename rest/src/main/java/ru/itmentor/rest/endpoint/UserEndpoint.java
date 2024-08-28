package ru.itmentor.rest.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmentor.rest.dto.*;
import ru.itmentor.rest.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserEndpoint {

    private final UserService userService;

    @PostMapping("/auth")
    public ResponseEntity<UserAuthResponseDto> auth(@RequestBody UserAuthRequestDto userAuthRequestDto) {
        UserAuthResponseDto response = userService.authenticate(userAuthRequestDto);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/register")
    public ResponseEntity<CreateUserResponseDto> register(@RequestBody CreateUserRequestDto createUserRequestDto) {
        CreateUserResponseDto register = userService.register(createUserRequestDto);
        return ResponseEntity.ok(register);
    }
}