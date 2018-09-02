package com.zts.robot.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;
import com.sun.star.lang.XComponent;
import com.zts.robot.mapper.MatchMapper;
import com.zts.robot.mapper.MemberMapper;
import com.zts.robot.mapper.RaceTeamMemberMapper;
import com.zts.robot.mapper.RaceTeamScoreMapper;
import com.zts.robot.pojo.Match;
import com.zts.robot.pojo.Member;
import com.zts.robot.util.DeleteFileUtil;
import com.zts.robot.util.FileToZip;
import com.zts.robot.util.MyProperties;
import com.zts.robot.util.OperaterExcel;
import com.zts.robot.util.Tools;
import com.zts.robot.util.WordModel4Pdf;

@Service
public class StatisticsService {
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private RaceTeamMemberMapper raceTeamMemberMapper;
	@Autowired
	private RaceTeamScoreMapper raceTeamScoreMapper;
	@Autowired
	private MatchMapper matchMapper;
	
	public List<List> statisticsMemberByMid(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<List> list=new ArrayList<List>();
		//查询高等学校
		List<Map<String, Object>> theUniversitylist=memberMapper.findUniversitystatisticsMemberByMid(condition);
		list.add(theUniversitylist);
		//查询职业学校
		List<Map<String, Object>> careerlist=memberMapper.findcareerstatisticsMemberByMid(condition);
		list.add(careerlist);
		//查询高中
		List<Map<String, Object>> highlist=memberMapper.findhighstatisticsMemberByMid(condition);
		list.add(highlist);
		//查询初中
		List<Map<String, Object>> juniorlist=memberMapper.findjuniorstatisticsMemberByMid(condition);
		list.add(juniorlist);
		//查询小学
		List<Map<String, Object>> primarylist=memberMapper.findprimarystatisticsMemberByMid(condition);
		list.add(primarylist);
		//查询幼儿园
		List<Map<String, Object>> kindergartenlist=memberMapper.findkindergartenstatisticsMemberByMid(condition);
		list.add(kindergartenlist);
		//查询其他
		List<Map<String, Object>> otherlist=memberMapper.findotherstatisticsMemberByMid(condition);
		list.add(otherlist);
		//查询合计
		List<Map<String, Object>> totallist=memberMapper.findtotalstatisticsMemberByMid(condition);
		list.add(totallist);
		return list;
	}

	public void teamDerivedByMid(Map<String, Object> paramMap, Map<String, Object> resultMap) {
		// TODO 自动生成的方法存根
		List<Map<String, Object>> teamDerivedList = raceTeamMemberMapper.getTeamDerivedByMid(paramMap);
		if(teamDerivedList.size()==0){
			resultMap.put("status", 1);
			resultMap.put("errmsg", "没有检索到符合条件的队伍！");
			throw new RuntimeException();
		}
		List<Map<String,Object>> excelList= OperaterExcel.formatTeamData(teamDerivedList);
		String[] names = { "参赛队编号","参赛队名","参赛队学校","机构类型","地区","报名者","报名者电话","报名者邮箱","开票抬头","纳税人识别号","开户行及账号","地址及电话","收发票地址","指导教师人数","参赛队员人数","指导老师姓名","指导老师性别","指导老师证件类型","指导老师证件号","指导老师邮箱","指导老师手机","指导教师民族","指导教师餐食","队员姓名","队员性别","队员证件类型","队员证件号","队员邮箱","队员手机","队员民族","队员餐食","参赛大项","参赛小项编号","参赛小项","参赛队审核状态","参赛队缴费状态"};
		String sheetName = paramMap.get("mname")+"参赛队伍信息表";
		String PootPathkey = MyProperties.getKey("RootPathkey");
		String RootFileUrlkey = MyProperties.getKey("RootFileUrlkey");
		String parentfolder = "excelfile";
		String filetype = ".xlsx";
		int C=(int)(Math.random()*100000);
		String suiji=String.valueOf(C);
		// 子文件夹
		String sonfolder = Tools.dateStr();
		// 文件夹路径
		String folderpath = parentfolder + "/" + sonfolder + "/";

		// 文件名
		String filename = Tools.dateStr() + suiji +paramMap.get("mname")+"参赛队伍"+ filetype;
		// 全文件目录
		// String dirpath = PootPathkey + folderpath;
		// 文件全路径，文件全访问地址
		String path = PootPathkey + folderpath + filename;
		String fileurl = RootFileUrlkey + folderpath + filename;
		//String path = fileurl.replace(RootFileUrlkey, PootPathkey);
		File savedir = new File(path);
		if (!savedir.getParentFile().exists())
			savedir.getParentFile().mkdirs();
		//String url = "C:\\Documents and Settings\\Administrator\\桌面\\用户信息表.txt";
		OperaterExcel.outputExcelxlsx(excelList, path, names, sheetName);
		resultMap.put("path", path);
		resultMap.put("fileurl", fileurl);
		resultMap.put("status", 0);
	}

