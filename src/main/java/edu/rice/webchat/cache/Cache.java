package edu.rice.webchat.cache;

import java.util.concurrent.ConcurrentHashMap;

public class Cache<T> {
    protected final ConcurrentHashMap<T, Object> cache = new ConcurrentHashMap<>();

    public void put(T key, Object value) {
        cache.put(key, value);
    }

    public Object delete(T key) {
        return cache.remove(key);
    }

    public Object get(T key) {
        return cache.get(key);
    }
}
