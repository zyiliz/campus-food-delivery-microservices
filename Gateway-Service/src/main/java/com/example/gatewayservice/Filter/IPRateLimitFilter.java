package com.example.gatewayservice.Filter;

import com.example.utils.IPUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class IPRateLimitFilter implements GlobalFilter, Ordered {

    private final ReactiveRedisTemplate<String, String> redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String ip = IPUtils.getIpAddress(exchange.getRequest());
        String key = "rate_limit:"+ip;
        return redisTemplate.opsForValue().increment(key)
                .flatMap(count->{
                    if (count == 1){
                        return redisTemplate.expire(key, Duration.ofSeconds(1))
                                .then(chain.filter(exchange));
                    }
                    if (count >5){
                        log.warn("【网关限流】检测到异常频率访问，IP: {}, 当前频率: {}/s", ip, count);
                        exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                        return exchange.getResponse().setComplete();
                    }
                    return chain.filter(exchange);
                });

    }

    @Override
    public int getOrder() {
        return -100;
    }
}
