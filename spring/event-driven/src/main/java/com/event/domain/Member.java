package com.event.domain;

import lombok.Builder;

public class Member {

    private Long id;
    private String name;

    @Builder
    private Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Member of(Long id, String name) {
        return Member.builder()
                .id(id)
                .name(name)
                .build();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
