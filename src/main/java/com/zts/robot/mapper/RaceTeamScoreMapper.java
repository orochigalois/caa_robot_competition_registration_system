package com.zts.robot.mapper;

import com.zts.robot.pojo.RaceTeamScore;
import com.zts.robot.pojo.RaceTeamScoreKey;

public interface RaceTeamScoreMapper extends WriteRaceTeamScoreMapper{
    int deleteByPrimaryKey(RaceTeamScoreKey key);

    int insert(RaceTeamScore record);

    int insertSelective(RaceTeamScore record);

    RaceTeamScore selectByPrimaryKey(RaceTeamScoreKey key);

    int updateByPrimaryKeySelective(RaceTeamScore record);

    int updateByPrimaryKey(RaceTeamScore record);


}