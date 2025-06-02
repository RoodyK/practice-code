package com.board.domain.dto;

import com.board.domain.PostType;
import lombok.Getter;

@Getter
public class PostDto {

    private final String title;
    private final String content;
    private final PostType postType;
    private final Long boardId;

    public PostDto(String title, String content, PostType postType, Long boardId) {
        this.title = title;
        this.content = content;
        this.postType = postType;
        this.boardId = boardId;
    }
}
