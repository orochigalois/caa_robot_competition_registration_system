package com.zts.robot.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zts.robot.pojo.Payorder;
import com.zts.robot.service.PayService;
import com.zts.robot.unionpay.sdk.AcpService;
import com.zts.robot.unionpay.sdk.DemoBase;
import com.zts.robot.unionpay.sdk.LogUtil;
import com.zts.robot.unionpay.sdk.SDKConfig;
import com.zts.robot.unionpay.sdk.SDKConstants;

@Controller
public class PayController {
	@Autowired
	private PayService payService;
	
	 /**
	  * 查询该赛事下的所有订单
	  * @param req
	  * @param resp
	  * @param iDisplayLength
	  * @param iDisplayStart
	  * @return
	  */
	@RequestMapping("/signuidPayListByMid")
	@ResponseBody
	public Map<String, Object> signuidPayListByMid(HttpServletRequest req, HttpServletResponse resp,Integer iDisplayLength,
			Integer iDisplayStart){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String mid = req.getParameter("mid");
			String orderid = req.getParameter("orderid");
			String signuid = req.getParameter("signuid");
			String school = req.getParameter("school");
			String txntype = req.getParameter("txntype");
			String txnstatus = req.getParameter("txnstatus");
			String uname = req.getParameter("uname");
			
			paramMap.put("mid", mid);
			paramMap.put("signuid", signuid);
			paramMap.put("school", school);
			paramMap.put("txntype", txntype);
			paramMap.put("txnstatus", txnstatus);
			paramMap.put("orderid", orderid);
			paramMap.put("uname", uname);
			
			int totalSize = payService.signuidPayListByMidTotalSize(paramMap);
			if (iDisplayLength==null||iDisplayLength != -1) {
				paramMap.put("beginNo", iDisplayStart);
				paramMap.put("endNo", iDisplayLength);
			}
			List<Map<String, Object>> list = payService.signuidPayListByMid(paramMap);
			resultMap.put("status", 0);
			resultMap.put("list",list);
			resultMap.put("iTotalRecords", totalSize);
		} catch (Exception e) {
			// TODO: handle exception
		}		
		return resultMap;		
	}
	
	/**
	 * 现金交易
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping("/cashPayConsume")
	@ResponseBody
	public Map<String, Object> cashPayConsume(HttpServletRequest req, HttpServletResponse resp){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String txnAmt = req.getParameter("txnAmt");
		String orderId = req.getParameter("orderId");
		String txnTime = req.getParameter("txnTime");
		String signuid =  req.getParameter("signuid");
		String mid = req.getParameter("mid");
		
		Payorder payorder = new Payorder();
		payorder.setMid(mid);
		payorder.setSignuid(signuid);
		payorder.setOrderid(orderId);
		//支付类型（00现金01银联02凭证）
		payorder.setTxntype("00");
		payorder.setTxnamt(Integer.parseInt(txnAmt));
		//交易状态00已支付确认/01待支付确认02作废
		payorder.setTxnstatus("01");		
		payorder.setTxntime(txnTime);
		payorder.setPaymenturl("");
		try {
			payService.PayConsume(payorder);
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			resultMap.put("status", 1);
			resultMap.put("errmsg", "系统错误"+e);
		}
		
		return resultMap;
		
	}
	/**
	 * 现金交易确认/废除
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping("/cashPayRcv")
	@ResponseBody
	public Map<String, Object> cashPayRcv(HttpServletRequest req, HttpServletResponse resp){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String orderId = req.getParameter("orderId");		
		String signuid =  req.getParameter("signuid");
		String mid = req.getParameter("mid");
		String txnstatus = req.getParameter("txnstatus");
		
		Payorder payorder = new Payorder();
		payorder.setMid(mid);
		payorder.setSignuid(signuid);
		payorder.setOrderid(orderId);		
		//交易状态00已支付确认01待支付确认02废除03已开发票
		payorder.setTxnstatus(txnstatus);
		
		try {
			payService.PayRcv(payorder);
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			resultMap.put("status", 1);
			resultMap.put("errmsg", "系统错误"+e);
		}
		
		return resultMap;
		
	}
	
	/**
	 * 凭证交易
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping("/proofPayConsume")
	@ResponseBody
	public Map<String, Object> proofPayConsume(HttpServletRequest req, HttpServletResponse resp){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String orderId = req.getParameter("orderId");
		String txnTime = req.getParameter("txnTime");
		String signuid =  req.getParameter("signuid");
		String mid = req.getParameter("mid");
		String paymenturl =  req.getParameter("paymenturl");
		String txnstatus =  req.getParameter("txnstatus");
		
		Payorder payorder = new Payorder();
		payorder.setMid(mid);
		payorder.setSignuid(signuid);
		payorder.setOrderid(orderId);
		//支付类型（00现金01银联02凭证）
		payorder.setTxntype("02");
		//交易状态00已支付确认/01待支付确认02作废
		payorder.setTxnstatus(txnstatus);		
		payorder.setTxntime(txnTime);
		payorder.setTxnamt(0);
		payorder.setPaymenturl(paymenturl);
		try {
			payService.PayConsume(payorder);
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			resultMap.put("status", 1);
			resultMap.put("errmsg", "系统错误"+e);
		}
		
		return resultMap;
		
	}
	
	/**
	 * 凭证交易确认/废除
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping("/proofPayRcv")
	@ResponseBody
	public Map<String, Object> proofPayRcv(HttpServletRequest req, HttpServletResponse resp){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String orderId = req.getParameter("orderId");		
		String signuid =  req.getParameter("signuid");
		String mid = req.getParameter("mid");
		String txnstatus = req.getParameter("txnstatus");
		String txnamt = req.getParameter("txnamt");
		
		Payorder payorder = new Payorder();
		payorder.setMid(mid);
		payorder.setSignuid(signuid);
		payorder.setOrderid(orderId);		
		//交易状态00已支付确认01待支付确认02废除
		payorder.setTxnstatus(txnstatus);
		payorder.setTxnamt(Integer.parseInt(txnamt));
		try {
			payService.PayRcv(payorder);
			resultMap.put("status", 0);
		} catch (Exception e) {
			// TODO: handle exception
			resultMap.put("status", 1);
			resultMap.put("errmsg", "系统错误"+e);
		}
		
		return resultMap;
		
	}
	
	/**
	 * 银联支付——消费
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	@RequestMapping("/unionPayConsume")
	@ResponseBody
	public void unionPayConsume(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		//Map<String, Object> resultMap = new HashMap<String, Object>();
		//前台页面传过来的
		//String merId ="777290058151001";
		String merId ="802110086410505";
		//String merId ="700000000000001";
		String mid = req.getParameter("mid");
		String txnAmt = req.getParameter("txnAmt");
		String orderId = req.getParameter("orderId");
		String txnTime = req.getParameter("txnTime");
		String signuid =  req.getParameter("signuid");
		
		
		Payorder payorder = new Payorder();
		payorder.setMid(mid);
		payorder.setOrderid(orderId);
		payorder.setPaymenturl("");
		payorder.setSignuid(signuid);
		payorder.setTxnamt(Integer.parseInt(txnAmt));
		payorder.setTxnstatus("02");
		payorder.setTxntime(txnTime);
		payorder.setTxntype("01");
		payService.PayConsume(payorder);
		Map<String, String> requestData = new HashMap<String, String>();
		
		/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
		requestData.put("version", DemoBase.version);   			  //版本号，全渠道默认值
		requestData.put("encoding", DemoBase.encoding); 			  //字符集编码，可以使用UTF-8,GBK两种方式
		requestData.put("signMethod", SDKConfig.getConfig().getSignMethod()); //签名方法
		requestData.put("txnType", "01");               			  //交易类型 ，01：消费
		requestData.put("txnSubType", "01");            			  //交易子类型， 01：自助消费
		requestData.put("bizType", "000201");           			  //业务类型，B2C网关支付，手机wap支付
		requestData.put("channelType", "07");           			  //渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板  08：手机
		
		/***商户接入参数***/
		requestData.put("merId", merId);    	          			  //商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
		requestData.put("accessType", "0");             			  //接入类型，0：直连商户 
		requestData.put("orderId",orderId);             //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则		
		requestData.put("txnTime", txnTime);        //订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
		requestData.put("currencyCode", "156");         			  //交易币种（境内商户一般是156 人民币）		
		requestData.put("txnAmt", txnAmt);      			      //交易金额，单位分，不要带小数点
		requestData.put("reqReserved", signuid+","+mid); 
		//requestData.put("mid", mid); 
		//请求方保留域，如需使用请启用即可；透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节。出现&={}[]符号时可能导致查询接口应答报文解析失败，建议尽量只传字母数字并使用|分割，或者可以最外层做一次base64编码(base64编码之后出现的等号不会导致解析失败可以不用管)。		
		
		//前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面 点击“返回商户”按钮的时候将异步通知报文post到该地址
		//如果想要实现过几秒中自动跳转回商户页面权限，需联系银联业务申请开通自动返回商户权限
		//异步通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
		requestData.put("frontUrl", DemoBase.frontUrl);
		
		//后台通知地址（需设置为【外网】能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
		//后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
		//注意:1.需设置为外网能访问，否则收不到通知    2.http https均可  3.收单后台通知后需要10秒内返回http200或302状态码 
		//    4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
		//    5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
		requestData.put("backUrl", DemoBase.backUrl);

		// 订单超时时间。
		// 超过此时间后，除网银交易外，其他交易银联系统会拒绝受理，提示超时。 跳转银行网银交易如果超时后交易成功，会自动退款，大约5个工作日金额返还到持卡人账户。
		// 此时间建议取支付时的北京时间加15分钟。
		// 超过超时时间调查询接口应答origRespCode不是A6或者00的就可以判断为失败。
		requestData.put("payTimeout", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date().getTime() + 15 * 60 * 1000));
		
		//////////////////////////////////////////////////
		//
		//       报文中特殊用法请查看 PCwap网关跳转支付特殊用法.txt
		//
		//////////////////////////////////////////////////
		
		/**请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面**/
		Map<String, String> submitFromData = AcpService.sign(requestData,DemoBase.encoding);  //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
		
		String requestFrontUrl = SDKConfig.getConfig().getFrontRequestUrl();  //获取请求银联的前台地址：对应属性文件acp_sdk.properties文件中的acpsdk.frontTransUrl
		String html = AcpService.createAutoFormHtml(requestFrontUrl, submitFromData,DemoBase.encoding);   //生成自动跳转的Html表单
		
		LogUtil.writeLog("打印请求HTML，此为请求报文，为联调排查问题的依据："+html);
		//将生成的html写到浏览器中完成自动跳转打开银联支付页面；这里调用signData之后，将html写到浏览器跳转到银联页面之前均不能对html中的表单项的名称和值进行修改，如果修改会导致验签不通过
		resp.getWriter().write(html);
		//return resultMap;		
	}
	
	@RequestMapping("/unionPayFrontRcv")
	@ResponseBody
	private void unionPayFrontRcv(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LogUtil.writeLog("FrontRcvResponse前台接收报文返回开始");

		String encoding = req.getParameter(SDKConstants.param_encoding);
		LogUtil.writeLog("返回报文中encoding=[" + encoding + "]");		
		Map<String, String> respParam = getAllRequestParam(req);

		// 打印请求报文
		LogUtil.printRequestLog(respParam);

		Map<String, String> valideData = null;
		if (null != respParam && !respParam.isEmpty()) {
			Iterator<Entry<String, String>> it = respParam.entrySet()
					.iterator();
			valideData = new HashMap<String, String>(respParam.size());
			while (it.hasNext()) {
				Entry<String, String> e = it.next();
				String key = (String) e.getKey();
				String value = (String) e.getValue();
				value = new String(value.getBytes(encoding), encoding);
				valideData.put(key, value);
			}
		}
		if (!AcpService.validate(valideData, encoding)) {
			LogUtil.writeLog("验证签名结果[失败].");
		} else {
			LogUtil.writeLog("验证签名结果[成功].");
			System.out.println(valideData.get("orderId")); //其他字段也可用类似方式获取			
			String respCode = valideData.get("respCode");
			System.out.println(respCode);
			//判断respCode=00、A6后，对涉及资金类的交易，请再发起查询接口查询，确定交易成功后更新数据库。
			if("00".equals(respCode) || "A6".equals(respCode)){
				unionPayQuery(req, resp);
			}
			
		}
		
		resp.sendRedirect("http://robotreg.drct-caa.org.cn/jsp/payRelevant.html");
		LogUtil.writeLog("FrontRcvResponse前台接收报文返回结束");
	}
	
	@RequestMapping("/unionPayBackRcv")
	@ResponseBody
	private void unionPayBackRcv(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LogUtil.writeLog("BackRcvResponse接收后台通知开始");

		
		// 获取银联通知服务器发送的后台通知参数
		Map<String, String> reqParam = getAllRequestParamStream(req);
		String encoding = req.getParameter(SDKConstants.param_encoding);
		LogUtil.printRequestLog(reqParam);
		
		//重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
		if (!AcpService.validate(reqParam, encoding)) {
			LogUtil.writeLog("验证签名结果[失败].");
			//验签失败，需解决验签问题
			
		} else {
			LogUtil.writeLog("验证签名结果[成功].");
			//【注：为了安全验签成功才应该写商户的成功处理逻辑】交易成功，更新商户订单状态
			
			String orderId =reqParam.get("orderId"); //获取后台通知的数据，其他字段也可用类似方式获取
			String respCode = reqParam.get("respCode");
			//判断respCode=00、A6后，对涉及资金类的交易，请再发起查询接口查询，确定交易成功后更新数据库。
			if("00".equals(respCode) || "A6".equals(respCode)){
				String txnAmt = reqParam.get("txnAmt");
				String reqReserved = reqParam.get("reqReserved");
				String signuid = reqReserved.split(",")[0];				
				String mid=reqReserved.split(",")[1];
				String txnTime=reqParam.get("txnTime");
				Payorder payorder = new Payorder();
				payorder.setMid(mid);
				payorder.setOrderid(orderId);
				payorder.setPaymenturl("");
				payorder.setSignuid(signuid);
				payorder.setTxnamt(Integer.parseInt(txnAmt));
				payorder.setTxnstatus("00");
				payorder.setTxntime(txnTime);
				payorder.setTxntype("01");
				payService.PayRcv(payorder);
			}
		}
		LogUtil.writeLog("BackRcvResponse接收后台通知结束");
		//返回给银联服务器http 200  状态码
		resp.getWriter().print("ok");
	}
	
	/**
	  * 获取请求参数中所有的信息。
	  * 非struts可以改用此方法获取，好处是可以过滤掉request.getParameter方法过滤不掉的url中的参数。
	  * struts可能对某些content-type会提前读取参数导致从inputstream读不到信息，所以可能用不了这个方法。理论应该可以调整struts配置使不影响，但请自己去研究。
	  * 调用本方法之前不能调用req.getParameter("key");这种方法，否则会导致request取不到输入流。
	  * @param request
	  * @return
	  */
	
	 public Map<String, String> getAllRequestParamStream(HttpServletRequest request) {
		  Map<String, String> res = new HashMap<String, String>();
		  try {
		   String notifyStr = new String(IOUtils.toByteArray(request.getInputStream()),DemoBase.encoding);
		   LogUtil.writeLog("收到通知报文：" + notifyStr);
		   String[] kvs= notifyStr.split("&");
		   for(String kv : kvs){
		    String[] tmp = kv.split("=");
		    if(tmp.length >= 2){
		     String key = tmp[0];
		     String value = URLDecoder.decode(tmp[1],DemoBase.encoding);
		     res.put(key, value);
		    }
		   }
		  } catch (UnsupportedEncodingException e) {
		   LogUtil.writeLog("getAllRequestParamStream.UnsupportedEncodingException error: " + e.getClass() + ":" + e.getMessage());
		  } catch (IOException e) {
		   LogUtil.writeLog("getAllRequestParamStream.IOException error: " + e.getClass() + ":" + e.getMessage());
		  }
		  return res;
	 }
	
	@RequestMapping("/unionPayQuery")
	@ResponseBody
	private void unionPayQuery(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LogUtil.writeLog("unionPayQuery开始");
		//String mid = req.getParameter("mid");
		String orderId = req.getParameter("orderId");
		String txnTime = req.getParameter("txnTime");
		//String merId ="777290058151001";
		String merId ="802110086410505";
		//String merId ="700000000000001";
		
		Map<String, String> data = new HashMap<String, String>();
		
		/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
		data.put("version", DemoBase.version);                 //版本号
		data.put("encoding", DemoBase.encoding);               //字符集编码 可以使用UTF-8,GBK两种方式
		data.put("signMethod", SDKConfig.getConfig().getSignMethod()); //签名方法
		data.put("txnType", "00");                             //交易类型 00-默认
		data.put("txnSubType", "00");                          //交易子类型  默认00
		data.put("bizType", "000201");                         //业务类型 B2C网关支付，手机wap支付
		
		/***商户接入参数***/
		data.put("merId", merId);                  //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
		data.put("accessType", "0");                           //接入类型，商户接入固定填0，不需修改
		
		/***要调通交易以下字段必须修改***/
		data.put("orderId", orderId);                 //****商户订单号，每次发交易测试需修改为被查询的交易的订单号
		data.put("txnTime", txnTime);                 //****订单发送时间，每次发交易测试需修改为被查询的交易的订单发送时间

		/**请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文------------->**/
		
		Map<String, String> reqData = AcpService.sign(data,DemoBase.encoding);//报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
		
		String url = SDKConfig.getConfig().getSingleQueryUrl();// 交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.singleQueryUrl
		//这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
		Map<String, String> rspData = AcpService.post(reqData,url,DemoBase.encoding);
		
		/**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
		//应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
		if(!rspData.isEmpty()){
			if(AcpService.validate(rspData, DemoBase.encoding)){
				LogUtil.writeLog("验证签名成功");
				if("00".equals(rspData.get("respCode"))){//如果查询交易成功
					//处理被查询交易的应答码逻辑
					String origRespCode = rspData.get("origRespCode");
					String txnAmt = rspData.get("txnAmt");
					String reqReserved = rspData.get("reqReserved");
					String signuid = reqReserved.split(",")[0];
					String mid=reqReserved.split(",")[1];
					if("00".equals(origRespCode)){//交易成功，更新商户订单状态
						Payorder payorder = new Payorder();
						payorder.setMid(mid);
						payorder.setOrderid(orderId);
						payorder.setPaymenturl("");
						payorder.setSignuid(signuid);
						payorder.setTxnamt(Integer.parseInt(txnAmt));
						payorder.setTxnstatus("00");
						payorder.setTxntime(txnTime);
						payorder.setTxntype("01");
						payService.PayRcv(payorder);
						//TODO
					}else if("03".equals(origRespCode) ||
							 "04".equals(origRespCode) ||
							 "05".equals(origRespCode)){
						//需再次发起交易状态查询交易 
						unionPayQuery(req, resp);
						//TODO
					}else{
						//其他应答码为失败请排查原因
						Payorder payorder = new Payorder();
						payorder.setMid(mid);
						payorder.setOrderid(orderId);
						payorder.setPaymenturl("");
						payorder.setSignuid(signuid);
						payorder.setTxnamt(Integer.parseInt(txnAmt));
						payorder.setTxnstatus("02");
						payorder.setTxntime(txnTime);
						payorder.setTxntype("01");
						payService.PayRcv(payorder);
						//TODO
					}
				}else{//查询交易本身失败，或者未查到原交易，检查查询交易报文要素
					//TODO
				}
			}else{
				LogUtil.writeErrorLog("验证签名失败");
				//TODO 检查验证签名失败的原因
			}
		}else{
			//未返回正确的http状态
			LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");
		}
		/*String reqMessage = DemoBase.genHtmlResult(reqData);
		String rspMessage = DemoBase.genHtmlResult(rspData);
		resp.getWriter().write("</br>请求报文:<br/>"+reqMessage+"<br/>" + "应答报文:</br>"+rspMessage+"");*/
	}
	
	
	/**
	 * 获取请求参数中所有的信息
	 * 当商户上送frontUrl或backUrl地址中带有参数信息的时候，
	 * 这种方式会将url地址中的参数读到map中，会导多出来这些信息从而致验签失败，这个时候可以自行修改过滤掉url中的参数或者使用getAllRequestParamStream方法。
	 * @param request
	 * @return
	 */
	public Map<String, String> getAllRequestParam(HttpServletRequest request) {
		Map<String, String> res = new HashMap<String, String>();
		Enumeration<?> temp = request.getParameterNames();
		if (null != temp) {
			while (temp.hasMoreElements()) {
				String en = (String) temp.nextElement();
				String value = request.getParameter(en);
				res.put(en, value);
				// 在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
				if (res.get(en) == null || "".equals(res.get(en))) {
					// System.out.println("======为空的字段名===="+en);
					res.remove(en);
				}
			}
		}
		return res;
	}
	
	
}
