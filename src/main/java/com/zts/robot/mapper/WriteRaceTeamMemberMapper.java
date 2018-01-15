package com.zts.robot.mapper;

import java.util.List;
import java.util.Map;

public interface WriteRaceTeamMemberMapper {
	// 校验赛项是否有该队员存在
	int checkMemberOfRace(String rid, String tmid);
	// 查询竞赛项下队伍编号
	String selectbyRTMTcode(String rid, String tid);
	//根据tid查询以报赛项列表
	List<Map<String, Object>> findRidListOfTid(String tid);
	//修改队伍审批状态
	void changeInfoStatusByTid(String tid, String infoStatus);
	//从队伍中移除队员
	void deleteMemeberByTidAndTmid(String tid, String tmid);
	//查询赛项成员列表
	List<Map<String, Object>> findRaceAllMembersByRidTid(String tid,String rid);
	//查询所在队伍的所有成员
	List<Map<String, Object>> findAllMembersByTid(String tid);
	//根据队伍id获取报名人ID
	String findSignuidByTid(String tid);
	//查询报名人所有成员列表总数
	int getTotalAllMemberBySignuid(String signuid, String mid);
	//分页查询报名人所有成员列表
	List<Map<String, Object>> findAllMembersBySignuid(Map<String, Object> condition);
	//查询报名人队伍列表
	List<Map<String, Object>> findTeamsBySignuid(String signuid, String mid);
	//删除记录通过TID
	void deleteMemeberByTid(String tid);
	/**
	 * 根据tmid删除该成员参赛关系表
	 * @param tmid
	 */
	void delflgByTmid(String tmid);
	
	//报名RCJ青少年项目队伍
	int findTeamsToRCJBySignuidTotalSize(Map<String, Object> paramMap);

	//报名RCJ青少年项目队伍
	List<Map<String, Object>> findTeamsToRCJBySignuidPages(Map<String, Object> paramMap);
	
	int findTeamsBySignuidTotalSize(Map<String, Object> paramMap);
	
	List<Map<String, Object>> findTeamsBySignuidPages(Map<String, Object> paramMap);
	
	List<Map<String, Object>> findoneteamper(String tid);
	
	int findperson(String rid, String tid, String string);
	
	int findAllTeamsBySignuidPagesTotalSize(Map<String, Object> paramMap);

	List<Map<String, Object>> findAllTeamsBySignuidPages(Map<String, Object> paramMap);
	
	void updateInfostatus(Map<String, Object> paramMap);
	
	List<Map<String, Object>> findAllTeamsRace(String tid);
	
	void delRaceTeamByTid(String tid, String rid);
	
	int isSignUpRaceByRidTmid(String rid, String string);	

	int isJoinRaceByTmid(String tmid);
	
	List<Map<String, Object>> findJoinRaceByTmid(String tmid);
	
	List<Map<String, Object>> findJoinRaceByTmidSignuid(String tmid, String signuid);
	
	void deleteMemberBySignuid(Map<String, Object> paramMap);
	
	List<Map<String, Object>> getTeamDerivedByMid(Map<String, Object> paramMap);
	
	List<Map<String, Object>> getMemberDerivedByMid(Map<String, Object> paramMap);
	
	List<Map<String, Object>> getTeamRaceDerivedByMid(Map<String, Object> paramMap);

	void updateFeestatusByTeamRace(String tid, String rid, String feestatus);
	
	String findFeestatusByTeamRace(String tid, String rid);
	
	int findCountByTmid(String tmid);
	
	List<Map<String, Object>> findMemberBySignuid(String signuid);
	
	int findCountSignuidByTmid(String tmid, String signuid);
	
	int findCountByRid(String rid);
	
	int findSerialnumByTid(int serialnum, String tid, String roleflg);
	
	void updateSerialnumByTid(String tmid, int serialnum, String tid, String rid);
	
	int findTeamCountByRid(String rid);
	
	List<Map<String, Object>> findRaceTeamMemberByRidTid(String rid, String tid);
	
	int repeatedCount(String rid, String tid, String entryno);
	
	void updateEntryno(String rid, String tid, String tmid, String entryno);	

	void updateEntryurl(String rid, String tid, String tmid, String savePdfPath);
	
	List<String> findEntryUrlList(String rid, String tid);
	
	List<Map<String, Object>> findMemberRaceListByMidSiginuid(String mid, String signuid);
	
	int findyingjjeByMidSiginuid(String mid, String signuid);
	
	List<Map<String, Object>> findUnitpriceByTid(String tid, String rid);
	
	void updateUnitprice(String unitprice, String tmid, String signuid);
	
	void updateUnitpriceByMid(int unitprice, String mid);
	
	void updateFeestatusBySignuidMid(String signuid, String mid, String feestatus);
	
	List<Map<String, Object>> findMatchByUid(String uid);
	
	List<Map<String, Object>> findAllTidByTmid(String tmid, String mid);

}