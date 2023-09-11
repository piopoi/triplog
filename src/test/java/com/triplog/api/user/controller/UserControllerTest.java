package com.triplog.api.user.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.triplog.api.BaseControllerTest;
import com.triplog.api.user.domain.Role;
import com.triplog.api.user.dto.PasswordUpdateRequestDTO;
import com.triplog.api.user.dto.UserCreateRequestDTO;
import com.triplog.api.user.dto.UserGetRequestDTO;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

class UserControllerTest extends BaseControllerTest {

    private final String requestUri = "/api/users";
    private final String email = "test@test.com";
    private final String password = "12345678";


    @Test
    @DisplayName("사용자를 등록할 수 있다.")
    void createUser() throws Exception {
        //given
        UserCreateRequestDTO userCreateRequestDTO = UserCreateRequestDTO.of(email, password);
        List<String> roles = Arrays.stream(Role.values())
                .map(Enum::name)
                .toList();

        //when then
        mockMvc.perform(RestDocumentationRequestBuilders.post(requestUri)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateRequestDTO))
                        .with(user(adminUserDetailsImpl)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("user/create",
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호")
                                        .attributes(key("constraint").value("8자 이상의 문자열")),
                                fieldWithPath("role").description("권한")
                                        .attributes(key("constraint").value(roles))
                                        .optional()
                        ))
                );
    }

    @Test
    @DisplayName("이메일 없이 사용자를 등록할 수 없다.")
    void createUser_emptyEmail() throws Exception {
        //given
        UserCreateRequestDTO userCreateRequestDTO = UserCreateRequestDTO.of("", password);

        //when then
        mockMvc.perform(post(requestUri)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateRequestDTO))
                        .with(user(adminUserDetailsImpl)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("잘못된 이메일로 사용자를 등록할 수 없다.")
    void createUser_invalidEmail() throws Exception {
        //given
        UserCreateRequestDTO userCreateRequestDTO = UserCreateRequestDTO.of("test", password);

        //when then
        mockMvc.perform(post(requestUri)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateRequestDTO))
                        .with(user(adminUserDetailsImpl)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("비밀번호 없이 사용자를 등록할 수 없다.")
    void createUser_emptyPassword() throws Exception {
        //given
        UserCreateRequestDTO userCreateRequestDTO = UserCreateRequestDTO.of(email, "");

        //when then
        mockMvc.perform(post(requestUri)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateRequestDTO))
                        .with(user(adminUserDetailsImpl)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("8자 미만의 비밀번호로 사용자를 등록할 수 없다.")
    void createUser_shortPassword() throws Exception {
        //given
        UserCreateRequestDTO userCreateRequestDTO = UserCreateRequestDTO.of(email, "123");

        //when then
        mockMvc.perform(post(requestUri)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateRequestDTO))
                        .with(user(adminUserDetailsImpl)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("id로 사용자를 조회할 수 있다.")
    void getUserById() throws Exception {
        //when then
        mockMvc.perform(RestDocumentationRequestBuilders.get(requestUri + "/{id}", admin.getId())
                        .accept(APPLICATION_JSON)
                        .with(user(adminUserDetailsImpl)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(admin.getId()))
                .andExpect(jsonPath("$.email").value(admin.getEmail()))
                .andExpect(jsonPath("$.role").value(admin.getRole().getValue()))
                .andDo(document("user/getById",
                        pathParameters(
                                parameterWithName("id").description("사용자 아이디")
                        ),
                        responseFields(
                                fieldWithPath("id").description("사용자 아이디"),
                                fieldWithPath("email").description("사용자 이메일"),
                                fieldWithPath("role").description("사용자 권한")
                        ))
                );
    }

    @Test
    @DisplayName("권한 없이 id로 사용자를 조회할 수 없다.")
    void getUserById_unauth() throws Exception {
        //when then
        mockMvc.perform(get(requestUri + "/{id}", admin.getId())
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("없는 사용자를 조회할 수 없다 by id")
    void getUserById_notExists() throws Exception {
        //when then
        mockMvc.perform(get(requestUri + "/{id}", 99L)
                        .accept(APPLICATION_JSON)
                        .with(user(adminUserDetailsImpl)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("email로 사용자를 조회할 수 있다.")
    void getUserByEmail() throws Exception {
        //given
        UserGetRequestDTO userGetRequestDTO = UserGetRequestDTO.from(admin.getEmail());

        //when then
        mockMvc.perform(RestDocumentationRequestBuilders.get(requestUri)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userGetRequestDTO))
                        .with(user(adminUserDetailsImpl)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(admin.getId()))
                .andExpect(jsonPath("$.email").value(admin.getEmail()))
                .andExpect(jsonPath("$.role").value(admin.getRole().getValue()))
                .andDo(document("user/getByEmail",
                        requestFields(
                                fieldWithPath("email").description("사용자 이메일")
                        ),
                        responseFields(
                                fieldWithPath("id").description("사용자 아이디"),
                                fieldWithPath("email").description("사용자 이메일"),
                                fieldWithPath("role").description("사용자 권한")
                        ))
                );
    }

    @Test
    @DisplayName("권한 없이 email로 사용자를 조회할 수 없다.")
    void getUserByEmail_unauth() throws Exception {
        //given
        UserGetRequestDTO userGetRequestDTO = UserGetRequestDTO.from(admin.getEmail());

        //when then
        mockMvc.perform(get(requestUri)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userGetRequestDTO)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("잘못된 email로 사용자를 조회할 수 없다.")
    void getUserByEmail_invalidEmail() throws Exception {
        //given
        UserGetRequestDTO userGetRequestDTO = UserGetRequestDTO.from("foo");

        //when then
        mockMvc.perform(get(requestUri)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userGetRequestDTO))
                        .with(user(adminUserDetailsImpl)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("없는 사용자를 조회할 수 없다 by email")
    void getUserByEmail_notExists() throws Exception {
        //given
        UserGetRequestDTO userGetRequestDTO = UserGetRequestDTO.from("foo@test.com");

        //when then
        mockMvc.perform(get(requestUri)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userGetRequestDTO))
                        .with(user(adminUserDetailsImpl)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("사용자 암호를 수정할 수 있다.")
    void updatePassword() throws Exception {
        //given
        PasswordUpdateRequestDTO passwordUpdateRequestDTO = PasswordUpdateRequestDTO.from("123456789");

        //when then
        mockMvc.perform(RestDocumentationRequestBuilders.patch(requestUri + "/{id}/password", admin.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordUpdateRequestDTO))
                        .with(user(adminUserDetailsImpl)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user/updatePassword",
                        pathParameters(
                                parameterWithName("id").description("사용자 아이디")
                        ),
                        requestFields(
                                fieldWithPath("password").description("비밀번호")
                                        .attributes(key("constraint").value("8자 이상의 문자열"))
                        ))
                );
    }
}
