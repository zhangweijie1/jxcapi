package com.mrguo.util;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * redis客户端根据
 */
@Configuration
public class RedisClient {

    @Autowired
    private JedisPool jedisPool;

    public String set(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.set(key, value);
        jedis.close();
        return result;
    }

    public String set(String key, String val, int exp) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.setex(key, exp, val);
        jedis.close();
        return result;

    }

    public String setList(String key, List<Map<String, Object>> lists) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.set(key, JSON.toJSONString(lists));
        jedis.close();
        return result;

    }

    public long del(String key) {
        Jedis jedis = jedisPool.getResource();
        long result = jedis.del(key);
        jedis.close();
        return result;
    }

    public long del(String[] key) {
        Jedis jedis = jedisPool.getResource();
        long result = jedis.del(key);
        jedis.close();
        return result;
    }

    public String get(String key) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.get(key);
        jedis.close();
        return result;
    }

    public Boolean exists(String key) {
        Jedis jedis = jedisPool.getResource();
        Boolean result = jedis.exists(key);
        jedis.close();
        return result;
    }

    public Long expire(String key, int seconds) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.expire(key, seconds);
        jedis.close();
        return result;
    }

    public Long ttl(String key) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.ttl(key);
        jedis.close();
        return result;
    }

    public Long incr(String key) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.incr(key);
        jedis.close();
        return result;
    }

    public Long incr(String key, int seconds) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.incr(key);
        if (result == 1) {
            jedis.expire(key, seconds);
        }
        jedis.close();
        return result;
    }

    public Long incrBy(String key, Long val) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.incrBy(key, val);
        jedis.close();
        return result;
    }

    public Long hset(String key, String field, String value) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.hset(key, field, value);
        jedis.close();
        return result;
    }

    public String hget(String key, String field) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.hget(key, field);
        jedis.close();
        return result;
    }

    public Long hdel(String key, String... field) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.hdel(key, field);
        jedis.close();
        return result;
    }

    /**
     * 获取指定的list的长度
     *
     * @param key key
     * @return 结果
     */
    public Long llenght(String key) {
        Jedis jedis = jedisPool.getResource();
        Long llen = jedis.llen(key);
        jedis.close();
        return llen;
    }

    public Long lpush(String key, String... value) {
        Jedis jedis = jedisPool.getResource();
        long resultStatus = jedis.lpush(key, value);
        jedis.close();
        return resultStatus;
    }

    public Long rpush(String key, String... value) {
        Jedis jedis = jedisPool.getResource();
        long resultStatus = jedis.rpush(key, value);
        jedis.close();
        return resultStatus;
    }

    public List<String> lrange(String key, long start, long end) {
        Jedis jedis = jedisPool.getResource();
        List<String> lrange = jedis.lrange(key, start, end);
        jedis.close();
        return lrange;
    }

    public String ltrim(String key, long start, long end) {
        Jedis jedis = jedisPool.getResource();
        String item = jedis.ltrim(key, start, end);
        jedis.close();
        return item;
    }

    public String lpop(String key) {

        Jedis jedis = jedisPool.getResource();
        String item = jedis.lpop(key);
        jedis.close();
        return item;

    }

    public String rpop(String key) {
        Jedis jedis = jedisPool.getResource();
        String item = jedis.rpop(key);
        jedis.close();
        return item;
    }

    public String lclear(String key) {
        Jedis jedis = jedisPool.getResource();
        String ltrim = jedis.ltrim(key, 0, 0);
        jedis.close();
        return ltrim;

    }

    public Long llen(String key) {
        Jedis jedis = jedisPool.getResource();
        Long length = jedis.llen(key);
        jedis.close();
        return length;
    }

    public List<String> lrange(String key, int start, int end) {
        Jedis jedis = jedisPool.getResource();
        List<String> list = jedis.lrange(key, start, end);
        jedis.close();
        return list;
    }

    public Set<String> keys(String key) {
        Jedis jedis = jedisPool.getResource();
        Set<String> set = jedis.keys(key);
        jedis.close();
        return set;
    }
}
