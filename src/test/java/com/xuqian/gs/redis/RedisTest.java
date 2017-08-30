package com.xuqian.gs.redis;

import com.sun.org.apache.regexp.internal.RE;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedisTest {

    private final static int TOTAL_USER_COUNT = 1000000;
    private final static String HOST = "127.0.0.1";
    private final static int PORT = 6379;

    public static void main(String[] args) {
        RedisTest redisTest = new RedisTest();
        redisTest.testSegmentHash();
    }

    public void testString() {
        int mBatchSize = 2000;
        Jedis jedis = null;
        try {
            jedis = new Jedis(HOST, PORT);
            List<String> kvsList = new ArrayList<String>(mBatchSize);

            System.out.println("start time " + System.currentTimeMillis());
            long start = System.currentTimeMillis();
            for (int i = 1; i <= TOTAL_USER_COUNT; i++) {
                String key = "u:" + i;
                kvsList.add(key);
                String value = "v:" + i;
                kvsList.add(value);
                if (i % mBatchSize == 0) {
                    jedis.mset(kvsList.toArray(new String[kvsList.size()]));
                    kvsList = new ArrayList<String>(mBatchSize);
                }
            }
            System.out.println("end time " + (System.currentTimeMillis() - start));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void testHash() {
        int mBatchSize = 2000;
        String hashKey = "allUser";
        Jedis jedis = null;
        try {
            jedis = new Jedis(HOST, PORT);
            Map<String, String> kvMap = new HashMap<String, String>();
            for (int i = 1; i <= TOTAL_USER_COUNT; i++) {
                String key = "u:" + i;
                String value = "v:" + i;
                kvMap.put(key, value);
                if (i % mBatchSize == 0) {
                    System.out.println(i);
                    jedis.hmset(hashKey, kvMap);
                    kvMap = new HashMap<String, String>();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void testSegmentHash() {
        int segment = 100;
        Jedis jedis = null;
        try {
            jedis = new Jedis(HOST, PORT);
            Map<String, String> kvMap = new HashMap<String, String>();

            long start = System.currentTimeMillis();

            for (int i = 1; i <= TOTAL_USER_COUNT; i++) {
                String key = "f:" + String.valueOf(i % segment);
                String value = "v:" + i;
                kvMap.put(key, value);
                if (i % segment == 0) {
                    int hash = (i - 1) / segment;
                    jedis.hmset("u:" + String.valueOf(hash), kvMap);
                    kvMap = new HashMap<String, String>();
                }
            }

            System.out.println("end time " + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

}
