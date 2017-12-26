package com.zts.robot.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public class DownLoadFile {
	/**
	 * 文件下载
	 * 
	 * @param response
	 * @param path
	 * @param result
	 * @throws Exception
	 */
	public static void downLoadFileWeb(
			HttpServletResponse response, Map<String, Object> result) throws Exception {
		String path = result.get("path").toString();
		File file = new File(path);
		if (!file.exists()) {
			result.put("status", 1);
			result.put("errmsg", "文件不存在！");
			Tools.WriteString(response, JSONObject.fromObject(result)
					.toString());
			return;
		} else {

			// 设置响应头，控制浏览器下载该文件
			response.setHeader("content-disposition", "attachment;filename="
					+ URLEncoder.encode(file.getName(), "UTF-8"));
			// 读取要下载的文件，保存到文件输入流
			FileInputStream in = new FileInputStream(path);
			// 创建输出流
			OutputStream out = response.getOutputStream();
			// 创建缓冲区
			byte buffer[] = new byte[1024];
			int len = 0;
			// 循环将输入流中的内容读取到缓冲区当中
			while ((len = in.read(buffer)) > 0) {
				// 输出缓冲区的内容到浏览器，实现文件下载
				out.write(buffer, 0, len); // result.put("buffer",buffer);
			}
			// 关闭文件输入流
			in.close();
			// 关闭输出流
			out.flush();
			out.close();
		}
	}

}
