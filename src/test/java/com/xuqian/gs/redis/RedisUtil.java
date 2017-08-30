//package com.xuqian.gs.redis;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Properties;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.JedisCluster;
//import redis.clients.jedis.JedisPool;
//import redis.clients.jedis.JedisPoolConfig;
//
//
//
///**
// * @author lzr
// * @date 2016.6.7
// * 解决注入JedisPool导致的连接不释放引发的无法从连接池获取资源
// */
//
//public class RedisUtil {
////	public static BinaryJedisCluster jc;
//	public static JedisCluster jc = null;
//
//	private static final Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);
//
//	public static List<String> hostL = new ArrayList<String>();
//	public static List<Integer> portL= new ArrayList<Integer>();
//	public static final String HOST_STR = "redis.hostname";
//	public static final String PORT_STR = "redis.port";
//
//	private static int MAX_ACTIVE = 10240;
//
//	    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
//    private static int MAX_IDLE = 200;
//
//	    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
//	private static int MAX_WAIT = 10000;
//
//	private static int TIMEOUT = 60;
//
//	//在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
//	private static boolean TEST_ON_BORROW = true;
//
//	private static JedisPool jedisPool = null;
//
//	public static String REDIS_URL = "";
//
//	public static String REDIS_PORT = "";
//
//	/*static{
//		Properties prop = null;
//		try {
//			prop = ProperytiesUtil.loadProperties("conf/redis.properties");
//		} catch (Exception e) {
//
//		}
//		if (prop != null) {
//			int i =0;
//			while(prop.getProperty(HOST_STR+i)!=null){
//				hostL.add(prop.getProperty(HOST_STR+i));
//				portL.add(Integer.parseInt(prop.getProperty(PORT_STR+i)));
//				i++;
//			}
//		}
//	}*/
//
//	/**
//	 * 初始化Redis连接池
//
//	public static JedisCluster getJedisCluster() {
//		while(jc == null){
//			try {
//				Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
//				for(int i =0;i<hostL.size();i++){
//					jedisClusterNodes.add(new HostAndPort(hostL.get(i),portL.get(i)));
//				}
////		        jc = new BinaryJedisCluster(jedisClusterNodes);
//				jc = new JedisCluster(jedisClusterNodes,5000,1000);
////		        jc = new JedisCluster(jedisClusterNodes);
//				 //System.out.println("ShardedJedisPool  inited ");
//		        } catch (Exception e) {
//		            e.printStackTrace();
//		       }
//	     }
//		return jc;
//	}	*/
//
//	private static JedisPoolConfig config;
//
//	static {
//		 config = new JedisPoolConfig();
//         config.setMaxIdle(MAX_IDLE);
//         config.setMaxWaitMillis(MAX_WAIT);
//         config.setTestOnBorrow(TEST_ON_BORROW);
//         config.setMaxTotal(MAX_ACTIVE);
//         LOGGER.info("mat active:"+config.getMaxTotal());
//
//        initRedis();
//    }
//
//	private static void initRedis() {
//		 LOGGER.info("init redis！！！！！！！！");
//		try {
//        	Properties prop = null;
//    		try {
//    			prop = ProperytiesUtil.loadProperties("conf/redis.properties");
//    			REDIS_URL = prop.getProperty(HOST_STR);
//    			REDIS_PORT = prop.getProperty(PORT_STR);
//    			System.out.println("REDIS_URL:"+REDIS_URL+"===REDIS_PORT:"+REDIS_PORT);
//    		} catch (Exception e) {
//
//    		}
////            jedisPool = new JedisPool(config, "192.168.1.185", 6379, TIMEOUT);
//            jedisPool = new JedisPool(config, REDIS_URL, Integer.parseInt(REDIS_PORT), TIMEOUT);
////            jedisPool = new JedisPool(config, prop.getProperty(HOST_STR),
////            		Integer.valueOf(prop.getProperty(PORT_STR)), TIMEOUT);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//	}
//
//    /**
//     * 获取Jedis实例
//     * @return
//     */
//    public synchronized static Jedis getJedis() {
//    	Jedis resource = null;
//    	while(resource == null){
//	        try {
//	            if (jedisPool != null) {
//	            	resource = jedisPool.getResource();
//	            }
//	            else {
//	            	LOGGER.info("jedisPool null!!!!!!");
//	            	initRedis();
//	            	resource = jedisPool.getResource();
//	            }
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	            initRedis();
//	        	resource = jedisPool.getResource();
//	        }
//    	}
//		return resource;
//    }
//
//    /**
//     * 释放jedis资源
//     * @param jedis
//     */
//    public static void returnResource(final Jedis jedis) {
//
//
//    	if (jedis != null) {
//            jedisPool.returnResource(jedis);
//            jedis.getClient().close();
//        }
//    }
//
//
//
//	public static void returnJedisClusterResource(JedisCluster jc) {
//		if(jc!=null){
//			try {
//					jc.close();
//				}
//			catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//			}
//		}
//	}
//}
//
