package com.example.common.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Repository("redisDataSource")
@Slf4j
public class RedisDataSource {

    @Autowired
    private ShardedJedisPool shardedJedisPool;

    public ShardedJedis getShardedJedis(){
        try {
            return shardedJedisPool.getResource();
        } catch (Exception e) {
            log.error("fail to get shardedJedisPool", e);
        }
        return null;
    }

    public void returnResource(ShardedJedis shardedJedis){
        shardedJedisPool.returnResource(shardedJedis);
    }


}
