package com.zts.robot.service;

import java.util.Date;
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
public class TeamService {
	@Autowired
	private TeamMapper mapper;
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private RaceTeamMemberMapper rtmMapper;
	@Autowired
	private RaceTeamMemberMapper raceTeamMemberMapper;
	@Autowired
	private MatchMapper matchMapper;

	public void regTeam(Team team, JSONArray array, String rid, Map<String, Object> resultMap) throws Exception {
		
		String date = Tools.getStringByDateAndTime(new Date());
		String mid = team.getMid();
		String uid = team.getCreateuid();
		String tid = Tools.get32UUID();
		team.setTid(tid);
		team.setCreatedate(date);
		team.setTstatus("00");
		team.setDelflg("00");
		
		mapper.insertSelective(team);
		//赛事信息 
		Match match=matchMapper.selectByPrimaryKey(mid);
		
		//队伍编码
		String tcode = "Y"+Tools.getStringByDateAndTime(new Date()).substring(2, 7).replace("-", "")+"T";
		String random;
		for(;;){
			random= Tools.getRandom(5);
			String tempTcode = tcode+random;
			Integer countTempTcode = rtmMapper.findCountByRaceTeamMemberTcode(tempTcode);
			if(countTempTcode==0){
				tcode=tcode+random;
				break;
			}
		}
		
		//批量插入队员
		for(int i=0;i<array.size();i++) {
			JSONObject json = array.getJSONObject(i);
			//判断该人是否存在其他赛事
			String alreadyflg="";
			int num=memberMapper.findMemberNumByDid(mid,json.getString("didtype"), json.getString("did"));
			if(num>0){
				alreadyflg="00";
			}else{
				alreadyflg="01";
			}
			//针对赛事批量插入队员
			String tmid = memberMapper.findTmidByMidDid(mid, json.getString("didtype"), json.getString("did"));//判断该人是否存在当前赛事
			Member member = (Member) JSONObject.toBean(json, Member.class);			
			if (tmid == null) {
				tmid = Tools.get32UUID();				
				member.setTmstatus("00"); //00正常 01 关闭
				member.setCkstatus("01"); //00已签到 01未签到
				member.setTmid(tmid);
				member.setCreateuid(uid);
				member.setCreatedate(date);
				member.setDelflg("00");
				member.setAlreadyflg(alreadyflg);
				String random1;
				String tcode1 = "Y"+Tools.getStringByDateAndTime(new Date()).substring(2, 7).replace("-", "")+"TM";
				for(;;){
					random1= Tools.getRandom(6);
					String tempTcode = tcode1+random1;
					Integer countTempTcode = memberMapper.findCountByMemberTcode(tempTcode);
					if(countTempTcode==0){
						tcode1=tcode1+random1;
						break;
					}
				}	
				member.setTmcode(tcode);
				memberMapper.insertSelective(member);
			}else{
				Map<String, Object> memberMap= memberMapper.findMemberMapByDidMid(mid, json.getString("didtype"), json.getString("did"));
				//修改该成员的是否参赛过标志位
				/*Member member2=new Member();
				member2.setTmid(tmid);
				member2.setAlreadyflg(alreadyflg);
				memberMapper.updateByPrimaryKey(member2);*/
				
				if(!member.getRoleflg().equals(memberMap.get("roleflg"))){
					String errmsg;
					if("01".equals(memberMap.get("roleflg"))){
						//教师
						errmsg =  member.getTmname()+"该成员身份类型为指导教师，不能再添加为队员！";
					}else{
						errmsg =  member.getTmname()+"该成员身份类型为队员，不能再添加为指导教师！";
					}
					resultMap.put("errmsg",errmsg);
					throw new RuntimeException();
				}
				//身份对应，教师可以重复报赛项，学生不可以重复报赛项在新队伍中
				if("02".equals(memberMap.get("roleflg"))){
					int count = raceTeamMemberMapper.isSignUpRaceByRidTmid(rid,(String)memberMap.get("tmid"));
					if(count>0){
						resultMap.put("errmsg",member.getTmname()+"该成员已经报名过该赛项，不能重复报名！");
						throw new RuntimeException();
					}
				}
			}
			/*String roleflg=member.getRoleflg();
			int num=rtmMapper.findSerialnumByTid(serialnum,tid,roleflg);
			if(num>0){
				resultMap.put("status", 1);
				resultMap.put("errmsg", member.getTmname()+"该成员的序列号存在，不允许再次添加，请进行修改！");
				throw new RuntimeException();
			}else{
				
			}*/
			//针对赛项插入队员名单
			RaceTeamMember rtm = new RaceTeamMember();
			rtm.setRid(rid);
			rtm.setInfostatus("01");
			rtm.setFeestatus("01");
			rtm.setMid(mid);
			rtm.setSignuid(uid);
			rtm.setTid(tid);
			rtm.setTmid(tmid);
			rtm.setCreatedate(date.substring(0, 16));
			rtm.setSerialnum(Integer.parseInt(json.getString("serialnum")));
			rtm.setUnitprice(match.getUnitprice());
			rtm.setTcode(tcode);
			rtmMapper.insertSelective(rtm);
			resultMap.put("status", 0);
		}
		
	}

	public void saveTeamInfo(Team team, String upduid) {
		team.setUpddate(Tools.getStringByDateAndTime(new Date()));
		team.setUpduid(upduid);
		mapper.updateByPrimaryKeySelective(team);
		
		//修改队伍审批状态
		rtmMapper.changeInfoStatusByTid(team.getTid(), "01");
	}

	public void delTeamByTid(String tid) {
		//查询这个队伍下有哪些成员
		List<Map<String, Object>> memberList=rtmMapper.findAllMembersByTid(tid);
		//从队伍成员关系表删除队伍成员关系记录
		rtmMapper.deleteMemeberByTid(tid);
		for(Map<String, Object> map : memberList){
			String tmid=map.get("tmid").toString();
			int count=rtmMapper.findCountByTmid(tmid);//这个成员是否还存在其他赛项
			if(count<1){
				//删除成员
				memberMapper.deleteByPrimaryKey(tmid);
			}
		}
		//从队伍表删除队伍
		mapper.deleteByPrimaryKey(tid);
	}

	public void delRaceTeamByTid(String tid, String rid) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> memberList=rtmMapper.findAllMembersByTid(tid);//查询这个队伍下的成员
		//从队伍成员关系表删除队伍成员关系记录   取消报名
		rtmMapper.delRaceTeamByTid(tid,rid);
		
		List<Map<String, Object>> list=rtmMapper.findAllTeamsRace(tid);//查询这个队伍是否还有赛项
		if(list.size()<1){			
			for(Map<String, Object> map : memberList){
				String tmid=map.get("tmid").toString();
				int count=rtmMapper.findCountByTmid(tmid);//取消报名后  这个成员是否还存在其他赛项
				if(count<1){
					//删除成员
					memberMapper.deleteByPrimaryKey(tmid);
				}
			}
			mapper.deleteByPrimaryKey(tid);//删除队伍 逻辑
		}	
		
		
	}

	public int findTeamName(String tname, String mid) {
		// TODO Auto-generated method stub
		return mapper.findTeamName(tname,mid);
	}

	public Integer findCountByTempTcode(String tempTcode) {
		// TODO 自动生成的方法存根
		return mapper.findCountByTempTcode(tempTcode);
	}
	
}
