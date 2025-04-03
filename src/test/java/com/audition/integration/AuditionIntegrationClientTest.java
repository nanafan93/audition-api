package com.audition.integration;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.net.URI;
import java.net.URISyntaxException;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;


@SpringBootTest
@Getter
class AuditionIntegrationClientTest {

    @Autowired
    private AuditionIntegrationClient client;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${spring.application.api.baseUrl}")
    private String baseUrl;
    private static final String PATH_POSTS = "/posts";
    private static final String PATH_COMMENTS = "/comments";
    private MockRestServiceServer mockServer;

    @BeforeEach
    void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void whenGetPostsThenCallClient() throws URISyntaxException {
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(baseUrl + PATH_POSTS)))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess());
        client.getPosts();
        Assertions.assertDoesNotThrow(() -> mockServer.verify());
    }

    @Test
    void whenGetPostByIdThenCallClient() throws URISyntaxException {
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(baseUrl + PATH_POSTS + "/25")))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess());
        client.getPostById(25);
        Assertions.assertDoesNotThrow(() -> mockServer.verify());
    }

    @Test
    void whenGetPostWithCommentsThenCallClient() throws URISyntaxException {
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(baseUrl + PATH_POSTS + "/25")))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(
                "{\"userId\":123,\"id\":456,\"title\":\"Seeking Talented Vocalist for Upcoming Project\",\"body\":\""
                    + "We are looking for a versatile vocalist with a strong range and stage presence for a new musical"
                    + " production. Please send us your demos and resume. Auditions will be held next week."
                    + "\",\"comments\":[]}", MediaType.APPLICATION_JSON));

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(baseUrl + PATH_POSTS + "/25" + PATH_COMMENTS)))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess());
        client.getPostWithComments(25);
        Assertions.assertDoesNotThrow(() -> mockServer.verify());
    }

    @Test
    void whenGetCommentsForPostThenCallClient() {
        mockServer.expect(ExpectedCount.once(),
                requestTo(baseUrl + PATH_COMMENTS + "?postId=25"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess());
        client.getCommentsForPost(25);
        Assertions.assertDoesNotThrow(() -> mockServer.verify());
    }
}