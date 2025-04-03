package com.audition.integration;

import com.audition.model.AuditionPost;
import com.audition.model.PostComment;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Getter
public class AuditionIntegrationClient {

    private final RestTemplate restTemplate;
    @Value("${spring.application.api.baseUrl}")
    private String baseUrl;
    private static final String PATH_POSTS = "/posts";
    private static final String PATH_COMMENTS = "/comments";
    private static final String ID_PARAM = "/{id}";

    public AuditionIntegrationClient(final @Autowired RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public List<AuditionPost> getPosts() {
        final String url = UriComponentsBuilder.fromHttpUrl(baseUrl).path(PATH_POSTS).toUriString();
        final ResponseEntity<List<AuditionPost>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {
            }
        );
        return response.getBody() == null ? Collections.emptyList() : response.getBody();
    }

    public AuditionPost getPostById(final int id) {
        final String url = UriComponentsBuilder.fromHttpUrl(baseUrl).path(PATH_POSTS + ID_PARAM)
            .buildAndExpand(id)
            .toUriString();
        return restTemplate.getForObject(url, AuditionPost.class);
    }

    public AuditionPost getPostWithComments(final int postId) {
        final String url = UriComponentsBuilder.fromHttpUrl(baseUrl).path(PATH_POSTS + ID_PARAM + PATH_COMMENTS)
            .buildAndExpand(postId)
            .toUriString();
        final var response = this.getPostById(postId);
        final ResponseEntity<List<PostComment>> comments = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {
            }
        );
        response.setComments(comments.getBody() == null ? Collections.emptyList() : comments.getBody());
        return response;
    }

    public List<PostComment> getCommentsForPost(final int postId) {
        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl).path(PATH_COMMENTS)
            .queryParam("postId", postId);
        final ResponseEntity<List<PostComment>> response = restTemplate.exchange(
            builder.toUriString(),
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {
            }
        );
        return response.getBody() == null ? Collections.emptyList() : response.getBody();
    }
}
