package com.triplog.api.post.ui;

import static com.triplog.api.post.constants.PostConstants.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.triplog.api.ControllerTest;
import com.triplog.api.auth.domain.UserDetailsImpl;
import com.triplog.api.post.dto.PostCreateRequest;
import com.triplog.api.user.domain.User;
import com.triplog.api.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

public class PostControllerTest extends ControllerTest {

    private final String title = "foo";
    private final String content = "bar";

    @Autowired
    private UserRepository userRepository;

    private UserDetailsImpl userDetailsImpl;

    @BeforeEach
    void setUp() {
        User user = userRepository.save(new User("test@test.com", "12345678"));
        userDetailsImpl = new UserDetailsImpl(user);
    }

    @Test
    @DisplayName("게시글을 작성할 수 있다.")
    void createPost() throws Exception {
        //given
        PostCreateRequest postCreateRequest = PostCreateRequest.builder()
                .title(title)
                .content(content)
                .build();

        //when then
        mockMvc.perform(post("/api/post")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCreateRequest))
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetailsImpl)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("post/create",
                        requestFields(
                                fieldWithPath("title").description("제목")
                                        .attributes(key("constraint").value("max length = 50")),
                                fieldWithPath("content").description("본문")
                        ))
                );
    }

    @Test
    @DisplayName("로그인 없이 게시글을 작성할 수 없다.")
    void createPost_noLogin() throws Exception {
        //given
        PostCreateRequest postCreateRequest = PostCreateRequest.builder()
                .title(title)
                .content(content)
                .build();

        //when then
        mockMvc.perform(post("/api/post")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCreateRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("제목 없이 게시글을 작성할 수 없다.")
    void createPost_emptyTitle() throws Exception {
        //given
        PostCreateRequest postCreateRequest = PostCreateRequest.builder()
                .title("")
                .content(content)
                .build();

        //when then
        mockMvc.perform(post("/api/post")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCreateRequest))
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetailsImpl)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("title"))
                .andExpect(jsonPath("$.errors[0].message").value(MESSAGE_POST_TITLE_EMPTY));
    }

    @Test
    @DisplayName("제목이 50자를 초과할 수 없다.")
    void createPost_invalidTitle() throws Exception {
        //given
        PostCreateRequest postCreateRequest = PostCreateRequest.builder()
                .title("_123456789_123456789_123456789_123456789_123456789_") //length = 51
                .content(content)
                .build();

        //when then
        mockMvc.perform(post("/api/post")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCreateRequest))
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetailsImpl)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("title"))
                .andExpect(jsonPath("$.errors[0].message").value(MESSAGE_POST_TITLE_LENGTH));
    }

    @Test
    @DisplayName("본문 없이 게시글을 작성할 수 없다.")
    void createPost_emptyContent() throws Exception {
        //given
        PostCreateRequest postCreateRequest = PostCreateRequest.builder()
                .title(title)
                .content("")
                .build();

        //when then
        mockMvc.perform(post("/api/post")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCreateRequest))
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetailsImpl)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("content"))
                .andExpect(jsonPath("$.errors[0].message").value(MESSAGE_POST_CONTENT_EMPTY));
    }

}
