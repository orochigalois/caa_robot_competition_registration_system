package com.zts.robot.mapper;

import com.zts.robot.pojo.AreaCode;

public interface AreaCodeMapper extends WriteAreaCodeMapper{
    int deleteByPrimaryKey(String code);

    int insert(AreaCode record);

    int insertSelective(AreaCode record);

    AreaCode selectByPrimaryKey(String code);

    int updateByPrimaryKeySelective(AreaCode record);

    int updateByPrimaryKey(AreaCode record);
}