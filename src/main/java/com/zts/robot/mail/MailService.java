package com.zts.robot.mail;
 

public class MailService {
	
	private String mailSmtpHost = "smtp.mxhichina.com";
	private String sendMailUser = "zdtd@zdtdchina.com";
	private String sendMailPassword="Email8888*";
	private String mailFrom="zdtd@zdtdchina.com";
	
	/*private String mailSmtpHost = "smtp.ym.163.com";
	private String sendMailUser = "admin@txboss.com";
	private String sendMailPassword="txboss886";
	private String mailFrom="admin@txboss.com";*/
	/**
	 * @param mailto 收件人
	 * @param mailcc 抄送人
	 * @param subject 标题
	 * @param bodyText 正文文本, 可以为NULL
	 * @param bodyHtml 正文html, 可以为NULL
	 * @param aAffix 附件数组{{"显示名", "附件"}, {"a", "/a.jpg"}}
	 */
	public void send(String mailto,String mailcc, String subject, String bodyText, String bodyHtml, String[][] aAffix) {
		MailHandler mh = new MailHandler(new String[]{mailSmtpHost, sendMailUser, sendMailPassword, mailFrom});
		mh.send(mailto,mailcc,subject, bodyText, bodyHtml, aAffix);
	}
	

	
	/**
	 * 
	 * @param mailSmtpHost
	 * @param sendMailUser
	 * @param sendMailPassword
	 * @param mailFrom
	 */
	public MailService(String mailSmtpHost, String sendMailUser, String sendMailPassword, String mailFrom){
		this.mailSmtpHost = mailSmtpHost;
		this.sendMailUser = sendMailUser;
		this.sendMailPassword = sendMailPassword;
		this.mailFrom = mailFrom;
	}
	
	
	public MailService() {
		
	}
	/*public static void main(String[] args) {
		//MailHandler mh = new MailHandler(new String[]{"smtp.ym.163.com","resume@verygrow.com","85TldjpdbA","resume@verygrow.com"});
		MailService service = new MailService();
		String[][] affix = {{"附件1.pdf","D:\\ROOT\\static\\htpdf\\20151022\\1_115053613448.pdf"}};
		service.send("chenxi.wang@ztstech.com","这是测试邮件111", "这是邮件正文","", affix);
	}*/
}
