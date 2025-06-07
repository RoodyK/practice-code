package com.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SortedSetRedisService {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * key에 해당하는 value 추가
     */
    public void addValue(String key, String value, Long score) {
        stringRedisTemplate.opsForZSet().add(key, value, score.doubleValue());
    }

    /**
     * key에 해당하는 value 추가, 만료시간 지정
     */
    public void addValue(String key, String value, Long score, Duration ttl) {
        stringRedisTemplate.executePipelined((RedisCallback<?>) redisConnection -> {
            StringRedisConnection connection = (StringRedisConnection) redisConnection;
            connection.zAdd(key, score, value);
            connection.expire(key, ttl.toSeconds());

            return null;
        });
    }

    /**
     * key 에 해당하는 상위 range 만큼을 제외한 데이터 제거
     */
    public void removeRangeExceptTopX(String key, Long range) {
        stringRedisTemplate.opsForZSet().removeRange(key, 0, - range -1);
    }

    /**
     * key 에 해당하는 상위 5개를 제외한 데이터 제거
     */
    public void removeRangeExceptTopFive(String key) {
        removeRangeExceptTopX(key, 5L);
    }

    /**
     * key 에 해당하는 전체 데이터 제거
     */
    public void removeRangeAll(String key) {
        removeRangeExceptTopX(key, 0L);
    }

    /**
     * 해당 키 제거
     */
    public void removeKey(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * key에 해당하는 모든 값을 score:value 형태로 읽어오기
     */
    public List<String> readAll(String key) {
        return stringRedisTemplate.opsForZSet().reverseRangeWithScores(key, 0, -1).stream()
                .map(tuple -> {
                    Double score = tuple.getScore();
                    String value = tuple.getValue();
                    return "%s:%s".formatted(value, score.intValue());
                })
                .toList();
    }
}
