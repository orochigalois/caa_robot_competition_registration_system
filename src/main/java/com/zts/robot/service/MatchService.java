package com.zts.robot.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zts.robot.mapper.MatchMapper;
import com.zts.robot.mapper.MemberMapper;
import com.zts.robot.mapper.RaceTeamMemberMapper;
import com.zts.robot.mapper.ReuserMatchMapper;
import com.zts.robot.mapper.TeamMapper;
import com.zts.robot.mapper.WriteConstantMapper;
import com.zts.robot.pojo.Match;
import com.zts.robot.pojo.ReuserMatch;
import com.zts.robot.pojo.ReuserMatchKey;
import com.zts.robot.util.RedisUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class MatchService {
	@Autowired
	private MatchMapper matchMapper;
	@Autowired
	private WriteConstantMapper writeConstantMapper;
	@Autowired
	private TeamMapper teamMapper;
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private RaceTeamMemberMapper raceTeamMemberMapper;
	@Autowired
	private ReuserMatchMapper reuserMatchMapper;
	
	public void addMatch(Match match) {
		// TODO 自动生成的方法存根
		matchMapper.insertSelective(match);
	}

	public void updateMatch(Match match) {
		// TODO 自动生成的方法存根
		String mid=match.getMid();
		int unitprice=match.getUnitprice();
		Match match2=matchMapper.selectByPrimaryKey(mid);
		matchMapper.updateByPrimaryKeySelective(match);
		//修改单价的时候要修改对应每个人的单价
		if(!match2.getUnitprice().equals(match.getUnitprice())){
			raceTeamMemberMapper.updateUnitpriceByMid(unitprice, mid);
		}		
	}

	public int findMatchlistTotalSize(Map<String, Object> paramMap) {
		// TODO 自动生成的方法存根
		return matchMapper.findMatchlistTotalSize(paramMap);
	}
	
	public List<Map<String, Object>> findMatchListByPage(Map<String, Object> paramMap) {
		// TODO 自动生成的方法存根
		List<Map<String, Object>> matchList=matchMapper.findMatchListByPage(paramMap);
		return matchList;
	}

	public Map<String, Object> findMatchInfoByMid(String mid) {
		// TODO 自动生成的方法存根
		return matchMapper.findMatchInfoByMid(mid);
	}

	public void updateDelflgMatchByMid(String mid, Map<String, Object> resultMap) {
		// TODO 自动生成的方法存根
		Match match=matchMapper.selectByPrimaryKey(mid);
		if("00".equals(match.getMstatus())){
			resultMap.put("status", 1);
			resultMap.put("errmsg", "当前赛事为开启状态，不允许删除！");
		}else{
			matchMapper.updateDelflgMatchByMid(mid);
			resultMap.put("status", 0);
		}
		
	}

	public void onOrOffMatchByMid(String mid, Map<String, Object> resultMap) {
		// TODO 自动生成的方法存根
		Match match = matchMapper.selectByPrimaryKey(mid);
		if("01".equals(match.getMstatus())){//开启赛事
			int count=matchMapper.findmstatus();//判断当前是否有正在开启的赛事
			/*if(count>0){
				resultMap.put("status", 1);
				resultMap.put("errmsg", "当前有开启的赛事，不允许开启其他赛事！");
			}else{
				 RedisUtil.set("sys_openedMatchMid", match.getMid());
				 RedisUtil.set("sys_openedMatchGPS", match.getGps());
				 RedisUtil.set("sys_openedMatchName", match.getMname());
				match.setMstatus("00");
				matchMapper.updateByPrimaryKeySelective(match);
				resultMap.put("status", 0);
			}*/
			match.setMstatus("00");
			matchMapper.updateByPrimaryKeySelective(match);
			resultMap.put("status", 0);
		}else if("00".equals(match.getMstatus())){//关闭赛事
			match.setMstatus("02");
			matchMapper.updateByPrimaryKeySelective(match);//关闭赛事
			//关闭这个赛事队伍
			//teamMapper.updatetstatus(mid);
			//关闭这个队伍成员
			//memberMapper.updatetmstatus(mid);
			RedisUtil.set("sys_openedMatchMid", "");
			RedisUtil.set("sys_openedMatchGPS", "");
			RedisUtil.set("sys_openedMatchName", "");
			resultMap.put("status", 0);
		}else if("02".equals(match.getMstatus())){//赛事不可逆，关闭后不可开启
			match.setMstatus("02");
			matchMapper.updateByPrimaryKeySelective(match);
			//关闭这个赛事队伍
			//teamMapper.updatetstatus(mid);
			//关闭这个队伍成员
			//memberMapper.updatetmstatus(mid);
			resultMap.put("status", 0);
		}
		// Map<String, Object> matchMap = writeConstantMapper.findOpenedMatch();
		// String sys_openedMatchMid="";
		// String sys_openedMatchGPS="";
		// if(matchMap!=null){
			// sys_openedMatchMid = matchMap.containsKey("mid")?(String) matchMap.get("mid"):"";
			// sys_openedMatchGPS= matchMap.containsKey("gps")?(String) matchMap.get("gps"):"";
		// }
		// RedisUtil.set("sys_openedMatchMid", sys_openedMatchMid);
		// RedisUtil.set("sys_openedMatchGPS", sys_openedMatchGPS);
		
	}

	public List<Map<String, Object>> findAllMatchUser(String mid) {
		// TODO Auto-generated method stub
		return matchMapper.findAllMatchUser(mid);
	}

	public Match findMatch(String mid) {
		// TODO Auto-generated method stub
		return matchMapper.selectByPrimaryKey(mid);
	}

	public List<Map<String, Object>> findAllMatchList() {
		// TODO Auto-generated method stub
		return matchMapper.findAllMatchList();
	}

	public List<Map<String, Object>> findstartMatch() {
		// TODO Auto-generated method stub
		return writeConstantMapper.findOpenedMatch();
	}

	public int findMachname(String mname) {
		// TODO Auto-generated method stub
		return matchMapper.findMachname(mname);
	}

	public List<Map<String, Object>> findAllMatch() {
		// TODO Auto-generated method stub
		return matchMapper.findAllMatch();
	}

	public void addUserByMid(ReuserMatch reuserMatch, JSONArray array) {
		// TODO Auto-generated method stub
		for (int i = 0; i < array.size(); i++) {
			JSONObject json = array.getJSONObject(i);
			String uid=json.getString("uid");
			reuserMatch.setUid(uid);
			reuserMatchMapper.insertSelective(reuserMatch);
		}
	}

	public List<Map<String, Object>> findMatchUserByMid(String mid) {
		// TODO Auto-generated method stub
		return reuserMatchMapper.findMatchUserByMid(mid);
	}

	public void deleteMatchUserByUid(ReuserMatchKey reuserMatchKey) {
		// TODO Auto-generated method stub
		reuserMatchMapper.deleteByPrimaryKey(reuserMatchKey);
	}

	public List<Map<String, Object>> findNotMatchUserByMid(String mid) {
		// TODO Auto-generated method stub
		return reuserMatchMapper.findNotMatchUserByMid(mid);
	}

	

}
