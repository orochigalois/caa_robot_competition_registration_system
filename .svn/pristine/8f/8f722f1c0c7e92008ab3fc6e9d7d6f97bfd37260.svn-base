package com.zts.robot.util;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;

import net.sf.json.JSONObject;

public class Tools {
	
	public static JSONObject getUserJsonByCookie(HttpServletRequest request){
		Cookie cookie = CookieOperation.getCookieByName(request, "authId");
		JSONObject userJson = RedisUtil.get("USER"+(String)cookie.getValue(),JSONObject.class);//获取登录人信息
		return userJson;
		
	}
	
	
	/**
	 * 输出JSON字符串
	 * 
	 * @param response
	 * @param str
	 * @throws Exception
	 */
	public static void WriteString(HttpServletResponse response, String str)
			throws Exception {
		// HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json; charset=UTF-8");
		OutputStream out = response.getOutputStream();
		out.write(str.getBytes("UTF-8"));
		out.flush();
		out.close();
	}

	/**
	 * 检测字符串是否不为空(null,"","null")
	 * 
	 * @param s
	 * @return 不为空则返回true，否则返回false
	 */
	public static boolean notEmpty(String s) {
		return s != null && !"".equals(s) && !"null".equals(s);
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String getStringByDateParren(Date date, String parren) {
		SimpleDateFormat sdf = new SimpleDateFormat(parren);
		String str = sdf.format(date);
		return str;
	}

	/**
	 * 获取时间戳
	 * 
	 * @param request
	 * @return
	 */
	public static String getStamp(HttpServletRequest request) {
		String stamp = null;
		if (null != request.getParameter("stamp")
				&& !"".equals(request.getParameter("stamp"))) {
			stamp = request.getParameter("stamp");
		} else {
			stamp = getStringByDateAndTime(new Date());
		}
		return stamp;
	}

	/**
	 * 获取日期和时间字符串 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String getStringByDateAndTime(Date date) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = sdf.format(date);
		return str;
	}

	/**
	 * 年月日生成
	 * 
	 * @return //返回值：当前系统日期
	 */
	public static String dateStr() {
		java.util.Date dt = new java.util.Date(System.currentTimeMillis());
		SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
		String dateStr = date.format(dt);
		return dateStr;
	}

	/**
	 * 时间生成
	 * 
	 * @return //返回值：当前系统时间
	 */
	public static String timeStr() {
		java.util.Date dt = new java.util.Date(System.currentTimeMillis());
		SimpleDateFormat time = new SimpleDateFormat("HHmmss");
		String timeStr = time.format(dt);
		return timeStr;
	}

	public static String filenameStr() {
		Random random = new Random();
		int randnum = random.nextInt(900000) + 100000;
		String filenameStr = "0_" + timeStr() + randnum;
		return filenameStr;
	}

	/**
	 * 得到32位的uuid
	 * 
	 * @return
	 */
	public static String get32UUID() {
		return UUID.randomUUID().toString().trim().replaceAll("-", "");
	}

	/**
	 * 得到32位MD5加密码
	 * 
	 * @param str
	 * @return
	 */
	public static String getMd5Str(String str) {
		return DigestUtils.md5Hex(str);
	}

	/**
	 * 获得4位验证码
	 * 
	 * @return
	 */
	/*
	 * public static String getSMSCheckCode() { String str = ""; Random random =
	 * new Random(); // 制定输出的验证码为四位 for (int i = 0; i < 4; i++) { str +=
	 * random.nextInt(9); } return str; }
	 */

	/**
	 * 根据authId得到user信息
	 * 
	 * @param loginname
	 * @param tokenkey
	 * @return
	 * @throws Exception
	 *//*
	public static String getUserAuthId(String userid) throws Exception {
		String date = getStringByDateAndTime(new Date());
		String mysign = getToken(userid + date);
		return mysign;
	}*/

	/**
	 * 获取token值 *
	 * 
	 * @param wsc
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public static String getToken(String content) throws Exception {
		String tokenkey = MyProperties.getKey("tokenkey");
		// 对token进行加密
		String encryptResult = AESUtil.encrypt(content, tokenkey);
		return encryptResult;
	}

	/**
	 * 对authId进行解密
	 * 
	 * @param authId
	 * @param tokenkey
	 * @return
	 * @throws Exception
	 *//*
	public static String getUseridByAuthId(String authId) throws Exception {
		try {
			String auth = authId.replace(' ', '+');
			String tokenkey = MyProperties.getKey("tokenkey");
			String encryptResult = AESUtil.decrypt(auth, tokenkey);

			
			 * if(encryptResult != null && encryptResult.length()>32){ return
			 * encryptResult.substring(0, 32); }
			 
			if (encryptResult != null) {
				return encryptResult.substring(0, encryptResult.length() - 19);
			}

		} catch (Exception e) {
			System.out.println(1);
			return null;
		}
		return null;

	}*/

	/**
	 * 获取 userjson
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	/*
	 * public static JSONObject getuserJson(HttpServletRequest request) throws
	 * Exception{ String userid =
	 * getLoginphoneByAuthId(request.getParameter("authId")); JSONObject
	 * userJSON = JSONObject.fromObject(); return userJSON; }
	 */

	public static String getStringByDate(Date date) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(date);
		return str;
	}

	/**
	 * 时间加减计算 n n年 n月 n日 n时 （可为负数，减） type 00年 01月 02日 03时 formaType
	 * 00("yyyy-MM-dd") 01("yyyy-MM-dd HH:mm:ss")
	 * 
	 * @param n
	 * @param type
	 * @param formaType
	 * @return
	 */
	public static String getNextDay(int n, String type, String formaType) {
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		if ("00".equals(type)) {// YEAR
			calendar.add(Calendar.YEAR, n);
		} else if ("01".equals(type)) {// MONTH
			calendar.add(Calendar.MONTH, n);
		} else if ("02".equals(type)) {// DATE
			calendar.add(Calendar.DATE, n);
		} else if ("03".equals(type)) {// HOUR
			calendar.add(Calendar.HOUR, n);
		}
		String nextDay = null;
		if ("00".equals(formaType)) {
			// yyyy-MM-dd
			nextDay = getStringByDate(calendar.getTime());
		} else if ("01".equals(formaType)) {
			// yyyy-MM-dd HH:mm:ss
			nextDay = getStringByDateAndTime(calendar.getTime());
		}
		return nextDay;
	}

	/**
	 * 生成n位随机数
	 * 
	 * @return
	 */
	public static String getRandom(int n) {
		String str = "";
		for (int i = 0; i < n; i++) {
			str += (int) (Math.random() * 10);
		}
		return str;
	}

	public static boolean isNumeric(String str) {
		if ("".equals(str)) {
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断是否为空字符串
	 * @param str
	 * @return
	 */
	public static boolean isNotNullStr(String str) {
		return null != str && !str.trim().equals("") ? true : false;
	}
}
