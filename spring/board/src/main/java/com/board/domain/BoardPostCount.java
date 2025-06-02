package com.board.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class BoardPostCount {

    @Id
    private Long boardId;

    private Long postCount;

    public static BoardPostCount create(Long boardId) {
        BoardPostCount boardPostCount = new BoardPostCount();
        boardPostCount.boardId = boardId;
        boardPostCount.postCount = 1L;

        return boardPostCount;
    }
}
