package com.zts.robot.mapper;

import java.util.List;
import java.util.Map;

public interface WriteRaceTeamScoreMapper {

	List<Map<String, Object>> findTeamScoreByRid(Map<String, Object> map);	

	Map<String, Object> findRaceTeamNumByRid(String rid);
	
	List<Map<String, Object>> findTeamCitationByRid(String rid);
	
	List<Map<String, Object>> findTeamScoreByRidBySetScore(Map<String, Object> map);
	
	List<Map<String, Object>> teamMemberScoreByMid(String mid);
	
	List<Map<String, Object>> teamScoreByMid(String string);

	List<Map<String, Object>> findAwardsListByMidRid(String mid, String rid);
	
	List<Map<String, Object>> findCupListByMidRid(String mid, String rid);
	
	List<Map<String, Object>> findEntryListByMidRid(String mid, String rid);
	
}