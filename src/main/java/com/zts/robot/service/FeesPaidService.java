package com.zts.robot.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zts.robot.mapper.PayorderMapper;
import com.zts.robot.mapper.RaceTeamMemberMapper;

@Service
public class FeesPaidService {
	@Autowired
	private RaceTeamMemberMapper raceTeamMemberMapper;
	@Autowired
	private PayorderMapper payorderMapper;
	
	public List<Map<String, Object>> findMemberRaceListByMidSiginuid(String mid, String signuid) {
		// TODO 自动生成的方法存根		
		return raceTeamMemberMapper.findMemberRaceListByMidSiginuid(mid,signuid);
	}

	public void findTotalCostByMidSiginuid(String mid, String signuid, Map<String, Object> resultMap) {
		// TODO 自动生成的方法存根
		//应缴金额
		int yingjje = raceTeamMemberMapper.findyingjjeByMidSiginuid(mid,signuid);
		//已缴支付金额
		int yizhifuconfirmje = payorderMapper.findyizhifuconfirmjeByMidSiginuid(mid,signuid);
		//待缴支付金额
		int yizhifunotconfirmje = payorderMapper.findyizhifunotconfirmjeByMidSiginuid(mid,signuid);
		//未缴金额
		int daijje = yingjje-yizhifuconfirmje-yizhifunotconfirmje;
		resultMap.put("yingjje", yingjje);
		resultMap.put("yizhifuconfirmje", yizhifuconfirmje);
		resultMap.put("yizhifunotconfirmje", yizhifunotconfirmje);
		resultMap.put("daijje", daijje);
	}

	public void findOrderListByMidSiginuid(String mid, String signuid, Map<String, Object> resultMap) {
		// TODO 自动生成的方法存根
		List<Map<String, Object>> orderList = payorderMapper.findOrderListByMidSiginuid(mid,signuid);
		resultMap.put("list", orderList);
	}
	
}
