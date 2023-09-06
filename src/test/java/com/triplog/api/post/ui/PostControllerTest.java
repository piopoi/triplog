package com.triplog.api.post.ui;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.triplog.api.BaseControllerTest;
import com.triplog.api.auth.domain.UserDetailsImpl;
import com.triplog.api.post.domain.Post;
import com.triplog.api.post.dto.PostCreateRequestDTO;
import com.triplog.api.post.repository.PostRepository;
import com.triplog.api.user.domain.User;
import com.triplog.api.user.repository.UserRepository;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PostControllerTest extends BaseControllerTest {

    private final String title = "foo";
    private final String content = "bar";

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private UserDetailsImpl userDetailsImpl;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("test@test.com", "12345678"));
        userDetailsImpl = new UserDetailsImpl(user);
    }

    @Test
    @DisplayName("게시글을 작성할 수 있다.")
    void createPost() throws Exception {
        //given
        PostCreateRequestDTO postCreateRequestDTO = PostCreateRequestDTO.builder()
                .title(title)
                .content(content)
                .build();

        //when then
        mockMvc.perform(post("/api/post")
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
        PostCreateRequestDTO postCreateRequestDTO = PostCreateRequestDTO.builder()
                .title(title)
                .content(content)
                .build();

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
        PostCreateRequestDTO postCreateRequestDTO = PostCreateRequestDTO.builder()
                .title("")
                .content(content)
                .build();

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
        PostCreateRequestDTO postCreateRequestDTO = PostCreateRequestDTO.builder()
                .title("a".repeat(51)) //length = 51
                .content(content)
                .build();

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
        PostCreateRequestDTO postCreateRequestDTO = PostCreateRequestDTO.builder()
                .title(title)
                .content("")
                .build();

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
        Post post = postRepository.save(new Post(title, content, user));

        //when then
        mockMvc.perform(get("/api/post/{id}", post.getId())
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
        Post post = postRepository.save(new Post(title, content, user));

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
                .forEach(i -> postRepository.save(new Post(title + i, content + i, user)));

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
}
