package com.triplog.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import java.io.UnsupportedEncodingException;
import java.util.List;
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

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;

    protected void checkJsonResponse(MvcResult mvcResult, String expression, String findString) throws UnsupportedEncodingException {
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<String> strings = JsonPath.read(jsonResponse, expression);
        assertThat(strings).contains(findString);
    }
}