	public void memberDerivedByMid(Map<String, Object> paramMap, Map<String, Object> resultMap) {
		// TODO 自动生成的方法存根
		List<Map<String, Object>> memberDerivedList = raceTeamMemberMapper.getMemberDerivedByMid(paramMap);
		if(memberDerivedList.size()==0){
			resultMap.put("status", 1);
			resultMap.put("errmsg", "没有检索到符合条件的参赛人员！");
			throw new RuntimeException();
		}
		List<Map<String,Object>> excelList= OperaterExcel.formatMemberData(memberDerivedList);
		String[] names = { "姓名","人员类别","性别","证件类型","证件号","邮箱","手机","民族","餐食","参赛队编号","参赛队名","参赛队学校","机构类型","地区","指导教师","报名者","报名者电话","报名者邮箱","开票抬头","纳税人识别号","开户行及账号","地址及电话","收发票地址","参赛大项","参赛小项编号","参赛小项","参赛队审核状态","参赛队缴费状态"};
		String sheetName = paramMap.get("mname")+"参赛人员信息表";
		String PootPathkey = MyProperties.getKey("RootPathkey");
		String RootFileUrlkey = MyProperties.getKey("RootFileUrlkey");
		String parentfolder = "excelfile";
		String filetype = ".xlsx";
		int C=(int)(Math.random()*100000);
		String suiji=String.valueOf(C);
		// 子文件夹
		String sonfolder = Tools.dateStr();
		// 文件夹路径
		String folderpath = parentfolder + "/" + sonfolder + "/";

		// 文件名
		String filename = Tools.dateStr() + suiji +paramMap.get("mname")+"参赛人员"+ filetype;
		// 全文件目录
		// String dirpath = PootPathkey + folderpath;
		// 文件全路径，文件全访问地址
		String path = PootPathkey + folderpath + filename;
		String fileurl = RootFileUrlkey + folderpath + filename;
		//String path = fileurl.replace(RootFileUrlkey, PootPathkey);
		File savedir = new File(path);
		if (!savedir.getParentFile().exists())
			savedir.getParentFile().mkdirs();
		//String url = "C:\\Documents and Settings\\Administrator\\桌面\\用户信息表.txt";
		OperaterExcel.outputExcelxlsx(excelList, path, names, sheetName);
		resultMap.put("path", path);
		resultMap.put("fileurl", fileurl);
		resultMap.put("status", 0);
	}

	public void teamRaceDerivedByMid(Map<String, Object> paramMap, Map<String, Object> resultMap) {
		// TODO 自动生成的方法存根
		List<Map<String, Object>> teamRaceDerivedList = raceTeamMemberMapper.getTeamRaceDerivedByMid(paramMap);
		if(teamRaceDerivedList.size()==0){
			resultMap.put("status", 1);
			resultMap.put("errmsg", "没有检索到符合条件的赛项报名！");
			throw new RuntimeException();
		}
		List<Map<String,Object>> excelList= OperaterExcel.formatTeamRaceData(teamRaceDerivedList);
		String[] names = { "参赛大项","参赛小项编号","参赛小项","参赛队编号","参赛队名","参赛队学校","机构类型","地区","报名者","报名者电话","报名者邮箱","开票抬头","纳税人识别号","开户行及账号","地址及电话","收发票地址","指导老师姓名","指导老师性别","指导老师证件类型","指导老师证件号","指导老师邮箱","指导老师手机","指导教师餐食","队员姓名","队员数量","参赛队审核状态","参赛队缴费状态","签到状态"};
		String sheetName = paramMap.get("mname")+"赛项报名信息表";
		String PootPathkey = MyProperties.getKey("RootPathkey");
		String RootFileUrlkey = MyProperties.getKey("RootFileUrlkey");
		String parentfolder = "excelfile";
		String filetype = ".xlsx";
		int C=(int)(Math.random()*100000);
		String suiji=String.valueOf(C);
		// 子文件夹
		String sonfolder = Tools.dateStr();
		// 文件夹路径
		String folderpath = parentfolder + "/" + sonfolder + "/";

		// 文件名
		String filename = Tools.dateStr() + suiji +paramMap.get("mname")+"赛项报名"+ filetype;
		// 全文件目录
		// String dirpath = PootPathkey + folderpath;
		// 文件全路径，文件全访问地址
		String path = PootPathkey + folderpath + filename;
		String fileurl = RootFileUrlkey + folderpath + filename;
		//String path = fileurl.replace(RootFileUrlkey, PootPathkey);
		File savedir = new File(path);
		if (!savedir.getParentFile().exists())
			savedir.getParentFile().mkdirs();
		//String url = "C:\\Documents and Settings\\Administrator\\桌面\\用户信息表.txt";
		OperaterExcel.outputExcelxlsx(excelList, path, names, sheetName);
		resultMap.put("path", path);
		resultMap.put("fileurl", fileurl);
		resultMap.put("status", 0);
	}

