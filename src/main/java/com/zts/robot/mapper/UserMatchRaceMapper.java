package com.zts.robot.mapper;

import com.zts.robot.pojo.UserMatchRace;
import com.zts.robot.pojo.UserMatchRaceKey;

public interface UserMatchRaceMapper extends WriteUserMatchRaceMapper{
    int deleteByPrimaryKey(UserMatchRaceKey key);

    int insert(UserMatchRace record);

    int insertSelective(UserMatchRace record);

    UserMatchRace selectByPrimaryKey(UserMatchRaceKey key);

    int updateByPrimaryKeySelective(UserMatchRace record);

    int updateByPrimaryKey(UserMatchRace record);
}