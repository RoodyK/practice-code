package com.board.api.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiResponse {

    private final Boolean result;
    private final Integer code;
    private final String message;
}
