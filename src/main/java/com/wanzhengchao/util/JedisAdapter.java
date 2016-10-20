package com.wanzhengchao.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanzhengchao.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * Created by Administrator on 16.10.18.
 */
@Service
public class JedisAdapter implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    private JedisPool pool;

    public static void print(int index, Object obj) {
        System.out.println(String.format("%d,%s", index, obj));
    }


    public static void main(String[] args) {
        Jedis jedis = new Jedis("redis://localhost:6379/2");
        jedis.flushDB();
        jedis.set("hello", "world");
        print(1, jedis.get("hello"));

        jedis.rename("hello", " newhello");
        print(1, jedis.get("newhello"));

        jedis.setex("hello", 20, "world");

        //
        jedis.set("pv", "100");
        jedis.incr("pv");
        jedis.incrBy("pv", 5);

        print(2, jedis.get("pv"));
        jedis.decrBy("pv", 3);
        jedis.decr("pv");
        print(2, jedis.get("pv"));

        print(3, jedis.keys("*"));

        String list = "list";
        jedis.del(list);
        for (int i = 0; i < 10; i++) {
            jedis.lpush(list, "a" + i);
        }
        print(4, jedis.lrange(list, 0, 12));

        print(5, jedis.llen(list));
        print(6, jedis.lpop(list));
        print(7, jedis.llen(list));

        print(10, jedis.linsert(list, BinaryClient.LIST_POSITION.BEFORE, "a7", "xxx"));
        print(4, jedis.lrange(list, 0, 12));


        //hash
        String userKey = "userx";
        jedis.hset(userKey, "name", "wan");
        jedis.hset(userKey, "age", "24");
        jedis.hset(userKey, "phone", "132423143124");
        print(12, jedis.hget(userKey, "name"));
        print(12, jedis.hgetAll(userKey));

        String likeKey = "like1";
        String likeKey2 = "like2";

        for (int i = 0; i < 5; i++) {
            jedis.sadd(likeKey, String.valueOf(i));
            jedis.sadd(likeKey2, "" + i * i);
        }
        print(20, jedis.smembers(likeKey));
        print(220, jedis.sismember(likeKey, "4"));
        print(20, jedis.sunion(likeKey2, likeKey));
        print(20, jedis.sinter(likeKey2, likeKey));


        User user = new User();
        user.setPassword("pp");
        user.setSalt("22");
        user.setHeadUrl("a.png");
        user.setName("1234");
        user.setId(1);

        jedis.set("user1", JSONObject.toJSONString(user));
        print(46, JSONObject.toJSONString(user));

        String value = jedis.get("user1");
        User user2 = JSON.parseObject(value, User.class);
        print(47, user2);


    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("redis://localhost:6379/10");
    }

    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("redis error");
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }
    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("redis error");
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }
    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("redis error");
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }
    public boolean ismember(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("redis error");
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }
}
