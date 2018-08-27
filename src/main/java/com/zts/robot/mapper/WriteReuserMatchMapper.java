package com.zts.robot.mapper;

import java.util.List;
import java.util.Map;

public interface WriteReuserMatchMapper {
	List<Map<String, Object>> findMatchUserByMid(String mid);
	
	List<Map<String, Object>> findNotMatchUserByMid(String mid);
}