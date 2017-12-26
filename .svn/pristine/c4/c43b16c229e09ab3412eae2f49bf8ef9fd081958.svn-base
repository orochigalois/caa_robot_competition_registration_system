package com.zts.robot.mapper;

public interface WriteCheckCodeMapper {
	//1存在code 0 不存在
	Integer verifyCheckCode(String email, String code);
	
	void modifyCheckCodeByEmail(String email, String code, String usedate);
	
	Integer findcode(String email, String code);
}