package com.happy.gene.thirdparty.email;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

/**
 * Created by zhaolisong on 02/11/2017.
 */
public final class EmailUtil {

    private String  charset        = "utf-8";
    private String  senderNickname = "北京幸福基石科技有限公司";
    private String  senderAccount  = "zls@happy-gene.com";
    private String  senderPassword = "Zhls1984hebei";
    private String  senderSMTPHost = "smtp.exmail.qq.com";
    private int     senderSMTPPort = 465;
    private boolean senderUseSSL   = false;
    private String  receiverAccount= "";
    private Session session        = null;

    public static final EmailUtil newInstance() { return new EmailUtil(); }
    private EmailUtil() {}

    public EmailUtil setProperty(String charset,
                                 String senderNickname, String senderAccount, String senderPassword,
                                 String senderSMTPHost, Integer senderSMTPPort,
                                 Boolean senderUseSSL
    ) {
        if (null!=charset && !charset.equals(this.charset))                     { this.charset = charset;               }
        if (null!=senderNickname && !senderNickname.equals(this.senderNickname)){ this.senderNickname = senderNickname; }
        if (null!=senderAccount && !senderAccount.equals(this.senderAccount))   { this.senderAccount  = senderAccount;  }
        if (null!=senderPassword && !senderPassword.equals(this.senderPassword)){ this.senderPassword = senderPassword; }
        setSenderSMTP(senderSMTPHost, senderSMTPPort, senderUseSSL);
        return this;
    }
    public EmailUtil setSenderNickname(String senderNickname)  { this.senderNickname = senderNickname;   return this; }
    public EmailUtil setSenderAccount(String senderAccount)    { this.senderAccount  = senderAccount;    return this; }
    public EmailUtil setSenderPassword(String senderPassword)  { this.senderPassword = senderPassword;   return this; }
    public EmailUtil setCharset(String charset)                { this.charset = charset;                 return this; }
    public EmailUtil setReceiverAccount(String receiverAccount){ this.receiverAccount = receiverAccount; return this; }
    public EmailUtil setSenderSMTP(String senderSMTPHost, Integer senderSMTPPort, Boolean senderUseSSL)  {
        boolean changed = false;
        if (null!=senderSMTPHost && !senderSMTPHost.equals(this.senderSMTPHost)){ this.senderSMTPHost = senderSMTPHost;   changed = true; }
        if (null!=senderSMTPPort && !senderSMTPPort.equals(this.senderSMTPPort)){ this.senderSMTPPort = senderSMTPPort;   changed = true; }
        if (null!=senderUseSSL && !senderUseSSL.equals(this.senderUseSSL))      { this.senderUseSSL   = senderUseSSL;     changed = true; }
        if (changed) { newSession(); }
        return this;
    }

    public Session newSession() {
        // 1. 创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = new Properties();                    // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", senderSMTPHost);    // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 需要请求认证

        // PS: 某些邮箱服务器要求 SMTP 连接需要使用 SSL 安全认证
        //     (为了提高安全性, 邮箱支持SSL连接, 也可以自己开启),
        //     如果无法连接邮件服务器, 仔细查看控制台打印的 log,
        //     如果有有类似 “连接失败, 要求 SSL 安全连接” 等错误,
        //     打开下面 /* ... */ 之间的注释代码, 开启 SSL 安全连接。
        // SMTP 服务器的端口:
        //     (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果开启了 SSL 连接,
        //      需要改为对应邮箱的 SMTP 服务器的端口, 具体可查看对应邮箱服务的帮助,
        //      QQ邮箱的SMTP(SLL)端口为465或587, 其他邮箱自行去查看)
        //final String smtpPort = "465";
        if (senderUseSSL) {
            props.setProperty("mail.smtp.port", ""+senderSMTPPort);
            props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.socketFactory.port", ""+senderSMTPPort);
        }

        // 2. 根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getDefaultInstance(props);
        // 设置为debug模式, 可以查看详细的发送 log
        session.setDebug(true);


        this.session = session;
        return session;
    }

