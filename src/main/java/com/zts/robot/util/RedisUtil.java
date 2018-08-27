package com.zts.robot.util;



import java.util.Set;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.zts.robot.redis.RedisService;


/**
 * Redis辅助类
 * @author zts
 *
 */
public class RedisUtil {
	
	private static RedisService redisService;
	
	public static void init(ServletContext servletContext) {

		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		redisService=(RedisService)ctx.getBean("redisService");
	}
	
	
	 /**
     * 通过key删除（字节）
     * @param key
     */
    public static void del(byte [] key){
    	redisService.del(key);
    }
    /**
     * 通过key删除
     * @param key
     */
    public static void del(String key){
    	redisService.del(key);
    }
    /**
     * 判断 member 元素是否set集合 key 的成员
     * @param key
     * @param member
     * @return
     */
    public static boolean sismember(String key,String member){
    	return redisService.sismember(key, member);
    }
    /**
     * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。
     * @param key
     * @param members
     * @return
     */
    public static long sadd(final String key, final String... members){
    	return redisService.sadd(key, members);
    	
    }
    
    /**
     * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。
     * @param key
     * @param members
     * @return
     */
    public static Long srem(final String key, final String... members) {
    	return redisService.srem(key, members);
      }
    
    public static Set<String> smembers(final String key) {
    	
        return redisService.smembers(key);
      }
    /**
     * 将结果保存到 destination 集合，而不是简单地返回结果集。
     * @param dstkey
     * @param keys
     * @return
     */
    public static Long sinterstore(final String dstkey, final String... keys) {
    	
    	return redisService.sinterstore(dstkey, keys);    	
    }
    /**
     * 返回集合 key 的基数(集合中元素的数量)。
     * @param key
     * @return
     */
    public static long scard(final String key){
    	return redisService.scard(key);
    	
    }
    /**
     * 添加key value 并且设置存活时间(byte)
     * @param key
     * @param value
     * @param liveTime
     */
    public static void set(byte [] key,byte [] value,int liveTime){
    	redisService.set(key, value, liveTime);
    }
    /**
     * 添加key value 并且设置存活时间
     * @param key
     * @param value
     * @param liveTime
     */
    public static void set(String key,String value,int liveTime){
    	redisService.set(key, value, liveTime);
    }
    /**
     * 添加key value
     * @param key
     * @param value
     */
    public static void set(String key,String value){
    	redisService.set(key, value);
    }
    /**添加key value (字节)(序列化)
     * @param key
     * @param value
     */
    public static void set(byte [] key,byte [] value){
    	redisService.set(key, value);
    }
    /**
     * 获取redis value (String)
     * @param key
     * @return
     */
    public static String get(String key){
        return redisService.get(key);
    }
    /**
     * 获取redis value (byte [] )(反序列化)
     * @param key
     * @return
     */
    public static byte[] get(byte [] key){
        return redisService.get(key);
    }
    
    /**
     * 添加对象
     * @param key
     * @param o
     */
    public static void set(String key,Object o){
    	redisService.set(key, o);
    }
    
    /**
     * 查找对象
     * @param key
     * @param o
     */
    public static <T> T get(String key,Class<T> clazz){
    	return redisService.get(key, clazz);
    }
    

    /**
     * 通过正则匹配keys
     * @param pattern
     * @return
     */
    public static Set<String> keys(String pattern){
        return redisService.keys(pattern);
    }

    /**
     * 检查key是否已经存在
     * @param key
     * @return
     */
    public static boolean exists(String key){
        return redisService.exists(key);
    }
    /**
     * 清空redis 所有数据
     * @return
     */
    public static String flushDB(){
        return redisService.flushDB();
    }
    /**
     * 查看redis里有多少数据
     */
    public static long dbSize(){
        return redisService.dbSize();
    }
    /**
     * 检查是否连接成功
     * @return
     */
    public static String ping(){
        return redisService.ping();
    }
    /**
     * 失效时间
     * @return
     */
    public static void expire(String key, int time){
        redisService.expire(key, time);
    }
    
}
