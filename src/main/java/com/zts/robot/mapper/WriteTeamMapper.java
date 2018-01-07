package com.zts.robot.mapper;

import java.util.Map;

public interface WriteTeamMapper {
	
	void updatetstatus(String mid);
	
	void updatedelflg(String tid);
	
	int findTeamName(String tname, String mid, String rid);
	
	void updateshutdownteam(Map<String, Object> paramMap);
	
	Integer findCountByTempTcode(String tempTcode);
	
	void deleteTeamBySignuid(String signuid);

}