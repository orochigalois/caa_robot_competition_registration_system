package com.zts.robot.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zts.robot.pojo.Match;
import com.zts.robot.service.SignOnSiteService;

/**
 * 现场签到确认
 * 
 * @author ZTS_Q
 *
 */
@Controller
public class SignOnSiteController {
	@Autowired
	private SignOnSiteService signOnSiteController;

	/**
	 * 赛事现场GPS 1由二维码获取赛事现场确认坐标
	 * 
	 * @return
	 */
	@RequestMapping("/findGpsByMid")
	@ResponseBody
	public Map<String, Object> findGpsByMid(String mid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Match match=signOnSiteController.findGpsByMid(mid);
				resultMap.put("status", 0);
				resultMap.put("match", match);	

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", 1);
			resultMap.put("errmsg", "系统异常！：" + e.getMessage());
		}
		return resultMap;

	}

	/**
	 * 发送验证码 1输入身份证号 2查询用户邮箱，发送验证码
	 * 
	 * @return
	 */
	@RequestMapping("/sendCheckCodeEmail")
	@ResponseBody
	public Map<String, Object> sendCheckCodeEmail(String didtype, String did, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String ip = request.getRemoteAddr();
			String mid=request.getParameter("mid");
			Map<String, Object> memberMap = signOnSiteController.sendCheckCodeEmail(didtype, did, ip,mid);
			if (memberMap == null) {
				resultMap.put("status", 1);
				resultMap.put("errmsg", "未找到对应的参赛人员，请核实填写信息！");
			} else {
				resultMap.put("status", 0);
				resultMap.put("info", memberMap);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
			resultMap.put("errmsg", "系统异常！：" + e.getMessage());
		}
		return resultMap;

	}

	/**
	 * 现场确认 1、获取邮件、验证码 2、校验验证码 3、验证码正确，更改成员表用户状态
	 * 
	 * @return
	 */
	@RequestMapping("/signOnSite")
	@ResponseBody
	public Map<String, Object> signOnSite(String email, String checkcode, String tmid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			int status = signOnSiteController.signOnSite(email, checkcode, tmid);
			if (status == 1) {
				resultMap.put("status", 1);
				resultMap.put("errmsg", "验证码错误");
			} else {
				resultMap.put("status", 0);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultMap.put("status", 1);
			resultMap.put("errmsg", "系统异常！：" + e.getMessage());
		}
		return resultMap;
	}
	
	@RequestMapping("/signOnSiteNew")
	@ResponseBody
	public Map<String, Object> signOnSiteNew(String didtype, String did,String mid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		signOnSiteController.signOnSiteNew(didtype,did,resultMap,mid);
		return resultMap;
	}
	
}
