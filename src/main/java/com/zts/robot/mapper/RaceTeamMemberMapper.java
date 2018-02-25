package com.zts.robot.mapper;

import java.util.Map;

import com.zts.robot.pojo.RaceTeamMember;
import com.zts.robot.pojo.RaceTeamMemberKey;

public interface RaceTeamMemberMapper extends WriteRaceTeamMemberMapper{
    int deleteByPrimaryKey(RaceTeamMemberKey key);

    int insert(RaceTeamMember record);

    int insertSelective(RaceTeamMember record);

    RaceTeamMember selectByPrimaryKey(RaceTeamMemberKey key);

    int updateByPrimaryKeySelective(RaceTeamMember record);

    int updateByPrimaryKey(RaceTeamMember record);
    //insert 队员和赛项时查询Tcode是否重复
    int findCountByRaceTeamMemberTcode(String tcode);
   
}