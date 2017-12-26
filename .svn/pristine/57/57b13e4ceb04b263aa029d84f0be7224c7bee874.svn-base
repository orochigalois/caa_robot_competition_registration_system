package com.zts.robot.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zts.robot.mapper.MemberMapper;
import com.zts.robot.mapper.RaceTeamMemberMapper;
import com.zts.robot.mapper.TeamMapper;
import com.zts.robot.mapper.UserMapper;
import com.zts.robot.mapper.UserMatchRaceMapper;
import com.zts.robot.pojo.User;
@Service
public class UserService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private TeamMapper teamMapper;
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private RaceTeamMemberMapper raceTeamMemberMapper;
	@Autowired
	private UserMatchRaceMapper userMatchRaceMapper;

	public User findLoginUser(String uid) {
		// TODO Auto-generated method stub
		return userMapper.selectByPrimaryKey(uid);
	}

	public void uodateLoginUser(User user) {
		// TODO Auto-generated method stub
		userMapper.updateByPrimaryKeySelective(user);
	}

	public int findAllregisteredTotalSize(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return userMapper.findAllregisteredTotalSize(paramMap);
	}

	public List<Map<String, Object>> findAllregistered(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return userMapper.findAllregistered(paramMap);
	}

	public void addUser(User user, Map<String, Object> resultMap) {
		// TODO Auto-generated method stub
		int count=userMapper.finduser(user.getEmail());
		if(count>0){
			resultMap.put("status", 1);
			resultMap.put("errmsg", "此人存在 ，不允许再次添加！");
		}else{
			userMapper.insertSelective(user);
			resultMap.put("status", 0);
		}
	}

	public void uodateRegisteredUserBatch(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		if("02".equals(paramMap.get("status"))){		
			String[] uid=(String[]) paramMap.get("uid");
			for (int i = 0; i < uid.length; i++) {
				String signuid=uid[i];
				List<Map<String, Object>> memberList=raceTeamMemberMapper.findMemberBySignuid(signuid);//查询这个注册人下的所有成员
				for(Map<String, Object> map:memberList){
					int count=raceTeamMemberMapper.findCountSignuidByTmid( map.get("tmid").toString(),signuid);//查询这个成员是否在别的注册人下
					if(count<1){
						//删除成员
						memberMapper.deleteByPrimaryKey(map.get("tmid").toString());
					}
				}				
				//删除队伍
				teamMapper.deleteTeamBySignuid(signuid);
				//删除对应关系
				raceTeamMemberMapper.deleteMemberBySignuid(paramMap);
			}	
		}
		userMapper.uodateRegisteredUserBatch(paramMap);
	}

	public List<Map<String, Object>> findTCUser(String rid) {
		// TODO Auto-generated method stub
		return userMapper.findTCUser(rid);
	}

	public List<Map<String, Object>> findRaceByUid(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return userMatchRaceMapper.findRaceByUid(map);
	}

	public int findRaceByUidTotalSize(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return userMatchRaceMapper.findRaceByUidTotalSize(map);
	}

	public List<Map<String, Object>> findMatchByUid(String uid) {
		// TODO Auto-generated method stub
		return raceTeamMemberMapper.findMatchByUid(uid);
	}


}
