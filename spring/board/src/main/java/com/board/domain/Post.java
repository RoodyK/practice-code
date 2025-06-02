package com.board.domain;

import com.board.domain.dto.PostDto;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@ToString
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private PostType postType;

    private long views;

    private Long boardId;

    @Builder
    private Post(String title, String content, PostType postType, long views, Long boardId) {
        this.title = title;
        this.content = content;
        this.postType = postType;
        this.views = views;
        this.boardId = boardId;
    }

    public static Post create(PostDto postDto) {
        return Post.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .postType(postDto.getPostType())
                .views(0)
                .boardId(postDto.getBoardId())
                .build();
    }
}
