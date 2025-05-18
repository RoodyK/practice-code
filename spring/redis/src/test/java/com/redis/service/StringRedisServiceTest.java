package com.redis.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StringRedisServiceTest {

    @Autowired
    private StringRedisService stringRedisService;

    @Test
    void setValue() {
        // given
        String key = "message";
        String value = "hello redis";

        // when
        stringRedisService.setValue(key, value);

        // then
        String result = stringRedisService.getValue(key);
        assertThat(result).isEqualTo(value);

        stringRedisService.deleteValue(key);
    }

    @Test
    void setValueTimeToLive() throws InterruptedException {
        // given
        String key = "message";
        String value = "hello redis";

        // when
        stringRedisService.setValue(key, value, Duration.ofSeconds(2));

        // then
        String result = stringRedisService.getValue(key);
        assertThat(result).isEqualTo(value);

        TimeUnit.SECONDS.sleep(3);

        String result2 = stringRedisService.getValue(key);
        assertThat(result2).isNull();
    }

    @Test
    void setValueIfAbsent() {
        // given
        String key = "message";
        String value = "hello redis";

        // when
        boolean result1 = stringRedisService.setIfAbsent(key, value);
        boolean result2 = stringRedisService.setIfAbsent(key, value);

        // then
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();

        String result = stringRedisService.getValue(key);
        assertThat(result).isEqualTo(value);

        stringRedisService.deleteValue(key);
    }

    @Test
    void deleteValueWillReturnNull() {
        // given
        String key = "message";
        String value = "hello redis";

        // when
        stringRedisService.setValue(key, value);

        // then
        String result = stringRedisService.getValue(key);
        assertThat(result).isEqualTo(value);

        stringRedisService.deleteValue(key);
        String result2 = stringRedisService.getValue(key);
        assertThat(result2).isNull();
    }

    @Test
    void multiGet() {
        // given
        String key1 = "name";
        String value1 = "Bob";

        String key2 = "phone";
        String value2 = "010-1111-1111";

        stringRedisService.setValue(key1, value1);
        stringRedisService.setValue(key2, value2);

        // when
        List<String> result = stringRedisService.multiGetValue(List.of(key1, key2));

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualTo(value1);
        assertThat(result.get(1)).isEqualTo(value2);

        stringRedisService.deleteValue(key1);
        stringRedisService.deleteValue(key2);
    }

    @Test
    void keysAllPattern() {
        // given
        String key1 = "name";
        String value1 = "Bob";

        String key2 = "phone";
        String value2 = "010-1111-1111";

        stringRedisService.setValue(key1, value1);
        stringRedisService.setValue(key2, value2);

        // when
        Set<String> result = stringRedisService.keys("*");

        // then
        assertThat(result).hasSize(2);
        assertThat(result.contains(key1)).isTrue();
        assertThat(result.contains(key2)).isTrue();

        stringRedisService.deleteValue(key1);
        stringRedisService.deleteValue(key2);
    }

    @Test
    void valueSize() {
        // given
        String key1 = "name";
        String value1 = "Bob";

        stringRedisService.setValue(key1, value1);

        // when
        Long result = stringRedisService.valueSize(key1);

        // then
        assertThat(result).isEqualTo(3L);

        stringRedisService.deleteValue(key1);
    }
}