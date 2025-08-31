package com.gateway.filters;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CircuitBreakerFilter implements GlobalFilter, Ordered {

    private final CircuitBreaker circuitBreaker;

    @Autowired
    public CircuitBreakerFilter(CircuitBreakerRegistry registry) {
        this.circuitBreaker = registry.circuitBreaker("gatewayCircuitBreaker");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // This is just a placeholder to show where you'd wrap calls with the circuit breaker in reactive flows.
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
