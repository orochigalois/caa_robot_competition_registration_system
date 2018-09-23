package com.zts.robot.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;
import com.zts.robot.mail.MailService;
import com.zts.robot.mapper.AwardsMapper;
import com.zts.robot.mapper.CupMapper;
import com.zts.robot.mapper.MatchMapper;
import com.zts.robot.mapper.MemberMapper;
import com.zts.robot.mapper.RaceMapper;
import com.zts.robot.mapper.RaceTeamMemberMapper;
import com.zts.robot.mapper.RaceTeamScoreMapper;
import com.zts.robot.mapper.TeamMapper;
import com.zts.robot.mapper.UserMapper;
import com.zts.robot.pojo.Match;
import com.zts.robot.pojo.Race;
import com.zts.robot.pojo.RaceTeamMember;
import com.zts.robot.pojo.RaceTeamScore;
import com.zts.robot.pojo.RaceTeamScoreKey;
import com.zts.robot.util.FileToZip;
import com.zts.robot.util.MyProperties;
import com.zts.robot.util.PdfModeForPdf;

import net.sf.json.JSONArray;

@Service
public class ModelService {
	@Autowired
	private TeamMapper teamMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private RaceTeamScoreMapper raceTeamScoreMapper;
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private RaceMapper raceMapper;
	@Autowired
	private MatchMapper matchMapper;
	@Autowired
	private CupMapper cupMapper;
	@Autowired
	private AwardsMapper awardsMapper;
	@Autowired
	private  RaceTeamMemberMapper raceTeamMemberMapper;

