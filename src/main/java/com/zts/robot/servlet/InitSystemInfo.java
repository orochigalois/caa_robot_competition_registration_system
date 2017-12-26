package com.zts.robot.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.zts.robot.mapper.WriteConstantMapper;
import com.zts.robot.util.MyProperties;
import com.zts.robot.util.RedisUtil;

/**
 * 初始化信息
 * @author purple
 *
 */
public class InitSystemInfo  extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1885721096142413947L;

	@Override  
	public void init() throws ServletException { 
		MyProperties.init();
         //获得全局变量
		 ServletContext servletContext = getServletContext();
		 RedisUtil.init(servletContext);		 
		 WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);		 
		 WriteConstantMapper writeConstantMapper = (WriteConstantMapper)ctx.getBean("writeConstantMapper");
		// Map<String, Object> matchMap = writeConstantMapper.findOpenedMatch();
		 //String sys_openedMatchMid="";
		 //String sys_openedMatchGPS="";
		// String sys_openedMatchName="";
		// if(matchMap!=null){
			 //sys_openedMatchMid = matchMap.containsKey("mid")?(String) matchMap.get("mid"):"";
			// sys_openedMatchGPS= matchMap.containsKey("gps")?(String) matchMap.get("gps"):"";
			// sys_openedMatchName= matchMap.containsKey("mname")?(String) matchMap.get("mname"):"";
		// }
		 //RedisUtil.set("sys_openedMatchMid", sys_openedMatchMid);
		// RedisUtil.set("sys_openedMatchGPS", sys_openedMatchGPS);
		// RedisUtil.set("sys_openedMatchName", sys_openedMatchName);	
		 
		 
		 if(!RedisUtil.exists("sys_defaultUserStatus")){
			 RedisUtil.set("sys_defaultUserStatus", "00");	
		 }
	 }
	
}
