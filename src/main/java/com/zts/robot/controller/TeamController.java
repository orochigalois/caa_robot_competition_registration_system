package com.zts.robot.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zts.robot.pojo.Member;
import com.zts.robot.pojo.Team;
import com.zts.robot.redis.RedisService;
import com.zts.robot.service.MemberService;
import com.zts.robot.service.RaceTeamMemberService;
import com.zts.robot.service.TeamService;
import com.zts.robot.util.CookieOperation;
import com.zts.robot.util.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class TeamController {
	@Autowired
	private TeamService service;
	@Autowired
	private RedisService redisService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private RaceTeamMemberService rtmService;
	
	/**
	 * 注册队伍
	 * @param request
	 * @param response
	 * @return
	 */

	@RequestMapping("/regTeam")
	@ResponseBody
	public Map<String, Object> regTeam(HttpServletRequest request, HttpServletResponse response, Team team, String rid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String menJSON = request.getParameter("memList");
			JSONArray array = JSONArray.fromObject(menJSON);
	
			Cookie cookie = CookieOperation.getCookieByName(request, "authId");
			JSONObject json = redisService.get("USER"+(String)cookie.getValue(),JSONObject.class);//获取登录人信息
			String uid = json.optString("uid");
			String mid = request.getParameter("mid");
			
			int count=service.findTeamName(team.getTname(),mid,rid);
			if(count>0){
				resultMap.put("status", 1);
				resultMap.put("errmsg", "参赛队名已被占用，请更改队名，或添加后缀！");
			}else{
				team.setCreateuid(uid);
				team.setSignuid(uid);
				team.setMid(mid);
				String random;
//				//队伍编码
//				String tcode = "Y"+Tools.getStringByDateAndTime(new Date()).substring(2, 7).replace("-", "")+"T";
//				for(;;){
//					random= Tools.getRandom(5);
//					String tempTcode = tcode+random;
//					Integer countTempTcode = service.findCountByTempTcode(tempTcode);
//					if(countTempTcode==0){
//						tcode=tcode+random;
//						break;
//					}
//				}				
//				team.setTcode(tcode);
				service.regTeam(team, array, rid,resultMap);
			}
			
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	
	
	/**
	 * 成员添加到队伍
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addMemeberToTeam")
	@ResponseBody
	public Map<String, Object> addMemeberToTeam(HttpServletRequest request, HttpServletResponse response, String tid, Member member,int serialnum) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {	
			Cookie cookie = CookieOperation.getCookieByName(request, "authId");
			JSONObject json = redisService.get("USER"+(String)cookie.getValue(),JSONObject.class);//获取登录人信息
			String uid = json.optString("uid");
			String mid = member.getMid();
			//String uid = "USER11111";
			//String mid = "MATCH0001";
			String didType = member.getDidtype();
			String did = member.getDid();
			String alreadyflg="";
			//判断该人是否存在其他赛事
			int  num=memberService.findMemberNumByDid(mid, didType, did);
			if(num>0){
				alreadyflg="00";
			}else{
				alreadyflg="01";
			}
			member.setAlreadyflg(alreadyflg);
			String tmid = memberService.findTmidByMidDid(mid, didType, did);
			Map<String, Object> memberMap = memberService.findMemberMapByDidMid(mid, didType, did);
			//已有成员
			if (memberMap != null) {
				String name=memberMap.get("tmname").toString();
				String roleflg=memberMap.get("roleflg").toString();
				//教师可重复参与赛项，队员不可以
				if("02".equals(memberMap.get("roleflg"))){
					if(rtmService.checkMemberOfRace(tid, tmid)){
						resultMap.put("status", 1);
						resultMap.put("errmsg", "不能添加此成员，该成员已在其他队伍中填报过此赛项！");
						return resultMap;
					}					
				}
					rtmService.addRtm(mid, tid, tmid,serialnum,resultMap,name,roleflg);
					resultMap.put("status", 0);
				
			} else {
				rtmService.addMemberAndRtm(mid, tid, member, uid,serialnum,resultMap);
				resultMap.put("status", 0);

			}			
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 从队伍中移除成员
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/removeMemeberFromTeam")
	@ResponseBody
	public Map<String, Object> removeMemeberFromTeam(HttpServletRequest request, HttpServletResponse response, String tid, String tmid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {	
			Cookie cookie = CookieOperation.getCookieByName(request, "authId");
			JSONObject json = redisService.get("USER"+(String)cookie.getValue(),JSONObject.class);//获取登录人信息
			String uid = json.optString("uid");
			String mid = request.getParameter("mid");
			//String uid = "USER11111";
			//String mid = "MATCH0001";
			rtmService.removeMemeberFromTeam(tid, tmid,uid);
			resultMap.put("status", 0);
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 查询赛项成员列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/findRaceAllMembersByRidTid")
	@ResponseBody
	public Map<String, Object> findRaceAllMembersByRidTid(HttpServletRequest request, HttpServletResponse response, String tid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {	
			Cookie cookie = CookieOperation.getCookieByName(request, "authId");
			JSONObject json = redisService.get("USER"+(String)cookie.getValue(),JSONObject.class);//获取登录人信息
			String uid = json.optString("uid");
			String mid = request.getParameter("mid");
			String rid = request.getParameter("rid"); //add by grace at 20180107
			List<Map<String, Object>> list = rtmService.findRaceAllMembersByRidTid(tid,rid);
			resultMap.put("list", list);
			resultMap.put("status", 0);
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	
	
	/**
	 * 查询队伍成员列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/findAllMembersByTid")
	@ResponseBody
	public Map<String, Object> findAllMembersByTid(HttpServletRequest request, HttpServletResponse response, String tid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {	
			Cookie cookie = CookieOperation.getCookieByName(request, "authId");
			JSONObject json = redisService.get("USER"+(String)cookie.getValue(),JSONObject.class);//获取登录人信息
			String uid = json.optString("uid");
			String mid = request.getParameter("mid");
			//String uid = "USER11111";
			//String mid = "MATCH0001";
			List<Map<String, Object>> list = rtmService.findAllMembersByTid(tid);
			resultMap.put("list", list);
			resultMap.put("status", 0);
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	
	
	/**
	 * 根据id查看成员具体信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/findMemberInfoByTmid")
	@ResponseBody
	public Map<String, Object> findMemberInfoByTmid(HttpServletRequest request, HttpServletResponse response, String tmid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {	
//			Cookie cookie = CookieOperation.getCookieByName(request, "authId");
//			JSONObject json = redisService.get("USER"+(String)cookie.getValue(),JSONObject.class);//获取登录人信息
//			String uid = json.optString("uid");
//			String mid = redisService.get("sys_openedMatchMid");	

			Member info = memberService.findMemberInfoByTmid(tmid);
			resultMap.put("info", info);
			resultMap.put("status", 0);
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	
	
	/**
	 * 报名人所在成员列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/findAllMembersBySignuidAndPage")
	@ResponseBody
	public Map<String, Object> findAllMembersBySignuidAndPage(HttpServletRequest request, HttpServletResponse response, 
			Integer iDisplayLength, Integer iDisplayStart) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> condition = new HashMap<String, Object>();
		try {	
			
			Cookie cookie = CookieOperation.getCookieByName(request, "authId");
			JSONObject json = redisService.get("USER"+(String)cookie.getValue(),JSONObject.class);//获取登录人信息
			String uid = json.optString("uid");
			String mid = request.getParameter("mid");	
			String signuid = uid;
			condition.put("signuid", signuid);
			condition.put("mid", mid);
			int total = rtmService.getTotalAllMemberBySignuid(signuid,mid);
			if (iDisplayLength==null||iDisplayLength != -1) {
				condition.put("beginNo", iDisplayStart);
				condition.put("endNo", iDisplayLength);
			}
			List<Map<String, Object>> list = rtmService.findAllMembersBySignuid(condition);
			resultMap.put("list", list);
			resultMap.put("iTotalRecords", total);
			resultMap.put("status", 0);
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 报名人所在队伍列表没有分页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/findTeamsBySignuid")
	@ResponseBody
	public Map<String, Object> findTeamsBySignuid(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Cookie cookie = CookieOperation.getCookieByName(request, "authId");
			JSONObject json = redisService.get("USER"+(String)cookie.getValue(),JSONObject.class);//获取登录人信息
			String uid = json.optString("uid");
			String mid = request.getParameter("mid");
			String signuid = uid;
			List<Map<String, Object>> list = rtmService.findTeamsBySignuid(signuid,mid);
			resultMap.put("list", list);
			resultMap.put("status", 0);
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	/**
	 * 报名人所在队伍列表有分页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/findTeamsBySignuidPages")
	@ResponseBody
	public Map<String, Object> findTeamsBySignuidPages(HttpServletRequest request, HttpServletResponse response,Integer iDisplayLength, Integer iDisplayStart) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			Cookie cookie = CookieOperation.getCookieByName(request, "authId");
			JSONObject json = redisService.get("USER"+(String)cookie.getValue(),JSONObject.class);//获取登录人信息
			String uid = json.optString("uid");
			String mid = request.getParameter("mid");
			String signuid = uid;
			paramMap.put("signuid", signuid);
			paramMap.put("mid", mid);
			int totalSize = rtmService.findTeamsBySignuidTotalSize(paramMap);
			if (iDisplayLength==null||iDisplayLength != -1) {
				paramMap.put("beginNo", iDisplayStart);
				paramMap.put("endNo", iDisplayLength);
			}
			List<Map<String, Object>> list = rtmService.findTeamsBySignuidPages(paramMap);
			resultMap.put("list", list);
			resultMap.put("iTotalRecords", totalSize);
			resultMap.put("status", 0);
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * RCJ报名人所在队伍列表有分页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/findTeamsToRCJBySignuidPages")
	@ResponseBody
	public Map<String, Object> findTeamsToRCJBySignuidPages(HttpServletRequest request, HttpServletResponse response,Integer iDisplayLength, Integer iDisplayStart) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			Cookie cookie = CookieOperation.getCookieByName(request, "authId");
			JSONObject json = redisService.get("USER"+(String)cookie.getValue(),JSONObject.class);//获取登录人信息
			String uid = json.optString("uid");
			String mid = request.getParameter("mid");
			String signuid = uid;
			paramMap.put("signuid", signuid);
			paramMap.put("mid", mid);
			int totalSize = rtmService.findTeamsToRCJBySignuidTotalSize(paramMap);
			if (iDisplayLength==null||iDisplayLength != -1) {
				paramMap.put("beginNo", iDisplayStart);
				paramMap.put("endNo", iDisplayLength);
			}
			List<Map<String, Object>> list = rtmService.findTeamsToRCJBySignuidPages(paramMap);
			resultMap.put("list", list);
			resultMap.put("iTotalRecords", totalSize);
			resultMap.put("status", 0);
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	
	
	/**
	 * RCJ报名人所在队伍列表有分页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/findTeamsinfoToRCJ")
	@ResponseBody
	public Map<String, Object> findTeamsinfoToRCJ(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			Cookie cookie = CookieOperation.getCookieByName(request, "authId");
			JSONObject json = redisService.get("USER"+(String)cookie.getValue(),JSONObject.class);//获取登录人信息
			String uid = json.optString("uid");
			String mid = request.getParameter("mid");
			String tid = request.getParameter("tid");
			String rid = request.getParameter("rid");
			String signuid = uid;
			paramMap.put("signuid", signuid);
			paramMap.put("mid", mid);
			paramMap.put("tid", tid);
			paramMap.put("rid", rid);
			List<Map<String, Object>> list = rtmService.findTeamsinfoToRCJ(paramMap);
			resultMap.put("list", list);
			resultMap.put("status", 0);
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 保存队伍信息（无成员信息）
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/saveTeamInfo")
	@ResponseBody
	public Map<String, Object> saveTeamInfo(HttpServletRequest request, HttpServletResponse response, Team team) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Cookie cookie = CookieOperation.getCookieByName(request, "authId");
			JSONObject json = redisService.get("USER"+(String)cookie.getValue(),JSONObject.class);//获取登录人信息
			String uid = json.optString("uid");
			String mid = request.getParameter("mid");
			//String uid = "USER11111";
			service.saveTeamInfo(team, uid);
			resultMap.put("status", 0);
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	

	/**
	 * 删除队伍
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/delTeamByTid")
	@ResponseBody
	public Map<String, Object> delTeamByTid(HttpServletRequest request, HttpServletResponse response, String tid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Cookie cookie = CookieOperation.getCookieByName(request, "authId");
			JSONObject json = redisService.get("USER"+(String)cookie.getValue(),JSONObject.class);//获取登录人信息
			String uid = json.optString("uid");
			String mid =request.getParameter("mid");
			//String uid = "USER11111";
			service.delTeamByTid(tid);
			resultMap.put("status", 0);
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	/**
	 * 删除这个赛项下的队伍
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/delRaceTeamByTid")
	@ResponseBody
	public Map<String, Object> delRaceTeamByTid(HttpServletRequest request, HttpServletResponse response, String tid,String rid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Cookie cookie = CookieOperation.getCookieByName(request, "authId");
			JSONObject json = redisService.get("USER"+(String)cookie.getValue(),JSONObject.class);//获取登录人信息
			String uid = json.optString("uid");
			String mid =request.getParameter("mid");
			//String uid = "USER11111";
			service.delRaceTeamByTid(tid,rid);
			resultMap.put("status", 0);
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 队伍审批操作
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/approveTeam")
	@ResponseBody
	public Map<String, Object> approveTeam(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Cookie cookie = CookieOperation.getCookieByName(request, "authId");
			JSONObject json = redisService.get("USER"+(String)cookie.getValue(),JSONObject.class);//获取登录人信息
			String uid = json.optString("uid");
			String mid = request.getParameter("mid");
			//String uid = "USER1111";
			String tid = request.getParameter("tid");
			String rid = request.getParameter("rid");
			String infostatus = request.getParameter("infostatus");
			String feedback = request.getParameter("feedback");
			rtmService.approveTeam(rid, tid, infostatus, feedback, uid);
			resultMap.put("status", 0);
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	/**
	 * 选择已有队伍报名
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addAvailableTeam")
	@ResponseBody
	public Map<String, Object> addAvailableTeam(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Cookie cookie = CookieOperation.getCookieByName(request, "authId");
			JSONObject json = redisService.get("USER"+(String)cookie.getValue(),JSONObject.class);//获取登录人信息
			String uid = json.optString("uid");
			String mid = request.getParameter("mid");		
			String rid = request.getParameter("rid");
			String tid = request.getParameter("tid");
			//mid="0d0147661cdc48b18967d236389ab1d2";
			//rid="4bbbedc8ceda46e2ba57467248ee4be4";
			//tid="f3b881fa89d9474babcb8e4289feab9c";
			//String uid="admin";
			rtmService.addAvailableTeam(uid,mid,rid,tid,resultMap);
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	/**
	 * 查询所有队伍列表
	 * @param request
	 * @param response
	 * @param iDisplayLength
	 * @param iDisplayStart
	 * @return
	 */
	@RequestMapping("/findAllTeamsBySignuidPages")
	@ResponseBody
	public Map<String, Object> findAllTeamsBySignuidPages(HttpServletRequest request, HttpServletResponse response,Integer iDisplayLength, Integer iDisplayStart) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String tname=request.getParameter("name");
			String infostatus=request.getParameter("infostatus");
			String school=request.getParameter("school");
			String feestatus=request.getParameter("feestatus");
			String orgtype=request.getParameter("orgtype");
			String region=request.getParameter("region");
			String rid=request.getParameter("rid");
			String mid=request.getParameter("mid");
			String ckstatus=request.getParameter("ckstatus");
			String tcode=request.getParameter("tcode");
			paramMap.put("school", school);
			paramMap.put("orgtype", orgtype);
			paramMap.put("region", region);
			paramMap.put("feestatus", feestatus);
			paramMap.put("tname", tname);
			paramMap.put("infostatus", infostatus);
			paramMap.put("rid", rid);
			paramMap.put("mid", mid);
			paramMap.put("ckstatus", ckstatus);
			paramMap.put("tcode", tcode);
			int totalSize = rtmService.findAllTeamsBySignuidPagesTotalSize(paramMap);
			if (iDisplayLength==null||iDisplayLength != -1) {
				paramMap.put("beginNo", iDisplayStart);
				paramMap.put("endNo", iDisplayLength);
			}
			List<Map<String, Object>> list = rtmService.findAllTeamsBySignuidPages(paramMap);
			resultMap.put("list", list);
			resultMap.put("iTotalRecords", totalSize);
			resultMap.put("status", 0);
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	/**
	 * 查询这个队伍报名的所有赛项
	 * @param request
	 * @param response
	 * @param iDisplayLength
	 * @param iDisplayStart
	 * @return
	 */
	@RequestMapping("/findAllTeamsRace")
	@ResponseBody
	public Map<String, Object> findAllTeamsRace(HttpServletRequest request, HttpServletResponse response,String tid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			
			List<Map<String, Object>> list = rtmService.findAllTeamsRace(tid);
			resultMap.put("list", list);
			resultMap.put("status", 0);
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 查询这个队伍报名的赛项相关信息
	 * @param request
	 * @param response
	 * @param iDisplayLength
	 * @param iDisplayStart
	 * @return
	 */
	@RequestMapping("/findTeamsRaceInfo")
	@ResponseBody
	public Map<String, Object> findTeamsRaceInfo(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String tid = request.getParameter("tid");
			String rid = request.getParameter("rid");
			List<Map<String, Object>> list = rtmService.findTeamsRaceInfo(tid,rid);
			resultMap.put("list", list);
			resultMap.put("status", 0);
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 查询赛项教师，队员人数上限
	 * @param request
	 * @param response
	 * @param iDisplayLength
	 * @param iDisplayStart
	 * @return
	 */
	@RequestMapping("/findRaceTechStuMax")
	@ResponseBody
	public Map<String, Object> findRaceTechStuMax(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String mid = request.getParameter("mid");
			String rid = request.getParameter("rid");
			List<Map<String, Object>> list = rtmService.findRaceTechStuMax(mid,rid);
			resultMap.put("list", list);
			resultMap.put("status", 0);
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 缴费审批（按照队伍赛项）
	 * @param request
	 * @param response
	 * @param tid
	 * @return
	 */
	@RequestMapping("/updateFeestatusByTeamRace")
	@ResponseBody
	public Map<String, Object> updateFeestatusByTeamRace(HttpServletRequest request, HttpServletResponse response,String tid,String rid,String feestatus) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			
			 rtmService.updateFeestatusByTeamRace(tid,rid,feestatus,resultMap);
			
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	/**
	 * 队伍下的成员上移下移排序
	 * @param request
	 * @param response
	 * @param tid
	 * @param rid
	 * @param feestatus
	 * @return
	 */
	@RequestMapping("/updateSerialnumByTid")
	@ResponseBody
	public Map<String, Object> updateSerialnumByTid(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String menJSON = request.getParameter("memList");
			JSONArray array = JSONArray.fromObject(menJSON);
			String tid=request.getParameter("tid");
			String rid=request.getParameter("rid");
			 rtmService.updateSerialnumByTid(tid,rid,array,resultMap);
			
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	/**
	 * 查询该赛项下该队伍成员的单价
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/findUnitpriceByTid")
	@ResponseBody
	public Map<String, Object> findUnitpriceByTid(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String rid=request.getParameter("rid");
			String tid=request.getParameter("tid");
			 List<Map<String, Object>> list=rtmService.findUnitpriceByTid(tid,rid);
			 resultMap.put("status", 0);
			 resultMap.put("list", list);
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	/**
	 * 修改该注册人下的成员单价
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateUnitprice")
	@ResponseBody
	public Map<String, Object> updateUnitprice(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String tid=request.getParameter("tid");
			String memberList=request.getParameter("memberList");
			//memberList="[{'tmid':'0deda60925ce4e568bc9f340a99f50ab','unitprice':'10000'}]";
			JSONArray array = JSONArray.fromObject(memberList);
			rtmService.updateUnitprice(array,tid);
			 resultMap.put("status", 0);
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 保存日志
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/savelog")
	@ResponseBody
	public Map<String, Object> savelog(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {	
			Cookie cookie = CookieOperation.getCookieByName(request, "authId");
			Map<String, Object> paramMap = new HashMap<String, Object>();
			JSONObject json = redisService.get("USER"+(String)cookie.getValue(),JSONObject.class);//获取登录人信息
			String uid = json.optString("uid");
			String mid = request.getParameter("mid");
			String tid = request.getParameter("tid");
			String rid = request.getParameter("rid");
			String stlogurl = request.getParameter("stlogurl");
			String ndlogurl = request.getParameter("ndlogurl");
			String rdlogurl = request.getParameter("rdlogurl");
			String signuid = uid;
			paramMap.put("signuid", signuid);
			paramMap.put("mid", mid);
			paramMap.put("tid", tid);
			paramMap.put("rid", rid);
			paramMap.put("stlogurl", stlogurl);
			paramMap.put("ndlogurl", ndlogurl);
			paramMap.put("rdlogurl", rdlogurl);
			rtmService.savelog(paramMap);
			resultMap.put("status", 0);
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}

}