    public MimeMessage createMessage(String emlPath, String subject, String content, String contentType) throws Exception {
        // 1. 创建一封邮件
        MimeMessage message = null;
        if (null!=emlPath && (new File(emlPath)).exists()) {
            // 可以根据已有的eml邮件文件创建 MimeMessage 对象
            message = new MimeMessage(session, new FileInputStream(emlPath));
        }
        else {
            // 创建邮件对象
            message = new MimeMessage(session);
        }

        // 2. From: 发件人
        //    其中 InternetAddress 的三个参数分别为: 邮箱---显示的昵称(只用于显示, 没有特别的要求)---昵称的字符集编码
        //    真正要发送时, 邮箱必须是真实有效的邮箱。
        message.setFrom(new InternetAddress(senderAccount, MimeUtility.encodeText(senderNickname), charset));
        // 3. To: 收件人
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiverAccount, null, charset));
        //    To: 增加收件人（可选）
        //message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress("zls@cool-too.com", "zls2", charset));
        //    Cc: 抄送（可选）
        //message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress("zls@happy-gene.com", "chao-song", charset));
        //    Bcc: 密送（可选）
        //message.setRecipient(MimeMessage.RecipientType.BCC, new InternetAddress("zls@happy-gene.com", "mi-chao", charset));

        // 4. Subject: 邮件主题
        message.setSubject(subject, charset);

        // 5. Content: 邮件正文（可以使用html标签）
        if (null==contentType || "".equals(contentType.trim())) {
            message.setText(content, charset);
        }
        else {
            message.setContent(content, contentType);
        }

        // 6. 设置显示的发件时间
        message.setSentDate(new Date());

        // 7. 保存前面的设置
        message.saveChanges();

        // 8. 将该邮件保存到本地
        // OutputStream out = new FileOutputStream("MyEmail.eml");
        // message.writeTo(out);
        // out.flush();
        // out.close();

        return message;
    }

    public void transportMessage(MimeMessage message) throws MessagingException {
        // 1. 根据 Session 获取邮件传输对象
        Transport transport = session.getTransport();

        // 2. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
        //    PS_01: 成败的判断关键在此一句, 如果连接服务器失败, 都会在控制台输出相应失败原因的 log,
        //           仔细查看失败原因, 有些邮箱服务器会返回错误码或查看错误类型的链接, 根据给出的错误
        //           类型到对应邮件服务器的帮助网站上查看具体失败原因。
        //    PS_02: 连接失败的原因通常为以下几点, 仔细检查代码:
        //           (1) 邮箱没有开启 SMTP 服务;
        //           (2) 邮箱密码错误, 例如某些邮箱开启了独立密码;
        //           (3) 邮箱服务器要求必须要使用 SSL 安全连接;
        //           (4) 请求过于频繁或其他原因, 被邮件服务器拒绝服务;
        //           (5) 如果以上几点都确定无误, 到邮件服务器网站查找帮助。
        //    PS_03: 仔细看log, 认真看log, 看懂log, 错误原因都在log已说明。
        transport.connect(senderAccount, senderPassword);

        // 3. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(message, message.getAllRecipients());

        // 4. 关闭连接
        transport.close();
    }

    public static void main(String[] args) throws Exception {
//        EmailUtil emailUtil = EmailUtil.newInstance();
//        emailUtil.setProperty("UTF-8",
//                              "幸福基石科技有限公司", "zls@happy-gene.com", "Zhls1984hebei",
//                              "smtp.exmail.qq.com", 465,
//                              true
//        ).setReceiverAccount("lisongzhao0@163.com");
//
//        Session session = emailUtil.newSession();
//        MimeMessage msg = emailUtil.createMessage(null, "测试",
//                "<table style='border-collapse: collapse; border-spacing: 0; font-size: 16px;" +
//                        " line-height: 1.5;  margin: 0 auto; max-width: 680px; min-width: 300px;" +
//                        " width: 100%'> <tbody><tr><td style='-moz-hyphens: auto; -webkit-hyphens: auto;" +
//                        " border-collapse: collapse !important; color: #222; font-family: 'Helvetica'," +
//                        " 'Arial', sans-serif; font-size: 16px; hyphens: auto; margin: 0; padding: 0;" +
//                        " word-break: break-word'> <table width='100%' style='border-collapse: collapse;" +
//                        " border-spacing: 0'> <tbody><tr> <td style='-moz-hyphens: auto; -webkit-hyphens: auto;" +
//                        " border-collapse: collapse !important; color: #222; font-family: 'Helvetica', 'Arial'," +
//                        " sans-serif; font-size: 16px; hyphens: auto; margin: 0; padding: 0; word-break: break-word'>" +
//                        " <div style='background: #fff; margin-bottom: 5%'> <img src='http://towerfiles.oss.aliyuncs.com/mail/mail-towerlogo.png'" +
//                        " style='-ms-interpolation-mode: bicubic; clear: both; display: block; float: none; height: 60px;" +
//                        " margin: 5% auto 2%; max-width: 100%; outline: none; text-decoration: none; width: 60px' align='none'>" +
//                        " <hr style='background: #e8e8e8; border: none; color: #ddd; height: 1px; margin-bottom: 5%'>" +
//                        " <p style='color: #222; font-family: 'Helvetica', 'Arial', sans-serif; font-size: 16px; margin: 5% 3%; padding: 0'>" +
//                        " 我很想继续聊下去，不过今天就先聊到这里吧。明天，我将向你介绍如何用 Tower 来推动项目，把工作变得井井有条。如果你仍然有一些疑问，可以在" +
//                        " <a href='https://tower.im/help' style='color: #2ba6cb; display: inline-block; text-decoration: none' target='_blank'>" +
//                        "帮助中心</a> 获得需要的解答或向我们提问 :) </p> <p style='color: #222; font-family: 'Helvetica'," +
//                        " 'Arial', sans-serif; font-size: 16px; margin: 5% 3%; padding: 0; text-align: right' align='right'>" +
//                        " <b style='font-size: 22px'>沈学良</b><br> <span style='color: #999; font-size: 14px;" +
//                        " text-align: center'>彩程设计 CEO</span> </p> </td> </tr> </tbody></table> <hr" +
//                        " style='background: #e8e8e8; border: none; color: #ddd; height: 1px; margin: 5% 0'> <table width='100%'" +
//                        " style='border-collapse: collapse; border-spacing: 0'> <tbody><tr> <td colspan='2' align='center'" +
//                        " style='-moz-hyphens: auto; -webkit-hyphens: auto; border-collapse: collapse !important; color: #222;" +
//                        " font-family: 'Helvetica', 'Arial', sans-serif; font-size: 16px; hyphens: auto; margin: 0; padding: 0;" +
//                        " word-break: break-word'> <p style='color: #999999; font-family: 'Helvetica', 'Arial', sans-serif;" +
//                        " font-size: 12px; margin: 0 0 10px; padding: 0'> 如有疑问请联系我们 ：<a href='mailto:tower@mycolorway.com'" +
//                        " style='color: #999999; text-decoration: underline' target='_blank'>tower@mycolorway.com</a> 或进入" +
//                        "<a href='https://tower.im/help' target='_blank' style='color: #999999; text-decoration: underline'>" +
//                        "帮助中心</a></p> </td> </tr> </tbody></table> </td> </tr> </tbody></table>", "text/html;charset=UTF-8");
//
//        emailUtil.transportMessage(msg);
        List<Integer> integer = new ArrayList<>();
        integer.add(3);
        integer.add(8);
        integer.add(6);
        integer.add(null);
        integer.add(4);
        integer.add(9);
        integer.add(2);
        integer.add(null);
        integer.add(11);
        Collections.sort(integer, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (null==o1 && null==o2) { return 0; }
                if (null!=o1 && null==o2) { return -1; }
                if (null==o1 && null!=o2) { return 1; }
                long detal = o1 - o2;

                return 0==detal ? 0 : (detal > 0 ? -1 : 1);
            }
        });
        System.out.println(integer);
    }
}
