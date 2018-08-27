package com.zts.robot.mapper;

import java.util.List;
import java.util.Map;

public interface WriteRaceMapper {
	/**
	 * 查询赛项总条数
	 * @param paramMap 
	 * @return
	 */
	int findRacelistTotalSize(Map<String, Object> paramMap);
	
	/**
	 * 查询赛项列表
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> findRaceListByPage(Map<String, Object> paramMap);
	
	/**
	 * 查询赛项信息ByRid
	 * @param rid
	 * @return
	 */
	Map<String, Object> findRaceInfoByRid(String rid);
	
	/**
	 * 逻辑删除赛项ByRid
	 * @param rid
	 */
	void updateDelflgRaceByRid(String rid);
	
	/**
	 * 查询往届赛事赛项
	 * @return 
	 */
	List<Map<String, Object>> findPreviousRace();
	
	int findRaceName(String rname, String mid);
	
	List<Map<String, Object>> findRaceFrnameBymid(String mid);
	
	List<Map<String, Object>> findRaceRnameBymidFrname(String mid, String frname);
	
	int findCountRaceByMid(String mid);
	
	List<Map<String, Object>> findAllRaceRnameBymid(String mid);
}