package com.example.common.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.ShardedJedis;

import java.util.List;
import java.util.Set;

@Slf4j
@Repository(value = "redisCache")
public class RedisCache {

    @Autowired
    private RedisDataSource redisDataSource;


    /**
     * auto-select 0 database in cluster
     *
     * @param key
     * @param val
     * @return
     */
    public String set(String key, String val) {
        String result = null;
        try (ShardedJedis shardedJedis = redisDataSource.getShardedJedis()) {
            if (shardedJedis == null) {
                return null;
            }
            result = shardedJedis.set(key, val);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    public String get(String key) {
        String result = null;
        try (ShardedJedis shardedJedis = redisDataSource.getShardedJedis()) {
            if (shardedJedis == null) {
                return null;
            }
            result = shardedJedis.get(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    public Long setnx(String key, String val) {
        Long result = null;
        try (ShardedJedis shardedJedis = redisDataSource.getShardedJedis()) {
            if (shardedJedis == null) {
                return null;
            }
            result = shardedJedis.setnx(key, val);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    public Long incr(String key) {
        Long result = null;
        try (ShardedJedis shardedJedis = redisDataSource.getShardedJedis()) {
            if (shardedJedis == null) {
                return null;
            }
            result = shardedJedis.incr(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    public Long rpush(String key, String val) {
        Long result = null;
        try (ShardedJedis shardedJedis = redisDataSource.getShardedJedis()) {
            if (shardedJedis == null) {
                return null;
            }
            result = shardedJedis.rpush(key, val);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    public Long lpush(String key, String val) {
        Long result = null;
        try (ShardedJedis shardedJedis = redisDataSource.getShardedJedis()) {
            if (shardedJedis == null) {
                return null;
            }
            result = shardedJedis.lpush(key, val);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    public List<String> lrange(String key, long start, long end) {
        List<String> result = null;
        try (ShardedJedis shardedJedis = redisDataSource.getShardedJedis()) {
            if (shardedJedis == null) {
                return null;
            }
            result = shardedJedis.lrange(key, start, end);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * trim the pre-list
     * @param key
     * @param start
     * @param end
     * @return
     */
    public String ltrim(String key, long start, long end) {
        String result = null;
        try (ShardedJedis shardedJedis = redisDataSource.getShardedJedis()) {
            if (shardedJedis == null) {
                return null;
            }
            result = shardedJedis.ltrim(key, start, end);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    public Long sadd(String key, String... value) {
        Long result = null;
        try (ShardedJedis shardedJedis = redisDataSource.getShardedJedis()) {
            if (shardedJedis == null) {
                return null;
            }
            result = shardedJedis.sadd(key,value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    public Long srem(String key, String... value) {
        Long result = null;
        try (ShardedJedis shardedJedis = redisDataSource.getShardedJedis()) {
            if (shardedJedis == null) {
                return null;
            }
            result = shardedJedis.srem(key,value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    public Set<String> smembers(String key) {
        Set<String> result = null;
        try (ShardedJedis shardedJedis = redisDataSource.getShardedJedis()) {
            if (shardedJedis == null) {
                return null;
            }
            result = shardedJedis.smembers(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }


}
