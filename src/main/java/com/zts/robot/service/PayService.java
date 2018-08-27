package com.zts.robot.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zts.robot.mapper.PayorderMapper;
import com.zts.robot.mapper.RaceTeamMemberMapper;
import com.zts.robot.pojo.Payorder;

@Service
public class PayService {
	@Autowired
	private PayorderMapper payorderMapper;
	@Autowired
	private RaceTeamMemberMapper raceTeamMemberMapper;
	public void PayConsume(Payorder payorder) {
		// TODO 自动生成的方法存根
		payorderMapper.insertSelective(payorder);
	}
	public void PayRcv(Payorder payorder) {
		// TODO 自动生成的方法存根
		payorderMapper.updateByPrimaryKeySelective(payorder);
		System.out.println(payorder.getTxnstatus());
		String mid = payorder.getMid();
		String signuid = payorder.getSignuid();
		//应缴金额
		int yingjje = raceTeamMemberMapper.findyingjjeByMidSiginuid(mid,signuid);
		//已缴支付金额
		int yizhifuconfirmje = payorderMapper.findyizhifuconfirmjeByMidSiginuid(payorder.getMid(),payorder.getSignuid());
		String feestatus="01";
		if(yingjje <= yizhifuconfirmje){
			feestatus="00";			
		}else{
			feestatus="02";
		}
		raceTeamMemberMapper.updateFeestatusBySignuidMid(signuid,mid,feestatus);
	}
	public int signuidPayListByMidTotalSize(Map<String, Object> paramMap) {
		// TODO 自动生成的方法存根
		return payorderMapper.signuidPayListByMidTotalSize(paramMap);
	}
	public List<Map<String, Object>> signuidPayListByMid(Map<String, Object> paramMap) {
		// TODO 自动生成的方法存根
		return payorderMapper.signuidPayListByMid(paramMap);
	}

}
