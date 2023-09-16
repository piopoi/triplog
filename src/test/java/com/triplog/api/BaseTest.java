package com.triplog.api;

import com.triplog.api.post.domain.Post;
import com.triplog.api.post.repository.PostRepository;
import com.triplog.api.user.domain.Role;
import com.triplog.api.user.domain.User;
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
        User user = User.of(email, password, role.name(), passwordEncoder);
        return userRepository.save(user);
    }

    protected Post createPost(String title, String content, User user) {
        Post post = Post.of(title, content, user);
        return postRepository.save(post);
    }
}
