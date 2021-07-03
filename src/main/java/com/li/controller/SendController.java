package com.li.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author liql
 * @date 2021/7/2
 */
@Controller
public class SendController {


    @Controller
    public class SampleMail {
        private static final String ALIDM_SMTP_HOST = "smtp.qq.com";
        private static final String ALIDM_SMTP_PORT = "25";//或"80"


        @ResponseBody
        @RequestMapping("/test/1")
        public String test() {
            send();
            return "成功";
        }


        /**
         * 这个发附件+text时 不能发html    ;发html+附件时  又需要设置setText 属性，但是text属性又不会在邮件中显示
         */
        public void send() {
            // 配置发送邮件的环境属性
            final Properties props = new Properties();
            // 表示SMTP发送邮件，需要进行身份验证
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.host", ALIDM_SMTP_HOST);
            props.put("mail.smtp.port", ALIDM_SMTP_PORT);
            // 如果使用ssl，则去掉使用25端口的配置，进行如下配置,
            // props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            // props.put("mail.smtp.socketFactory.port", "465");
            // props.put("mail.smtp.port", "465");
            // 发件人的账号，填写控制台配置的发信地址,比如xxx@xxx.com
            props.put("mail.user", "574550413@qq.com");
            // 访问SMTP服务时需要提供的密码(在控制台选择发信地址进行设置)
            props.put("mail.password", "ffzuvbzjqgvjbege");

            // 构建授权信息，用于进行SMTP进行身份验证
            Authenticator authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    // 用户名、密码
                    String userName = props.getProperty("mail.user");
                    String password = props.getProperty("mail.password");
                    return new PasswordAuthentication(userName, password);
                }
            };
            // 使用环境属性和授权信息，创建邮件会话
            Session mailSession = Session.getInstance(props, authenticator);
             mailSession.setDebug(true);
            //UUID uuid = UUID.randomUUID();
            //final String messageIDValue = "<" + uuid.toString() + ">";
            // 创建邮件消息
            MimeMessage message = new MimeMessage(mailSession) {
                @Override
                protected void updateMessageID() throws MessagingException {
               // 设置自定义Message-ID值
                setHeader("Message-ID", "第一种测试邮件");
                }
            };
            try {
                // 设置发件人邮件地址和名称。填写控制台配置的发信地址,比如xxx@xxx.com。和上面的mail.user保持一致。名称用户可以自定义填写。
                InternetAddress from = new InternetAddress("574550413@qq.com", "发件人名称（用户自定义填写）");
                message.setFrom(from);
                //可选。设置回信地址
                /* Address[] a = new Address[1];
                 a[0] = new InternetAddress("***");
                 message.setReplyTo(a);*/
                // 设置收件人邮件地址，比如yyy@yyy.com
                InternetAddress to = new InternetAddress("13225542821@163.com");
                message.setRecipient(MimeMessage.RecipientType.TO, to);
                //如果同时发给多人，才将上面两行替换为如下（因为部分收信系统的一些限制，尽量每次投递给一个人；同时我们限制单次允许发送的人数是60人）：
                //InternetAddress[] adds = new InternetAddress[2];
                //adds[0] = new InternetAddress("xxxxx@qq.com");
                //adds[1] = new InternetAddress("xxxxx@qq.com");
                //message.setRecipients(Message.RecipientType.TO, adds);
                String ccUser = "抄送邮箱";
                // 设置多个抄送地址
                /* if(null != ccUser && !ccUser.isEmpty()){
                     @SuppressWarnings("static-access")
                     InternetAddress[] internetAddressCC = new InternetAddress().parse(ccUser);
                     message.setRecipients(Message.RecipientType.CC, internetAddressCC);
                     }*/
                String bccUser = "密送邮箱";
                // 设置多个密送地址
                /* if(null != bccUser && !bccUser.isEmpty()){
                     @SuppressWarnings("static-access")
                     InternetAddress[] internetAddressBCC = new InternetAddress().parse(bccUser);
                     message.setRecipients(Message.RecipientType.BCC, internetAddressBCC);
                     }*/
                // 设置邮件标题
                message.setSubject("测试邮件主体");
                // 设置邮件的内容体
                //  message.setContent("测试的HTML邮件", "text/html;charset=UTF-8");//想要发送附件 需要把这个注释掉
                //若需要开启邮件跟踪服务，请使用以下代码设置跟踪链接头。前置条件和约束见文档"如何开启数据跟踪功能？"
                //String tagName = "Test";
                //HashMap<String, String> trace = new HashMap<>();
                //这里为字符串"1"
                //trace.put("OpenTrace", "1");
                //trace.put("TagName", tagName);
                //String jsonTrace = JSON.toJSONString(trace);
                //String base64Trace = new String(Base64.encodeBase64(jsonTrace.getBytes()));
                //设置跟踪链接头
                //message.addHeader("X-AliDM-Trace", base64Trace);
                // 发送附件，总的邮件大小不超过15M，创建消息部分
                BodyPart messageBodyPart = new MimeBodyPart();
                // 消息 如果把线面的 setTEXT去掉  则邮件无法正常发送/被覆盖？
               messageBodyPart.setText("消息Text \n <p style=\"background-color:yellow; height:50px;width:200px\" >this is html</p>");

               // 暂时未找到 html 与附件共同发送的方法
                //BodyPart messageBodyPart2 = new MimeBodyPart();
                //messageBodyPart2.setContent("测试的HTML邮件 \n  <p style=\"background-color:yellow; height:50px;width:200px\" >this is html</p>", "text/html;charset=UTF-8");//想要发送附件 需要把这个注释掉
                // 创建多重消息
                Multipart multipart = new MimeMultipart();
                // 设置文本消息部分
                multipart.addBodyPart(messageBodyPart);
               // multipart.addBodyPart(messageBodyPart2);

//                // 附件部分 未测试 感觉好像有问题
//                messageBodyPart = new MimeBodyPart();
//                //设置要发送附件的文件路径
//                String filename = "C:\\Users\\HIAPAD\\Desktop\\李秋亮的简历.doc";
//                FileDataSource source = new FileDataSource(filename);
//                messageBodyPart.setDataHandler(new DataHandler(source));
//                //处理附件名称中文（附带文件路径）乱码问题
//                messageBodyPart.setFileName(MimeUtility.encodeText(filename));
//                messageBodyPart.addHeader("Content-Transfer-Encoding", "base64");
//                multipart.addBodyPart(messageBodyPart);
//                // 发送含有附件的完整消息
//               message.setContent(multipart);

                //可实现的附件部分
                String path = "C:\\Users\\HIAPAD\\Desktop\\李秋亮的简历.doc";
                MimeBodyPart bodyPart = new MimeBodyPart();
                DataSource dh = new FileDataSource(path);
                bodyPart.setDataHandler(new DataHandler(dh));
                bodyPart.setFileName(dh.getName());
                multipart.addBodyPart(bodyPart);
                message.setContent(multipart);
                // 发送附件代码，结束
                // 发送邮件
                Transport.send(message);
            } catch (MessagingException | UnsupportedEncodingException e) {
                String err = e.getMessage();
                // 在这里处理message内容， 格式是固定的
                System.out.println(err);
            }
        }
    }




    @ResponseBody
    @RequestMapping("/test/2")
    public String test() throws Exception {
        ArrayList<String> filepath = new ArrayList<>();
        filepath.add("C:\\Users\\HIAPAD\\Desktop\\李秋亮的简历.doc");
        sendEmail("13225542821@163.com",null,"主体是啥","邮件正文+\n <p style=\"background-color:yellow; height:50px;width:200px\" >this is html</p>",filepath);
        return "成功";
    }


    // 发件人的 邮箱 和 密码（替换为自己的邮箱和密码）
    // PS: 某些邮箱服务器为了增加邮箱本身密码的安全性，给 SMTP 客户端设置了独立密码（有的邮箱称为“授权码”）,
    //     对于开启了独立密码的邮箱, 这里的邮箱密码必需使用这个独立密码（授权码）。
    public static String myEmailAccount = "574550413@qq.com";
    public static String myEmailPassword = "ffzuvbzjqgvjbege";
    // 发件人邮箱的 SMTP 服务器地址, 必须准确, 不同邮件服务器地址不同, 一般(只是一般, 绝非绝对)格式为: smtp.xxx.com
    // 网易163邮箱的 SMTP 服务器地址为: smtp.163.com
    public static String myEmailSMTPHost = "smtp.qq.com";

    /**
     * 这个可以发附件 网页
     * @param otherCount
     * @param chaopSongCount
     * @param subject
     * @param body
     * @param filepath
     * @return
     * @throws Exception
     */
    public  Message sendEmail(String otherCount, String chaopSongCount,
                              String subject, String body, List<String> filepath) throws Exception {
        if (subject == null) {
            subject = "无标题邮件";
        }

        if (body == null) {
            body = "";
        }

        // 加载配置信息
        Properties props = new Properties();                    // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", myEmailSMTPHost);   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");
            /*MailHandler md = new MailHandler();
            Properties prop = md.conf;
            String hostName = prop.getProperty("mail.host");
            String username = prop.getProperty("mail.username");
            String password = prop.getProperty("mail.password");
            String from = "service@pjsfax.com"; // 发件人信息*/

        //   System.out.println("配置信息：" + hostName + "/" + username + "/" + password);

        Session session = Session.getInstance(props);
        session.setDebug(true);

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(myEmailAccount));
        message.setHeader("邮件标题", "Good noon");

        MimeMultipart multipart = new MimeMultipart();
        // 邮件正文
        MimeBodyPart text = new MimeBodyPart();
        text.setContent(body, "text/html;charset=UTF-8");
        multipart.addBodyPart(text);

        // 多个收件人地址
        InternetAddress[] addressesTo = null;
        if (otherCount != null && otherCount.trim().length() > 0) {
            String[] receiveList = otherCount.split(",");
            int receiveCount = receiveList.length;
            if (receiveCount > 0) {
                addressesTo = new InternetAddress[receiveCount];
                for (int i = 0; i < receiveCount; i++) {
                    addressesTo[i] = new InternetAddress(receiveList[i]);
                }
            }
        } else {
            System.out.println("None receiver!");
            // return false;
        }

        // 多个抄送人地址
        InternetAddress[] addressesCc = null;
        if (chaopSongCount != null && chaopSongCount.trim().length() > 0) {
            String[] copyList = chaopSongCount.split(",");
            int copyCount = copyList.length;
            if (copyCount > 0) {
                addressesCc = new InternetAddress[copyCount];
                for (int i = 0; i < copyCount; i++) {
                    addressesCc[i] = new InternetAddress(copyList[i]);
                }
            }
        }

        //多个附件
        if (filepath != null && filepath.size() > 0) {
            for (String path : filepath) {
                MimeBodyPart bodyPart = new MimeBodyPart();
                DataSource dh = new FileDataSource(path);
                bodyPart.setDataHandler(new DataHandler(dh));
                bodyPart.setFileName(dh.getName());
                multipart.addBodyPart(bodyPart);
            }

        }

        message.setContent(multipart);
        message.setRecipients(MimeMessage.RecipientType.TO, addressesTo);
        message.setRecipients(MimeMessage.RecipientType.CC, addressesCc);
        message.setSubject(subject);
        message.setSentDate(new Date());
        message.saveChanges();

        Transport transport = session.getTransport();
        transport.connect(myEmailSMTPHost, myEmailAccount, myEmailPassword);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
        return message;
    }


}
