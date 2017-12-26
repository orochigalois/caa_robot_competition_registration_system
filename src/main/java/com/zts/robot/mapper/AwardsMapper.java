package com.zts.robot.mapper;

import com.zts.robot.pojo.Awards;
import com.zts.robot.pojo.AwardsKey;

public interface AwardsMapper extends WriteAwardsMapper{
    int deleteByPrimaryKey(AwardsKey key);

    int insert(Awards record);

    int insertSelective(Awards record);

    Awards selectByPrimaryKey(AwardsKey key);

    int updateByPrimaryKeySelective(Awards record);

    int updateByPrimaryKey(Awards record);
    
}