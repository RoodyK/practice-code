package com.board.api.service;

import com.board.api.dto.PageRequest;
import com.board.api.dto.PageResponse;
import com.board.api.service.request.PostServiceEditRequest;
import com.board.api.service.request.PostServiceCreateRequest;
import com.board.api.service.response.PostResponse;
import com.board.domain.BoardPostCount;
import com.board.domain.Post;
import com.board.repository.BoardPostCountRepository;
import com.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final BoardPostCountRepository boardPostCountRepository;

    public Long create(Long boardId, PostServiceCreateRequest request) {
        // 실제 Board 기능을 구현한 애플리케이션의 API를 호출해서 boardId가 존재하는지 확인하는 작업이 필요
        // 여기서는 예제를 단순화하기 위해서 모듈을 분리하지는 않음

        // 게시글 저장
        Post post = Post.create(request.toPostDto(boardId));
        Post savedPost = postRepository.save(post);

        // 게시글 수 증가
        int row = boardPostCountRepository.increaseCount(boardId);
        // 레코드가 존재하지 않는다면 count 값을 1로 레코드 생성
        if (row == 0) {
            boardPostCountRepository.save(BoardPostCount.create(boardId));
        }

        return savedPost.getId();
    }

    public void delete(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow();

        postRepository.deleteById(postId);
        boardPostCountRepository.decreaseCount(post.getBoardId());
    }

    public void edit(Long postId, PostServiceEditRequest request) {
        postRepository.findById(postId)
                .orElseThrow();
    }

    @Transactional
    public PostResponse read(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow();

        return PostResponse.from(post);
    }

    @Transactional(readOnly = true)
    public PageResponse<PostResponse> readAll(Long boardId, PageRequest request) {
        List<Post> posts = postRepository.findAll(boardId, request.getPageSize(), request.getOffset());
        BoardPostCount boardPostCount = boardPostCountRepository.findById(boardId).orElseThrow();

        return new PageResponse<>(posts, PostResponse.class, request, boardPostCount.getPostCount());
    }
}
