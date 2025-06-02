package com.board.api.dto;

import lombok.Getter;
import java.lang.reflect.Constructor;
import java.util.List;

@Getter
public class PageResponse<T> {

    private final long page;
    private final long pageSize;
    private final long totalCount;
    private final List<T> items;

    public PageResponse(List<?> items, Class<T> clazz, PageRequest pageRequest, long totalCount) {
        this.page = pageRequest.getPage();
        this.pageSize = pageRequest.getPageSize();
        this.totalCount = totalCount;
        this.items = items.stream()
                .map(item -> {
                    try {
                        Constructor<T> constructor = clazz.getDeclaredConstructor(item.getClass());
                        return constructor.newInstance(item);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }
}
