package com.example.common.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.ShardedJedis;

@Slf4j
@Repository(value = "redisCache")
public class RedisCache {

    @Autowired
    private RedisDataSource redisDataSource;


    /**
     * 集群模式下自动选择0号数据库
     * @param key
     * @param val
     * @return
     */
    public String set(String key, String val) {
        String result = null;
        try (ShardedJedis shardedJedis = redisDataSource.getShardedJedis()) {
            if(shardedJedis == null){
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
            if(shardedJedis == null){
                return null;
            }
            result = shardedJedis.get(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    public Long setnx(String key,String val) {
        Long result = null;
        try (ShardedJedis shardedJedis = redisDataSource.getShardedJedis()) {
            if(shardedJedis == null){
                return null;
            }
            result = shardedJedis.setnx(key,val);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }



}
