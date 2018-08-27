package com.zts.robot.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

public class Verify {
	
	private static Logger logger = Logger.getLogger(Verify.class);
	
	public static boolean CheckJSONKey(JSONObject obj, String key) {
		if (!obj.containsKey(key)) {
			logger.debug("Key [" + key +"] is missing, please input this parameter in json.");
			return false;
		}
		return true;
	}

	/**
	 * 字符串是否以数字开头 ，true数字开头 ， 
	 * @param str
	 * @return 
	 */
	public static boolean CheckDigitalBegin(String str) {
		
		Pattern pattern = Pattern.compile("^(\\d+)(.*)");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	/**
	 * 校验参数
	 * @param request
	 * @param name
	 * @return
	 */
	public static boolean CheckParam(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		if (value == null || value =="") {
			return false;
		} else {
			return true;
		}
	}

}
