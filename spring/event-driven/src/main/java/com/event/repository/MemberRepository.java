package com.event.repository;

import com.event.domain.Member;

import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findById(Long id);
}
