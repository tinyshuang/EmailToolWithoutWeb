package email.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;




/**
 * 邮件发送工具类
 */
public class MailUtil {

	public static void main(String[] args) throws Exception{
		 
	}
	//发送成功邮件的保存输入器
	static PrintWriter succEmailWriter;
	//发送失败邮件的保存输入器
	static PrintWriter failEmailWriter;
	private static List<String> mailAddList = new ArrayList<String>(); 
	
	
	/**
	     * 根据XML文件，建立JDom的Document对象
	     * @param file 		XML文件
	     * @return Document 返回建立的JDom的Document对象，建立不成功将抛出异常。
	     * @throws IOException 
	     * @throws JDOMException 
	*/
	private static Document getDocument(File file) throws JDOMException, IOException
	    {
	        SAXBuilder builder = new SAXBuilder();
	        Document anotherDocument = builder.build(file);
	        
	        return anotherDocument;
	    }
	
	/**
        *  获取邮件配置属性
        * @return
    	* @throws IOException 
    	* @throws JDOMException 
        */
	public static MailInfo getMailInfo() throws JDOMException, IOException{		
		MailInfo mailInfo = new MailInfo();
		Document xmlDoc = getDocument(new File("src//mail-config.xml"));
		Element root = xmlDoc.getRootElement();
		mailInfo.setHostName(root.getChildText("MailHost"));
		mailInfo.setUsername(root.getChildText("Username"));
		mailInfo.setPassword(root.getChildText("Password"));
		mailInfo.setFrom(root.getChildText("MailFrom"));
		mailInfo.setTitle(root.getChildText("MailTitle"));
		mailInfo.setHtmlPath(root.getChildText("MailHtmlPath"));
		mailInfo.setAddressPath(root.getChildText("MailAddressFile"));
		mailInfo.setAttachmentPath(root.getChildText("MailAttachmentPath"));
		mailInfo.setCharset(root.getChildText("MailCharset"));
		mailInfo.setSendedAddressPath(root.getChildText("MailSendedFile"));		
		mailInfo.setSaveMailSendedSuccFile(root.getChildText("SaveMailSendedSuccFile"));
		mailInfo.setSaveMailSendedFailFile(root.getChildText("SaveMailSendedFailFile"));
		mailInfo.setRecevier(root.getChildText("MailTo"));
		return mailInfo;		
	}

