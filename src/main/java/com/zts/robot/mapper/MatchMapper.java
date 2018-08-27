package com.zts.robot.mapper;

import com.zts.robot.pojo.Match;

public interface MatchMapper extends WriteMatchMapper{
    int deleteByPrimaryKey(String mid);

    int insert(Match record);

    int insertSelective(Match record);

    Match selectByPrimaryKey(String mid);

    int updateByPrimaryKeySelective(Match record);

    int updateByPrimaryKey(Match record);

}