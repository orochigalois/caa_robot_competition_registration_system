package com.zts.robot.mapper;

import com.zts.robot.pojo.Race;

public interface RaceMapper extends WriteRaceMapper{
    int deleteByPrimaryKey(String rid);

    int insert(Race record);

    int insertSelective(Race record);

    Race selectByPrimaryKey(String rid);

    int updateByPrimaryKeySelective(Race record);

    int updateByPrimaryKey(Race record);

}