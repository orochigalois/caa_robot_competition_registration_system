package com.zts.robot.redis;

import java.io.IOException;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringUtil implements ApplicationContextAware {

	/**
    * 
    */
	private static ApplicationContext applicationContext;
	
	private static String path;

   /**
   * 
   */
	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		applicationContext = arg0;
		try {
			 path = applicationContext.getResource("").getFile().getAbsolutePath()+"\\pic\\" ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 */
	public static Object getObject(String id) {
		Object object = null;
		object = applicationContext.getBean(id);
		return object;
	}
	
	public  static String getpath(){
		return path;
	}

}