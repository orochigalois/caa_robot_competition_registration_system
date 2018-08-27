package com.zts.robot.mapper;

import java.util.List;
import java.util.Map;

public interface WriteAwardsMapper {
	//根据赛项ID删除当前的奖项
	void delAwardsByRid(String rid);
	
	List<Map<String, Object>> findAwardsListByRid(String rid);
	
	int findAwardsNumByRid(String rid);
}