	public void teamMemberScoreByMid(Map<String, Object> paramMap, Map<String, Object> resultMap) {
		// TODO 自动生成的方法存根
		List<Map<String, Object>> teamMemberScoreList = raceTeamScoreMapper.teamMemberScoreByMid((String)paramMap.get("mid"));
		if(teamMemberScoreList.size()==0){
			resultMap.put("status", 1);
			resultMap.put("errmsg", "没有检索到需要导出的数据！");
			throw new RuntimeException();
		}
		List<Map<String,Object>> excelList= OperaterExcel.formatTeamMemberScoreData(teamMemberScoreList);
		String[] names = {"证书编号","学校","队伍名称","赛项名称","奖项","指导教师","参赛队员"};
		String sheetName = paramMap.get("mname")+"奖励证书数据";
		String PootPathkey = MyProperties.getKey("RootPathkey");
		String RootFileUrlkey = MyProperties.getKey("RootFileUrlkey");
		String parentfolder = "excelfile";
		String filetype = ".xlsx";
		int C=(int)(Math.random()*100000);
		String suiji=String.valueOf(C);
		// 子文件夹
		String sonfolder = Tools.dateStr();
		// 文件夹路径
		String folderpath = parentfolder + "/" + sonfolder + "/";

		// 文件名
		String filename = Tools.dateStr() + suiji +paramMap.get("mname")+"奖励证书数据"+ filetype;
		// 全文件目录
		// String dirpath = PootPathkey + folderpath;
		// 文件全路径，文件全访问地址
		String path = PootPathkey + folderpath + filename;
		String fileurl = RootFileUrlkey + folderpath + filename;
		//String path = fileurl.replace(RootFileUrlkey, PootPathkey);
		File savedir = new File(path);
		if (!savedir.getParentFile().exists())
			savedir.getParentFile().mkdirs();
		//String url = "C:\\Documents and Settings\\Administrator\\桌面\\用户信息表.txt";
		OperaterExcel.outputExcelxlsx(excelList, path, names, sheetName);
		resultMap.put("path", path);
		resultMap.put("fileurl", fileurl);
		resultMap.put("status", 0);
	}

	public void teamScoreByMid(Map<String, Object> paramMap, Map<String, Object> resultMap) {
		// TODO 自动生成的方法存根
		// TODO 自动生成的方法存根
				List<Map<String, Object>> teamScoreList = raceTeamScoreMapper.teamScoreByMid((String)paramMap.get("mid"));
				if(teamScoreList.size()==0){
					resultMap.put("status", 1);
					resultMap.put("errmsg", "没有检索到需要导出的数据！");
					throw new RuntimeException();
				}
				List<Map<String,Object>> excelList= OperaterExcel.formatTeamScoreData(teamScoreList);
				String[] names = {"证书编号","学校","队伍名称","赛项名称","奖项"};
				String sheetName = paramMap.get("mname")+"单项奖证书数据";
				String PootPathkey = MyProperties.getKey("RootPathkey");
				String RootFileUrlkey = MyProperties.getKey("RootFileUrlkey");
				String parentfolder = "excelfile";
				String filetype = ".xlsx";
				int C=(int)(Math.random()*100000);
				String suiji=String.valueOf(C);
				// 子文件夹
				String sonfolder = Tools.dateStr();
				// 文件夹路径
				String folderpath = parentfolder + "/" + sonfolder + "/";

				// 文件名
				String filename = Tools.dateStr() + suiji +paramMap.get("mname")+"单项奖证书数据"+ filetype;
				// 全文件目录
				// String dirpath = PootPathkey + folderpath;
				// 文件全路径，文件全访问地址
				String path = PootPathkey + folderpath + filename;
				String fileurl = RootFileUrlkey + folderpath + filename;
				//String path = fileurl.replace(RootFileUrlkey, PootPathkey);
				File savedir = new File(path);
				if (!savedir.getParentFile().exists())
					savedir.getParentFile().mkdirs();
				//String url = "C:\\Documents and Settings\\Administrator\\桌面\\用户信息表.txt";
				OperaterExcel.outputExcelxlsx(excelList, path, names, sheetName);
				resultMap.put("path", path);
				resultMap.put("fileurl", fileurl);
				resultMap.put("status", 0);
	}

