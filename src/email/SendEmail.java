package email;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.JDOMException;

import email.util.MailInfo;
import email.util.MailUtil;



/**
 * 
 * @description 发送邮件的工具类
 *2015-12-16  下午10:06:01
 */

public class SendEmail {
    private static MailInfo mailInfo =null; 
    private static List<String> mailAddList = new ArrayList<String>(); 
    
    	static{
    	   try {
    	       //读取邮箱的基本信息,比如用户名和密码等
	   	mailInfo = MailUtil.getMailInfo();
	   	//读取收取方的邮件集合
	   	mailAddList = MailUtil.getMailToList(mailInfo.getAddressPath());
	   } catch (JDOMException e) {
	   	e.printStackTrace();
	   } catch (IOException e) {
	   	e.printStackTrace();
	   }
    	}
    	
    	
    	public static void main(String[] args) {
            try {
            	SendEmail.sendMail(mailAddList,setContent());
            } catch (Exception e) {
                    e.printStackTrace();
            }
    	}
    	
    
        /***以后需要两个参数：接收方地址 、 内容***/
        public static void send(List<String> list,String content)throws Exception {
                EmailHandle emailHandle = new EmailHandle(mailInfo.getHostName());
                emailHandle.setFrom(mailInfo.getUsername());
                emailHandle.setNeedAuth(true);
                emailHandle.setSubject(mailInfo.getTitle());
                emailHandle.setBody(content,mailInfo);
                emailHandle.setTo(mailInfo.getRecevier());
                emailHandle.setCopyTo(list);
                emailHandle.addFileAffix(mailInfo.getAttachmentPath());// 附件文件路径
                emailHandle.setNamePass(mailInfo.getUsername(), mailInfo.getPassword());
                emailHandle.sendEmail();
        }
        
        
        /**
         * @description	群发送邮件
         * @param list 邮件收取方的集合
         * @param content 邮件内容
         * @throws Exception
         *2015-12-16  下午10:18:30
         *返回类型:void
         */
        public static void sendMail(List<String> list,String content) throws Exception{
            //由于免费邮箱的限制..抄送者这里限量为40个
            final int group = 40;
            int times = list.size()/group+1;
            for (int i = 0; i < times; i++) {
        	int start = i*group;
        	int end = group*(i+1);
        	if (i==times-1) 
        	     end = list.size();
        	//防止刚好是40的倍数
        	if (start!=end) {
        	    System.out.println("from "+ (start+1) + " to " + end + " people are ready to sent");
        	    send(list.subList(start,end), content);
		}
	    }
            
        }
        
        
        /**
         * @description 获取要发送的内容	
         * @return
         *2015-12-16  下午10:17:54
         *返回类型:String
         */
        private static String setContent(){
            String htmlPath = mailInfo.getHtmlPath();
            StringBuilder builder = new StringBuilder();
            try {
        	String string = null;
		BufferedReader reader = new BufferedReader(new FileReader(htmlPath));
		while ((string =reader.readLine())!=null) {
		    builder.append(string);
		}
		reader.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
            return builder.toString();
            
        }
}