package com.zts.robot.mapper;

import com.zts.robot.pojo.User;

public interface UserMapper extends WriteUserMapper{
    int deleteByPrimaryKey(String uid);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String uid);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}