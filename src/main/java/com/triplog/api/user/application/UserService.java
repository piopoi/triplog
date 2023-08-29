package com.triplog.api.user.application;

import com.triplog.api.user.domain.User;
import com.triplog.api.user.dto.UserRequest;
import com.triplog.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(UserRequest userRequest) {
        return userRepository.save(userRequest.toUser());
    }
}
