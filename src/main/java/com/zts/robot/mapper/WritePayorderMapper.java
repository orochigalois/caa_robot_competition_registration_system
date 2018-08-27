package com.zts.robot.mapper;

import java.util.List;
import java.util.Map;

public interface WritePayorderMapper {
	int findyizhifuconfirmjeByMidSiginuid(String mid, String signuid);
	
	int findyizhifunotconfirmjeByMidSiginuid(String mid, String signuid);
	
	List<Map<String, Object>> findOrderListByMidSiginuid(String mid, String signuid);
	
	int signuidPayListByMidTotalSize(Map<String, Object> paramMap);
	
	List<Map<String, Object>> signuidPayListByMid(Map<String, Object> paramMap);
}
