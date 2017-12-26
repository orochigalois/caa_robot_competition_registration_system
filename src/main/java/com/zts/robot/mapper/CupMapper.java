package com.zts.robot.mapper;

import com.zts.robot.pojo.Cup;
import com.zts.robot.pojo.CupKey;

public interface CupMapper extends WriteCupMapper{
    int deleteByPrimaryKey(CupKey key);

    int insert(Cup record);

    int insertSelective(Cup record);

    Cup selectByPrimaryKey(CupKey key);

    int updateByPrimaryKeySelective(Cup record);

    int updateByPrimaryKey(Cup record);

}