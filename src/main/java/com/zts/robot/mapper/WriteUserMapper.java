package com.zts.robot.mapper;

import java.util.List;
import java.util.Map;

public interface WriteUserMapper {

	Map<String, Object> LogonByEmail(Map<String, Object> condition);
	
	int findAllregisteredTotalSize(Map<String, Object> paramMap);

	List<Map<String, Object>> findAllregistered(Map<String, Object> paramMap);
	
	int finduser(String email);
	
	void uodateRegisteredUserBatch(Map<String, Object> paramMap);
	
	List<Map<String, Object>> findTCUser(String rid);
}