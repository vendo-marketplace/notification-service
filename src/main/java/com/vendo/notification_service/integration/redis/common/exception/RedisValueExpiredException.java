package com.vendo.notification_service.integration.redis.common.exception;

// TODO move to common
public class RedisValueExpiredException extends RuntimeException {
    public RedisValueExpiredException(String message) {
        super(message);
    }
}
