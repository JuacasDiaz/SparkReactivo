package infrastructure.adapter.redis;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final ReactiveRedisTemplate<String, String> redisTemplate;

    public Mono<Boolean> saveData(String key, String value) {
        return redisTemplate.opsForValue().set(key, value);
    }

    public Mono<String> getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
