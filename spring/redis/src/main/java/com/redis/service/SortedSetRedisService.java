package com.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SortedSetRedisService {

    private final StringRedisTemplate stringRedisTemplate;

    public void setValue(String key, String value, Long score) {
        stringRedisTemplate.opsForZSet().add(key, value, score.doubleValue());
    }

    public void getValue(String key) {
        stringRedisTemplate.opsForZSet().reverseRangeWithScores(key, 0, -1);
    }
}
