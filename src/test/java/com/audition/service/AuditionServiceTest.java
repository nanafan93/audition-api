package com.audition.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionPost;
import com.audition.model.Identifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@Getter
class AuditionServiceTest {

    @MockBean
    private AuditionIntegrationClient auditionIntegrationClient;

    @Autowired
    private AuditionService auditionService;

    @Test
    void whenGetPostsThenReturnAll() {
        final var posts = List.of(
            new AuditionPost(1, 1, "Title1", "Body1", Collections.emptyList()),
            new AuditionPost(1, 2, "Title2", "Body2", Collections.emptyList()),
            new AuditionPost(1, 3, "Title3", "Body3", Collections.emptyList()),
            new AuditionPost(25, 4, "Title4", "Body4", Collections.emptyList())
        );
        Mockito.when(auditionIntegrationClient.getPosts()).thenReturn(posts);
        final var result = auditionService.getPosts("25");
        final var expected = List.of(new AuditionPost(25, 4, "Title4", "Body4", Collections.emptyList()));
        assertEquals(expected, result);
    }

    @Test
    void whenGetPostsFilteredThenFiltered() {
        final var posts = List.of(
            new AuditionPost(1, 1, "Title1", "Body1", Collections.emptyList()),
            new AuditionPost(1, 2, "Title2", "Body2", Collections.emptyList()),
            new AuditionPost(1, 3, "Title3", "Body3", Collections.emptyList()),
            new AuditionPost(25, 4, "Title4", "Body4", Collections.emptyList())
        );
        Mockito.when(auditionIntegrationClient.getPosts()).thenReturn(posts);
        final var result = auditionService.getPosts(null);
        assertEquals(posts, result);
    }

    @Test
    void whenGetPostByIdThenCall() {
        auditionService.getPostById(new Identifier("25"));
        Mockito.verify(auditionIntegrationClient, Mockito.times(1)).getPostById(25);
    }

    @Test
    void whenGetPostWithCommentsThenCall() {
        auditionService.getPostWithComments(new Identifier("25"));
        Mockito.verify(auditionIntegrationClient, Mockito.times(1)).getPostWithComments(25);
    }

    @Test
    void whenGetCommentsThenCall() {
        auditionService.getCommentsForPost(new Identifier("25"));
        Mockito.verify(auditionIntegrationClient, Mockito.times(1)).getCommentsForPost(25);
    }

    @Test
    void testAuditionPostHashCode() {
        final var post1 = new HashMap<>().put(new AuditionPost(1, 1, "This is such",
            "a box ticking exercise", Collections.emptyList()), 1);
        final var post2 = new HashMap<>().put(new AuditionPost(1, 1, "This is such",
            "a box ticking exercise", Collections.emptyList()), 1);

        assertEquals(post1, post2);
    }
}