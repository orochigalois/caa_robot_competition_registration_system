package com.zts.robot.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zts.robot.mapper.MatchMapper;
import com.zts.robot.mapper.MemberMapper;
import com.zts.robot.mapper.RaceTeamMemberMapper;
import com.zts.robot.mapper.TeamMapper;
import com.zts.robot.pojo.Match;
import com.zts.robot.pojo.Member;
import com.zts.robot.pojo.RaceTeamMember;
import com.zts.robot.pojo.Team;
import com.zts.robot.util.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class RaceTeamMemberService {

	@Autowired
	private RaceTeamMemberMapper mapper;
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private MatchMapper matchMapper;
	@Autowired
	private TeamMapper teamMapper;
	/**
	 * 检测队员是否已经填报队伍所在赛项
	 * @param tid
	 * @param tmid
	 * @return true 存在  false 不存在
	 */
	public boolean checkMemberOfRace(String tid, String tmid) {
		
		//检查队伍已报赛项
		List<Map<String, Object>> ridList = mapper.findRidListOfTid(tid);

		//检查赛项和队员之前关系
		for(Map<String, Object> map : ridList) {
			int n = mapper.checkMemberOfRace(map.get("rid").toString(), tmid);
			if (n > 0) {
				return true;
			}
		}
		return false;
	}

	public void addRtm(String mid, String tid, String tmid, int serialnum, Map<String, Object> resultMap, String name, String roleflg)throws Exception {
		//获取报名人ID
		String signuid = mapper.findSignuidByTid(tid);
		//检查队伍已报赛项
		List<Map<String, Object>> ridList = mapper.findRidListOfTid(tid);
		Match match=matchMapper.selectByPrimaryKey(mid);
		//检查赛项和队员之前关系
		for(Map<String, Object> map : ridList) {
			/*int count=mapper.findSerialnumByTid(serialnum, tid,roleflg);
			if(count>0){
				resultMap.put("status", 1);
				resultMap.put("errmsg",name+"该成员的序列号存在，不允许再次添加，请进行修改！" );
				throw new RuntimeException();
			}else{
				
			}	*/		
			RaceTeamMember rtm = new RaceTeamMember();
			rtm.setRid(map.get("rid").toString());
			rtm.setInfostatus("01");
			rtm.setFeestatus("01");
			rtm.setMid(mid);
			rtm.setSignuid(signuid);
			rtm.setTid(tid);
			rtm.setTmid(tmid);
			rtm.setCreatedate(map.get("createdate").toString());
			rtm.setSerialnum(serialnum);
			rtm.setUnitprice(match.getUnitprice());
			mapper.insertSelective(rtm);
		}
		//修改队伍审批状态
		mapper.changeInfoStatusByTid(tid, "01");
		resultMap.put("status", 0);
	}
	
	public void addMemberAndRtm(String mid, String tid, Member member, String uid, int serialnum, Map<String, Object> resultMap) {
		//赛事信息 
		Match match=matchMapper.selectByPrimaryKey(mid);
		String signuid = mapper.findSignuidByTid(tid);
		String date = Tools.getStringByDateAndTime(new Date());
		String tmid = Tools.get32UUID();	
		member.setTmstatus("00"); //00正常 01 关闭
		member.setCkstatus("01"); //00已签到 01未签到
		member.setTmid(tmid);
		member.setCreatedate(date);
		member.setCreateuid(uid);
		member.setDelflg("00");
		String random;
		String tcode = "Y"+Tools.getStringByDateAndTime(new Date()).substring(2, 7).replace("-", "")+"TM";
		for(;;){
			random= Tools.getRandom(6);
			String tempTcode = tcode+random;
			Integer countTempTcode = memberMapper.findCountByMemberTcode(tempTcode);
			if(countTempTcode==0){
				tcode=tcode+random;
				break;
			}
		}	
		member.setTmcode(tcode);
		memberMapper.insertSelective(member);

		//检查队伍已报赛项
		List<Map<String, Object>> ridList = mapper.findRidListOfTid(tid);
		
		//检查赛项和队员之前关系
		for(Map<String, Object> map : ridList) {
			//int count=mapper.findSerialnumByTid(serialnum, tid, roleflg);
			RaceTeamMember rtm = new RaceTeamMember();
			rtm.setRid(map.get("rid").toString());
			rtm.setInfostatus("01");
			rtm.setFeestatus("01");
			rtm.setMid(mid);
			rtm.setSignuid(signuid);
			rtm.setTid(tid);
			rtm.setTmid(tmid);
			rtm.setCreatedate(map.get("createdate").toString());
			rtm.setSerialnum(serialnum);
			rtm.setUnitprice(match.getUnitprice());
			mapper.insertSelective(rtm);
		}
		//修改队伍审批状态
		mapper.changeInfoStatusByTid(tid, "01");
		resultMap.put("status", 0);
	}

	public void removeMemeberFromTeam(String tid, String tmid, String uid) {
		//删除队伍成员表信息
		mapper.deleteMemeberByTidAndTmid(tid, tmid);
		int count = mapper.isJoinRaceByTmid(tmid);
		if(count==0){
			/*Member member = new Member();
			member.setTmid(tmid);
			member.setDelflg("01");
			member.setUpduid(uid);
			member.setTmstatus("01");
			member.setUpddate(Tools.getStringByDateAndTime(new Date()));
			memberMapper.updateByPrimaryKeySelective(member);*/
			memberMapper.deleteByPrimaryKey(tmid);
		}
		//修改队伍审批状态
		mapper.changeInfoStatusByTid(tid, "01");
	}
	
	public List<Map<String, Object>> findRaceAllMembersByRidTid(String tid,String rid) {
		return mapper.findRaceAllMembersByRidTid(tid,rid);
	}

	public List<Map<String, Object>> findAllMembersByTid(String tid) {
		return mapper.findAllMembersByTid(tid);
	}

	public int getTotalAllMemberBySignuid(String signuid, String mid) {

		return mapper.getTotalAllMemberBySignuid(signuid,mid);
	}
	
	public List<Map<String, Object>> findAllMembersBySignuid(Map<String, Object> condition) {
		
		return mapper.findAllMembersBySignuid(condition);
	}

	public List<Map<String, Object>> findTeamsBySignuid(String signuid, String mid) {
		
		return mapper.findTeamsBySignuid(signuid,mid);
	}

	public void deleteMemberFromListByTmid(String tmid) {
		Member member = new Member();
		member.setTmid(tmid);
		member.setDelflg("01");
		memberMapper.updateByPrimaryKey(member);
		mapper.delflgByTmid(tmid);
	}

	public void approveTeam(String rid, String tid, String infostatus, String feedback,
			String upduid) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("feedback", feedback);
		paramMap.put("infostatus", infostatus);
		paramMap.put("upduid", upduid);
		paramMap.put("upddate", Tools.getStringByDateAndTime(new Date()));
		paramMap.put("tid", tid);
		paramMap.put("rid", rid);
		paramMap.put("reviewuid", upduid);
		paramMap.put("reviewdate", Tools.getStringByDateAndTime(new Date()));

		mapper.updateInfostatus(paramMap);
	}

	public int findTeamsBySignuidTotalSize(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return mapper.findTeamsBySignuidTotalSize(paramMap);
	}

	public List<Map<String, Object>> findTeamsBySignuidPages(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return mapper.findTeamsBySignuidPages(paramMap);
	}

	public void addAvailableTeam(String uid, String mid, String rid, String tid, Map<String, Object> resultMap)throws Exception {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list =mapper.findoneteamper(tid);
		for (Map<String, Object> map:list) {			
			int count=mapper.findperson(rid,tid,map.get("tmid").toString());
			if(count<1){
				RaceTeamMember rtm = new RaceTeamMember();
				rtm.setRid(rid);
				rtm.setInfostatus("01");
				rtm.setFeestatus("01");
				rtm.setMid(mid);
				rtm.setSignuid(uid);
				rtm.setTid(tid);
				rtm.setTmid(map.get("tmid").toString());
				rtm.setCreatedate(map.get("createdate").toString());
				Integer serialnum=Integer.valueOf(map.get("serialnum").toString());
				rtm.setSerialnum(serialnum);
				mapper.insertSelective(rtm);
				resultMap.put("status", 0);
			}else{
				resultMap.put("status", 1);
				resultMap.put("errmsg", "该队伍部分参赛队员已报名此赛项，请选择其他队伍！");
				throw new RuntimeException();				
			}
			
		}
	}

	public int findAllTeamsBySignuidPagesTotalSize(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return mapper.findAllTeamsBySignuidPagesTotalSize(paramMap);
	}

	public List<Map<String, Object>> findAllTeamsBySignuidPages(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return mapper.findAllTeamsBySignuidPages(paramMap);
	}

	public List<Map<String, Object>> findAllTeamsRace(String tid) {
		// TODO Auto-generated method stub
		return mapper.findAllTeamsRace(tid);
	}

	public void updateFeestatusByTeamRace(String tid, String rid, String feestatus, Map<String, Object> resultMap) {
		// TODO Auto-generated method stub
		String oldfeestatus=mapper.findFeestatusByTeamRace(tid,rid);//查询本身缴费状态
		if("00".equals(oldfeestatus) && "01".equals(feestatus)){
			resultMap.put("status", 1);
			resultMap.put("errmsg", "已完成缴费，不允许更改！");
		}else{
			mapper.updateFeestatusByTeamRace(tid,rid,feestatus);
			resultMap.put("status", 0);
		}
		
	}

	public void updateSerialnumByTid(String tid, String rid, JSONArray array, Map<String, Object> resultMap) {
		// TODO Auto-generated method stub
		for(int i=0;i<array.size();i++) {
			JSONObject json = array.getJSONObject(i);
			String tmid=json.getString("tmid");
			int serialnum=Integer.parseInt(json.getString("serialnum"));
			mapper.updateSerialnumByTid(tmid,serialnum,tid,rid);
		}
		resultMap.put("status", 0);
	}

	public List<Map<String, Object>> findUnitpriceByTid(String tid, String rid) {
		// TODO Auto-generated method stub
		return mapper.findUnitpriceByTid(tid,rid);
	}

	public void updateUnitprice(JSONArray array, String tid) {
		// TODO Auto-generated method stub
		Team team=teamMapper.selectByPrimaryKey(tid);
		String signuid=team.getSignuid();
		for (int i = 0; i < array.size(); i++) {
			JSONObject json = array.getJSONObject(i);
			String tmid=json.getString("tmid");
			String unitprice=json.getString("unitprice");
			mapper.updateUnitprice(unitprice,tmid,signuid);
		}
	}

}
