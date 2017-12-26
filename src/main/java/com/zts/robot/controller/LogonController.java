package com.zts.robot.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sun.mail.util.MailConnectException;
import com.zts.robot.pojo.User;
import com.zts.robot.redis.RedisService;
import com.zts.robot.service.LogonService;
import com.zts.robot.util.CookieOperation;
import com.zts.robot.util.RedisUtil;
import com.zts.robot.util.Tools;

@Controller
public class LogonController {
	@Autowired
	private LogonService service;
	@Autowired
	private RedisService redisService;
	
	/**
	 * 邮箱登陆
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/LogonByEmail")
	@ResponseBody
	public Map<String, Object> LogonByEmail(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {

			String email = request.getParameter("email");			
			Map<String, Object> userMap = service.LogonByEmail(email);
			String pass=request.getParameter("password");
			if (userMap != null) {
				if("02".equals(userMap.get("status"))){
					resultMap.put("status", 1);
					resultMap.put("errmsg", "该用户已被禁用，不允许登录！");
				}else{
					String password =Tools.getMd5Str(Tools.getMd5Str(pass)+userMap.get("uid"));
					if (!userMap.get("password").equals(password)) {
						resultMap.put("status", 1);
						resultMap.put("errmsg", "用户密码不正确");
					} else {
						resultMap.put("status", 0);
						resultMap.put("info", userMap);
						String uname = userMap.get("uname").toString();
						//存入redis
						String authId = Tools.get32UUID();
						redisService.set("USER"+authId, userMap);
						redisService.expire("USER"+authId, 7200);
						//前台设置cookie
						CookieOperation.addCookie(response, "authId", authId, 0);
						CookieOperation.addCookie(response, "uname", uname, 0);
						CookieOperation.addCookie(response, "uid", userMap.get("uid").toString(), 0);
						resultMap.put("status", 0);
					}
				}
				
			} else {
				resultMap.put("status", 1);
				resultMap.put("errmsg", "用户不存在");
			}
		} catch (CannotCreateTransactionException e) {
			resultMap.put("status", 1);
			resultMap.put("errmsg", "数据库连接异常");
			e.printStackTrace();
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/*@RequestMapping("/LogonByCookie")
	@ResponseBody
	public Map<String, Object> LogonByCookie(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			
			String email = request.getParameter("email");			
			Map<String, Object> userMap = service.LogonByEmail(email);
			String pass=request.getParameter("password");
			if (userMap != null) {
				if("02".equals(userMap.get("status"))){
					resultMap.put("status", 1);
					resultMap.put("errmsg", "该用户已被禁用，不允许登录！");
				}else{
					String password =Tools.getMd5Str(Tools.getMd5Str(pass)+userMap.get("uid"));
					if (!userMap.get("password").equals(password)) {
						resultMap.put("status", 1);
						resultMap.put("errmsg", "用户密码不正确");
					} else {
						resultMap.put("status", 0);
						resultMap.put("info", userMap);
						String uname = userMap.get("uname").toString();
						//存入redis
						String authId = Tools.get32UUID();
						redisService.set("USER"+authId, userMap);
						redisService.expire("USER"+authId, 7200);
						//前台设置cookie
						CookieOperation.addCookie(response, "authId", authId, 0);
						CookieOperation.addCookie(response, "uname", uname, 0);
						CookieOperation.addCookie(response, "uid", userMap.get("uid").toString(), 0);
						resultMap.put("status", 0);
					}
				}
				
			} else {
				resultMap.put("status", 1);
				resultMap.put("errmsg", "用户不存在");
			}
		} catch (CannotCreateTransactionException e) {
			resultMap.put("status", 1);
			resultMap.put("errmsg", "数据库连接异常");
			e.printStackTrace();
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}*/
	
