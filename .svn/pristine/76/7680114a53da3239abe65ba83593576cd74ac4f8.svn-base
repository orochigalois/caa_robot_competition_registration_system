package com.zts.robot.util;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.sun.star.beans.PropertyValue;
import com.sun.star.comp.helper.BootstrapException;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.text.XTextDocument;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.XCloseable;
import com.sun.star.util.XReplaceDescriptor;
import com.sun.star.util.XReplaceable;

import ooo.connector.BootstrapSocketConnector;

public class WordModel4Pdf {
	private static XDesktop xDesktop;
	private static BootstrapSocketConnector bootstrapSocketConnector;
	public static void main(String[] args) throws UnknownHostException, InterruptedException {
		
			WordModel4Pdf.initLibreOffice();
			String wordModelPathCup = "C:/projects/奖励证书模板V3.0.docx";
			for(int i=0;i<10000;i++){
				String savePdfPath="C:/ROOT/staticrobot/pdf/"+i+"奖励证书.pdf";
				XComponent xCompCup =WordModel4Pdf.loadWordModel(wordModelPathCup);
				Map<String, String> dataParamMap = new HashMap<String, String>();
				dataParamMap.put("编号", "a"+i);
				dataParamMap.put("学校", "b");
				dataParamMap.put("队伍", "c");
				dataParamMap.put("小项", "d");
				dataParamMap.put("奖杯", "e");
				dataParamMap.put("教师", "f");
				dataParamMap.put("队员", "g");
				WordModel4Pdf.replaceParam(xCompCup, dataParamMap);
				WordModel4Pdf.savePdf(xCompCup, savePdfPath);
			}		
		
			closeLibreOffice();
	}
	
	
	//创建链接
	public static void initLibreOffice() throws UnknownHostException{			
			String oooExecFolder = "D:/Program Files/LibreOffice 5/program";
			//String oooExecFolder = "C:/server/LibreOffice 5/program";
			InetAddress address = InetAddress.getLocalHost();
			String host = address.getHostAddress();
			int port = 8100;
			System.out.println("主机地址："+host);
			bootstrapSocketConnector = new BootstrapSocketConnector(oooExecFolder);
			XComponentContext xContext = null;
			try {
				xContext = bootstrapSocketConnector.connect(host,port);
			} catch (BootstrapException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
			
			
			
			/*XComponentContext xContext = null;
			try {
				xContext = BootstrapSocketConnector.bootstrap(oooExecFolder, host, port);
			} catch (BootstrapException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}*/
			XMultiComponentFactory xMCF = xContext.getServiceManager();
			
			Object oDesktop = null;
			try {
				oDesktop = xMCF.createInstanceWithContext(
					     "com.sun.star.frame.Desktop", xContext);
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			
			xDesktop = (XDesktop) UnoRuntime.queryInterface(
			     XDesktop.class, oDesktop);
	}
	
	/**
	 * 加载模板
	 * @param wordModelPath
	 * @throws com.sun.star.io.IOException 
	 * @throws com.sun.star.io.IOException 
	 * @throws IllegalArgumentException 
	 */
	public static XComponent loadWordModel(String wordModelPath){

		if (!new File(wordModelPath).canRead()) {
			throw new RuntimeException("未能加载模板！模板有问题！:" + new File(wordModelPath));
		}
		
		XComponentLoader xCompLoader = (XComponentLoader) UnoRuntime
			.queryInterface(com.sun.star.frame.XComponentLoader.class, xDesktop);
		
		//PropertyValue[] propertyValues = new PropertyValue[0];
		
		PropertyValue[] propertyValues = new PropertyValue[1];
		
		propertyValues[0] = new PropertyValue();
		propertyValues[0].Name = "Hidden";
		propertyValues[0].Value = Boolean.TRUE;
		XComponent xComp = null;
		try {
			xComp = xCompLoader.loadComponentFromURL("file:///"+wordModelPath, "_blank", 0, propertyValues);
		} catch (com.sun.star.io.IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return xComp;
	}
	/**
	 * 模板替换参数
	 * @param xComp
	 * @param dataParamMap
	 */
	public static void replaceParam(XComponent xComp, Map<String, String> dataParamMap) {
		XReplaceDescriptor xReplaceDescr = null;
		XReplaceable xReplaceable = null;
		XTextDocument xTextDocument = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class, xComp);
		xReplaceable = (XReplaceable) UnoRuntime.queryInterface(XReplaceable.class, xTextDocument);
		xReplaceDescr = (XReplaceDescriptor) xReplaceable.createReplaceDescriptor();

		Set<Entry<String, String>> textSets = dataParamMap.entrySet();
		for (Entry<String, String> textSet : textSets) {
			xReplaceDescr.setSearchString("<" + textSet.getKey() + ">");
			xReplaceDescr.setReplaceString(textSet.getValue());
			xReplaceable.replaceAll(xReplaceDescr);
		}
	}
	//生成pdf
	public static void savePdf(XComponent xComp,String savePdfPath){
		/*File file = new File(savePdfPath);
		if(file.exists()){
			file.delete();
		}*/
		XStorable xStorable = (XStorable) UnoRuntime
				.queryInterface(XStorable.class, xComp);

		PropertyValue[] propertyValues = new PropertyValue[2];
		// Setting the flag for overwriting
		propertyValues[0] = new PropertyValue();
		propertyValues[0].Name = "Overwrite";
		propertyValues[0].Value = new Boolean(true);
		// Setting the filter name
		propertyValues[1] = new PropertyValue();
		propertyValues[1].Name = "FilterName";
		propertyValues[1].Value = "writer_pdf_Export";

		// Appending the favoured extension to the origin document name
		try {
			xStorable.storeToURL("file:///" + savePdfPath, propertyValues);		
			XCloseable xCloseable = UnoRuntime.queryInterface(XCloseable.class,xComp);
			 	if (xCloseable != null ) {
			 		xCloseable.close(false);
			 	} else{
			 		XComponent newxComp = UnoRuntime.queryInterface(XComponent.class, xComp );
			 		newxComp.dispose();
			 	}
			
		} catch (Exception e) {
			System.out.println("关闭出错！");
			e.printStackTrace();
		}
		
		System.out.println("Saved " + savePdfPath);
	}
	public static void closeLibreOffice(){
		bootstrapSocketConnector.disconnect();
	}
}
