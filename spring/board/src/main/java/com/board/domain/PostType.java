package com.board.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PostType {

    NORMAL("일반"),
    QUESTION("질문"),
    SHARE("공유"),
    INFORMATION("정보"),
    ETC("기타")
    ;

    private final String description;
}
