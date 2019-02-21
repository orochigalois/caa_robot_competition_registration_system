package com.zts.robot.controller;

import java.io.File;
import java.io.FilenameFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.zts.robot.util.MyProperties;
import com.zts.robot.util.Tools;

import net.coobird.thumbnailator.Thumbnails;
import net.sf.json.JSONObject;

/**
 * 上传文件
 * 
 * @author ZTS_Q
 * 
 */
@Controller
public class UploadFileController {

	/**
	 * 上传文件
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/uploadFiles")
	public void fileUpload(@RequestParam("files") MultipartFile[] files,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception { // TODO 自动生成的方法存根
		JSONObject result = new JSONObject();
		/*
		 * // 获取文件类型 System.out.println(file.getContentType()); // 获取文件大小
		 * System.out.println(file.getSize()); // 获取文件名称
		 * System.out.println(file.getOriginalFilename());
		 */
		// 根目录
		String PootPathkey = MyProperties.getKey("RootPathkey");
		String RootFileUrlkey = MyProperties.getKey("RootFileUrlkey");
		try {
			if (files != null && files.length > 0) {
				String fileurls = "";
				for (int i = 0; i < files.length; i++) {
					if(files[i].getSize()>0){
					// 00头像 、01文件
					String savetype = request.getParameter("savetype");
					String name="";
					if ("03".equals(savetype)) {
						name = request.getParameter("filenewname");
						System.out.print(name); 
					 }else{
						 name=files[i].getOriginalFilename();//获取文件名称
					 }

					String str=name.substring(name.lastIndexOf(".") + 1);//获取文件名称后缀
					String strname=name.substring(0,name.indexOf("."));//截取文件名称不带后缀名
					// 父文件夹
					String parentfolder = null;
					String filetype = null;
					if ("00".equals(savetype)) {
						parentfolder = "headImage";
						filetype = "."+str;
					} else if ("01".equals(savetype)) {
						parentfolder = "pdfImage";
						filetype = "."+str;
					} else if ("02".equals(savetype)) {
						parentfolder = "modelImage";
						filetype = "."+str;
					} else if ("03".equals(savetype)) {
						parentfolder = "rcjlogimage";
						filetype = "."+str;
					}
					
					// 子文件夹
					//String sonfolder = Tools.dateStr();
					// 文件夹路径
					//String folderpath = parentfolder + "/" + sonfolder + "/";
					String folderpath = parentfolder + "/" ;
					// 文件名
					String filename=Tools.dateStr()+Tools.getRandom(4)+"_"+name;
					
					//String filename = Tools.filenameStr() + filetype;
					// 全文件目录
					// String dirpath = PootPathkey + folderpath;
					// 文件全路径，文件全访问地址
					String path = PootPathkey + folderpath + filename;
					String fileurl = RootFileUrlkey + folderpath + filename;

					File savedir = new File(path);
					if (!savedir.getParentFile().exists())
						savedir.getParentFile().mkdirs();
					files[i].transferTo(new File(path));
					fileurls = fileurls + "," + fileurl;
					if ("00".equals(savetype)) {
						File toPic = new File(path);
						Thumbnails.of(toPic).size(143, 200).outputFormat("jpg")
								.toFile(toPic);
					}
					}
					
				}
				result.put("status", 0);
				result.put("urls", fileurls.substring(1));
				String oldurls = request.getParameter("oldurls");
				if (oldurls != null && !"".equals(oldurls)) {
					oldurls = oldurls.replace(RootFileUrlkey, PootPathkey);
					String[] strs = oldurls.split(",");
					for (int i = 0; i < strs.length; i++) {
						File file = new File(strs[i]);
						file.delete();
					}
				}

			} else {
				result.put("status", 1);
				result.put("errmsg", "上传文件为空！请重试!");
			}
		} catch (Exception e) {
			result.put("status", 1);
			result.put("errmsg", "文件格式不对！" + e);
		}
		Tools.WriteString(response, JSONObject.fromObject(result).toString());
	}

	
	/**
	 * 上传特殊路径文件
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/uploadFiles_path")
	public void fileUpload_path(@RequestParam("files") MultipartFile[] files,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception { // TODO 自动生成的方法存根
		JSONObject result = new JSONObject();
		/*
		 * // 获取文件类型 System.out.println(file.getContentType()); // 获取文件大小
		 * System.out.println(file.getSize()); // 获取文件名称
		 * System.out.println(file.getOriginalFilename());
		 */
		// 根目录
		String PootPathkey = MyProperties.getKey("RootPathkey");
		String RootFileUrlkey = MyProperties.getKey("RootFileUrlkey");
		try {
			if (files != null && files.length > 0) {
				String fileurls = "";
				for (int i = 0; i < files.length; i++) {
					if(files[i].getSize()>0){
					// 00头像 、01文件
					String savetype = request.getParameter("savetype");
					String name="";
					String mname="";
					String rname="";
					String tcode="";
					String tmname="";
					if("00".equals(savetype)){
						tcode = request.getParameter("tcode");
						mname = request.getParameter("mname");
						tmname = request.getParameter("tmname");
						System.out.print(tcode); 
						System.out.print(mname); 
						System.out.print(tmname); 
					}
					if ("03".equals(savetype)) {
						name = request.getParameter("filenewname");
						mname = request.getParameter("mname");
						rname = request.getParameter("rname");
						System.out.print(name); 
						System.out.print(mname); 
						System.out.print(rname); 
					 }else{
						 name=files[i].getOriginalFilename();//获取文件名称
					 }

					String str=name.substring(name.lastIndexOf(".") + 1);//获取文件名称后缀
					String strname=name.substring(0,name.indexOf("."));//截取文件名称不带后缀名
					// 父文件夹
					String parentfolder = null;
					String filetype = null;
					if ("00".equals(savetype)) {
						parentfolder = "headImage";
						filetype = "."+str;
					} else if ("01".equals(savetype)) {
						parentfolder = "pdfImage";
						filetype = "."+str;
					} else if ("02".equals(savetype)) {
						parentfolder = "modelImage";
						filetype = "."+str;
					} else if ("03".equals(savetype)) {
						parentfolder = "rcjlogimage";
						filetype = "."+str;
					}
					
					// 子文件夹
					//String sonfolder = Tools.dateStr();
					// 文件夹路径
					//String folderpath = parentfolder + "/" + sonfolder + "/";
					String folderpath = parentfolder + "/" + mname + "/" + rname + "/";
					String filename;
					if ("00".equals(savetype)) {
						filename=tcode+"_"+tmname;
					}else{
						// 文件名
						filename=Tools.dateStr()+Tools.getRandom(4)+"_"+name;
					}
					
					
					/*_____Alex add 2018.02.25*/
					/*delete all files with the suffix such as "阶段二日志_机器人救援赛-机器人超级轨迹赛（小学）_Y1801T52879" under the folder*/
					final File alexfolder = new File(PootPathkey + folderpath);
					if(alexfolder.exists()){
						for (File f : alexfolder.listFiles()) {
						    if (f.getName().contains(name)) {
						        f.delete();
						    }
						}
					}
					/*_____Alex add 2018.02.25 END*/
					 
					 
					
					//String filename = Tools.filenameStr() + filetype;
					// 全文件目录
					// String dirpath = PootPathkey + folderpath;
					// 文件全路径，文件全访问地址
					String path = PootPathkey + folderpath + filename;
					String fileurl = RootFileUrlkey + folderpath + filename;

					File savedir = new File(path);
					if (!savedir.getParentFile().exists())
						savedir.getParentFile().mkdirs();
					files[i].transferTo(new File(path));
					fileurls = fileurls + "," + fileurl;
					if ("00".equals(savetype)) {
						File toPic = new File(path);
						Thumbnails.of(toPic).size(143, 200).outputFormat("jpg")
								.toFile(toPic);
					}					
					}
					
				}
				result.put("status", 0);
				result.put("urls", fileurls.substring(1));
				String oldurls = request.getParameter("oldurls");
				if (oldurls != null && !"".equals(oldurls)) {
					oldurls = oldurls.replace(RootFileUrlkey, PootPathkey);
					String[] strs = oldurls.split(",");
					for (int i = 0; i < strs.length; i++) {
						File file = new File(strs[i]);
						file.delete();
					}
				}

			} else {
				result.put("status", 1);
				result.put("errmsg", "上传文件为空！请重试!");
			}
		} catch (Exception e) {
			result.put("status", 1);
			result.put("errmsg", "文件格式不对！" + e);
		}
		Tools.WriteString(response, JSONObject.fromObject(result).toString());
	}

}
