package com.triplog.api;

import com.triplog.api.post.domain.Post;
import com.triplog.api.post.dto.PostCreateRequestDTO;
import com.triplog.api.post.repository.PostRepository;
import com.triplog.api.user.domain.Role;
import com.triplog.api.user.domain.User;
import com.triplog.api.user.dto.UserCreateRequestDTO;
import com.triplog.api.user.repository.UserRepository;
import com.triplog.api.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public class BaseTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    protected void baseTestSetUp() {
        databaseCleanup.execute();
    }

    protected User createUser(String email, String password, Role role) {
        UserCreateRequestDTO userCreateRequestDTO = UserCreateRequestDTO.of(email, password, role.name());
        User user = User.of(userCreateRequestDTO, passwordEncoder);
        return userRepository.save(user);
    }

    protected Post createPost(String title, String content, User user) {
        PostCreateRequestDTO postCreateRequestDTO = PostCreateRequestDTO.of(title, content);
        Post post = Post.of(postCreateRequestDTO, user);
        return postRepository.save(post);
    }
}
