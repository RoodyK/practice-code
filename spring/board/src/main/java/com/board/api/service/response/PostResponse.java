package com.board.api.service.response;

import com.board.domain.Post;
import com.board.domain.PostType;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PostResponse {

    private final Long postId;
    private final String title;
    private final String content;
    private final PostType postType;
    private final long views;

    public PostResponse(Long postId, String title, String content, PostType postType, long views) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.postType = postType;
        this.views = views;
    }

    public PostResponse(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.postType = post.getPostType();
        this.views = post.getViews();
    }

    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getPostType(),
                post.getViews()
        );
    }
}
