package com.triplog.api.user.ui;

import com.triplog.api.user.application.UserService;
import com.triplog.api.user.dto.UserCreateRequestDTO;
import com.triplog.api.user.dto.UserCreateRequestValidator;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserCreateRequestValidator userCreateRequestValidator;

    @InitBinder
    public void init(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(userCreateRequestValidator);
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody @Validated UserCreateRequestDTO userCreateRequestDTO) {
        Long userId = userService.createUser(userCreateRequestDTO);
        return ResponseEntity.created(URI.create("/user/" + userId)).build();
    }
}
