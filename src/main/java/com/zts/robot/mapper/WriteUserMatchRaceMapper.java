package com.zts.robot.mapper;

import java.util.List;
import java.util.Map;

public interface WriteUserMatchRaceMapper {
	
	List<Map<String, Object>> findRaceTcUserByRid(String rid);
	
	List<Map<String, Object>> findRaceByUid(Map<String, Object> map);
	
	int findRaceByUidTotalSize(Map<String, Object> map);

}