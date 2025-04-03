package com.audition.web;

import com.audition.model.AuditionPost;
import com.audition.model.Identifier;
import com.audition.model.PostComment;
import com.audition.service.AuditionService;
import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Getter
public class AuditionController {

    private final AuditionService auditionService;

    public AuditionController(
        final @Autowired AuditionService auditionService
    ) {
        this.auditionService = auditionService;
    }

    @GetMapping(value = "/posts", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AuditionPost> getPosts(@RequestParam(name = "userId", required = false) final String userId) {
        return auditionService.getPosts(userId);
    }

    @GetMapping(value = "/posts/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AuditionPost getPostById(@PathVariable("id") final String postId) {
        return auditionService.getPostById(new Identifier(postId));
    }

    @GetMapping(value = "/posts/{id}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public AuditionPost getPostWithComments(@PathVariable("id") final String postId) {
        return auditionService.getPostWithComments(new Identifier(postId));
    }

    @GetMapping(value = "/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PostComment> getCommentsForPost(@RequestParam(name = "postId") final String postId) {
        return auditionService.getCommentsForPost(new Identifier(postId));
    }

}
