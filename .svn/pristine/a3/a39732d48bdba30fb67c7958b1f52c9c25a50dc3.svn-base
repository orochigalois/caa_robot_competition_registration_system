package com.zts.robot.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesHelper {

	private Properties properties;

	private String path;

	public PropertiesHelper(String path) throws IOException {

		this.path = path;

		// InputStream is = new BufferedInputStream(new FileInputStream(new
		// File(path)));
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(path);
		properties = new Properties();
		properties.load(is);
	}

	public void setProperties(String key, Object value) {
		properties.put(key, value);
	}

	public Object getProperties(String key) {
		return properties.get(key);
	}

	public void save() throws IOException {
		// /保存属性到b.properties文件
		FileOutputStream oFile = new FileOutputStream(path, true);// true表示追加打开
		properties.store(oFile, "");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {

			PropertiesHelper helper = new PropertiesHelper("./param.properties");
			// helper.setProperties("tester", "123");
			System.out.println(helper.getProperties("user"));
			helper.save();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
