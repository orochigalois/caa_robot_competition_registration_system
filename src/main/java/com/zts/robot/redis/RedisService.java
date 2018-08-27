package com.zts.robot.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zts.robot.util.MyProperties;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


/**
 * 
 */

@Component
public class RedisService {
	
	public static final Logger logger = Logger.getLogger(RedisService.class);
	@Autowired
	JsonRedisSeriaziler jsonRedisSeriaziler;

    /**
     * 通过key删除（字节）
     * @param key
     */
    public void del(byte [] key){
    	Jedis jedis = getJedis();
    	jedis.del(key);
    	close(jedis);
    }
    /**
     * 通过key删除
     * @param key
     */
    public void del(String key){
    	Jedis jedis = getJedis();
    	jedis.del(key);
    	close(jedis);
    }

    /**
     * 添加key value 并且设置存活时间(byte)
     * @param key
     * @param value
     * @param liveTime
     */
    public void set(byte [] key,byte [] value,int liveTime){
 
    	Jedis jedis = getJedis();
    	jedis.set(key, value);
    	jedis.expire(key,liveTime);
    	close(jedis);
    }
    /**
     * 添加key value 并且设置存活时间
     * @param key
     * @param value
     * @param liveTime
     */
    public void set(String key,String value,int liveTime){ 
    	Jedis jedis = getJedis();
    	jedis.set(key, value);
    	jedis.expire(key,liveTime);
    	close(jedis);
    }
    
    /**
     * 设置失效时间
     * @param key
     * @param value
     * @param liveTime
     */
    public void expire(String key, int liveTime){
 
    	Jedis jedis = getJedis(); 
    	jedis.expire(key,liveTime);
    	close(jedis);
    }
    
    /**
     * 添加key value
     * @param key
     * @param value
     */
    public void set(String key,String value){
 
    	Jedis jedis = getJedis();
    	jedis.set(key, value); 
    	close(jedis);
    }
    /**添加key value (字节)(序列化)
     * @param key
     * @param value
     */
    public void set(byte [] key,byte [] value){
    	 
    	Jedis jedis = getJedis();
    	jedis.set(key, value); 
    	close(jedis);
    }
    /**
     * 获取redis value (String)
     * @param key
     * @return
     */
    public String get(String key){
    	 
    	Jedis jedis = getJedis();
    	String value = jedis.get(key); 
    	close(jedis);
        return value;
    }
    /**
     * 获取redis value (byte [] )(反序列化)
     * @param key
     * @return
     */
    public byte[] get(byte [] key){
 
    	Jedis jedis = getJedis();
    	byte[] value = jedis.get(key); 
    	close(jedis);
        return value;
    }
    
    /**
     * 添加对象
     * @param key
     * @param o
     */
    public void set(String key,Object o){
    	String objectJson = jsonRedisSeriaziler.seriazileAsString(o);
    	//System.out.println("set object::"+objectJson);
    	Jedis jedis = getJedis();
    	jedis.set(key, objectJson);
    	close(jedis);
    }
    
    /**
     * 查找对象
     * @param key
     * @param o
     */
    public <T> T get(String key,Class<T> clazz){
 
    	Jedis jedis = getJedis();
    	String result = jedis.get(key); 
    	close(jedis);
    	return jsonRedisSeriaziler.deserializeAsObject(result,clazz);
    }
    

