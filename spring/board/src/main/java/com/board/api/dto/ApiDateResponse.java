package com.board.api.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiDateResponse<T> extends ApiResponse {

    private final T data;

    private ApiDateResponse(T data) {
        super(true, HttpStatus.OK.value(), "Ok");
        this.data = data;
    }

    public static <T> ApiDateResponse<T> of(T data) {
        return new ApiDateResponse<>(data);
    }

    public static <T> ApiDateResponse<T> empty() {
        return new ApiDateResponse<>(null);
    }
}