	/**
	 * 退出登录
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/logout")
	@ResponseBody
	private Map<String, Object> logout(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			
			String authId = CookieOperation.getCookieByName(request, "authId")
					.getValue();
			redisService.del("USER" + authId);
			CookieOperation.delCookieByName(request, response, "authId");
			CookieOperation.delCookieByName(request, response, "uname");
			CookieOperation.delCookieByName(request, response, "uid");
			resultMap.put("status", 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 注册用户
	 * @param request
	 * @param response
	 * @param user
	 * @return
	 */
	@RequestMapping("/signUser")
	@ResponseBody
	public Map<String, Object> signUser(HttpServletRequest request, HttpServletResponse response, User user,String code) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String uid=Tools.get32UUID();
			user.setUid(uid);
			service.signUser(user,resultMap,code);			
		} catch (CannotCreateTransactionException e) {
			resultMap.put("status", 1);
			resultMap.put("errmsg", "数据库连接异常");
			e.printStackTrace();
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	/**
	 * 忘记密码
	 * @param request
	 * @param response
	 * @param user
	 * @return
	 */
	@RequestMapping("/updateUserpassword")
	@ResponseBody
	public Map<String, Object> updateUserpassword(HttpServletRequest request, HttpServletResponse response, User user) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			service.updateUserpassword(user);
			resultMap.put("status", 0);
		} catch (CannotCreateTransactionException e) {
			resultMap.put("status", 1);
			resultMap.put("errmsg", "数据库连接异常");
			e.printStackTrace();
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 指定邮箱发送验证码
	 * @param request
	 * @param response
	 * @param email
	 * @return
	 */
	@RequestMapping("/sendCheckCodeToEmail")
	@ResponseBody
	public Map<String, Object> sendCheckCodeToEmail(HttpServletRequest request, HttpServletResponse response, String email,String registerflg) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if("00".equals(registerflg)){
				int count= service.finduser(email);
				if(count>0){
					resultMap.put("status", 1);
					resultMap.put("errmsg", "此邮箱已经存在，不允许注册！");	
					return resultMap;
				}
			}			
			String ip = request.getRemoteAddr();
			service.sendCheckCodeToEmail(email, ip);
			resultMap.put("status", 0);
		} catch (CannotCreateTransactionException e) {
			resultMap.put("status", 1);
			resultMap.put("errmsg", "数据库连接异常");
			e.printStackTrace();
		} catch (MailConnectException e) {
			resultMap.put("status", 1);
			resultMap.put("errmsg", "网络异常，找不到邮件服务器");
		} catch (Exception e) {
			resultMap.put("status", 1);
			resultMap.put("errmsg", "网络异常");
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 验证邮箱验证码
	 * @param request
	 * @param response
	 * @param email
	 * @return
	 */
	@RequestMapping("/verifyCheckCode")
	@ResponseBody
	public Map<String, Object> verifyCheckCode(HttpServletRequest request, HttpServletResponse response, String email, String code) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if(service.verifyCheckCode(email, code)) {
				resultMap.put("status", 0);
			} else {
				resultMap.put("status", 1);
				resultMap.put("errmsg", "没有可用的验证码");
			}
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	/**
	 * 验证邮箱验证码并修改密码
	 * @param request
	 * @param response
	 * @param email
	 * @return
	 */
	@RequestMapping("/verifyCheckCodeAndUpdatePsw")
	@ResponseBody
	public Map<String, Object> verifyCheckCodeAndUpdatePsw(HttpServletRequest request, HttpServletResponse response, String email, String code,String psw) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if(service.verifyCheckCodeAndUpdatePsw(email, code,psw)) {
				resultMap.put("status", 0);
			} else {
				resultMap.put("status", 1);
				resultMap.put("errmsg", "没有可用的验证码");
			}
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	
	@RequestMapping("/updatedefaultUserStatusByRedis")
	@ResponseBody
	public Map<String, Object> updatedefaultUserStatusByRedis(String defaultUserStatus) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			RedisUtil.set("sys_defaultUserStatus", defaultUserStatus);			
			resultMap.put("status", 0);
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	
	@RequestMapping("/getdefaultUserStatusByRedis")
	@ResponseBody
	public Map<String, Object> getdefaultUserStatusByRedis() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String defaultUserStatus= RedisUtil.get("sys_defaultUserStatus");
			resultMap.put("status", 0);
			resultMap.put("info", defaultUserStatus);
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
}
