package com.board.api.service.request;

import com.board.domain.dto.PostDto;
import com.board.domain.PostType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostServiceCreateRequest {
    private String title;
    private String content;
    private PostType postType;

    public PostDto toPostDto(Long boardId) {
        return new PostDto(title, content, postType, boardId);
    }
}