	public void generateBadge(String mid, Map<String, Object> resultMap) throws IOException, DocumentException {
		// TODO Auto-generated method stub
		mid="7b17a899547b444eaf3ebb6002f808b8";
		WordModel4Pdf.initLibreOffice();
		List<Map<String, Object>> memberList=memberMapper.findAllMemberBage(mid);//每个成员的信息List
		Match match=matchMapper.selectByPrimaryKey(mid);
		//胸卡模板
		//String wordModelPathCup = match.getBadgemodel().replace(MyProperties.getKey("RootFileUrlkey"), MyProperties.getKey("RootPathkey"));
		String wordModelPathCup="C:/projects/胸卡模板.docx";
		String name="";
		int i=0;
		
		//删除文件zip及文件夹
		 String dir = "~/ROOT/staticrobot/badge";
		 DeleteFileUtil.delete(dir);
		//循环生成胸卡
		for(Map<String, Object> map:memberList){
			i+=1;
			System.out.println(i);
			
			String tmid=map.get("tmid").toString();
			String tmname=map.get("tmname").toString();
			String picurl=map.get("picurl").toString();
			String roleflg=map.get("roleflg").toString();
			String diningtype=map.get("diningtype").toString();
			String tname=map.get("tname").toString();
			String school=map.get("school").toString();
			String rname=map.get("rname").toString();
			String uname=map.get("uname").toString();
			//反向图片路径
			String imagePath=picurl.replace(MyProperties.getKey("RootFileUrlkey"), MyProperties.getKey("RootPathkey"));
			
			String savePdfPath =  MyProperties.getKey("RootPathkey")+"badge/"+match.getMname()+"/"+tmname+tmid+"胸卡.pdf";
			//String url =  MyProperties.getKey("RootFileUrlkey")+"badge/"+tmname+"胸卡.pdf";
			String url =  "~/ROOT/staticrobot/badge/"+match.getMname()+"/"+tmname+tmid+"胸卡.pdf";
			
			/*File file = new File(savePdfPath);
			if(!file.exists()){//文件是否存在
*/				
			int num=0;
			if(name.equals(uname)){
				num+=1;
			}else{
				num=1;
			}
			name=uname;
			//读取word模板
			XComponent xCompCup =WordModel4Pdf.loadWordModel(wordModelPathCup);
						
			if("01".equals(diningtype)){
				diningtype="普通";
			}else if("02".equals(diningtype)){
				diningtype="清真";
			}else if("03".equals(diningtype)){
				diningtype="素食";
			}
			if("01".equals(roleflg)){
				roleflg="指导教师";
			}else if("02".equals(roleflg)){
				roleflg="参赛队员";
			}
			
			Map<String, String> dataParamMap = new HashMap<String, String>();
			dataParamMap.put("序号", Integer.toString(num));
			dataParamMap.put("餐食", diningtype);
			dataParamMap.put("姓名", tmname);
			dataParamMap.put("人员类别", roleflg);
			dataParamMap.put("学校", school);
			dataParamMap.put("参赛队名", tname);
			dataParamMap.put("赛项", rname);
			
			//生成PDF
			/*
			WordModel4Pdf.deleteOldFile(savePdfPath);
			*/
			WordModel4Pdf.replaceParam(xCompCup, dataParamMap);
			WordModel4Pdf.savePdf(xCompCup, savePdfPath);
			//PDF中添加照片
			//OpenOfficeAPI.pdfaddpicurl(savePdfPath, imagePath);
			//保存每个人的胸卡
			Member member=new Member();
			member.setTmid(tmid);
			member.setBadgeurl(url);
			memberMapper.updateByPrimaryKeySelective(member);
		//}
			
		}
		//保存这个赛事下的所有胸卡的zip
		String sourceFilePath = "~/ROOT/staticrobot/badge"; // 待压缩的文件路径 
        String zipFilePath = "C:/ROOT";  //压缩后存放路径 
        String fileName = "pdfZip";  //:压缩后文件的名称 
        boolean flag = FileToZip.fileToZip(sourceFilePath, zipFilePath, fileName);  
        if(flag){  
            System.out.println("文件打包成功!");  
        }else{  
            System.out.println("文件打包失败!");  
        }  
        String url=zipFilePath+"/"+fileName;
        match.setBadgeurl(url);
        matchMapper.updateByPrimaryKeySelective(match);
		//关闭
		WordModel4Pdf.closeLibreOffice();
	}


}
