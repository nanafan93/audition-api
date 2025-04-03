package com.audition.service;

import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionPost;
import com.audition.model.Identifier;
import com.audition.model.PostComment;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
public class AuditionService {

    private final AuditionIntegrationClient auditionIntegrationClient;

    AuditionService(
        final @Autowired AuditionIntegrationClient auditionIntegrationClient
    ) {
        this.auditionIntegrationClient = auditionIntegrationClient;
    }


    public List<AuditionPost> getPosts(@Nullable final String userId) {
        final var posts = auditionIntegrationClient.getPosts();
        return Objects.isNull(userId) ? posts
            : posts.stream().filter(it -> it.getUserId() == new Identifier(userId).getRawValue()).toList();
    }

    public AuditionPost getPostById(final Identifier postIdentifier) {
        return auditionIntegrationClient.getPostById(postIdentifier.getRawValue());
    }

    public AuditionPost getPostWithComments(final Identifier postIdentifier) {
        return auditionIntegrationClient.getPostWithComments(postIdentifier.getRawValue());
    }

    public List<PostComment> getCommentsForPost(final Identifier postIdentifier) {
        return auditionIntegrationClient.getCommentsForPost(postIdentifier.getRawValue());
    }
}