        /**
         * 收件人的数目
         * @return
         */
	public static int getToCounts(){
		return mailAddList.size();
	}
	
	
	/**
	 * 从指定的文件中获取发送邮件列表，该文件每行保存一个邮件地址
	 * @param pathOrFilename 可以是存放邮件的绝对地址，也可以是当前路径的相对路径下的文件名
	 * @return
	 */
	public static List<String> getMailToList(String pathOrFilename){
		FileReader  read = null;
		BufferedReader br = null;
		try{
			if(pathOrFilename.indexOf(":")<0){//根据传入的路径中是否带":"确认传入的是相对路径还是绝对路径，相对路径则做路径补充，绝对路径直接使用
				pathOrFilename=new File("").getAbsolutePath()+File.separator+pathOrFilename;
			}	
			read = new FileReader (pathOrFilename);
			br = new BufferedReader(read);				
			String info = null;
			while((info = br.readLine())!=null) {
				if(!info.trim().equals("")){
					mailAddList.add(info);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try{
				br.close();
				read.close();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return mailAddList;
	}
	/**
	 * 获取已经发送了的邮件并保存于内存中，发送的时候比较是否已经发送过的邮件，发送过的就不要再次发送
	 * @param sendEmailFile		可以是存放邮件的绝对地址，也可以是当前路径的相对路径下的文件名
	 * @throws Exception
	 */
	public static ArrayList<String> getSendedEmail(String sendEmailFile) throws Exception{
		ArrayList<String> sendedEmail = new ArrayList<String>();
		if(sendEmailFile==null || sendEmailFile.trim().equals("")){
			return sendedEmail;
		}
		FileReader  read = null;
		BufferedReader br = null;
		try{
			if(sendEmailFile.indexOf(":")<0){//根据传入的路径中是否带":"确认传入的是相对路径还是绝对路径，相对路径则做路径补充，绝对路径直接使用
				sendEmailFile=new File("").getAbsolutePath()+File.separator+sendEmailFile;
			}
			read = new FileReader (sendEmailFile);
			br = new BufferedReader(read);
			String email = null;
			while((email = br.readLine())!=null) {
				if(!email.trim().equals("")){
					//只加载有效的email地址
					if(!(email.indexOf("@")<=0 || email.endsWith("@") || email.endsWith(".") || email.indexOf(".")<=0)){
						sendedEmail.add(email);
					}					
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			throw new Exception(ex);
		}finally{
			try{
				br.close();
				read.close();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return sendedEmail;
	}
	/**
	 * 保存发送成功的邮件到指定的文件中
	 * @param email					当前保存的电子邮件
	 * @param saveSuccEmailFile		输出文件
	 */
	public static void saveSuccEmail(String email,String saveSuccEmailFile){
		if(succEmailWriter==null){
			initSaveSuccEmail(saveSuccEmailFile);
		}		
		succEmailWriter.write(email);
		succEmailWriter.println();
		succEmailWriter.flush();
	}
	/**
	 * 初使化
	 * @param saveSuccEmailFile
	 */
	private static void initSaveSuccEmail(String saveSuccEmailFile){
		String emailFile = "";
		if(emailFile.indexOf(":")<0){//根据传入的路径中是否带":"确认传入的是相对路径还是绝对路径，相对路径则做路径补充，绝对路径直接使用
			emailFile=new File("").getAbsolutePath()+File.separator+saveSuccEmailFile;
		}	
        if(succEmailWriter==null){
	        try {
	            succEmailWriter = new PrintWriter(new FileWriter(emailFile, true), true);
	        }
	        catch (IOException e) {
	        	System.err.println("初使化EMAIL保存文件错误！");
	            succEmailWriter = new PrintWriter(System.err);
	        }
		}
	}
	/**
	 * 保存发送失败的邮件到指定的文件中
	 * @param email					当前保存的电子邮件
	 * @param saveFailEmailFile		输出文件
	 */
	public static void saveFailEmail(String email,String saveFailEmailFile){
		if(failEmailWriter==null){
			initFailSuccEmail(saveFailEmailFile);
		}		
		failEmailWriter.write(email);
		failEmailWriter.println();
		failEmailWriter.flush();
	}
	/**
	 * 初使化
	 * @param saveSuccEmailFile
	 */
	private static void initFailSuccEmail(String saveFailEmailFile){
		String emailFile = "";
		if(emailFile.indexOf(":")<0){//根据传入的路径中是否带":"确认传入的是相对路径还是绝对路径，相对路径则做路径补充，绝对路径直接使用
			emailFile=new File("").getAbsolutePath()+File.separator+saveFailEmailFile;
		}	
        if(failEmailWriter==null){
	        try {
	        	failEmailWriter = new PrintWriter(new FileWriter(emailFile, true), true);
	        }
	        catch (IOException e) {
	        	System.err.println("初使化EMAIL保存文件错误！");
	        	failEmailWriter = new PrintWriter(System.err);
	        }
		}
	}
	/**
	 * 将不发送邮件的列表放到List中
	 * @param notSendEmailType
	 * @return
	 */
	public static List<String> getDonNotSendEmailType(String notSendEmailType){
		List<String> list = new ArrayList<String>();
		if(!(notSendEmailType==null || notSendEmailType.trim().equals(""))){
			String[] arrs = notSendEmailType.split("\\|");
			for(int i=0;i<arrs.length;i++){
				list.add(arrs[i]);
			}
		}
		return list;
	}
	/**
	 * 根据电子邮件获取邮件服务器后缀，如传入"56553655@163.com"，得到结果"163.com"
	 * @param email
	 * @return
	 */
	public static String getEmailHost(String email){
		String host="";
		host = email.substring(email.indexOf("@")+1);
		return host;
	}
}
