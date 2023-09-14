package com.triplog.api.user.controller;

import com.triplog.api.auth.domain.UserAdapter;
import com.triplog.api.user.dto.PasswordUpdateRequestDTO;
import com.triplog.api.user.dto.UserCreateRequestDTO;
import com.triplog.api.user.dto.UserGetRequestDTO;
import com.triplog.api.user.dto.UserGetResponseDTO;
import com.triplog.api.user.service.UserService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody @Valid UserCreateRequestDTO userCreateRequestDTO) {
        Long userId = userService.createUser(userCreateRequestDTO);
        return ResponseEntity.created(URI.create("/api/users/" + userId)).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserGetResponseDTO> getUserById(@PathVariable Long userId) {
        UserGetResponseDTO userGetResponseDTO = userService.getUserById(userId);
        return ResponseEntity.ok(userGetResponseDTO);
    }

    @GetMapping
    public ResponseEntity<UserGetResponseDTO> getUserByEmail(@RequestBody @Valid UserGetRequestDTO userGetRequestDTO) {
        UserGetResponseDTO userGetResponseDTO = userService.getUserByEmail(userGetRequestDTO);
        return ResponseEntity.ok(userGetResponseDTO);
    }

    @PatchMapping("/{userId}/password")
    @PreAuthorize("@userService.hasAuthManageUser(#userAdapter, #userId)")
    public ResponseEntity<Void> updatePassword(@AuthenticationPrincipal UserAdapter userAdapter,
                                               @PathVariable Long userId,
                                               @RequestBody @Valid PasswordUpdateRequestDTO passwordUpdateRequestDTO) {
        userService.updatePassword(userId, passwordUpdateRequestDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("@userService.hasAuthManageUser(#userAdapter, #userId)")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal UserAdapter userAdapter,
                                           @PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}
