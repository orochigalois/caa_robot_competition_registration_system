package com.zts.robot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zts.robot.pojo.User;
import com.zts.robot.redis.RedisService;
import com.zts.robot.service.UserService;
import com.zts.robot.util.Tools;
import com.zts.robot.util.Verify;

@Controller
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private RedisService redisService;
	/**
	 * 查询出登录人信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/findLoginUser")
	@ResponseBody
	public Map<String, Object> findLoginUser(HttpServletRequest request, HttpServletResponse response,String uid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			User user=userService.findLoginUser(uid);
			resultMap.put("status", 0);
			resultMap.put("user", user);
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
	 * 修改登录人信息
	 * @param request
	 * @param response
	 * @param uid
	 * @return
	 */
	@RequestMapping("/updateLoginUser")
	@ResponseBody
	public Map<String, Object> updateLoginUser(HttpServletRequest request, HttpServletResponse response,User user) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if("01".equals(user.getDelflg()) && "admin".equals(user.getUid())){
				resultMap.put("status", 1);
				resultMap.put("errmsg", "该账户为总管理员，不能删除！");
				return resultMap;
			}
			userService.uodateLoginUser(user);
			
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
	 * 原密码校验，修改密码
	 * @param request
	 * @param response
	 * @param user
	 * @return
	 */
	@RequestMapping("/updatepassword")
	@ResponseBody
	public Map<String, Object> updatepassword(HttpServletRequest request, HttpServletResponse response,String uid,String oldpassword,String newpassword) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String password=Tools.getMd5Str(Tools.getMd5Str(oldpassword)+uid);
			User olduser=userService.findLoginUser(uid);
			if(!password.equals(olduser.getPassword())){//判断原密码是否正确
				resultMap.put("status", 1);
				resultMap.put("errmsg", "原密码不正确，请重新输入！");
			}else{
				User user=new User();
				newpassword=Tools.getMd5Str(Tools.getMd5Str(newpassword)+uid);//新密码
				user.setPassword(newpassword);
				user.setUid(uid);
				userService.uodateLoginUser(user);
				resultMap.put("status", 0);
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
	/**
	 * 查询出所有注册人或者管理员
	 * @param request
	 * @param response
	 * @param user
	 * @return
	 */
	@RequestMapping("/findAllregistered")
	@ResponseBody
	public Map<String, Object> findAllregistered(HttpServletRequest request, HttpServletResponse response,Integer iDisplayLength,
			Integer iDisplayStart) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str=request.getParameter("str");
			String sex=request.getParameter("sex");
			String school=request.getParameter("school");
			String departname=request.getParameter("departname");
			String roleflg=request.getParameter("roleflg");
			String status=request.getParameter("status");
			String name="";
			String phone="";
			String email="";
			if(str.indexOf("@")>0){
				str=str.substring(0,str.indexOf("@"));
				email=str;
				paramMap.put("email", email);
			}else if(Verify.CheckDigitalBegin(str)){
				phone=str;
				paramMap.put("phone", phone);
			} else if(str!=null && !"".equals(str)){
				name=str;
				paramMap.put("name", name);
			}
			paramMap.put("sex", sex);
			paramMap.put("status", status);
			paramMap.put("school", school);
			paramMap.put("departname", departname);
			paramMap.put("roleflg", roleflg);
			int totalSize = userService.findAllregisteredTotalSize(paramMap);
			if (iDisplayLength==null||iDisplayLength != -1) {
				paramMap.put("beginNo", iDisplayStart);
				paramMap.put("endNo", iDisplayLength);
			}
			
				List<Map<String,Object>> matchList=userService.findAllregistered(paramMap);
				resultMap.put("status", 0);
				resultMap.put("list",matchList);
				resultMap.put("iTotalRecords", totalSize);
			
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
	 * 添加管理员
	 * @param request
	 * @param response
	 * @param user
	 * @return
	 */
	@RequestMapping("/addUser")
	@ResponseBody
	public Map<String, Object> addUser(HttpServletRequest request, HttpServletResponse response,User user) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String uid=Tools.get32UUID();
			String password=Tools.getMd5Str(Tools.getMd5Str(user.getPassword())+uid);
			user.setUid(uid);
			user.setPassword(password);
			user.setDelflg("00");
			user.setCreatedate(Tools.getStamp(request));
			user.setStatus("00");
			userService.addUser(user,resultMap);
			
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
	 * 批量审批注册人
	 * @param request
	 * @param response
	 * @param user
	 * @return
	 */
	@RequestMapping("/uodateRegisteredUserBatch")
	@ResponseBody
	public Map<String, Object> uodateRegisteredUserBatch(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		try {
			String struid=request.getParameter("struid");
			String status=request.getParameter("status");
			if(struid==null || "".equals(struid)){
				resultMap.put("status", 1);
				resultMap.put("errmsg", "您选择的用户无法执行该操作！");
			}else{
				String[] uid=struid.split(",");
				paramMap.put("status", status);
				paramMap.put("uid", uid);
				userService.uodateRegisteredUserBatch(paramMap);
				resultMap.put("status", 0);
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
	/**
	 * 查询出不在当前赛项下的TC
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/findTCUser")
	@ResponseBody
	public Map<String, Object> findTCUser(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String rid=request.getParameter("rid");
			List<Map<String, Object>> list=userService.findTCUser(rid);
			resultMap.put("status", 0);
			resultMap.put("list", list);
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
	 * 查询当前登录人TC管理哪些赛项
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/findRaceByUid")
	@ResponseBody
	public Map<String, Object> findRaceByUid(HttpServletRequest request, HttpServletResponse response,Integer iDisplayLength, Integer iDisplayStart) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String uid=request.getParameter("uid");
			String mid=request.getParameter("mid");
			String rname=request.getParameter("rname");
			map.put("uid", uid);
			map.put("mid", mid);
			map.put("rname", rname);
			int totalSize = userService.findRaceByUidTotalSize(map);
			if (iDisplayLength==null||iDisplayLength != -1) {
				map.put("beginNo", iDisplayStart);
				map.put("endNo", iDisplayLength);
			}
			List<Map<String, Object>> list=userService.findRaceByUid(map);
			resultMap.put("status", 0);
			resultMap.put("list", list);
			resultMap.put("iTotalRecords", totalSize);
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
	 * 查询出注册人所报名的赛事
	 * @param request
	 * @param response
	 * @param iDisplayLength
	 * @param iDisplayStart
	 * @return
	 */
	@RequestMapping("/findMatchByUid")
	@ResponseBody
	public Map<String, Object> findMatchByUid(HttpServletRequest request, HttpServletResponse response,Integer iDisplayLength, Integer iDisplayStart) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String uid=request.getParameter("uid");
			List<Map<String, Object>> list=userService.findMatchByUid(uid);
			resultMap.put("status", 0);
			resultMap.put("list", list);
			
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
	

}
