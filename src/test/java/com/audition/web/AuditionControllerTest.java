package com.audition.web;

import static org.mockito.ArgumentMatchers.argThat;

import com.audition.service.AuditionService;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@Getter
class AuditionControllerTest {

    @Autowired
    AuditionController controller;

    @MockBean
    AuditionService auditionService;

    @Test
    void whenGetPostsThenCallService() {
        controller.getPosts(null);
        Mockito.verify(auditionService, Mockito.times(1)).getPosts(null);
    }

    @Test
    void whenGetPostsFilteredThenCallServiceFiltered() {
        controller.getPosts("25");
        Mockito.verify(auditionService, Mockito.times(1)).getPosts("25");
    }

    @Test
    void whenGetPostByIdThenCallServiceById() {
        controller.getPostById("1");
        Mockito.verify(auditionService, Mockito.times(1))
            .getPostById(argThat(it -> it.getRawValue() == 1));
    }

    @Test
    void whenGetCommentsThenCallService() {
        controller.getPostWithComments("1");
        Mockito.verify(auditionService, Mockito.times(1))
            .getPostWithComments(argThat(it -> it.getRawValue() == 1));
    }

    @Test
    void whenGetCommentsForPostThenCallService() {
        controller.getCommentsForPost("1");
        Mockito.verify(auditionService, Mockito.times(1))
            .getCommentsForPost(argThat(it -> it.getRawValue() == 1));
    }

}