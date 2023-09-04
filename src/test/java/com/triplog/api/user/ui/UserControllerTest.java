package com.triplog.api.user.ui;

import static com.triplog.api.user.constants.UserConstants.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.triplog.api.ControllerTest;
import com.triplog.api.user.dto.UserCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserControllerTest extends ControllerTest {

    private final String email = "test@test.com";
    private final String password = "12345678";

    @Test
    @DisplayName("사용자를 등록할 수 있다.")
    void createUser() throws Exception {
        //given
        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .email(email)
                .password(password)
                .build();

        //when then
        mockMvc.perform(post("/api/user")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("user/create",
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호")
                                        .attributes(key("constraint").value("8자 이상의 문자열"))
                        ))
                );
    }

    @Test
    @DisplayName("이메일 없이 사용자를 등록할 수 없다.")
    void createUser_emptyEmail() throws Exception {
        //given
        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .email("")
                .password(password)
                .build();

        //when then
        mockMvc.perform(post("/api/user")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("email"))
                .andExpect(jsonPath("$.errors[0].message").value(MESSAGE_USER_EMAIL_EMPTY));
    }

    @Test
    @DisplayName("잘못된 이메일로 사용자를 등록할 수 없다.")
    void createUser_invalidEmail() throws Exception {
        //given
        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .email("test")
                .password(password)
                .build();

        //when then
        mockMvc.perform(post("/api/user")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("email"))
                .andExpect(jsonPath("$.errors[0].message").value(MESSAGE_USER_EMAIL_INVALID));
    }

    @Test
    @DisplayName("비밀번호 없이 사용자를 등록할 수 없다.")
    void createUser_emptyPassword() throws Exception {
        //given
        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .email(email)
                .password("")
                .build();

        //when then
        mockMvc.perform(post("/api/user")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("password"))
                .andExpect(jsonPath("$.errors[0].message").value(MESSAGE_USER_PASSWORD_EMPTY));
    }

    @Test
    @DisplayName("8자 미만의 비밀번호로 사용자를 등록할 수 없다.")
    void createUser_shortPassword() throws Exception {
        //given
        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .email(email)
                .password("123")
                .build();

        //when then
        mockMvc.perform(post("/api/user")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("password"))
                .andExpect(jsonPath("$.errors[0].message").value(MESSAGE_USER_PASSWORD_LENGTH_MIN));
    }
}
