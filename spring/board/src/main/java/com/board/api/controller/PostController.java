package com.board.api.controller;

import com.board.api.dto.PageRequest;
import com.board.api.dto.PageResponse;
import com.board.api.service.PostService;
import com.board.api.service.response.PostResponse;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PostController {

    private final PostService postService;

    @GetMapping("/boards/{boardId}/posts")
    public PageResponse<PostResponse> readAll(
            @PathVariable("boardId") Long boardId,
            PageRequest pageRequest
    ) {
        log.info("call readAll()");
        return postService.readAll(boardId, pageRequest);
    }
}
