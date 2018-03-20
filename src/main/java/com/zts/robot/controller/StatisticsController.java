package com.zts.robot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zts.robot.redis.RedisService;
import com.zts.robot.service.StatisticsService;
import com.zts.robot.util.DownLoadFile;

@Controller
public class StatisticsController {
	@Autowired
	private StatisticsService statisticsService;
	@Autowired
	private RedisService redisService;
	/**
	 * 数据统计
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/statisticsMemberByMid")
	@ResponseBody
	public Map<String, Object> statisticsMemberByMid(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {	
			Map<String, Object> condition = new HashMap<String, Object>();
			String mid = request.getParameter("mid");	
			if(mid==null || "".equals(mid)){
				resultMap.put("status", 1);
				resultMap.put("errmsg", "当前没有开启的赛事！");
			}else{
				condition.put("mid", mid);
				List<List> list=statisticsService.statisticsMemberByMid(condition);
				resultMap.put("status", 0);
				resultMap.put("list", list);
			}
			
			
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	/**
	 * 赛事下的队伍筛选导出
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/teamDerivedByMid")
	@ResponseBody
	private Map<String, Object> teamDerivedByMid(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			
			String mid = request.getParameter("mid");
			String mname = request.getParameter("mname");
			String tcode = request.getParameter("tcode");
			String tname = request.getParameter("tname");
			String school = request.getParameter("tschool");
			String orgtype = request.getParameter("orgtype");
			String region = request.getParameter("region");
			String uname = request.getParameter("uname");
			String email = request.getParameter("email");
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("mid", mid);
			paramMap.put("mname", mname);
			paramMap.put("tcode", tcode);
			paramMap.put("tname", tname);
			paramMap.put("school", school);
			paramMap.put("orgtype", orgtype);
			paramMap.put("region", region);
			paramMap.put("uname", uname);
			paramMap.put("email", email);
			statisticsService.teamDerivedByMid(paramMap,resultMap);
			//DownLoadFile.downLoadFileWeb(response, resultMap);
		} catch(RuntimeException e){
			//无符合条件
			resultMap.put("status", 1);
		} catch (Exception e) {
			resultMap.put("status", 1);
			resultMap.put("errmsg", "系统错误");
		}
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println(resultMap.get("fileurl"));
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		return resultMap;
		
	}
	/**
	 * 赛事下的成员筛选导出
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/memberDerivedByMid")
	@ResponseBody
	private Map<String, Object> memberDerivedByMid(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			
			String mid = request.getParameter("mid");
			String mname = request.getParameter("mname");
			String tmname = request.getParameter("tmname");
			String roleflg = request.getParameter("roleflg");
			String sex = request.getParameter("sex");
			String didtype = request.getParameter("didtype");
			String did = request.getParameter("did");
			String diningtype = request.getParameter("diningtype");			
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("mid", mid);
			paramMap.put("mname", mname);
			paramMap.put("tmname", tmname);
			paramMap.put("roleflg", roleflg);
			paramMap.put("sex", sex);
			paramMap.put("didtype", didtype);
			paramMap.put("did", did);
			paramMap.put("diningtype", diningtype);
			statisticsService.memberDerivedByMid(paramMap,resultMap);
			//DownLoadFile.downLoadFileWeb(response, resultMap);
		} catch(RuntimeException e){
			//无符合条件
			resultMap.put("status", 1);
		} catch (Exception e) {
			resultMap.put("status", 1);
			resultMap.put("errmsg", "系统错误");
			e.printStackTrace();
		}
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println(resultMap.get("fileurl"));
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		return resultMap;
		
	}
	/**
	 * 赛事下报名的队伍筛选导出
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/teamRaceDerivedByMid")
	@ResponseBody
	private Map<String, Object> teamRaceDerivedByMid(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String mid = request.getParameter("mid");
			String mname = request.getParameter("mname");
			String frname = request.getParameter("frname");
			String rname = request.getParameter("rname");
			String tname = request.getParameter("tname");
			String tcode = request.getParameter("tcode");
			String school = request.getParameter("tschool");
			String orgtype = request.getParameter("orgtype");
			String region = request.getParameter("region");
			String uname = request.getParameter("uname");
			String email = request.getParameter("email");
			String infostatus = request.getParameter("infostatus");
			String ckstatus = request.getParameter("ckstatus");
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("mid", mid);
			paramMap.put("mname", mname);
			paramMap.put("frname", frname);
			paramMap.put("rname", rname);
			paramMap.put("tname", tname);
			paramMap.put("tcode", tcode);
			paramMap.put("school", school);
			paramMap.put("orgtype", orgtype);
			paramMap.put("region", region);
			paramMap.put("uname", uname);
			paramMap.put("email", email);
			paramMap.put("infostatus", infostatus);
			paramMap.put("ckstatus", ckstatus);
			
			statisticsService.teamRaceDerivedByMid(paramMap,resultMap);
			//DownLoadFile.downLoadFileWeb(response, resultMap);
		} catch(RuntimeException e){
			//无符合条件
			resultMap.put("status", 1);
		} catch (Exception e) {
			// TODO: handle exception
			resultMap.put("status", 1);
			resultMap.put("errmsg", "系统错误");
		}
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println(resultMap.get("fileurl"));
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		return resultMap;
	}
	/**
	 * 奖励证书生成数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/teamMemberScoreByMid")
	@ResponseBody
	private Map<String, Object> teamMemberScoreByMid(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String mid = request.getParameter("mid");
			String mname = request.getParameter("mname");
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("mid", mid);
			paramMap.put("mname", mname);
			
			statisticsService.teamMemberScoreByMid(paramMap,resultMap);
			//DownLoadFile.downLoadFileWeb(response, resultMap);
		} catch(RuntimeException e){
			//无符合条件
			resultMap.put("status", 1);
		} catch (Exception e) {
			// TODO: handle exception
			resultMap.put("status", 1);
			resultMap.put("errmsg", "系统错误");
		}
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println(resultMap.get("fileurl"));
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		return resultMap;
	}
	
	
	/**
	 * 单项奖证书生成数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/teamScoreByMid")
	@ResponseBody
	private Map<String, Object> teamScoreByMid(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String mid = request.getParameter("mid");
			String mname = request.getParameter("mname");
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("mid", mid);
			paramMap.put("mname", mname);
			
			statisticsService.teamScoreByMid(paramMap,resultMap);
			//DownLoadFile.downLoadFileWeb(response, resultMap);
		} catch(RuntimeException e){
			//无符合条件
			resultMap.put("status", 1);
		} catch (Exception e) {
			// TODO: handle exception
			resultMap.put("status", 1);
			resultMap.put("errmsg", "系统错误");
		}
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println(resultMap.get("fileurl"));
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		return resultMap;
	}
	/**
	 * 文件下载
	 * @param response
	 * @param path
	 * @return
	 */
	@RequestMapping("/downFileByPath")
	@ResponseBody
	private Map<String, Object> downFileByPath(HttpServletResponse response,String path){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("path", path);
		try {
			DownLoadFile.downLoadFileWeb(response, resultMap);
			resultMap.put("status", 0);
		} catch (Exception e) {			// TODO 自动生成的 catch 块
			resultMap.put("status", 1);
			resultMap.put("errmsg", "系统错误");
		}
		return resultMap;
		
	}
	/**
	 * 当前赛事下所有成员生成胸卡
	 * @param response
	 * @param path
	 * @return
	 */
	@RequestMapping("/generateBadge")
	@ResponseBody
	private Map<String, Object> generateBadge(HttpServletRequest request,String path){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String mid = request.getParameter("mid");
			statisticsService.generateBadge(mid,resultMap);
			resultMap.put("status", 0);
		} catch (Exception e) {			// TODO 自动生成的 catch 块
			resultMap.put("status", 1);
			resultMap.put("errmsg", "系统错误");
		}
		return resultMap;
		
	}
}
