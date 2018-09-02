package com.zts.robot.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zts.robot.util.CookieOperation;
import com.zts.robot.util.MyConstants;
import com.zts.robot.util.RedisUtil;

import net.sf.json.JSONObject;

public class MyFilter implements javax.servlet.Filter {
	
	
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO 自动生成的方法存根
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// TODO 自动生成的方法存根
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		HttpServletResponse servletResponse = (HttpServletResponse) response;
		servletRequest.setCharacterEncoding("UTF-8");
		servletResponse.setCharacterEncoding("UTF-8");
		try {
			
		
			// 获得用户请求的URI
			String path = servletRequest.getRequestURI();
			// 创建类MyConstants.java，无需过滤的页面
			for (int i = 0; i < MyConstants.NoFilter_Pages.length; i++) {
				if (path.indexOf(MyConstants.NoFilter_Pages[i]) > -1) {
					chain.doFilter(servletRequest, servletResponse);
					return;
				}
			}	
			if(path.equals("/robot/") || path.equals("/") ){
				servletResponse.sendRedirect("http://robotreg.drct-caa.org.cn/login.html");
				return;
			}
			if(path.indexOf("login.html")> -1){
				Cookie cookie1 = CookieOperation.getCookieByName(servletRequest, "authId");
				if(cookie1 == null){
					chain.doFilter(servletRequest, servletResponse);
					return;
				}else{
					String authId = cookie1.getValue();
					if(!RedisUtil.exists("USER" + authId)){
						chain.doFilter(servletRequest, servletResponse);
						return;
					}
					JSONObject json = RedisUtil.get("USER"+(String)cookie1.getValue(),JSONObject.class);//获取登录人信息
					String roleflg = json.optString("roleflg");
					String url;
					if("03".equals(roleflg)){
						url="http://robotreg.drct-caa.org.cn/jsp/main.html";
						
					}else{
						url="http://robotreg.drct-caa.org.cn/jsp/BMS-matchInfo.html";
					}
					servletResponse.sendRedirect(url);
				}
				
			}
			
				Cookie cookie = CookieOperation.getCookieByName(servletRequest, "authId");
				if(cookie==null){
					//登陆失效
					servletResponse.setStatus(MyConstants.HTTP_CODE_LOSTLOGIN);				
					return;
				}
				String authId = cookie.getValue();
				if(RedisUtil.exists("USER" + authId)){
					RedisUtil.expire("USER"+authId, 7200);
					// 已经登陆,继续此次请求
					chain.doFilter(request, response);
					return;
				}else{
					//登陆失效
					servletResponse.setStatus(MyConstants.HTTP_CODE_LOSTLOGIN);				
					return;
				}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println();
		}
			
	}

	@Override
	public void destroy() {
		// TODO 自动生成的方法存根

	}

}
