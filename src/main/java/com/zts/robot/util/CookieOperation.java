package com.zts.robot.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * 设置Cookie时，name=URLEncoder.encode(name,"UTF-8");

读取Cookie时，name=URLDecoder.decode(name,"UTF-8");
 * */

public class CookieOperation {
	
	public static void addCookie(HttpServletResponse response, String name,
			String value, int maxAge) throws UnsupportedEncodingException {
		if (!Tools.isNotNullStr(value)) {
			value = "";
		}
		Cookie cookie = new Cookie(URLEncoder.encode(name,"UTF-8"), URLEncoder.encode(value,"UTF-8"));
		cookie.setPath("/");
		//cookie.setVersion(3);
		if (maxAge > 0) {
			cookie.setMaxAge(maxAge);
		}
		response.addCookie(cookie);
		
	}
	public static String delCookieByName(HttpServletRequest request, HttpServletResponse response, String name){
		Map<String, Cookie> cookieMap = ReadCookieMap(request);
		if (cookieMap.containsKey(name)) {
			Cookie cookie = (Cookie) cookieMap.get(name);
			cookie.setMaxAge(0);
			cookie.setValue(null);
			cookie.setPath("/");
			response.addCookie(cookie);
			return "success";
		} else {
			return "error";
		}
	}
	
	
	public static Cookie getCookieByName(HttpServletRequest request, String name) {
		Map<String, Cookie> cookieMap = ReadCookieMap(request);
		if (cookieMap.containsKey(name)) {
			Cookie cookie = (Cookie) cookieMap.get(name);
			return cookie;
		} else {
			return null;
		}
	}

	public static Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
		Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie);
			}
		}
		return cookieMap;
	}
}
