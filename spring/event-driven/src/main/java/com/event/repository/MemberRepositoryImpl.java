package com.event.repository;

import com.event.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private static final Map<Long, Member> store = Map.of(
            1L, Member.of(1L, "회원1"),
            2L, Member.of(2L, "회원2")
    );

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }
}
