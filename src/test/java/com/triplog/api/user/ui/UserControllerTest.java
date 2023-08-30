package com.triplog.api.user.ui;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.triplog.api.ControllerTest;
import com.triplog.api.user.dto.UserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserControllerTest extends ControllerTest {

    @Test
    @DisplayName("사용자 생성")
    void createUser() throws Exception {
        //given
        UserRequest userRequest = UserRequest.builder()
                .email("test@test.com")
                .password("12345678")
                .build();
        String json = objectMapper.writeValueAsString(userRequest);

        //when then
        mockMvc.perform(post("/user")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(json))
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

}
