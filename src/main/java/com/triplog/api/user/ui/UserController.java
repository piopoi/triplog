package com.triplog.api.user.ui;

import com.triplog.api.user.application.UserService;
import com.triplog.api.user.dto.UserRequest;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Long> createUser(@RequestBody @Valid UserRequest userRequest) {
        Long userId = userService.createUser(userRequest);
        return ResponseEntity.created(URI.create("/user/" + userId)).build();
    }
}
