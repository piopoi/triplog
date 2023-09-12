package com.triplog.api.user.service;

import static com.triplog.api.user.constants.UserConstants.MESSAGE_USER_NOT_EXISTS;

import com.triplog.api.auth.domain.UserAdapter;
import com.triplog.api.user.domain.User;
import com.triplog.api.user.dto.PasswordUpdateRequestDTO;
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
        User user = User.of(userCreateRequestDTO, passwordEncoder);
        return userRepository.save(user).getId();
    }

    @Transactional(readOnly = true)
    public UserGetResponseDTO getUserById(Long userId) {
        User user = findUserById(userId);
        return UserGetResponseDTO.from(user);
    }

    @Transactional(readOnly = true)
    public UserGetResponseDTO getUserByEmail(UserGetRequestDTO userGetRequestDTO) {
        User user = userRepository.findByEmail(userGetRequestDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException(MESSAGE_USER_NOT_EXISTS));
        return UserGetResponseDTO.from(user);
    }

    @Transactional
    public void updatePassword(Long userId, PasswordUpdateRequestDTO passwordUpdateRequestDTO) {
        User user = findUserById(userId);
        user.updatePassword(passwordEncoder, passwordUpdateRequestDTO.getPassword());
    }

    @Transactional
    public void deleteUser(Long userId) {
        findUserById(userId);
        userRepository.deleteById(userId);
    }

    public boolean hasAuthManageUser(UserAdapter userAdapter, Long managedUserId) {
        User loginUser = userAdapter.getUser();
        return loginUser.isAdmin() || loginUser.isSameUser(managedUserId);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(MESSAGE_USER_NOT_EXISTS));
    }
}
