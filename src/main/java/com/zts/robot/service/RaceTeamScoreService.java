package com.zts.robot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zts.robot.mapper.AwardsMapper;
import com.zts.robot.mapper.CupMapper;
import com.zts.robot.mapper.RaceMapper;
import com.zts.robot.mapper.RaceTeamMemberMapper;
import com.zts.robot.mapper.RaceTeamScoreMapper;
import com.zts.robot.pojo.Race;
import com.zts.robot.pojo.RaceTeamScore;
import com.zts.robot.pojo.RaceTeamScoreKey;
import com.zts.robot.util.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class RaceTeamScoreService {
	@Autowired
	private RaceTeamScoreMapper raceTeamScoreMapper;
	@Autowired
	private AwardsMapper awardsMapper;	
	@Autowired
	private CupMapper cupMapper;
	@Autowired
	private RaceMapper raceMapper;
	@Autowired
	private RaceTeamMemberMapper raceTeamMemberMapper;
	
	public void addTeamScore(JSONArray array, String mid, Map<String, Object> resultMap) {
		// TODO Auto-generated method stub
		String rid = null;
		for(int i=0;i<array.size();i++) {
			JSONObject json = array.getJSONObject(i);
			rid=json.getString("rid");
			String tid=json.getString("tid");
			int score=Integer.parseInt(json.getString("score"));
			String flg=json.getString("flg");
			RaceTeamScoreKey raceTeamScoreKey=new RaceTeamScoreKey();
			raceTeamScoreKey.setRid(rid);
			raceTeamScoreKey.setTid(tid);
			raceTeamScoreMapper.deleteByPrimaryKey(raceTeamScoreKey);
			RaceTeamScore raceTeamScore=new RaceTeamScore();
			raceTeamScore.setRid(rid);
			raceTeamScore.setTid(tid);
			raceTeamScore.setScore(score);
			raceTeamScore.setFlg(flg);
			raceTeamScore.setMid(mid);
			raceTeamScoreMapper.insertSelective(raceTeamScore);
		}
		resultMap.put("rid", rid);
		
		
	}
	
	public void editnumber(String rid) {
		// TODO 自动生成的方法存根
		Race race = raceMapper.selectByPrimaryKey(rid);
		String rcode = race.getRcode();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rid", rid);
		List<Map<String, Object>> list = raceTeamScoreMapper.findTeamScoreByRid(map);
		int i =1;
		for (Map<String, Object> maps:list) {
			String no = String.format("%04d",i);
			i++;
				String tid=maps.get("tid").toString();
				RaceTeamScore raceTeamScore=new RaceTeamScore();
				raceTeamScore.setRid(rid);
				raceTeamScore.setTid(tid);
				raceTeamScore.setCupno(rcode+"C"+no);
				raceTeamScore.setAwardsno(rcode+"A"+no);
				raceTeamScoreMapper.updateByPrimaryKeySelective(raceTeamScore);
				//生成参赛证明编号
				/*
				 * Y1706R027E 0001 001参赛证明编号
				 * 赛项ID E 0001队伍排序 serialnum排序树
				 0、查询该赛项下的成员
				 1、生成参赛证明编号
				 2、检测赛项下得编号是否重复
				 3、重复，重新生成
				 4、保存编号
				 */
				List<Map<String, Object>> raceTeamMemberList= raceTeamMemberMapper.findRaceTeamMemberByRidTid(rid,tid);
				for(Map<String, Object> raceTeamMemberMap:raceTeamMemberList){
					String serialnum;
					String entryno;
					boolean repeated=false;
					String tmid = (String) raceTeamMemberMap.get("tmid");
					//生成
					for(;;){						
						if(!repeated && raceTeamMemberMap.containsKey("serialnum")&&raceTeamMemberMap.get("serialnum")!=null && !"".equals(raceTeamMemberMap.get("serialnum"))){
							serialnum = String.format("%03d",raceTeamMemberMap.get("serialnum"));													
						}else{
							serialnum = Tools.getRandom(3);						
						}
						entryno = rcode+"E"+no+serialnum;
						if(raceTeamMemberMapper.repeatedCount(rid,tid,entryno)==0){							
							break;
						}
						repeated=true;
					}
					raceTeamMemberMapper.updateEntryno(rid,tid,tmid,entryno);					
				}
			
		}
	}
	
	public List<Map<String, Object>> findTeamScoreByRid(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return raceTeamScoreMapper.findTeamScoreByRid(map);
	}


	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findScoreRankingByRid(HttpServletRequest request, Map<String, Object> resultMap) {
		// TODO 自动生成的方法存根
		Map<String, Object> map = new HashMap<String, Object>();
		String rid=request.getParameter("rid");
		String modelflg = request.getParameter("modelflg");
		map.put("rid", rid);
		map.put("tname", "");
		//进行奖项计算的队伍数量
		int teamnum = Integer.parseInt(request.getParameter("teamnum"));
		//该赛项下的参赛队伍
		List<Map<String, Object>> teamList = raceTeamScoreMapper.findTeamScoreByRid(map);
		//检测是否有成绩
			if(!teamList.get(0).containsKey("score")||teamList.get(0).get("score")==null || "0".equals(teamList.get(0).get("score"))){
				resultMap.put("status", 1);
				resultMap.put("errmsg", "该赛项参赛队成绩未录入！");
				return null;
			}
			
		
		List<Map<String, Object>> awardsList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> distributionAwardsList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> cupList = new ArrayList<Map<String,Object>>();
		

		if("00".equals(modelflg)){
			//成绩单查看，数据库检索	
			awardsList = awardsMapper.findAwardsListByRid(rid);
			if(awardsList.size()==0){
				resultMap.put("status", 1);
				resultMap.put("errmsg", "该赛项未进行奖项设置，请设置完成后查看成绩！");
				return null;
			}else{
				int i=0;
				for(Map<String, Object> awardsMap:awardsList){
					 i=i+(int)awardsMap.get("aproportion");
				}
				if(i>teamnum){
					resultMap.put("status", 1);
					resultMap.put("errmsg", "该赛项下的合格队伍数量有变化，请重新进行奖项设置后查看成绩！");
					return null;
				}
			}
			cupList = cupMapper.findCupListByrid(rid);
			if(cupList.size()==0){
				resultMap.put("status", 1);
				resultMap.put("errmsg", "该赛项未进行奖杯设置，请设置完成后查看成绩！");
				return null;
			}
			
		}else if("01".equals(modelflg)){
			//奖项预览，界面传值
			String 	awardsJson = request.getParameter("awardsJson");
			JSONArray awardsJSONArray = JSONArray.fromObject(awardsJson);
			for(int i = 0 ;i<awardsJSONArray.size();i++){
				Map<String, Object> tempMap = (Map<String, Object>) awardsJSONArray.get(i);
				awardsList.add(tempMap);
			}
			 
		}else if("02".equals(modelflg)){
			//奖杯预览，界面传值
			String 	cupJson = request.getParameter("cupJson");
			JSONArray cupJSONArray = JSONArray.fromObject(cupJson);
			for(int i = 0 ;i<cupJSONArray.size();i++){
				Map<String, Object> tempMap = (Map<String, Object>) cupJSONArray.get(i);
				cupList.add(tempMap);
			}
		}
		//处理奖项数量
		if(awardsList.size()!=0){
			for(Map<String, Object> awardsMap:awardsList){
				//四舍五入（队伍人数*比例数/100）
				 /*int count = new BigDecimal(teamnum*(int)awardsMap.get("aproportion")/100).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();*/
				 int count = (int) awardsMap.get("aproportion");
				 for(int i = 0;i<count;i++){
					 distributionAwardsList.add(awardsMap);
				 }
			}
		}else{
			Map<String, Object> awardsMap = new HashMap<String, Object>();
			awardsMap.put("aname", null);
			for(int i = 0;i<teamnum;i++){
				distributionAwardsList.add(awardsMap);
			}
		}
		
		List<Map<String, Object>> tempList=new ArrayList<Map<String,Object>>();
		//第一次 分配奖项
		for(int i=0;i<teamList.size();i++){
			Map<String, Object> scoreRankingMap = teamList.get(i);
			if("00".equals(scoreRankingMap.get("flg"))){
				
				if(i<distributionAwardsList.size()){
					scoreRankingMap.put("awards", distributionAwardsList.get(i).get("aname"));
				}else{
					scoreRankingMap.put("awards", null);
				}
				
			}else{
				scoreRankingMap.put("awards", null);
				scoreRankingMap.put("cup", null);
			}
			tempList.add(scoreRankingMap);
		}
		//第二次 处理成绩相同的奖项，奖杯
		//成绩单列表
		List<Map<String, Object>> scoreRankingList=new ArrayList<Map<String,Object>>();
		int j=0;
		for(int i=0;i<tempList.size();i++){
			Map<String, Object> scoreRankingMap = tempList.get(i);
			//处理奖杯
			if("00".equals(tempList.get(i).get("flg"))){			
				if(cupList.size()==0){
					scoreRankingMap.put("cup", null);
				}else{
					if(i==0){
						scoreRankingMap.put("cup", cupList.get(j).get("cname"));
						j++;
					}else{
						if(tempList.get(i-1).get("score").equals(scoreRankingMap.get("score"))){
							scoreRankingMap.put("cup", tempList.get(i-1).get("cup"));
						}else{
							if(j<cupList.size()){
								scoreRankingMap.put("cup", cupList.get(j).get("cname"));
								j++;
							}else{
								scoreRankingMap.put("cup", null);
							}						
						}
					}
				}
			}
			//处理奖项
			if(i!=0){
				if(tempList.get(i-1).get("score").equals(scoreRankingMap.get("score"))){
					scoreRankingMap.put("awards", tempList.get(i-1).get("awards"));
				}
			}
			scoreRankingList.add(scoreRankingMap);
		}
		return scoreRankingList;
	}

	public Map<String, Object> findRaceTeamNumByRid(String rid) {
		// TODO 自动生成的方法存根
		return raceTeamScoreMapper.findRaceTeamNumByRid(rid);
	}

	public List<Map<String, Object>> findTeamCitationByRid(String rid) {
		// TODO Auto-generated method stub
		return raceTeamScoreMapper.findTeamCitationByRid(rid);
	}

	public int findCupNumByRid(String rid) {
		// TODO Auto-generated method stub
		return cupMapper.findCupNumByRid(rid);
	}

	public int findAwardsNumByRid(String rid) {
		// TODO Auto-generated method stub
		return awardsMapper.findAwardsNumByRid(rid);
	}

	public List<Map<String, Object>> findTeamScoreByRidBySetScore(Map<String, Object> map) {
		// TODO 自动生成的方法存根
		return raceTeamScoreMapper.findTeamScoreByRidBySetScore(map);
	}

	

}
