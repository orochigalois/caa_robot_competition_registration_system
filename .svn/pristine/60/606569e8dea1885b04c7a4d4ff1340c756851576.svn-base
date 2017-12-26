package com.zts.robot.mapper;


import java.util.List;
import java.util.Map;

public interface WriteMemberMapper {
	
	String findTmidByMidDid(String mid, String didtype, String did);
	/**
	 * 查询成员信息根据MID、DID、DIDTYPE
	 * @param mid
	 * @param didtype
	 * @param did
	 * @return
	 */
	Map<String, Object> findMemberMapByDidMid(String mid, String didtype, String did);
	
	/**
	 * 查询成员总量
	 * @param paramMap 
	 * @return
	 */
	int findMemberListTotalSize(Map<String, Object> paramMap);
	
	/**
	 * 查询成员列表
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> findMatchListByPage(Map<String, Object> paramMap);
	
	int findAllMemberListByPageTotalSize(Map<String, Object> paramMap);

	List<Map<String, Object>> findAllMemberListByPage(Map<String, Object> paramMap);
	
	void updatetmstatus(String mid);
	
	void updateshutdownmember(String tmid);
	
	List<Map<String, Object>> findUniversitystatisticsMemberByMid(Map<String, Object> condition);

	List<Map<String, Object>> findcareerstatisticsMemberByMid(Map<String, Object> condition);

	List<Map<String, Object>> findhighstatisticsMemberByMid(Map<String, Object> condition);

	List<Map<String, Object>> findjuniorstatisticsMemberByMid(Map<String, Object> condition);

	List<Map<String, Object>> findprimarystatisticsMemberByMid(Map<String, Object> condition);

	List<Map<String, Object>> findkindergartenstatisticsMemberByMid(Map<String, Object> condition);

	List<Map<String, Object>> findotherstatisticsMemberByMid(Map<String, Object> condition);

	List<Map<String, Object>> findtotalstatisticsMemberByMid(Map<String, Object> condition);
	
	Integer findCountByMemberTcode(String tempTcode);
	
	String findAllMemberByTid(String tid, String rid);
	
	String findAllTeacherByTid(String tid, String rid);
	
	String findTeacherEmail(String tid, String rid);
	
	List<Map<String, Object>> findMemberByTid(String tid, String rid);
	
	int findMemberNumByDid(String mid, String string, String string2);
	
	List<Map<String, Object>> findAllMemberBage(String mid);

	
}