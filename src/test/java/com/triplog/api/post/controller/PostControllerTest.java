package com.triplog.api.post.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.triplog.api.BaseControllerTest;
import com.triplog.api.auth.domain.UserDetailsImpl;
import com.triplog.api.post.domain.Post;
import com.triplog.api.post.dto.PostCreateRequestDTO;
import com.triplog.api.post.dto.PostUpdateRequestDTO;
import com.triplog.api.post.repository.PostRepository;
import com.triplog.api.user.domain.User;
import com.triplog.api.user.dto.UserCreateRequestDTO;
import com.triplog.api.user.repository.UserRepository;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

public class PostControllerTest extends BaseControllerTest {

    private final String title = "foo";
    private final String content = "bar";

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private UserDetailsImpl userDetailsImpl;

    @BeforeEach
    void setUp() {
        userDetailsImpl = createUser("test@test.com", "12345678");
    }

    @Test
    @DisplayName("게시글을 작성할 수 있다.")
    void createPost() throws Exception {
        //given
        PostCreateRequestDTO postCreateRequestDTO = PostCreateRequestDTO.of(title, content);

        //when then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/post")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCreateRequestDTO))
                        .with(user(userDetailsImpl)))
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
    @DisplayName("권한 없이 게시글을 작성할 수 없다.")
    void createPost_unauth() throws Exception {
        //given
        PostCreateRequestDTO postCreateRequestDTO = PostCreateRequestDTO.of(title, content);

        //when then
        mockMvc.perform(post("/api/post")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCreateRequestDTO)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("제목 없이 게시글을 작성할 수 없다.")
    void createPost_emptyTitle() throws Exception {
        //given
        PostCreateRequestDTO postCreateRequestDTO = PostCreateRequestDTO.of("", content);

        //when then
        mockMvc.perform(post("/api/post")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCreateRequestDTO))
                        .with(user(userDetailsImpl)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("제목이 50자를 초과할 수 없다.")
    void createPost_invalidTitle() throws Exception {
        //given
        PostCreateRequestDTO postCreateRequestDTO = PostCreateRequestDTO.of("a".repeat(51), content);

        //when then
        mockMvc.perform(post("/api/post")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCreateRequestDTO))
                        .with(user(userDetailsImpl)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("본문 없이 게시글을 작성할 수 없다.")
    void createPost_emptyContent() throws Exception {
        //given
        PostCreateRequestDTO postCreateRequestDTO = PostCreateRequestDTO.of(title, "");

        //when then
        mockMvc.perform(post("/api/post")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCreateRequestDTO))
                        .with(user(userDetailsImpl)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("id로 게시글을 조회할 수 있다.")
    void getPost() throws Exception {
        //given
        PostCreateRequestDTO postCreateRequestDTO = PostCreateRequestDTO.of(title, content);
        Post post = postRepository.save(Post.of(postCreateRequestDTO, userDetailsImpl.getUser()));

        //when then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/post/{id}", post.getId())
                        .accept(APPLICATION_JSON)
                        .with(user(userDetailsImpl)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post/get",
                        pathParameters(
                                parameterWithName("id").description("게시글 아이디")
                        ),
                        responseFields(
                                fieldWithPath("id").description("게시글 아이디"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("본문"),
                                fieldWithPath("userId").description("작성자 아이디")
                        ))
                );
    }

    @Test
    @DisplayName("잘못된 id로 게시글을 조회할 수 없다.")
    void getPost_invalidId() throws Exception {
        //when then
        mockMvc.perform(get("/api/post/{id}", 99L)
                        .accept(APPLICATION_JSON)
                        .with(user(userDetailsImpl)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("권한 없이 게시글을 조회할 수 없다.")
    void getPost_unauth() throws Exception {
        //given
        PostCreateRequestDTO postCreateRequestDTO = PostCreateRequestDTO.of(title, content);
        Post post = postRepository.save(Post.of(postCreateRequestDTO, userDetailsImpl.getUser()));

        //when then
        mockMvc.perform(get("/api/post/{id}", post.getId())
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("모든 게시글을 조회할 수 있다.")
    void getAllPosts() throws Exception {
        //given
        IntStream.range(1, 10)
                .forEach(i -> {
                    PostCreateRequestDTO postCreateRequestDTO = PostCreateRequestDTO.of(title + i, content + i);
                    postRepository.save(Post.of(postCreateRequestDTO, userDetailsImpl.getUser()));
                });

        //when then
        mockMvc.perform(get("/api/post")
                        .accept(APPLICATION_JSON)
                        .with(user(userDetailsImpl)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].title").value(title + "9"))
                .andExpect(jsonPath("$[0].content").value(content + "9"))
                .andDo(document("post/getAll",
                        responseFields(
                                fieldWithPath("[].id").description("게시글 아이디"),
                                fieldWithPath("[].title").description("제목"),
                                fieldWithPath("[].content").description("본문"),
                                fieldWithPath("[].userId").description("작성자 아이디")
                        ))
                );
    }

    @Test
    @DisplayName("게시글의 제목과 본문을 수정할 수 있다.")
    void updatePost() throws Exception {
        //given
        PostCreateRequestDTO postCreateRequestDTO = PostCreateRequestDTO.of(title, content);
        Post post = postRepository.save(Post.of(postCreateRequestDTO, userDetailsImpl.getUser()));
        PostUpdateRequestDTO postUpdateRequestDTO = PostUpdateRequestDTO.of(title + "1", content + "1");

        //when then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/post/{id}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postUpdateRequestDTO))
                        .with(user(userDetailsImpl)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post/update",
                        pathParameters(
                                parameterWithName("id").description("게시글 아이디")
                        ),
                        requestFields(
                                fieldWithPath("title").description("제목")
                                        .attributes(key("constraint").value("max length = 50")),
                                fieldWithPath("content").description("본문")
                        ))
                );
    }

    @Test
    @DisplayName("게시글의 제목만 수정할 수 있다.")
    void updatePost_onlyTitle() throws Exception {
        //given
        PostCreateRequestDTO postCreateRequestDTO = PostCreateRequestDTO.of(title, content);
        Post post = postRepository.save(Post.of(postCreateRequestDTO, userDetailsImpl.getUser()));
        PostUpdateRequestDTO postUpdateRequestDTO = PostUpdateRequestDTO.of(title + "1", null);

        //when then
        mockMvc.perform(patch("/api/post/{id}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postUpdateRequestDTO))
                        .with(user(userDetailsImpl)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글의 제목만 수정할 수 있다.")
    void updatePost_onlyContent() throws Exception {
        //given
        PostCreateRequestDTO postCreateRequestDTO = PostCreateRequestDTO.of(title, content);
        Post post = postRepository.save(Post.of(postCreateRequestDTO, userDetailsImpl.getUser()));
        PostUpdateRequestDTO postUpdateRequestDTO = PostUpdateRequestDTO.of(null, content + "1");

        //when then
        mockMvc.perform(patch("/api/post/{id}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postUpdateRequestDTO))
                        .with(user(userDetailsImpl)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("권한이 없으면 게시글을 수정할 수 없다.")
    void updatePost_unauth() throws Exception {
        //given
        PostCreateRequestDTO postCreateRequestDTO = PostCreateRequestDTO.of(title, content);
        Post post = postRepository.save(Post.of(postCreateRequestDTO, userDetailsImpl.getUser()));
        PostUpdateRequestDTO postUpdateRequestDTO = PostUpdateRequestDTO.of(title + "1", content + "1");
        UserDetailsImpl fakeUserDetailsImpl = createUser("fake@test.com", "12345678");

        //when then
        mockMvc.perform(patch("/api/post/{id}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postUpdateRequestDTO))
                        .with(user(fakeUserDetailsImpl)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("게시글을 삭제할 수 있다.")
    void deletePost() throws Exception {
        //given
        PostCreateRequestDTO postCreateRequestDTO = PostCreateRequestDTO.of(title, content);
        Post post = postRepository.save(Post.of(postCreateRequestDTO, userDetailsImpl.getUser()));

        //when then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/post/{id}", post.getId())
                        .with(user(userDetailsImpl)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post/delete",
                        pathParameters(
                                parameterWithName("id").description("게시글 아이디")
                        ))
                );
    }

    @Test
    @DisplayName("잘못된 id로 게시글을 삭제할 수 없다.")
    void deletePost_invalidId() throws Exception {
        //when then
        mockMvc.perform(delete("/api/post/{id}", 99L)
                        .with(user(userDetailsImpl)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("권한이 없으면 게시글을 삭제할 수 없다.")
    void deletePost_unauth() throws Exception {
        //given
        PostCreateRequestDTO postCreateRequestDTO = PostCreateRequestDTO.of(title, content);
        Post post = postRepository.save(Post.of(postCreateRequestDTO, userDetailsImpl.getUser()));

        //when then
        mockMvc.perform(delete("/api/post/{id}", post.getId()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    private UserDetailsImpl createUser(String email, String password) {
        UserCreateRequestDTO userCreateRequestDTO = UserCreateRequestDTO.of(email, password);
        User user = userRepository.save(User.from(userCreateRequestDTO));
        return UserDetailsImpl.from(user);
    }
}
