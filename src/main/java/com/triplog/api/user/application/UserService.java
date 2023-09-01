package com.triplog.api.user.application;

import com.triplog.api.user.domain.User;
import com.triplog.api.user.dto.UserCreateRequest;
import com.triplog.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Long createUser(UserCreateRequest userCreateRequest) {
        User user = userCreateRequest.toUser();
        user.encodePassword(passwordEncoder);
        return userRepository.save(user).getId();
    }
}
