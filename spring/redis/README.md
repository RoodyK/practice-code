# Spring Redis 사용해보기

## Redis

- 레디스는 메모리 기반 데이터 저장소로, 데이터를 메모리에 저장함으로써 디스크 기반의 데이터베이스보다 빠른 데이터 접근 속도를 제공한다.  
- 레디스는 관계형 데이터베이스와 달리 스키마가 존재하지 않기 때문에 테이블이나 필드 정의 없이 데이터를 자유롭게 저장하고 조회할 수 있다.  
- 레디스는 데이터를 키-값 형태로 저장하며, 문자열 외에도 리스트, 셋, 해시 등 다양한 데이터 구조를 지원한다.

<br/>

## Spring Data Redis

Spring Data Redis는 레디스와 스프링(자바) 애플리케이션 간의 접근을 쉽게 할 수 있도록 도와준다.  

### Spring Data Redis가 제공하는 기능들
- RedisTemplate과 ReactiveRedisTemplate 도우미 클래스는 일반적인 Redis 작업을 수행할 때 생산성을 높인다.
- 예외를 Spring의 DataAccessException 계층 구조로 변환한다.
- Repository 인터페이스의 자동 구현, 사용자 정의 쿼리 메서드 지원 포함한다.
- Spring의 변환 서비스를 통해 풍부한 객체 매핑 기능을 제공한다.
- 다른 메타데이터 형식을 지원할 수 있도록 확장 가능한 주석 기반 매핑 메타데이터를 제공한다.
- 트랜잭션 및 파이프라이닝을 지원한다.
- Spring은 캐시 추상화를 제공하여 다양한 캐시 저장소와 통합할 수 있도록 한다.
- Redis는 Pub/Sub(발행/구독) 모델을 지원하여, 메시지 기반의 통신을 처리할 수 있다.
- Spring Data Redis는 RedisList나 RedisSet과 같은 Java용 Redis 컬렉션을 인터페이스로 구현해준다.  

<br/>

### RedisTemplate

Spring Data Redis가 제공하는 RedisTemplate는 Redis와의 상호작용을 추상화한 클래스이다. RedisTemplate은 직렬화 및 역직렬화, 서버와의 연결 설정 등을 커스터마이징할 수 있다.  

기본적으로 Spring Data Redis는 다양핝 자료구조에 대해서 별도의 RedisTemplate 클래스를 제공하고 있다.  

- RedisTemplate: 일반적은 템플릿으로 Redis에 저장할 수 있는 다양한 데이터 타입을 처리할 수 있다. Redis의 모든 자료형(String, Hash, List, Set 등)에 대한 CRUD 작업을 할 수 있으며, 기본적으로는 바이트 배열을 사용해서 데이터를 주고 받는다.
- StringRedisTemplate: `RedisTemplate<String, String>`의 서브 클래스로 키와 값 모두 String으로 제한되며, 문자열만 저장하거나 처리할때 효율적이다.
- HashOperations: Redis의 Hash 자료구조를 처리하는데 특화된 템플릿이다.
- ListOperations: Redis의 List 자료구조를 처리하는데 특화된 템플릿이다.
- SetOperations: Redis의 Set 자료구조를 처리하는데 특화된 템플릿이다.
- ZSetOperations: Redis의 Sorted Set 자료구조를 처리하는데 특화된 템플릿이다.

<br/>

### RedisTemplate을 사용할 때  ops...() 메서드의 의미

Spring Data Redis를 사용하면 RedisTemplate의 ops...() 메서드를 사용하게 되는데 ops는 연산(Operation)을 나타내며 유형에 맞는 연산을 구분해서 사용하기 위해 이러한 메서드를 제공한다.

- `opsForValue()`: Redis의 String 타입에 대한 연산을 처리한다.
- `opsForList()`: Redis의 List 타입에 대한 연산을 처리한다.
- `opsForSet()`: Redis의 Set 타입에 대한 연산을 처리한다.
- `opsForHash()`: Redis의 Hash 타입에 대한 연산을 처리한다.
- `opsForZSet()`: Redis의 Sorted Set 타입에 대한 연산을 처리한다.

<br/>

### RedisTemplate의 execuete(), executePipelined() 메서드

`execute()` 메서드는 Redis 서버에 하나의 명령어를 실행할 때 사용한다. 이 메서드는 명령을 순차적으로 Redis 서버로 전송하고 서버의 응답을 받아 결과를 즉시 반환한다.  
이 메서드는 네트워크 통신이 각 명령에 대해서 발생히므로, 여러 명령을 연속적으로 실행할 때 성능이 저하될 수 있다.  
단일 Redis 명령을 순차적으로 실행하고, 하나의 명령을 실행하고 그 결과를 즉시 처리하기 때문에 트랜젹션 환경에 적합하다.

```java
public class RedisService {
    private final StringRedisTemplate stringRedisTemplate;

    public void addValue(String key, String value) {
        stringRedisTemplate.execute((RedisCallback<?>) redisConnection -> {
            StringRedisConnection connection = (StringRedisConnection) redisConnection;
            connection.set(key, value);

            return null;
        });
    }
}
```

<br/>

`executePipelined()` 메서드는 여러 Redis 명령을 한 번에 실행할 수 있게 해준다. 여러 명령어를 묶어서(파이프라이닝) 전송하고, 그에 대한 응답을 한 번에 처리하는 방식이다. 이 메서드는 너트워크 지연을 줄여 성능을 최적화할 수 있다.  
이 메서드는 여러 명령을 한 번에 보내고 응답을 한 번에 받기 때문에, 네트워크 지연을 줄이고 성능을 개선시킬 수 있다. 즉, 많은 양의 데이터를 처리할 때 유용하다.  

```java
public class RedisService {
    private final StringRedisTemplate stringRedisTemplate;

    public void addValue(String key, String value, Long score, Duration ttl) {
        stringRedisTemplate.executePipelined((RedisCallback<?>) redisConnection -> {
            StringRedisConnection connection = (StringRedisConnection) redisConnection;
            connection.zAdd(key, score, value);
            connection.expire(key, ttl.toSeconds());

            return null;
        });
    }
}
```

<br/>

### RedisTemplate의 Sorted Set의 Range 시용 

Spring Data Redis가 제공하는 RedisTemplate의 `opsForSet()` 메서드를 통해 Sorted Set 자료구조를 사용할 수 있는데, 기본적으로 key에 대한 value값은 score 순으로 오름차순 정렬된다.  

`range(K key, long start, long end)` 메서드를 사용하면 오름차순 정렬된 데이터를 범위를 지정해서 조회할 수 있고, `reverseRangeWithScores(K key, long start, long end)` 메서드를 사용하면 내림차순 정렬된 데이터를 범위를 지정해서 조회할 수 있다.  
범위를 조회할 때 기본적으로 zero based index를 사용하며, end 인자에 -1은 가장 마지막 원소를 의미한다. 그래서 `range(key, 0, -1)`처럼 사용 시 전체 원소를 조회한다.

<br/>
