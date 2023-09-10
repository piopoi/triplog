package com.triplog.api.user.service;

import static com.triplog.api.user.constants.UserConstants.*;

import com.triplog.api.user.domain.User;
import com.triplog.api.user.dto.UserCreateRequestDTO;
import com.triplog.api.user.dto.UserGetRequestDTO;
import com.triplog.api.user.dto.UserGetResponseDTO;
import com.triplog.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long createUser(UserCreateRequestDTO userCreateRequestDTO) {
        User user = User.from(userCreateRequestDTO);
        user.encodePassword(passwordEncoder);
        return userRepository.save(user).getId();
    }

    @Transactional(readOnly = true)
    public UserGetResponseDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(MESSAGE_USER_NOT_EXISTS));
        return UserGetResponseDTO.from(user);
    }

    @Transactional(readOnly = true)
    public UserGetResponseDTO getUserByEmail(UserGetRequestDTO userGetRequestDTO) {
        User user = userRepository.findByEmail(userGetRequestDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException(MESSAGE_USER_NOT_EXISTS));
        return UserGetResponseDTO.from(user);
    }
}
