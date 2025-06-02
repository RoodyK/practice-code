package com.board.domain.dto;

import com.board.domain.PostType;
import lombok.Getter;

@Getter
public class PostEditDto {

    private String title;
    private String content;
    private PostType postType;

    public PostEditDto(String title, String content, PostType postType) {
        this.title = title;
        this.content = content;
        this.postType = postType;
    }

    public static class PostEditDtoBuilder {
        private String title;
        private String content;
        private PostType postType;

        PostEditDtoBuilder() {}
    }
}
