package com.triplog.api.auth.infrastructure;

import static com.triplog.api.auth.constants.AuthConstants.*;

import com.triplog.api.auth.application.UserDetailServiceImpl;
import com.triplog.api.auth.dto.TokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

    private final long expirationTimeMillis;
    private final Key key;

    private final UserDetailServiceImpl userDetailServiceImpl;

    public JwtTokenProvider(
            UserDetailServiceImpl userDetailServiceImpl,
            @Value("${security.jwt.token.secret-key}") String secretKey,
            @Value("${security.jwt.token.expire-length}") long expirationTimeMillis
    ) {
        this.userDetailServiceImpl = userDetailServiceImpl;
        byte[] secretByteKey = DatatypeConverter.parseBase64Binary(secretKey);
        this.key = Keys.hmacShaKeyFor(secretByteKey);
        this.expirationTimeMillis = expirationTimeMillis;
    }

    public TokenResponse generateToken(Authentication authentication) {
        String accessToken = generateAccessToken(authentication);
        String refreshToken = generateRefreshToken();

        return TokenResponse.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken); //토큰 복호화
        if (claims.get("auth") == null) {
            throw new RuntimeException(AUTH_JWT_TOKEN_UNPRIVILEGED);
        }
        UserDetails userDetails = userDetailServiceImpl.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, accessToken, userDetails.getAuthorities());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info(AUTH_JWT_TOKEN_INVALID, e);
        } catch (ExpiredJwtException e) {
            log.info(AUTH_JWT_TOKEN_EXPIRED, e);
        } catch (UnsupportedJwtException e) {
            log.info(AUTH_JWT_TOKEN_UNSUPPORTED, e);
        } catch (IllegalArgumentException e) {
            log.info(AUTH_JWT_CLAIMS_EMPTY, e);
        }
        return false;
    }

    private String generateRefreshToken() {
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMillis * 36))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateAccessToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMillis))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
