package com.board.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PageRequest {
    private long page;
    private long pageSize;

    public PageRequest(long page, long pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public long getOffset() {
        if (page == 0) {
            page = 1;
        }

        long pageSize = getPageSize();

        return (page - 1) * pageSize;
    }

    public long getPageSize() {
        return pageSize == 0 ? 20 : pageSize;
    }
}
