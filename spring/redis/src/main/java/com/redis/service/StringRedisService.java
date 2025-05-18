package com.redis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 가장 기본인 문자열 key, value 저장
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StringRedisService {

    private final StringRedisTemplate stringRedisTemplate;

    public void setValue(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 데이터를 저장할 때 만료시간 지정
     */
    public void setValue(String key, String value, Duration ttl) {
        stringRedisTemplate.opsForValue().set(key, value, ttl);
    }

    /**
     * setIfAbsent() 메서드는 key가 존재하지 않으면 데이터를 저장하고 true를 반환하고, key가 이미 존재하면 아무 작업을 하지 않고 false를 반환한다.
     */
    public boolean setIfAbsent(String key, String value) {
        return stringRedisTemplate.opsForValue().setIfAbsent(key, value);
    }

    public String getValue(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public List<String> multiGetValue(Collection<String> keys) {
        return stringRedisTemplate.opsForValue().multiGet(keys);
    }

    public Long valueSize(String key) {
        return stringRedisTemplate.opsForValue().size(key);
    }

    public void deleteValue(String key) {
        stringRedisTemplate.delete(key);
    }

    public Set<String> keys(String pattern) {
        return stringRedisTemplate.keys(pattern);
    }
}
