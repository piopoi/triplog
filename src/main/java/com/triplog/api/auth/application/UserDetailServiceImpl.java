package com.triplog.api.auth.application;

import static com.triplog.api.auth.constants.AuthConstants.*;

import com.triplog.api.auth.domain.UserDetailsImpl;
import com.triplog.api.user.domain.User;
import com.triplog.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(MESSAGE_AUTH_USER_NOT_EXISTS));
        return new UserDetailsImpl(user);
    }
}