    /**
     * 通过正则匹配keys
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern){ 
    	Jedis jedis = getJedis();
    	Set<String> value = jedis.keys(pattern); 
    	close(jedis);
        return value;
    }

    /**
     * 检查key是否已经存在
     * @param key
     * @return
     */
    public boolean exists(String key){
    	Jedis jedis = getJedis();
    	boolean flag = jedis.exists(key);
    	close(jedis);
        return flag;
    }
    /**
     * 清空redis 所有数据
     * @return
     */
    public String flushDB(){
    	Jedis jedis = getJedis();
    	String value = jedis.flushDB();
    	close(jedis);
        return value;
    }
    /**
     * 查看redis里有多少数据
     */
    public long dbSize(){
    	Jedis jedis = getJedis();
    	long value = jedis.dbSize();
        close(jedis);
        return value;
    }
    /**
     * 检查是否连接成功
     * @return
     */
    public String ping(){
    	Jedis jedis = getJedis();
    	String value = jedis.ping();
        close(jedis);
        return value;
    }
    /**
     * 保存set对象数组
     * @param key
     * @param members
     * @return
     */
    public Long sadd(String key, String members){
    	Jedis jedis = getJedis();
    	Long num = jedis.sadd(key, members);
    	close(jedis);
    	return num;
    }
    /**
     * 判断 member 元素是否set集合 key 的成员
     * @param key
     * @param member
     * @return
     */
    public boolean sismember(String key, String member) {
		// TODO 自动生成的方法存根
    	Jedis jedis = getJedis();
		return jedis.sismember(key, member);
	}
    /**
     * 删除set对象数组
     * @param key
     * @param members
     * @return
     */
    public Long srem(String key, String members){
    	Jedis jedis = getJedis();
    	Long num = jedis.srem(key, members);
    	close(jedis);
    	return num;
    }
    /**
     * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。
     * @param key
     * @param members
     * @return
     */
    public long sadd(final String key, final String... members) {
    	Jedis jedis = getJedis();
    	long ret= jedis.sadd(key, members);    
    	close(jedis);
    	return ret;
    }
    /**
     * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。
     * @param key
     * @param members
     * @return
     */
    public Long srem(final String key, final String... members) {
    	Jedis jedis = getJedis();
    	long ret= jedis.srem(key, members);  
    	close(jedis);
    	return ret;
      }
    public Set<String> smembers(final String key) {
    	Jedis jedis = getJedis();
    	Set<String> set = jedis.smembers(key);
    	close(jedis);
        return set;
      }
    /**
     * 将结果保存到 destination 集合，而不是简单地返回结果集。
     * @param dstkey
     * @param keys
     * @return
     */
    public Long sinterstore(final String dstkey, final String... keys) {
    	Jedis jedis = getJedis();
    	long ret= jedis.sinterstore(dstkey, keys);
    	close(jedis);
    	return ret;    	
    }
    /**
     * 返回集合 key 的基数(集合中元素的数量)。
     * @param key
     * @return
     */
    public long scard(final String key) {
    	Jedis jedis = getJedis();
    	long ret= jedis.scard(key);    
    	close(jedis);
    	return ret;
    }
    /**
     * 获取一个jedis 客户端
     * @return
     */
    private Jedis getJedis(){

	  Jedis jedis  = null;  
      int count =0;  
      JedisPool pool = getPool();
      do{  
          try{   
              jedis = pool.getResource(); 
          } catch (Exception e) {
              e.printStackTrace();
          }  
          count++;  
      }while(jedis==null&&count<10);  
	  // System.out.println("========================get redis :::"+jedis.toString());
      return jedis;
    }
    
    private RedisService (){

    }
    
    public void close(Jedis jedis) {
       	try{
    		jedis.close();
    	}catch(Exception e){
    		logger.error("关闭jedis失败" +e.getMessage());
    		e.printStackTrace();
    	}
    	//System.out.println("**********close redis :::"+jedis.toString());
    }

    private static Map<String,JedisPool> maps  = new HashMap<String,JedisPool>(); 

    /** 
     * 获取连接池. 
     * @return 连接池实例 
     */  
    public static JedisPool getPool() {  
    	String ip= MyProperties.getKey("redis.host");
    	int port = Integer.parseInt(MyProperties.getKey("redis.port"));
        String key = ip+":" +port;
        JedisPool pool = null;
        if(!maps.containsKey(key)) {  
            JedisPoolConfig config = new JedisPoolConfig();  
            //config.setmaxsetMaxsActive(30);  
            config.setMaxTotal(Integer.parseInt(MyProperties.getKey("redis.maxTotal")));
            config.setMaxIdle(Integer.parseInt(MyProperties.getKey("redis.maxIdle")));
            config.setMaxWaitMillis(Integer.parseInt(MyProperties.getKey("redis.maxWaitMillis")));//10秒
            config.setTestOnBorrow(Boolean.valueOf(MyProperties.getKey("redis.testOnBorrow")));  
            config.setTestOnReturn(Boolean.valueOf(MyProperties.getKey("redis.testOnReturn")));
            int timeout = Integer.parseInt(MyProperties.getKey("redis.timeout"));
            try{    
                /** 
                 *如果你遇到 java.net.SocketTimeoutException: Read timed out exception的异常信息 
                 *请尝试在构造JedisPool的时候设置自己的超时值. JedisPool默认的超时时间是2秒(单位毫秒) 
                 */ 
            	 
                pool = new JedisPool(config, ip, port, timeout);  
                maps.put(key, pool);  
            } catch(Exception e) {  
                e.printStackTrace();  
            }  
        }else{  
            pool = maps.get(key);  
        }  
        return pool;  
    }  
}