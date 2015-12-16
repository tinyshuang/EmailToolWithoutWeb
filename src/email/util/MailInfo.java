package email.util;

/**
 * 
 * @author Administrator
 * @description 封装邮件发送对象类
 *2015-12-16  下午10:30:53
 */
public class MailInfo {
    	//邮件登录账号
	private String username = "";
	//邮件登录密码
	private String password = "";
	//邮件服务器地址
	private String hostName = "";
	//发件人邮箱地址
	private String from = "";
	//邮件标题
	private String title = "";
	//要发送Html文本路径
	private String htmlPath = "";
	//包含电子邮件的文本文件，每行一个电子邮件
	private String addressPath=null;	
	//已经发送了的电子邮件所在的文件
	private String sendedAddressPath=null;
	//附件所在路径
	private String attachmentPath=null;
	//发送邮件使用的编码
	private String charset=null;
	//保存发送成功邮件的文件，可以配置为绝对路径，也可以配置为相对路径，不配置不保存
	private String saveMailSendedSuccFile=null;
	//包含已经发送了的电子邮件的地址.可以配置为绝对路径，也可以配置为相对路径，不配置不保存
	private String saveMailSendedFailFile=null;

	private String recevier;

	public MailInfo(){};
	public MailInfo(String username, String password, String hostName,
			String from, String title) {
		super();
		this.username = username;
		this.password = password;
		this.hostName = hostName;
		this.from = from;
		this.title = title;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHtmlPath() {
		return htmlPath;
	}


	public void setHtmlPath(String htmlPath) {
		this.htmlPath = htmlPath;
	}


	public String getAddressPath() {
		return addressPath;
	}


	public void setAddressPath(String addressPath) {
		this.addressPath = addressPath;
	}


	public String getAttachmentPath() {
		return attachmentPath;
	}


	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}


	public String getCharset() {
		return charset;
	}


	public void setCharset(String charset) {
		this.charset = charset;
	}


	public String getSendedAddressPath() {
		return sendedAddressPath;
	}


	public void setSendedAddressPath(String sendedAddressPath) {
		this.sendedAddressPath = sendedAddressPath;
	}




	public String getSaveMailSendedFailFile() {
		return saveMailSendedFailFile;
	}


	public void setSaveMailSendedFailFile(String saveMailSendedFailFile) {
		this.saveMailSendedFailFile = saveMailSendedFailFile;
	}


	public String getSaveMailSendedSuccFile() {
		return saveMailSendedSuccFile;
	}


	public void setSaveMailSendedSuccFile(String saveMailSendedSuccFile) {
		this.saveMailSendedSuccFile = saveMailSendedSuccFile;
	}

	public String getRecevier() {
	    return recevier;
	}

	public void setRecevier(String recevier) {
	    this.recevier = recevier;
	}


}
