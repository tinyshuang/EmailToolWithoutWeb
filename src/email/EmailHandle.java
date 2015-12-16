package email;

import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import email.util.MailInfo;

/**
 * 
 * @author Administrator 本程序用java来实现Email的发送， 所用到的协议为：smtp
 * @description
 *2015-12-16  下午10:22:44
 */
public class EmailHandle {

    private MimeMessage mimeMsg; //邮件对象

    private Session session; //发送邮件的Session会话

    private Properties props;//邮件发送时的一些配置信息的一个属性对象

    private String sendUserName; //发件人的用户名

    private String sendUserPass; //发件人密码

    private MimeMultipart mp;//附件添加的组件


    public EmailHandle(String smtp) {
            sendUserName = "";
            sendUserPass = "";
        setSmtpHost(smtp);// 设置邮件服务器
        createMimeMessage(); // 创建邮件
    }

    public void setSmtpHost(String hostName) {
        if (props == null)
            props = System.getProperties();
        props.put("mail.smtp.host", hostName);
    }

    public boolean createMimeMessage(){
        try {
            // 用props对象来创建并初始化session对象
            session = Session.getDefaultInstance(props, null);
        } catch (Exception e) {
            System.err.println("获取邮件会话对象时发生错误！" + e);
            return false;
        }
        try {
            mimeMsg = new MimeMessage(session);  // 用session对象来创建并初始化邮件对象
            mp = new MimeMultipart();// 生成附件组件的实例
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    /**
         * 设置SMTP的身份认证
         */
    public void setNeedAuth(boolean need) {
        if (props == null)
            props = System.getProperties();
        if (need)
            props.put("mail.smtp.auth", "true");
        else
            props.put("mail.smtp.auth", "false");
    }

    /**
         * 进行用户身份验证时，设置用户名和密码
         */
    public void setNamePass(String name, String pass) {
        sendUserName = name;
        sendUserPass = pass;
    }
    /**
         * 设置邮件主题
         * @param mailSubject
         * @return
         */
    public boolean setSubject(String mailSubject) {
        try {
            mimeMsg.setSubject(mailSubject);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    /**
         * 设置邮件内容,并设置其为文本格式或HTML文件格式，编码方式为UTF-8
         * @param mailBody
         * @return
         */
    public boolean setBody(String mailBody,MailInfo info) {
        try {
            MimeBodyPart bp = new MimeBodyPart();
            bp.setContent("<meta http-equiv=Content-Type content=text/html; charset=UTF-8>"+mailBody, "text/html;charset=UTF-8");
           
            //正文圖片
            MimeBodyPart img = new MimeBodyPart();
            DataHandler dh = new DataHandler(new FileDataSource("img//1.jpg"));
            img.setDataHandler(dh);
            //创建图片的一个表示用于显示在邮件中显示
            img.setContentID("a");
            MimeBodyPart img2 = new MimeBodyPart();
            DataHandler dh2 = new DataHandler(new FileDataSource("img//2.jpg"));
            img2.setDataHandler(dh2);
            //创建图片的一个表示用于显示在邮件中显示
            img2.setContentID("b");
            
            mp.addBodyPart(bp);
            mp.addBodyPart(img);
            mp.addBodyPart(img2);
            mp.setSubType("related");//设置正文与图片之间的关系
            
        } catch (Exception e) {
            System.err.println("设置邮件正文时发生错误！" + e);
            return false;
        }
        return true;
    }
    /**
         * 增加发送附件
         * @param filename
         * 邮件附件的地址，只能是本机地址而不能是网络地址，否则抛出异常
         * @return
         */
    public boolean addFileAffix(String filename) {
        try {
            BodyPart bp = new MimeBodyPart();
            FileDataSource fileds = new FileDataSource(filename);
            bp.setDataHandler(new DataHandler(fileds));
            bp.setFileName(MimeUtility.encodeText(fileds.getName(),"utf-8",null));  // 解决附件名称乱码
            mp.addBodyPart(bp);// 添加附件
        } catch (Exception e) {
            System.err.println("增加邮件附件：" + filename + "发生错误！" + e);
           return false;
        }
        return true;
   }
    /**
         * 设置发件人地址
         * @param from
         * 发件人地址
         * @return
         */
    public boolean setFrom(String from) {
        try {
            mimeMsg.setFrom(new InternetAddress(from));
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    /**
         * 设置收件人地址
         * @param to收件人的地址
         * @return
         */
    public boolean setTo(String to) {
        if (to == null)
            return false;
        try {
            mimeMsg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    /**
         * cc to other
         * @param copyto
         * @return
         */
    public boolean setCopyTo(String copyto) {
        if (copyto == null)
            return false;
        try {
            mimeMsg.setRecipients(Message.RecipientType.BCC, (Address[]) InternetAddress.parse(copyto));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * cc to others
     * @param copyto
     * @return
     */
    public boolean setCopyTo(List<String> copyto) {
	if (copyto == null)
            return false;
	
        try {
            for (String string : copyto) {
        	mimeMsg.addRecipients(Message.RecipientType.CC, (Address[]) InternetAddress.parse(string));
	    }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /**
         * 发送邮件
         * @return
         */
    public boolean sendEmail() throws Exception{
            mimeMsg.setContent(mp);
            mimeMsg.saveChanges();
            String sending = new String("正在发送邮件....".getBytes(),"utf-8");
            String sendout = new String("发送邮件成功！....".getBytes(),"utf-8");
            
            System.out.println(sending);
            Session mailSession = Session.getInstance(props, null);
            Transport transport = mailSession.getTransport("smtp");
            // 连接邮件服务器并进行身份验证
            transport.connect((String) props.get("mail.smtp.host"), sendUserName,sendUserPass);
            // 发送邮件
            transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
            transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.CC)); 
            System.out.println(sendout);
            transport.close();
        return true;
    }
}