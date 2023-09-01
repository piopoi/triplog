package com.triplog.api.user.ui;

import com.triplog.api.common.error.ErrorResult;
import com.triplog.api.user.application.UserService;
import com.triplog.api.user.dto.UserCreateRequest;
import com.triplog.api.user.dto.UserCreateRequestValidator;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity createUser(@RequestBody @Validated UserCreateRequest userCreateRequest,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ErrorResult errorResult = new ErrorResult(bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResult);
        }

        Long userId = userService.createUser(userCreateRequest);
        return ResponseEntity.created(URI.create("/user/" + userId)).build();
    }
}
