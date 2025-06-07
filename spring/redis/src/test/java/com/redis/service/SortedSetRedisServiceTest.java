package com.redis.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SortedSetRedisServiceTest {

    @Autowired
    private SortedSetRedisService sortedSetRedisService;

    private final String SORTED_KEY = "sortKey";

    @AfterEach
    void removeSet() {
        sortedSetRedisService.removeKey(SORTED_KEY);
    }

    @Test
    void addValue() {
        // given
        for (int i = 1; i <= 5; i++) {
            sortedSetRedisService.addValue(SORTED_KEY, "value" + i, Long.valueOf(i));
        }

        // when & then
        List<String> result = sortedSetRedisService.readAll(SORTED_KEY);
        assertThat(result).hasSize(5)
                .containsExactly("value5:5", "value4:4", "value3:3", "value2:2", "value1:1");
    }

    @Test
    void addValueSetTTL() throws InterruptedException {
        // given
        for (int i = 1; i <= 5; i++) {
            sortedSetRedisService.addValue(SORTED_KEY, "value" + i, Long.valueOf(i), Duration.ofSeconds(1L));
        }

        // when & then
        Thread.sleep(2000);

        List<String> result = sortedSetRedisService.readAll(SORTED_KEY);
        assertThat(result).isEmpty();
    }

    @Test
    void removeRangeTop5() {
        // given
        for (int i = 1; i <= 10; i++) {
            sortedSetRedisService.addValue(SORTED_KEY, "value" + i, Long.valueOf(i));
        }

        // when
        sortedSetRedisService.removeRangeExceptTopFive(SORTED_KEY);

        // then
        List<String> result = sortedSetRedisService.readAll(SORTED_KEY);
        assertThat(result).hasSize(5)
                .containsExactly("value10:10", "value9:9", "value8:8", "value7:7", "value6:6");
    }

    @Test
    void removeRangeAll() {
        // given
        for (int i = 1; i <= 5; i++) {
            sortedSetRedisService.addValue(SORTED_KEY, "value" + i, Long.valueOf(i));
        }

        // when
        sortedSetRedisService.removeRangeAll(SORTED_KEY);

        // then
        List<String> result = sortedSetRedisService.readAll(SORTED_KEY);
        assertThat(result).isEmpty();
    }

    @Test
    void removeRangeTopX() {
        // given
        for (int i = 1; i <= 10; i++) {
            sortedSetRedisService.addValue(SORTED_KEY, "value" + i, Long.valueOf(i));
        }

        // when
        sortedSetRedisService.removeRangeExceptTopX(SORTED_KEY, 3L);

        // then
        List<String> result = sortedSetRedisService.readAll(SORTED_KEY);
        assertThat(result).hasSize(3)
                .containsExactly("value10:10", "value9:9", "value8:8");
    }
}