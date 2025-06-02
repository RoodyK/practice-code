package com.board.domain;

import com.board.domain.dto.PostDto;
import com.board.repository.PostRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @PersistenceContext
    private EntityManager em;

    @BeforeEach
    void setup() {
        for (int i = 1; i <= 5; i++) {
            Post post = Post.create(new PostDto("제목" + i, "내용" + i, PostType.NORMAL, 1L));
            postRepository.save(post);
        }

        em.flush();
        em.clear();
    }

    @Test
    void findAllTest() {
        // given
        Long boardId = 1L;
        Long limit = 20L;
        Long offset = 0L;

        // when
        List<Post> posts = postRepository.findAll(boardId, limit, offset);

        // then
        assertThat(posts).hasSize(5)
                .extracting("title", "content", "postType", "views")
                .containsExactly(
                        Tuple.tuple("제목5", "내용5", PostType.NORMAL, 0L),
                        Tuple.tuple("제목4", "내용4", PostType.NORMAL, 0L),
                        Tuple.tuple("제목3", "내용3", PostType.NORMAL, 0L),
                        Tuple.tuple("제목2", "내용2", PostType.NORMAL, 0L),
                        Tuple.tuple("제목1", "내용1", PostType.NORMAL, 0L)
                );
    }
}