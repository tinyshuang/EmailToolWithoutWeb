# EmailToolWithoutWeb
一个普通的发送邮件的小工具

###注意点 :  

* email.txt : 收件者的邮件list 

* 关于正文放置图片 : 
 
      html页面 :   
      
          <  img width="95" height="93" src="cid:a">  使用cid读取  
          
      java代码 : 
      
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
                
                //设置正文与图片之间的关系 
                
                mp.setSubType("related");
