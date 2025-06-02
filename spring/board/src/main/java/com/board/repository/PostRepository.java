package com.board.repository;

import com.board.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // JPQL은 FROM 절의 서브 쿼리를 허용하지 않는다. 아래처럼 nativeQuery로 작성하거나, JDBC Template, MyBatis 등의 방법을 사용할 수 있다.
    @Query(
            value = "select p.* from ( " +
                    "select post_id from post where board_id = :boardId order by post_id desc limit :limit offset :offset " +
                    ") sp left join post p on sp.post_id = p.post_id",
            nativeQuery = true
    )
    List<Post> findAll(@Param("boardId") Long boardId, @Param("limit") Long limit, @Param("offset") Long offset);

    @Query(
            value = "select count(*) from post where board_id = :boardId",
            nativeQuery = true
    )
    Long totalCount(@Param("boardId") Long boardId);
}
