package com.zts.robot.redis;

import java.nio.charset.Charset;

import org.apache.commons.lang.SerializationException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
* 不使用sdr自带的json序列化工具，一切操作基于string
**/

@Component
public class JsonRedisSeriaziler{
	public static final String EMPTY_JSON = "{}";
	
	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	
	protected ObjectMapper objectMapper = new ObjectMapper();
	public JsonRedisSeriaziler(){}
	
	/**
	 * java-object as json-string
	 * @param object
	 * @return
	 */
	public String seriazileAsString(Object object){
		if (object== null) {
			return EMPTY_JSON;
		}
		try {
			return this.objectMapper.writeValueAsString(object);
		} catch (Exception ex) {
			throw new SerializationException("Could not write JSON: " + ex.getMessage(), ex);
		}
	} 
	
	/**
	 * json-string to java-object
	 * @param str
	 * @return
	 */
	public <T> T deserializeAsObject(String str,Class<T> clazz){
		if(str == null || clazz == null){
			return null;
		}
		try{
			return this.objectMapper.readValue(str, clazz);
		}catch (Exception ex) {
			throw new SerializationException("Could not write JSON: " + ex.getMessage(), ex);
		}
	}

}