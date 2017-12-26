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

import com.zts.robot.service.RaceTeamScoreService;

import net.sf.json.JSONArray;

@Controller
public class RaceTeamScoreController {
	@Autowired
	private RaceTeamScoreService raceTeamScoreService;
	/**
	 * 成绩的录入
	 * @param request
	 * @param response
	 * @param tid
	 * @param tmid
	 * @return
	 */
	@RequestMapping("/addTeamScore")
	@ResponseBody
	public Map<String, Object> addTeamScore(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String mid = request.getParameter("mid");
		try {	
			String scoreList=request.getParameter("scoreList");
			JSONArray array = JSONArray.fromObject(scoreList);
			
			raceTeamScoreService.addTeamScore(array,mid,resultMap);
			raceTeamScoreService.editnumber((String)resultMap.get("rid"));
			resultMap.put("status", 0);
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 查询该赛项下的所有队伍的成绩
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/findTeamScoreByRidBySetScore")
	@ResponseBody
	public Map<String, Object> findTeamScoreByRidBySetScore(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {	
			String tname=request.getParameter("tname");
			String rid=request.getParameter("rid");
			map.put("tname", tname);
			map.put("rid", rid);
			List<Map<String, Object>> list=raceTeamScoreService.findTeamScoreByRidBySetScore(map);
			resultMap.put("status", 0);
			resultMap.put("list", list);
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 查询该赛项下的所有队伍的成绩
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/findTeamScoreByRid")
	@ResponseBody
	public Map<String, Object> findTeamScoreByRid(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {	
			String tname=request.getParameter("tname");
			String rid=request.getParameter("rid");
			map.put("tname", tname);
			map.put("rid", rid);
			List<Map<String, Object>> list=raceTeamScoreService.findTeamScoreByRid(map);
			resultMap.put("status", 0);
			resultMap.put("list", list);
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	/**
	 * 查看队伍情况（全部队伍数量，记录分数的队伍数，违规，缺赛）
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/findRaceTeamNumByRid")
	@ResponseBody
	public Map<String, Object> findRaceTeamNumByRid(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();		
		try {	
			String rid=request.getParameter("rid");
			Map<String, Object> raceTeamInfoMap=raceTeamScoreService.findRaceTeamNumByRid(rid);
			resultMap.put("status", 0);
			resultMap.put("info", raceTeamInfoMap);
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	/**
	 * 获取该赛项下的队伍成绩排名（预览/查看）
	 * @param request
	 * @param response
	 * @return
	 */

	@RequestMapping("/findScoreRankingByRid")
	@ResponseBody
	public Map<String, Object> findScoreRankingByRid(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {			
			List<Map<String, Object>> list=raceTeamScoreService.findScoreRankingByRid(request,resultMap);
			if(list==null){
				return resultMap;
			}
			resultMap.put("status", 0);
			resultMap.put("list", list);
		} catch (Exception e) {
			resultMap.put("status", 1);
			resultMap.put("errmsg", "系统错误！");
			e.printStackTrace();
		}
		return resultMap;
	}
	/**
	 * 奖状发放查询
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/findTeamCitationByRid")
	@ResponseBody
	public Map<String, Object> findTeamCitationByRid(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();		
		try {				
			String rid=request.getParameter("rid");
			//查询当前赛项下的奖杯
			int cupnum=raceTeamScoreService.findCupNumByRid(rid);
			//查询当前赛项下的奖状
			int awardsnum=raceTeamScoreService.findAwardsNumByRid(rid);
			if(cupnum<1){
				resultMap.put("status", 1);
				resultMap.put("errmsg", "当前赛项下没有设置奖杯！请前往设置！");
			}else if(awardsnum<1){
				resultMap.put("status", 1);
				resultMap.put("errmsg", "当前赛项下没有设置奖项！请前往奖项！");
			}else{
			List<Map<String, Object>> list=raceTeamScoreService.findTeamCitationByRid(rid);
			resultMap.put("status", 0);
			resultMap.put("list", list);
			}
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	
}
