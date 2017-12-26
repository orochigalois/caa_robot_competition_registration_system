package com.zts.robot.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zts.robot.mapper.MemberMapper;
import com.zts.robot.mapper.RaceTeamMemberMapper;
import com.zts.robot.pojo.Member;
import com.zts.robot.util.Tools;

@Service
public class MemberService {

	@Autowired
	private MemberMapper mapper;
	@Autowired
	private RaceTeamMemberMapper raceTeamMemberMapper;

	public String findTmidByMidDid(String mid, String didType, String did) {
		
		return mapper.findTmidByMidDid(mid, didType, did);
	}
	
	public Member findMemberInfoByTmid(String tmid) {

		return mapper.selectByPrimaryKey(tmid);
	}

	public void addMember(Member member) {
		// TODO 自动生成的方法存根
		String random;
		String tcode = "Y"+Tools.getStringByDateAndTime(new Date()).substring(2, 7).replace("-", "")+"TM";
		for(;;){
			random= Tools.getRandom(6);
			String tempTcode = tcode+random;
			Integer countTempTcode = mapper.findCountByMemberTcode(tempTcode);
			if(countTempTcode==0){
				tcode=tcode+random;
				break;
			}
		}	
		member.setTmcode(tcode);
		
		mapper.insertSelective(member);
	}

	public void updateMember(Member member) {
		// TODO 自动生成的方法存根
		mapper.updateByPrimaryKeySelective(member);
	}

	public int findMemberListTotalSize(Map<String, Object> paramMap) {
		// TODO 自动生成的方法存根
		return mapper.findMemberListTotalSize(paramMap);
	}

	public List<Map<String, Object>> findMatchListByPage(Map<String, Object> paramMap) {
		// TODO 自动生成的方法存根
		return mapper.findMatchListByPage(paramMap);
	}

	public void updateDelflgMemberByTmid(String tmid, String upduid, Map<String, Object> resultMap) {
		// TODO 自动生成的方法存根
		int count = raceTeamMemberMapper.isJoinRaceByTmid(tmid);
		if(count>0){
			resultMap.put("status", 1);
			resultMap.put("errmsg", "该成员已报名赛项，请从所有队伍移除后，在彻底删除！");
			return;
		}
		/*Member member = new Member();
		member.setTmid(tmid);
		member.setDelflg("01");
		member.setUpduid(upduid);
		member.setUpddate(Tools.getStringByDateAndTime(new Date()));
		mapper.updateByPrimaryKeySelective(member);*/
		mapper.deleteByPrimaryKey(tmid);
		
		raceTeamMemberMapper.delflgByTmid(tmid);
		resultMap.put("status", 0);
	}

	public int findAllMemberListByPageTotalSize(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return mapper.findAllMemberListByPageTotalSize(paramMap);
	}

	public List<Map<String, Object>> findAllMemberListByPage(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return mapper.findAllMemberListByPage(paramMap);
	}

	public Map<String, Object> findMemberMapByDidMid(String mid, String didtype, String did) {
		// TODO 自动生成的方法存根
		return mapper.findMemberMapByDidMid(mid, didtype, did);
	}

	public List<Map<String, Object>> findJoinRaceByTmid(String tmid, String roleflg, String signuid) {
		// TODO 自动生成的方法存根
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if("03".equals(roleflg)){
			list=raceTeamMemberMapper.findJoinRaceByTmidSignuid(tmid,signuid);
		}else{
			list=raceTeamMemberMapper.findJoinRaceByTmid(tmid);
		}
		return list;
	}

	public int findMemberNumByDid(String mid, String didType, String did) {
		// TODO Auto-generated method stub
		return mapper.findMemberNumByDid(mid,didType, did);
	}


}
