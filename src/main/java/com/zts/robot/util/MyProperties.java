package com.zts.robot.util;

import java.io.IOException;

//import org.apache.axis.encoding.Base64;

public class MyProperties {

	private static PropertiesHelper helper;

	/**
	 * 初始化
	 * 
	 * @param list
	 */
	public static void init() {
		try {
			helper = new PropertiesHelper("./param.properties");

		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}

	/**
	 * 获取key值
	 * 
	 * @param key
	 * @return
	 */
	public static String getKey(String key) {
		return helper.getProperties(key).toString();
	}

	/**
	 * 重置对应Key
	 * 
	 * @param key
	 * @param value
	 * @throws IOException
	 */
	public static void resetKey(String key, String value) throws IOException {
		helper.setProperties(key, value);
		helper.save();
	}
}