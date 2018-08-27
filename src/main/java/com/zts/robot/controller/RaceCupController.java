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

import com.zts.robot.service.RaceCupService;

import net.sf.json.JSONArray;

@Controller
public class RaceCupController {
	@Autowired
	private RaceCupService raceCupService;
	/**
	 * 赛项奖杯的添加
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addRaceCup")
	@ResponseBody
	public Map<String, Object> addRaceCup(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {	
			String cupList=request.getParameter("cupList");
			JSONArray cupJSONArray = JSONArray.fromObject(cupList);
			raceCupService.addRaceCup(cupJSONArray,resultMap);
			resultMap.put("status", 0);
		}catch (RuntimeException e){
			resultMap.put("status", 1);
			e.printStackTrace();
		} catch (Exception e) {
			resultMap.put("status", 1);
			resultMap.put("errmsg", "系统错误！");
			e.printStackTrace();
		}
		return resultMap;
	}
	/**
	 * 查询该赛项的奖杯
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/findRaceCupByRid")
	@ResponseBody
	public Map<String, Object> findRaceCupByRid(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {	
			String rid=request.getParameter("rid");
			
			List<Map<String, Object>> list=raceCupService.findRaceCupByRid(rid);
			resultMap.put("status", 0);
			resultMap.put("list", list);
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}

}
