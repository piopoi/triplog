package com.triplog.api.post.comment.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.triplog.api.BaseControllerTest;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

public class CommentControllerTest extends BaseControllerTest {

    private final String requestUri = "/api/posts/{postId}/comments";
    private final String content = "test";

    private Long postId;

    @BeforeEach
    void setUp() {
        postId = createPost("foo", "bar", admin)
                .getId();
    }

    @Test
    @DisplayName("댓글을 작성할 수 있다.")
    void createComment() throws Exception {
        //given
        Map<String, String> params = Map.of("content", content);

        //when then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/posts/{postId}/comments", postId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params))
                        .with(user(adminUserAdapter)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("comment/create",
                        pathParameters(
                                parameterWithName("postId").description("게시글 아이디")
                        ),
                        requestFields(
                                fieldWithPath("content").description("댓글 내용")
                                        .attributes(key("constraint").value("max length = 100"))
                        ))
                );
    }

    @Test
    @DisplayName("권한 없이 댓글을 작성할 수 없다.")
    void createComment_unauth() throws Exception {
        //given
        Map<String, String> params = Map.of("content", content);

        //when then
        mockMvc.perform(post(requestUri, postId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("없는 게시글의 댓글을 작성할 수 없다.")
    void createComment_postNotExists() throws Exception {
        //given
        Long postId = 99L;
        Map<String, String> params = Map.of("content", content);

        //when then
        mockMvc.perform(post(requestUri, postId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params))
                        .with(user(adminUserAdapter)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("max length를 초과하는 댓글을 작성할 수 없다.")
    void createComment_contentMaxLength() throws Exception {
        //given
        Map<String, String> params = Map.of("content", "1".repeat(101));

        //when then
        mockMvc.perform(post(requestUri, postId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params))
                        .with(user(adminUserAdapter)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
