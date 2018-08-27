package com.zts.robot.mapper;

import com.zts.robot.pojo.Folk;

public interface FolkMapper {
    int deleteByPrimaryKey(String folkid);

    int insert(Folk record);

    int insertSelective(Folk record);

    Folk selectByPrimaryKey(String folkid);

    int updateByPrimaryKeySelective(Folk record);

    int updateByPrimaryKey(Folk record);
}