	public void delete(File file) throws IOException {
 
		for (File childFile : file.listFiles()) {
 
			if (childFile.isDirectory()) {
				delete(childFile);
			} else {
				if (!childFile.delete()) {
					throw new IOException();
				}
			}
		}
 
		if (!file.delete()) {
			throw new IOException();
		}
	}

	
	public void createNewPDFModel(String rid, String mid, JSONArray array, Map<String, Object> resultMap) throws DocumentException, IOException {
		// TODO 自动生成的方法存根
		Match match=matchMapper.selectByPrimaryKey(mid);//赛事信息		
		Race race=raceMapper.selectByPrimaryKey(rid);//赛项信息
		List<Map<String,Object>> cupList = raceTeamScoreMapper.findCupListByMidRid(mid,rid);//奖杯
		List<Map<String,Object>> awardsList = raceTeamScoreMapper.findAwardsListByMidRid(mid,rid);//奖项
		List<Map<String,Object>> entryList = raceTeamScoreMapper.findEntryListByMidRid(mid,rid);//参赛证明
		
		String mname = removeUnlawfulFileName(match.getMname());
		String rname = removeUnlawfulFileName(race.getRname());

		String ifsavePdfPath = MyProperties.getKey("RootPathkey")+"pdf/"+mname+"/"+rname;
		File ifFile = new File(ifsavePdfPath);
		if (ifFile.exists()) {
			// 如果路径存在,则删除
		
			try {
				//Deleting the directory recursively.
				delete(ifFile);
				System.out.println("Directory has been deleted recursively !");
			} catch (IOException e) {
				System.out.println("Problem occurs when deleting the directory ");
				e.printStackTrace();
			}

			
		}


		//奖励证书
		String wordModelPathCup = match.getCupmodel().replace(MyProperties.getKey("RootFileUrlkey"), MyProperties.getKey("RootPathkey"));
		//wordModelPathCup="D:/pdf/奖励证书模板V4.0表单.pdf";
		for(Map<String,Object> map:cupList){
			String tname =removeUnlawfulFileName((String) map.get("tname"));
			String savePdfPath =  MyProperties.getKey("RootPathkey")+"pdf/"+mname+"/"+rname+"/奖励证书/"+mname+rname+tname+"奖励证书.pdf";
			String url =  MyProperties.getKey("RootFileUrlkey")+"pdf/"+mname+"/"+rname+"/奖励证书/"+mname+rname+tname+"奖励证书.pdf";
			File file = new File(savePdfPath);
			if (!file.exists()) {  
                // 如果路径不存在,则创建  
				 file.getParentFile().mkdirs();
            }
			Map<String, String> dataParamMap = new HashMap<String, String>();
			dataParamMap.put("编号", (String) map.get("cupno"));
			
//			String name = (String) map.get("school")+" \n\r "+(String) map.get("tname");
			dataParamMap.put("学校", (String) map.get("school"));
			dataParamMap.put("队伍", (String) map.get("tname"));
//			dataParamMap.put("学校队伍", name);
			
			dataParamMap.put("小项", (String) map.get("rname"));
			dataParamMap.put("奖杯", (String) map.get("cup"));
			dataParamMap.put("教师",(String) map.get("teachers"));	
			dataParamMap.put("队员",(String) map.get("students"));	
			
			PdfModeForPdf.formFdfModeForPdf(dataParamMap, savePdfPath, wordModelPathCup);
			
			
			RaceTeamScore raceTeamScore = new RaceTeamScore();
			raceTeamScore.setTid((String) map.get("tid"));
			raceTeamScore.setRid((String) map.get("rid"));
			raceTeamScore.setCupurl(url);
			raceTeamScore.setCreatstatus("00");
			raceTeamScoreMapper.updateByPrimaryKeySelective(raceTeamScore);
			
		}
		
		//获奖证书
				String wordModelPathAwards = match.getAwardsmodel().replace(MyProperties.getKey("RootFileUrlkey"), MyProperties.getKey("RootPathkey"));
				//wordModelPathAwards="D:/pdf/获奖证书模板V4.0表单.pdf";
				
				
				
				for(Map<String,Object> map:awardsList){
					String tname =removeUnlawfulFileName((String) map.get("tname"));
					String savePdfPath =  MyProperties.getKey("RootPathkey")+"pdf/"+mname+"/"+rname+"/获奖证书/"+mname+rname+tname+"获奖证书.pdf";
					String url =  MyProperties.getKey("RootFileUrlkey")+"pdf/"+mname+"/"+rname+"/获奖证书/"+mname+rname+tname+"获奖证书.pdf";
					File file = new File(savePdfPath);
					if (!file.exists()) {  
		                // 如果路径不存在,则创建  
						 file.getParentFile().mkdirs();
		            }  
					Map<String, String> dataParamMap = new HashMap<String, String>();
					dataParamMap.put("编号", (String) map.get("awardsno"));
					
//					String name = (String) map.get("school")+"  "+(String) map.get("tname");
					//String name = (String) map.get("school")+"\n"+(String) map.get("tname");
//					dataParamMap.put("学校队伍", name);
					dataParamMap.put("学校", (String) map.get("school"));
					dataParamMap.put("队伍", (String) map.get("tname"));
					
					dataParamMap.put("小项", (String) map.get("rname"));
					dataParamMap.put("奖项", (String) map.get("awards"));
					dataParamMap.put("教师",(String) map.get("teachers"));	
					dataParamMap.put("队员",(String) map.get("students"));	
					
					PdfModeForPdf.formFdfModeForPdf(dataParamMap, savePdfPath, wordModelPathAwards);
					
					RaceTeamScore raceTeamScore = new RaceTeamScore();
					raceTeamScore.setTid((String) map.get("tid"));
					raceTeamScore.setRid((String) map.get("rid"));
					raceTeamScore.setAwardsurl(url);
					raceTeamScore.setCreatstatus("00");
					raceTeamScoreMapper.updateByPrimaryKeySelective(raceTeamScore);

				}
				//参赛证明
				String wordModelPathEntry = match.getEntrymodel().replace(MyProperties.getKey("RootFileUrlkey"), MyProperties.getKey("RootPathkey"));
				//wordModelPathEntry="D:/pdf/参赛证明模板V4.0表单.pdf";
				
				
				for(Map<String,Object> map:entryList){
					String tmname =removeUnlawfulFileName((String) map.get("tmname"));
					String savePdfPath =  MyProperties.getKey("RootPathkey")+"pdf/"+mname+"/"+rname+"/参赛证明/"+mname+rname+tmname+"参赛证明.pdf";
					String url =  MyProperties.getKey("RootFileUrlkey")+"pdf/"+mname+"/"+rname+"/参赛证明/"+mname+rname+tmname+"参赛证明.pdf";
					File file = new File(savePdfPath);
					if (!file.exists()) {  
		                // 如果路径不存在,则创建  
						 file.getParentFile().mkdirs();
		            }
					Map<String, String> dataParamMap = new HashMap<String, String>();
					dataParamMap.put("编号", (String) map.get("entryno"));
					dataParamMap.put("姓名", (String) map.get("tmname"));
					dataParamMap.put("学校", (String) map.get("school"));
					dataParamMap.put("队伍", (String) map.get("tname"));
					dataParamMap.put("小项", (String) map.get("rname"));
					dataParamMap.put("角色", (String) map.get("role"));					

					PdfModeForPdf.replaceFormFdfModeForPdf(dataParamMap, savePdfPath, wordModelPathEntry);
					
					RaceTeamScore raceTeamScore = new RaceTeamScore();
					raceTeamScore.setTid((String) map.get("tid"));
					raceTeamScore.setRid((String) map.get("rid"));
					raceTeamScore.setCreatstatus("00");
					raceTeamScoreMapper.updateByPrimaryKeySelective(raceTeamScore);
					
					RaceTeamMember raceTeamMember = new RaceTeamMember();
					raceTeamMember.setRid((String) map.get("rid"));
					raceTeamMember.setTid((String) map.get("tid"));
					raceTeamMember.setTmid((String) map.get("tmid"));
					raceTeamMember.setEntryurl(url);			
					raceTeamMemberMapper.updateByPrimaryKeySelective(raceTeamMember);

				}
				
				String zipFilePath = MyProperties.getKey("RootPathkey")+"zip/"+mname+"/"+rname+"/";
				
				File zipfile = new File(zipFilePath);
				if (zipfile.exists()) {  
					// 如果路径存在,则删除
					
					try {
						//Deleting the directory recursively.
						delete(zipfile);
						System.out.println("Directory has been deleted recursively !");
					} catch (IOException e) {
						System.out.println("Problem occurs when deleting the directory ");
						e.printStackTrace();
					}
	            }
				 // 如果路径不存在,则创建  
				zipfile.mkdirs();
				
				String sourceFilePath = MyProperties.getKey("RootPathkey")+"pdf/"+mname+"/"+rname+"/参赛证明";				
				String fileName = mname+rname+"参赛证明ZIP";
				FileToZip.fileToZip(sourceFilePath, zipFilePath, fileName);
				
				sourceFilePath = MyProperties.getKey("RootPathkey")+"pdf/"+mname+"/"+rname+"/获奖证书";			
				fileName = mname+rname+"获奖证书ZIP";
				FileToZip.fileToZip(sourceFilePath, zipFilePath, fileName);
				
				sourceFilePath = MyProperties.getKey("RootPathkey")+"pdf/"+mname+"/"+rname+"/奖励证书";				
				fileName = mname+rname+"奖励证书ZIP";
				FileToZip.fileToZip(sourceFilePath, zipFilePath, fileName);
				
				System.out.println("全部生成完毕");
				resultMap.put("status", 0);
	}
	
/*	public void createNewModel(String rid, String mid, JSONArray array, Map<String, Object> resultMap) throws UnknownHostException {
		// TODO 自动生成的方法存根
		WordModel4Pdf.initLibreOffice();
		
		Match match=matchMapper.selectByPrimaryKey(mid);		
		Race race=raceMapper.selectByPrimaryKey(rid);
		List<Map<String,Object>> cupList = raceTeamScoreMapper.findCupListByMidRid(mid,rid);
		List<Map<String,Object>> awardsList = raceTeamScoreMapper.findAwardsListByMidRid(mid,rid);
		List<Map<String,Object>> entryList = raceTeamScoreMapper.findEntryListByMidRid(mid,rid);
		

		
		String mname = removeUnlawfulFileName(match.getMname());
		String rname = removeUnlawfulFileName(race.getRname());
		//奖励证书
		String wordModelPathCup = match.getCupmodel().replace(MyProperties.getKey("RootFileUrlkey"), MyProperties.getKey("RootPathkey"));
		wordModelPathCup="C:/projects/奖励证书模板V3.0.docx";
		
				
		for(Map<String,Object> map:cupList){
			XComponent xCompCup =WordModel4Pdf.loadWordModel(wordModelPathCup);
			String tname =removeUnlawfulFileName((String) map.get("tname"));
			String savePdfPath =  MyProperties.getKey("RootPathkey")+"pdf/"+mname+rname+tname+"奖励证书.pdf";
			String url =  MyProperties.getKey("RootFileUrlkey")+"pdf/"+mname+rname+tname+"奖励证书.pdf";
			
			Map<String, String> dataParamMap = new HashMap<String, String>();
			dataParamMap.put("编号", (String) map.get("cupno"));
			dataParamMap.put("学校", (String) map.get("school"));
			dataParamMap.put("队伍", (String) map.get("tname"));
			dataParamMap.put("小项", (String) map.get("rname"));
			dataParamMap.put("奖杯", (String) map.get("cup"));
			dataParamMap.put("教师", (String) map.get("teachers"));
			dataParamMap.put("队员", (String) map.get("students"));
			
			//WordModel4Pdf.deleteOldFile(savePdfPath);
			WordModel4Pdf.replaceParam(xCompCup, dataParamMap);
			WordModel4Pdf.savePdf(xCompCup, savePdfPath);
			
			RaceTeamScore raceTeamScore = new RaceTeamScore();
			raceTeamScore.setTid((String) map.get("tid"));
			raceTeamScore.setRid((String) map.get("rid"));
			raceTeamScore.setCupurl(url);
			raceTeamScore.setCreatstatus("00");
			raceTeamScoreMapper.updateByPrimaryKeySelective(raceTeamScore);
			
		}
		
		
		//获奖证书
		String wordModelPathAwards = match.getAwardsmodel().replace(MyProperties.getKey("RootFileUrlkey"), MyProperties.getKey("RootPathkey"));
		wordModelPathAwards="C:/projects/获奖证书模板V3.0.docx";
		
		
		
		for(Map<String,Object> map:awardsList){
			XComponent xCompAwards =WordModel4Pdf.loadWordModel(wordModelPathAwards);
			String tname =removeUnlawfulFileName((String) map.get("tname"));
			String savePdfPath =  MyProperties.getKey("RootPathkey")+"pdf/"+mname+rname+tname+"获奖证书.pdf";
			String url =  MyProperties.getKey("RootFileUrlkey")+"pdf/"+mname+rname+tname+"获奖证书.pdf";
			
			Map<String, String> dataParamMap = new HashMap<String, String>();
			dataParamMap.put("编号", (String) map.get("awardsno"));
			dataParamMap.put("学校", (String) map.get("school"));
			dataParamMap.put("队伍", (String) map.get("tname"));
			dataParamMap.put("小项", (String) map.get("rname"));
			dataParamMap.put("奖项", (String) map.get("awards"));
			dataParamMap.put("教师", (String) map.get("teachers"));
			dataParamMap.put("队员", (String) map.get("students"));
			
			//WordModel4Pdf.deleteOldFile(savePdfPath);
			WordModel4Pdf.replaceParam(xCompAwards, dataParamMap);
			WordModel4Pdf.savePdf(xCompAwards, savePdfPath);
			
			RaceTeamScore raceTeamScore = new RaceTeamScore();
			raceTeamScore.setTid((String) map.get("tid"));
			raceTeamScore.setRid((String) map.get("rid"));
			raceTeamScore.setAwardsurl(url);
			raceTeamScore.setCreatstatus("00");
			raceTeamScoreMapper.updateByPrimaryKeySelective(raceTeamScore);

		}
		//参赛证明
		String wordModelPathEntry = match.getEntrymodel().replace(MyProperties.getKey("RootFileUrlkey"), MyProperties.getKey("RootPathkey"));
		wordModelPathEntry="C:/projects/参赛证明模板V3.0.docx";
		
		
		for(Map<String,Object> map:entryList){
			XComponent xCompEntry =WordModel4Pdf.loadWordModel(wordModelPathEntry);
			String tmname =removeUnlawfulFileName((String) map.get("tmname"));
			String savePdfPath =  MyProperties.getKey("RootPathkey")+"pdf/"+mname+rname+tmname+"参赛证明.pdf";
			String url =  MyProperties.getKey("RootFileUrlkey")+"pdf/"+mname+rname+tmname+"参赛证明.pdf";
			
			Map<String, String> dataParamMap = new HashMap<String, String>();
			dataParamMap.put("编号", (String) map.get("entryno"));
			dataParamMap.put("姓名", (String) map.get("tmname"));
			dataParamMap.put("学校", (String) map.get("school"));
			dataParamMap.put("队伍", (String) map.get("tname"));
			dataParamMap.put("小项", (String) map.get("rname"));
			dataParamMap.put("角色", (String) map.get("role"));
			
			//WordModel4Pdf.deleteOldFile(savePdfPath);
			WordModel4Pdf.replaceParam(xCompEntry, dataParamMap);
			WordModel4Pdf.savePdf(xCompEntry, savePdfPath);
			
			RaceTeamScore raceTeamScore = new RaceTeamScore();
			raceTeamScore.setTid((String) map.get("tid"));
			raceTeamScore.setRid((String) map.get("rid"));
			raceTeamScore.setCreatstatus("00");
			raceTeamScoreMapper.updateByPrimaryKeySelective(raceTeamScore);
			
			RaceTeamMember raceTeamMember = new RaceTeamMember();
			raceTeamMember.setRid((String) map.get("rid"));
			raceTeamMember.setTid((String) map.get("tid"));
			raceTeamMember.setTmid((String) map.get("tmid"));
			raceTeamMember.setEntryurl(url);			
			raceTeamMemberMapper.updateByPrimaryKeySelective(raceTeamMember);

		}
		WordModel4Pdf.closeLibreOffice();
		System.out.println("全部生成完毕");
		resultMap.put("status", 0);
	}
	
	public void createModel(String rid, String mid, JSONArray array, Map<String, Object> resultMap) throws Exception, BootstrapException, IOException {
		// TODO Auto-generated method stub
			
			WordModel4Pdf.initLibreOffice();
			
			for (int i = 0; i < array.size(); i++) {
				JSONObject json = array.getJSONObject(i);
				String tid=json.getString("tid");
				 String tid = array.getString(i);
				    Match match=matchMapper.selectByPrimaryKey(mid);
					Team team=teamMapper.selectByPrimaryKey(tid);

					
					Race race=raceMapper.selectByPrimaryKey(rid);//赛项信息
					
					RaceTeamScoreKey raceTeamScoreKey=new RaceTeamScoreKey();
					raceTeamScoreKey.setRid(rid);
					raceTeamScoreKey.setTid(tid);
					RaceTeamScore raceTeamScore=raceTeamScoreMapper.selectByPrimaryKey(raceTeamScoreKey);//成绩、奖杯、奖项等
					
					String strteacher=memberMapper.findAllTeacherByTid(tid,rid);//查询这个队伍下的所有老师
					String strmember=memberMapper.findAllMemberByTid(tid,rid);//查询这个队伍下的所有学生
					
					List<Map<String, Object>> memberList=memberMapper.findMemberByTid(tid, rid);//这个队伍下的所有老师和成员的信息
					
				
					//参赛证明
					int k;
					for (k=0;k<memberList.size();k++) {
						Map<String, Object> map = memberList.get(k);
						String name=map.get("tmname").toString();
						String tmid = map.get("tmid").toString();
						String roleflg=map.get("roleflg").toString();
						String entryno=map.get("entryno").toString();
						String role="";
						if("01".equals(roleflg)){
							role="指导教师";
						}else if("02".equals(roleflg)){
							role="参赛队员";
						}
						Map<String, String> dataParamMap = new HashMap<String, String>();
						dataParamMap.put("编号", entryno);
						dataParamMap.put("姓名", name);
						dataParamMap.put("学校", team.getSchool());
						dataParamMap.put("队伍", team.getTname());
						dataParamMap.put("大项", race.getFrname());
						dataParamMap.put("小项", race.getRname());
						dataParamMap.put("角色", role);
						
						String wordModelPath = match.getEntrymodel().replace(MyProperties.getKey("RootFileUrlkey"), MyProperties.getKey("RootPathkey"));
						wordModelPath="C:/projects/参赛证明模板.docx";
						String savePdfPath = MyProperties.getKey("RootPathkey")+"pdf/"+match.getMname()+race.getRname()+name+"参赛证明.pdf";
						String url=MyProperties.getKey("RootFileUrlkey")+"pdf/"+match.getMname()+race.getRname()+name+"参赛证明.pdf";
						//WordModel4Pdf.wordModel4Pdf(wordModelPath, savePdfPath,dataParamMap);
						//aAffix[k][0]=match.getMname()+race.getRname()+name+"参赛证明.pdf";
						//aAffix[k][1]=savePdfPath;
						//pathList.add(savePdfPath);
						//保存路径
						
						raceTeamMemberMapper.updateEntryurl(rid, tid, tmid, url);
						}
					k--;
					//获奖证书
					if(!"".equals(raceTeamScore.getAwards()) && raceTeamScore.getAwards()!=null){
						Map<String, String> dataParamMap = new HashMap<String, String>();
						dataParamMap.put("编号", raceTeamScore.getAwardsno());
						dataParamMap.put("学校", team.getSchool());
						dataParamMap.put("队伍", team.getTname());
						dataParamMap.put("大项", race.getFrname());
						dataParamMap.put("小项", race.getRname());
						dataParamMap.put("奖项", raceTeamScore.getAwards());
						dataParamMap.put("教师", strteacher);
						dataParamMap.put("队员", strmember);
						String wordModelPath = match.getAwardsmodel().replace(MyProperties.getKey("RootFileUrlkey"), MyProperties.getKey("RootPathkey"));
						wordModelPath="C:/projects/获奖证书模板.docx";
						String savePdfPath = MyProperties.getKey("RootPathkey")+"pdf/"+match.getMname()+race.getRname()+team.getTname()+"获奖证书.pdf";
						String url = MyProperties.getKey("RootFileUrlkey")+"pdf/"+match.getMname()+race.getRname()+team.getTname()+"获奖证书.pdf";
						//WordModel4Pdf.wordModel4Pdf(wordModelPath, savePdfPath,dataParamMap);
						k++;
						//aAffix[k][0]=match.getMname()+race.getRname()+team.getTname()+"获奖证书.pdf";
						//aAffix[k][1]=savePdfPath;
						//pathList.add(savePdfPath);
						//保存奖项路径
						raceTeamScore.setAwardsurl(url);
					}
					//奖励证书
					if(!"".equals(raceTeamScore.getCup()) && raceTeamScore.getCup()!=null){
						Map<String, String> dataParamMap = new HashMap<String, String>();
						dataParamMap.put("编号", raceTeamScore.getCupno());
						dataParamMap.put("学校", team.getSchool());
						dataParamMap.put("队伍", team.getTname());
						dataParamMap.put("大项", race.getFrname());
						dataParamMap.put("小项", race.getRname());
						dataParamMap.put("奖杯", raceTeamScore.getCup());
						dataParamMap.put("教师", strteacher);
						dataParamMap.put("队员", strmember);
						String wordModelPath = match.getCupmodel().replace(MyProperties.getKey("RootFileUrlkey"), MyProperties.getKey("RootPathkey"));
						wordModelPath="C:/projects/奖励证书模板.docx";
						String savePdfPath =  MyProperties.getKey("RootPathkey")+"pdf/"+match.getMname()+race.getRname()+team.getTname()+"奖励证书.pdf";
						String url =  MyProperties.getKey("RootFileUrlkey")+"pdf/"+match.getMname()+race.getRname()+team.getTname()+"奖励证书.pdf";
						//WordModel4Pdf.wordModel4Pdf(wordModelPath, savePdfPath,dataParamMap);
						k++;
						//aAffix[k][0]=match.getMname()+race.getRname()+team.getTname()+"奖励证书.pdf";
						//aAffix[k][1]=savePdfPath;
						//pathList.add(savePdfPath);
						//保存奖杯路径
						raceTeamScore.setCupurl(url);
					}
					//更新保存文件路径
					raceTeamScore.setCreatstatus("00");
					raceTeamScoreMapper.updateByPrimaryKeySelective(raceTeamScore);
					
					mailFileList.add(aAffix);
					raceTeamScoreList.add(raceTeamScore);
					System.out.println("PDF生成成功！");
			}
			WordModel4Pdf.closeLibreOffice();

	}*/
	
	
	
