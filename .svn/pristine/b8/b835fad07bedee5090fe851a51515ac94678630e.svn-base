package com.zts.robot.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zts.robot.pojo.Match;
import com.zts.robot.pojo.ReuserMatch;
import com.zts.robot.pojo.ReuserMatchKey;
import com.zts.robot.redis.RedisService;
import com.zts.robot.service.MatchService;
import com.zts.robot.util.CookieOperation;
import com.zts.robot.util.MyProperties;
import com.zts.robot.util.OperaterExcel;
import com.zts.robot.util.RedisUtil;
import com.zts.robot.util.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class MatchController {
	@Autowired
	private MatchService matchService;
	@Autowired
	private RedisService redisService;
	/**
	 * 添加赛事
	 * @param match
	 * @param createuid
	 * @return
	 */
	@RequestMapping("/addMatch")
	@ResponseBody
	public Map<String, Object> addMatch(HttpServletRequest request,Match match){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			int count=matchService.findMachname(match.getMname());//判断赛事是否重名
			if(count>0){
				resultMap.put("status", 1);
				resultMap.put("errmsg", "赛事名称重复，不允许添加！");
			}else{
				JSONObject userJson= Tools.getUserJsonByCookie(request);
				match.setMid(Tools.get32UUID());
				match.setMstatus("01");
				match.setDelflg("00");
				match.setCreatedate(Tools.getStringByDateAndTime(new Date()));
				match.setCreateuid(userJson.getString("uid"));
				matchService.addMatch(match);
				resultMap.put("status", 0);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", 1);
			resultMap.put("errmsg", "系统异常！：" + e.getMessage());
		}
		return resultMap;
		
	}
	
	/**
	 * 修改赛事
	 * @param match
	 * @param createuid
	 * @return
	 */
	@RequestMapping("/updateMatch")
	@ResponseBody
	public Map<String, Object> updateMatch(HttpServletRequest request,Match match){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String sys_openedMatchMid = request.getParameter("mid");
			if(sys_openedMatchMid.equals(match.getMid())){
				RedisUtil.set("sys_openedMatchMid", sys_openedMatchMid);
				RedisUtil.set("sys_openedMatchGPS", match.getGps());
				RedisUtil.set("sys_openedMatchName", match.getMname());	
			}
			matchService.updateMatch(match);
			resultMap.put("status", 0);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", 1);
			resultMap.put("errmsg", "系统异常！：" + e.getMessage());
		}
		return resultMap;
		
	}
	
	/**
	 * 查询赛事列表
	 * @param match
	 * @param createuid
	 * @return
	 */
	@RequestMapping("/findMatchListByPage")
	@ResponseBody
	public Map<String, Object> findMatchListByPage(HttpServletRequest request,Integer iDisplayLength, Integer iDisplayStart){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();	
			String uid=request.getParameter("uid");
			String roleflg=request.getParameter("roleflg");
			paramMap.put("uid", uid);
			paramMap.put("roleflg", roleflg);
			int totalSize = matchService.findMatchlistTotalSize(paramMap);
			if (iDisplayLength==null||iDisplayLength != -1) {
				paramMap.put("beginNo", iDisplayStart);
				paramMap.put("endNo", iDisplayLength);
			}
			
				List<Map<String,Object>> matchList=matchService.findMatchListByPage(paramMap);
				resultMap.put("status", 0);
				resultMap.put("list",matchList);
				resultMap.put("iTotalRecords", totalSize);
			
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", 1);
			resultMap.put("errmsg", "系统异常！：" + e.getMessage());
		}
		return resultMap;
		
	}
	
	/**
	 * 查询赛事信息
	 * @param match
	 * @param createuid
	 * @return
	 */
	@RequestMapping("/findMatchInfoByMid")
	@ResponseBody
	public Map<String, Object> findMatchInfoByMid(String mid){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Map<String, Object> matchMap = matchService.findMatchInfoByMid(mid);
			resultMap.put("status", 0);
			resultMap.put("info",matchMap);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", 1);
			resultMap.put("errmsg", "系统异常！：" + e.getMessage());
		}
		return resultMap;
		
	}
	
	/**
	 * 逻辑删除赛事ByMid
	 * @param match
	 * @param createuid
	 * @return
	 */
	@RequestMapping("/updateDelflgMatchByMid")
	@ResponseBody
	public Map<String, Object> updateDelflgMatchByMid(String mid){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			matchService.updateDelflgMatchByMid(mid,resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", 1);
			resultMap.put("errmsg", "系统异常！：" + e.getMessage());
		}
		return resultMap;
		
	}
	
	/**
	 * 开启或关闭赛事ByMid
	 * @param match
	 * @param createuid
	 * @return
	 */
	@RequestMapping("/onOrOffMatchByMid")
	@ResponseBody
	public Map<String, Object> onOrOffMatchByMid(String mid){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			matchService.onOrOffMatchByMid(mid,resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", 1);
			resultMap.put("errmsg", "系统异常！：" + e.getMessage());
		}
		return resultMap;
		
	}
	/**
	 * 导出这个赛事下的所有人
	 * @param request
	 * @param response
	 * @param mv
	 * @param mid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/downAllMatchUser")
	@ResponseBody
	private Map<String, Object> downAllMatchUser(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Cookie cookie = CookieOperation.getCookieByName(request, "authId");
			JSONObject json = redisService.get("USER"+(String)cookie.getValue(),JSONObject.class);//获取登录人信息
			String mid = request.getParameter("mid");
			//拿到集合数据
			List<Map<String,Object>> list=matchService.findAllMatchUser(mid);
			//赛事信息
			Match match=matchService.findMatch(mid);
			
			//格式化数据
			List<Map<String,Object>> excelList= OperaterExcel.formatData(list);
			//调用导出方法
			String[] names = { "赛事名称","所报赛项","报名人姓名","报名人联系电话","发票名称","纳税人识别号","开户行及账号","地址及电话","收发票地址","成员姓名","类型","成员编码","性别","证件类型","证件号码","email","手机","学校","所在院系","民族","用餐类型"};
			String sheetName = match.getMname()+"成员信息表";
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
			String filename = Tools.dateStr() + suiji +match.getMname()+ filetype;
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
			resultMap.put("fileurl", fileurl);
			resultMap.put("status", 0);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", 1);
			resultMap.put("errmsg", "系统异常！：" + e.getMessage());
		}
		return resultMap;
	}
	/**
	 * 查询出所有赛事，不包括删除的
	 * @param request
	 * @param iDisplayLength
	 * @param iDisplayStart
	 * @return
	 */
	@RequestMapping("/findAllMatchList")
	@ResponseBody
	public Map<String, Object> findAllMatchList(HttpServletRequest request){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {						
				List<Map<String,Object>> matchList=matchService.findAllMatchList();
				resultMap.put("status", 0);
				resultMap.put("list",matchList);				
			
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", 1);
			resultMap.put("errmsg", "系统异常！：" + e.getMessage());
		}
		return resultMap;
		
	}
	/**
	 * 查询已经开启的赛事信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/findstartMatch")
	@ResponseBody
	public Map<String, Object> findstartMatch(HttpServletRequest request){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {						
			 List<Map<String, Object>> matchList=matchService.findstartMatch();
			 if(matchList == null || matchList.size()==0){
				 resultMap.put("status", 1);
				 resultMap.put("errmsg", "未有赛事开启！敬请期待！");
				 return resultMap;
			 }
				resultMap.put("status", 0);
				resultMap.put("list",matchList);				
			
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", 1);
			resultMap.put("errmsg", "系统异常！：" + e.getMessage());
		}
		return resultMap;
		
	}
	
	/**
	 * 查询出所有赛事，不包括删除的
	 * @param request
	 * @return
	 */
	@RequestMapping("/findAllMatch")
	@ResponseBody
	public Map<String, Object> findAllMatch(HttpServletRequest request){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {						
				List<Map<String,Object>> matchList=matchService.findAllMatch();
				resultMap.put("status", 0);
				resultMap.put("list",matchList);				
			
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", 1);
			resultMap.put("errmsg", "系统异常！：" + e.getMessage());
		}
		return resultMap;
		
	}
	/**
	 * 给赛事分配管理员
	 * @param request
	 * @param response
	 * @param iDisplayLength
	 * @param iDisplayStart
	 * @return
	 */
	@RequestMapping("/addUserByMid")
	@ResponseBody
	public Map<String, Object> addUserByMid(HttpServletRequest request, HttpServletResponse response,Integer iDisplayLength, Integer iDisplayStart) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			String uid=request.getParameter("uid");
			String mid=request.getParameter("mid");
			String uidList=request.getParameter("uidList");
			//uidList="[{'uid':'c7311155c4dc4b4382d6f3279a6b3ba7'}]";
			JSONArray array = JSONArray.fromObject(uidList);
			String creatime=Tools.getStamp(request);
			ReuserMatch reuserMatch=new ReuserMatch();
			reuserMatch.setMid(mid);
			reuserMatch.setCreatetime(creatime);
			reuserMatch.setUpuid(uid);
			matchService.addUserByMid(reuserMatch,array);
			resultMap.put("status", 0);			
		} catch (CannotCreateTransactionException e) {
			resultMap.put("status", 1);
			resultMap.put("errmsg", "数据库连接异常");
			e.printStackTrace();
		} catch (Exception e) {
			resultMap.put("status", 1);
			e.printStackTrace();
		}
		return resultMap;
	}
	/**
	 * 查询当前赛事下的管理员
	 * @param request
	 * @return
	 */
	@RequestMapping("/findMatchUserByMid")
	@ResponseBody
	public Map<String, Object> findMatchUserByMid(HttpServletRequest request){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String mid=request.getParameter("mid");			
			List<Map<String, Object>> list=matchService.findMatchUserByMid(mid);
			resultMap.put("status", 0);
			resultMap.put("list", list);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", 1);
			resultMap.put("errmsg", "系统异常！：" + e.getMessage());
		}
		return resultMap;
		
	}
	/**
	 * 删除当前赛事下的管理员
	 * @param request
	 * @return
	 */
	@RequestMapping("/deleteMatchUserByUid")
	@ResponseBody
	public Map<String, Object> deleteMatchUserByUid(HttpServletRequest request){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String mid=request.getParameter("mid");	
			String uid=request.getParameter("uid");
			ReuserMatchKey reuserMatchKey=new ReuserMatchKey();
			reuserMatchKey.setMid(mid);
			reuserMatchKey.setUid(uid);
			matchService.deleteMatchUserByUid(reuserMatchKey);
			resultMap.put("status", 0);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", 1);
			resultMap.put("errmsg", "系统异常！：" + e.getMessage());
		}
		return resultMap;
		
	}
	/**
	 * 查询出不在当前赛事下的管理员
	 * @param request
	 * @return
	 */
	@RequestMapping("/findNotMatchUserByMid")
	@ResponseBody
	public Map<String, Object> findNotMatchUserByMid(HttpServletRequest request){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String mid=request.getParameter("mid");			
			List<Map<String, Object>> list=matchService.findNotMatchUserByMid(mid);
			resultMap.put("status", 0);
			resultMap.put("list", list);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", 1);
			resultMap.put("errmsg", "系统异常！：" + e.getMessage());
		}
		return resultMap;
		
	}
}
