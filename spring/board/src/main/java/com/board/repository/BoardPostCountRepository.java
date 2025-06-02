package com.board.repository;

import com.board.domain.BoardPostCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardPostCountRepository extends JpaRepository<BoardPostCount, Long> {

    @Query(
            value = "update board_post_count set post_count = post_count + 1 where board_id = :boardId",
            nativeQuery = true
    )
    @Modifying
    int increaseCount(@Param("boardId") Long boardId);

    @Query(
            value = "update board_post_count set post_count = post_count - 1 where board_id = :boardId",
            nativeQuery = true
    )
    @Modifying
    int decreaseCount(@Param("boardId") Long boardId);
}
