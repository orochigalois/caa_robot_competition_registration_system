package com.zts.robot.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.mail.util.MailConnectException;
import com.zts.robot.mail.MailService;
import com.zts.robot.mapper.CheckCodeMapper;
import com.zts.robot.mapper.UserMapper;
import com.zts.robot.pojo.CheckCode;
import com.zts.robot.pojo.User;
import com.zts.robot.util.RedisUtil;
import com.zts.robot.util.Tools;

@Service
public class LogonService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private CheckCodeMapper checkCodeMapper;
	
	public Map<String, Object> LogonByEmail(String email) {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("email", email);
		return userMapper.LogonByEmail(condition);
	}

	public void signUser(User user, Map<String, Object> resultMap, String code) {
		int count=userMapper.finduser(user.getEmail());
		if(count>0){
			resultMap.put("status", 1);
			resultMap.put("errmsg", "此邮箱已经存在，不允许注册！");
		}else{
			Integer cnt = checkCodeMapper.findcode(user.getEmail(), code);
			if (cnt > 0) {
				String createdate = Tools.getStringByDateAndTime(new Date());
				String password=Tools.getMd5Str(Tools.getMd5Str(user.getPassword())+user.getUid());//密码MD5加密然后加上uid,在进行md5加密
				user.setPassword(password);
				user.setCreatedate(createdate);
				user.setDelflg("00");				
				user.setStatus(RedisUtil.get("sys_defaultUserStatus"));
				user.setRoleflg("03");
				userMapper.insertSelective(user);
				resultMap.put("status", 0);
			}else{
				resultMap.put("status", 1);
				resultMap.put("errmsg", "没有可用的验证码！");
			}
		}
		
		
		
	}

	//设置随机码
	public void sendCheckCodeToEmail(String email, String ip) throws MailConnectException {
		
		Date now = new Date();
		Calendar ca = Calendar.getInstance();
		ca.setTime(now);
		ca.add(Calendar.MINUTE, 120); //2小时内有效
		String expiredate = Tools.getStringByDateAndTime(ca.getTime());//当前系统时间+2小时
		
		String code = Tools.getRandom(4);
		CheckCode CheckCode = new CheckCode();
		CheckCode.setEmail(email);
		CheckCode.setCreatedate(Tools.getStringByDateAndTime(new Date()));
		CheckCode.setCheckcode(code);
		CheckCode.setIp(ip);
		CheckCode.setExpiredate(expiredate);
		CheckCode.setIsused("00"); //00未使用 01已使用
		checkCodeMapper.insert(CheckCode);
		
		//发送邮箱邮件
		String bodyHtml = "<div style='font-size:20px;background:#e4d480;padding:20px 10px'>"
				+ "<p style='font-family:“华文行楷”'>您好！</p>"
				+ "<p style='margin:20px 0;font-family:“华文行楷”'>"
				+ "您正在进行邮箱验证，本次请求的验证码为：<font color='red'>" + code +"</font>(为了保障您帐号的安全性，请在2小时内完成验证。)"
				+ "</p> "
//				+ "<div style='font-size:12px; color:#999;line-height:20px;border-top:1px solid #e6e6e6;padding:10px 0;'>"
//				+ "如有任何问题，可以与我们联系，我们将尽快为您解答。"
//				+ "官网：www.txboss.com ，电话：15663166121，QQ:838204075 "
//				+ "</div>" 
				+ "</div>";
		MailService service = new MailService();// 新建邮件
		service.send(email, null, "机器人竞赛管理系统注册验证码", null, bodyHtml, null);
	}

	
	public Boolean verifyCheckCode(String email, String code) {
		Integer cnt = checkCodeMapper.verifyCheckCode(email, code);
		if (cnt > 0) {
			String usedate = Tools.getStringByDateAndTime(new Date());
			checkCodeMapper.modifyCheckCodeByEmail(email,code, usedate);
			return true;
		}
		return false;
	}

	public void updateUserpassword(User user) {
		// TODO Auto-generated method stub
		String password=Tools.getMd5Str(Tools.getMd5Str(user.getPassword())+user.getUid());//密码MD5加密然后加上uid,在进行md5加密
		user.setPassword(password);
		Map<String, Object> condition = new HashMap<String, Object>();
		String email=user.getEmail();
		condition.put("email", email);
		Map<String, Object> map=userMapper.LogonByEmail(condition);
		user.setUid(map.get("uid").toString());
		userMapper.updateByPrimaryKeySelective(user);
	}

	public int finduser(String email) {
		// TODO 自动生成的方法存根
		return userMapper.finduser(email);
	}

	public boolean verifyCheckCodeAndUpdatePsw(String email, String code, String psw) {
		// TODO 自动生成的方法存根
		Integer cnt = checkCodeMapper.verifyCheckCode(email, code);
		if (cnt > 0) {
			String usedate = Tools.getStringByDateAndTime(new Date());
			checkCodeMapper.modifyCheckCodeByEmail(email,code, usedate);
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("email", email);
			Map<String, Object> userMap = userMapper.LogonByEmail(condition);
			User user = new User();
			user.setUid((String)userMap.get("uid"));
			user.setPassword(Tools.getMd5Str(Tools.getMd5Str(psw)+(String)userMap.get("uid")));
			userMapper.updateByPrimaryKeySelective(user);
			return true;
		}
		return false;
	}
	
}
