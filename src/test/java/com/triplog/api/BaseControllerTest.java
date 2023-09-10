package com.triplog.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.triplog.api.auth.domain.UserDetailsImpl;
import com.triplog.api.user.domain.Role;
import com.triplog.api.user.domain.User;
import com.triplog.api.user.dto.UserCreateRequestDTO;
import com.triplog.api.user.repository.UserRepository;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@CustomAutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class BaseControllerTest extends BaseTest {

    protected String adminEmail = "admin@test.com";
    protected String adminPassword = "12345678";

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected UserRepository userRepository;

    protected User admin;
    protected UserDetailsImpl adminUserDetailsImpl;

    @BeforeEach
    void BaseControllerTestSetUp() {
        adminUserDetailsImpl = createUserAndLogin(adminEmail, adminPassword);
        admin = adminUserDetailsImpl.getUser();
    }

    protected UserDetailsImpl createUserAndLogin(String email, String password) {
        UserCreateRequestDTO userCreateRequestDTO = UserCreateRequestDTO.of(email, password, Role.ADMIN.name());
        User user = userRepository.save(User.from(userCreateRequestDTO));
        return UserDetailsImpl.from(user);
    }

    protected void checkJsonResponse(MvcResult mvcResult, String expression, String findString) throws UnsupportedEncodingException {
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<String> strings = JsonPath.read(jsonResponse, expression);
        assertThat(strings).contains(findString);
    }
}
