package com.zts.robot.mapper;

import java.util.List;
import java.util.Map;

public interface WriteMatchMapper {
	
	/**
	 * 查询赛事列表总条数
	 * @param paramMap 
	 * @return
	 */
	int findMatchlistTotalSize(Map<String, Object> paramMap);
	
	/**
	 * 查询赛事列表
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> findMatchListByPage(Map<String, Object> paramMap);
	
	/**
	 * 查询赛事详情ByMid
	 * @param mid
	 * @return
	 */
	Map<String, Object> findMatchInfoByMid(String mid);
	
	/**
	 * 逻辑删除赛事ByMid
	 * @param mid
	 * @return
	 */
	void updateDelflgMatchByMid(String mid);
	/**
	 * 查询出这个赛事下的所有人
	 * @param mid
	 * @return
	 */
	List<Map<String, Object>> findAllMatchUser(String mid);
	
	int findmstatus();
	
	List<Map<String, Object>> findAllMatchList();
	
	int findMachname(String mname);
	
	List<Map<String, Object>> findAllMatch();

}