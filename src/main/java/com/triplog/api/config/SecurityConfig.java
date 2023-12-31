package com.triplog.api.config;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.Customizer.withDefaults;

import com.triplog.api.auth.infrastructure.JwtAuthenticationFilter;
import com.triplog.api.auth.infrastructure.JwtTokenProvider;
import com.triplog.api.user.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeHttpRequest -> authorizeHttpRequest
                        .requestMatchers(new AntPathRequestMatcher("/api/auth/login/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/user/**", POST.name())).hasAuthority(Role.ADMIN.getValue())
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .formLogin(AbstractHttpConfigurer::disable)
                //.csrf(AbstractHttpConfigurer::disable)
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(toH2Console())
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/docs/**")) //API 문서 조회 URI
                        .disable()
                )
                .sessionManagement(sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .httpBasic(withDefaults());
        return http.build();
    }
}
