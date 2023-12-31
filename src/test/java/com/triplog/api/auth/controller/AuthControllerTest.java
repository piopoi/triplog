package com.triplog.api.auth.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.triplog.api.BaseControllerTest;
import com.triplog.api.auth.dto.TokenRequestDTO;
import com.triplog.api.user.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthControllerTest extends BaseControllerTest {

    private final String email = "test@test.com";
    private final String password = "12345678";

    @BeforeEach
    void setUp() {
        createUser(email, password, Role.USER);
    }

    @Test
    @DisplayName("로그인(jwt) 할 수 있다.")
    void login() throws Exception {
        //given
        TokenRequestDTO tokenRequestDTO = TokenRequestDTO.builder()
                .email(email)
                .password(password)
                .build();

        //when then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tokenRequestDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grantType").value("Bearer"))
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andDo(document("auth/login",
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호")
                                        .attributes(key("constraint").value("8자 이상의 문자열"))

                        ),
                        responseFields(
                                fieldWithPath("grantType").description("Grant type"),
                                fieldWithPath("accessToken").description("Access token"),
                                fieldWithPath("refreshToken").description("Refresh token")
                        )
                ));
    }
}
