package com.zts.robot.mapper;

import com.zts.robot.pojo.CheckCode;

public interface CheckCodeMapper extends WriteCheckCodeMapper{
	
    int insert(CheckCode record);

    int insertSelective(CheckCode record);

}