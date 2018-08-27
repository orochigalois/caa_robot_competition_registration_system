package com.zts.robot.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zts.robot.mapper.MatchMapper;
import com.zts.robot.mapper.RaceMapper;
import com.zts.robot.mapper.RaceTeamMemberMapper;
import com.zts.robot.mapper.UserMatchRaceMapper;
import com.zts.robot.pojo.Match;
import com.zts.robot.pojo.Race;
import com.zts.robot.pojo.UserMatchRace;
import com.zts.robot.pojo.UserMatchRaceKey;
import com.zts.robot.util.FileToZip;
import com.zts.robot.util.MyProperties;
import com.zts.robot.util.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class RaceService {
	@Autowired
	private RaceMapper raceMapper;
	@Autowired
	private RaceTeamMemberMapper raceTeamMemberMapper;
	@Autowired
	private UserMatchRaceMapper userMatchRaceMapper;
	@Autowired
	private MatchMapper matchMapper;
	
	public void addRace(Race race) {
		// TODO 自动生成的方法存根
		//赛项编码
		String rcode = "Y"+Tools.getStringByDateAndTime(new Date()).substring(2, 7).replace("-", "")+"R";
		int count = raceMapper.findCountRaceByMid(race.getMid());//获取正常状态下最大编号
		String numStr=String.format("%03d",count+1);
		rcode=rcode+numStr;
		race.setRcode(rcode);
		raceMapper.insertSelective(race);
	}

	public void updateRace(Race race) {
		// TODO 自动生成的方法存根
		raceMapper.updateByPrimaryKeySelective(race);
	}

	public int findRacelistTotalSize(Map<String, Object> paramMap) {
		// TODO 自动生成的方法存根
		return raceMapper.findRacelistTotalSize(paramMap);
	}
	
	public List<Map<String, Object>> findRaceListByPage(Map<String, Object> paramMap) {
		// TODO 自动生成的方法存根
		List<Map<String, Object>> raceList=raceMapper.findRaceListByPage(paramMap);
		return raceList;
	}
	
	public Map<String, Object> findRaceInfoByRid(String rid) {
		// TODO 自动生成的方法存根
		return raceMapper.findRaceInfoByRid(rid);
	}

	public void updateDelflgRaceByRid(String rid, Map<String, Object> resultMap) {
		// TODO 自动生成的方法存根
		//判断当前赛项是否使用中
		int count=raceTeamMemberMapper.findCountByRid(rid);
		if(count>0){
			resultMap.put("status", 1);
			resultMap.put("errmsg", "当前赛项正在使用中，不允许删除！");
		}else{
			raceMapper.updateDelflgRaceByRid(rid);
		}
		
	}

	public List<Map<String, Object>> findPreviousRace() {
		// TODO 自动生成的方法存根
		return raceMapper.findPreviousRace();
	}

	public void addImportRaceByRids(String[] ridArray, String mid, String uid) {
		// TODO 自动生成的方法存根
		
		for(String rid : ridArray){
			Race race = raceMapper.selectByPrimaryKey(rid);
			race.setMid(mid);
			//赛项编码
			String rcode = "Y"+Tools.getStringByDateAndTime(new Date()).substring(2, 7).replace("-", "")+"R";
			int count = raceMapper.findCountRaceByMid(mid);//获取正常状态下的最大号码
			String numStr=String.format("%03d",count+1);
			rcode=rcode+numStr;
			race.setRcode(rcode);
			race.setRid(Tools.get32UUID());
			race.setStartdate("");
			race.setEnddate("");
			race.setCreateuid(uid);
			race.setCreatedate(Tools.getStringByDateAndTime(new Date()));
			raceMapper.insertSelective(race);			
		}
		
	}

	public int findRaceName(String rname, String mid) {
		// TODO Auto-generated method stub
		return raceMapper.findRaceName(rname,mid);
	}

	public List<Map<String, Object>> findRaceFrnameBymid(String mid) {
		// TODO Auto-generated method stub
		return raceMapper.findRaceFrnameBymid(mid);
	}

	public List<Map<String, Object>> findRaceRnameBymidFrname(String mid, String frname) {
		// TODO Auto-generated method stub
		return raceMapper.findRaceRnameBymidFrname(mid,frname);
	}

	public List<Map<String, Object>> findAllRaceRnameBymid(String mid) {
		// TODO Auto-generated method stub
		return raceMapper.findAllRaceRnameBymid(mid);
	}

	public void addRaceTcUserByRid(UserMatchRace userMatchRace, JSONArray array) {
		// TODO Auto-generated method stub
		for (int i = 0; i < array.size(); i++) {
			JSONObject json = array.getJSONObject(i);
			String uid=json.getString("uid");
			userMatchRace.setUid(uid);
			userMatchRaceMapper.insertSelective(userMatchRace);
		}
	}

	public List<Map<String, Object>> findRaceTcUserByRid(String rid) {
		// TODO Auto-generated method stub
		return userMatchRaceMapper.findRaceTcUserByRid(rid);
	}

	public void deleteRaceTcUserByRid(UserMatchRaceKey userMatchRaceKey) {
		// TODO Auto-generated method stub
		userMatchRaceMapper.deleteByPrimaryKey(userMatchRaceKey);
	}
	
	public String removeUnlawfulFileName(String filename) {
		
		return filename.replace("\\", "").replace("/", "").replace(":", "").replace("*", "")
				.replace("?", "").replace("\"", "").replace("<", "").replace(">", "").replace("|", "");
	}
	
	
	public void downloadLog(String mid, Map<String, Object> resultMap) {
		Match match=matchMapper.selectByPrimaryKey(mid);//赛事信息		

		String mname = removeUnlawfulFileName(match.getMname());
		

		String zipFilePath = MyProperties.getKey("RootPathkey")+"zip/"+mname+"_日志/";
		File file = new File(zipFilePath);

		if (file.exists())
		{
			// if folder exists, delete it
			try{
				FileToZip.delete(file);
	        }
			catch(IOException e){
	            e.printStackTrace();
	        }
		}
		
		//create a new folder
		file.mkdirs();
		
		String sourceFilePath = MyProperties.getKey("RootPathkey")+"rcjlogimage/"+mname;				
		String fileName = mname+"_日志ZIP";
		//FileToZip.fileToZip_new(sourceFilePath, zipFilePath, fileName);
		
		/*_____Add by Alex 2018.02.25*/
		File directoryToZip = new File(sourceFilePath);
		String outputZip=zipFilePath+fileName+".zip";
		List<File> fileList = new ArrayList<File>();
		//System.out.println("---Getting references to all files in: " + directoryToZip.getCanonicalPath());

		if (!directoryToZip.exists())
		{
			resultMap.put("status", 1);
			resultMap.put("errmsg", "没有需要下载的文件");
			return;
		}
		
		FileToZip.getAllFiles(directoryToZip, fileList);
		//System.out.println("---Creating zip file");
		FileToZip.writeZipFile(directoryToZip, fileList,outputZip);
		//System.out.println("---Done");
		/*_____Add by Alex 2018.02.25 END*/
		
		
		System.out.println("全部生成赛项日志完毕");
		resultMap.put("status", 0);
	}

}