	/*public void updateGrantstatus(RaceTeamScore raceTeamScore) {
		// TODO 自动生成的方法存根
		//修改已发送按钮
		raceTeamScore.setGrantstatus("00");
		raceTeamScoreMapper.updateByPrimaryKeySelective(raceTeamScore);
	}
*/


	public void sendEmailPdf(String rid, String mid, String tid, Map<String, Object> resultMap, String flg) {
		// TODO 自动生成的方法存根
		/*for(int k=0;k<tid.size();k++){
			JSONObject json = tid.getJSONObject(k);
			String tid=json.getString("tid");*/
			String email=memberMapper.findTeacherEmail(tid,rid);
			//String email="yuanjiajia@zdtdchina.com";
			int num=0;
			String[][] aAffix=null;
			
			RaceTeamScoreKey raceTeamScoreKey = new RaceTeamScoreKey();
			raceTeamScoreKey.setRid(rid);
			raceTeamScoreKey.setTid(tid);
			
			RaceTeamScore raceTeamScore = raceTeamScoreMapper.selectByPrimaryKey(raceTeamScoreKey);
			resultMap.put("raceTeamScore", raceTeamScore);
			if("00".equals(flg)){
				//奖状发送				
				String awardsurl = raceTeamScore.getAwardsurl();
				String cupurl = raceTeamScore.getCupurl();
				
				if(awardsurl!=null && !"".equals(awardsurl)){
					num++;
				}
				if(cupurl!=null && !"".equals(cupurl)){
					num++;
				}
				aAffix=new String[num][2];
				int i=0;
				if(awardsurl!=null && !"".equals(awardsurl)){
					
					aAffix[i][0]=awardsurl.substring(awardsurl.lastIndexOf("/")+1);
					aAffix[i][1]=awardsurl;
					i++;					
				}
				if(cupurl!=null && !"".equals(cupurl)){
					aAffix[i][0]=cupurl.substring(cupurl.lastIndexOf("/")+1);
					aAffix[i][1]=cupurl;					
				}
				raceTeamScore.setGrantjzstatus("00");		
			}else if("01".equals(flg)){
				//参赛证明
				List<String> entryUrlList = raceTeamMemberMapper.findEntryUrlList(rid,tid);
				num=entryUrlList.size();
				aAffix=new String[num][2];
				int i;
				for(i=0;i<entryUrlList.size();i++){
					aAffix[i][0]=entryUrlList.get(i).substring(entryUrlList.get(i).lastIndexOf("/")+1);
					aAffix[i][1]=entryUrlList.get(i);
				}
				raceTeamScore.setGrantcsstatus("00");
			}
			//发送邮箱邮件
			String bodyHtml = "<div style='font-size:20px;background:#e4d480;padding:20px 10px'>"
					+ "<p style='font-family:“华文行楷”'>您好！</p>"
					+ "<p style='margin:20px 0;font-family:“华文行楷”'>"
					+ "本次参赛的证书链接，请点击下载：<font color='red'></font><br/>";
							
					for(int j=0;j<num;j++){
						bodyHtml+="<a href='"+aAffix[j][1]+"'>"+aAffix[j][0]+"</a><br/>";
					}
					
						bodyHtml+= "</p> "
								+ "</div>";

			
			MailService service = new MailService();// 新建邮件
			service.send(email, null, "机器人竞赛管理系统证书", null, bodyHtml,null);
				
			raceTeamScoreMapper.updateByPrimaryKeySelective(raceTeamScore);
			
		}



	public void failGrantstatus(RaceTeamScore raceTeamScore) {
		// TODO 自动生成的方法存根
		raceTeamScoreMapper.updateByPrimaryKeySelective(raceTeamScore);
	}
	
	public String removeUnlawfulFileName(String filename) {
		
		return filename.replace("\\", "").replace("/", "").replace(":", "").replace("*", "")
				.replace("?", "").replace("\"", "").replace("<", "").replace(">", "").replace("|", "");
	}

	

	

}
