package com.triplog.api.auth.application;

import com.triplog.api.auth.AuthConstants;
import com.triplog.api.auth.domain.UserDetail;
import com.triplog.api.user.domain.User;
import com.triplog.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(AuthConstants.AUTH_USER_NOT_EXISTS));
        return new UserDetail(user);
    }
}
