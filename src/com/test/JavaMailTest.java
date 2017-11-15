package com.test;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * Created by zhaolisong on 29/09/2017.
 */
public class JavaMailTest {
    // 发件人的 邮箱 和 密码（替换为自己的邮箱和密码）
    // PS: 某些邮箱服务器为了增加邮箱本身密码的安全性，给 SMTP 客户端设置了独立密码（有的邮箱称为“授权码”）,
    //     对于开启了独立密码的邮箱, 这里的邮箱密码必需使用这个独立密码（授权码）。
    public static String myEmailAccount = "lisongzhao0@163.com";
    public static String myEmailPassword = "$snp#2017>happy:";

    // 发件人邮箱的 SMTP 服务器地址, 必须准确, 不同邮件服务器地址不同, 一般(只是一般, 绝非绝对)格式为: smtp.xxx.com
    // 网易163邮箱的 SMTP 服务器地址为: smtp.163.com
    public static String myEmailSMTPHost = "smtp.163.com";

    // 收件人邮箱（替换为自己知道的有效邮箱）
    public static String receiveMailAccount = "zls@happy-gene.com";

    public static void main(String[] args) throws Exception {
        // 1. 创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = new Properties();                    // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", myEmailSMTPHost);   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "false");            // 需要请求认证

        // PS: 某些邮箱服务器要求 SMTP 连接需要使用 SSL 安全认证 (为了提高安全性, 邮箱支持SSL连接, 也可以自己开启),
        //     如果无法连接邮件服务器, 仔细查看控制台打印的 log, 如果有有类似 “连接失败, 要求 SSL 安全连接” 等错误,
        //     打开下面 /* ... */ 之间的注释代码, 开启 SSL 安全连接。
        /*
        // SMTP 服务器的端口 (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果开启了 SSL 连接,
        //                  需要改为对应邮箱的 SMTP 服务器的端口, 具体可查看对应邮箱服务的帮助,
        //                  QQ邮箱的SMTP(SLL)端口为465或587, 其他邮箱自行去查看)
        final String smtpPort = "465";
        props.setProperty("mail.smtp.port", smtpPort);
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", smtpPort);
        */

        // 2. 根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);                                 // 设置为debug模式, 可以查看详细的发送 log

        // 3. 创建一封邮件
        MimeMessage message = createMimeMessage(session, myEmailAccount, receiveMailAccount);

        // 4. 根据 Session 获取邮件传输对象
        Transport transport = session.getTransport();

        // 5. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
        //
        //    PS_01: 成败的判断关键在此一句, 如果连接服务器失败, 都会在控制台输出相应失败原因的 log,
        //           仔细查看失败原因, 有些邮箱服务器会返回错误码或查看错误类型的链接, 根据给出的错误
        //           类型到对应邮件服务器的帮助网站上查看具体失败原因。
        //
        //    PS_02: 连接失败的原因通常为以下几点, 仔细检查代码:
        //           (1) 邮箱没有开启 SMTP 服务;
        //           (2) 邮箱密码错误, 例如某些邮箱开启了独立密码;
        //           (3) 邮箱服务器要求必须要使用 SSL 安全连接;
        //           (4) 请求过于频繁或其他原因, 被邮件服务器拒绝服务;
        //           (5) 如果以上几点都确定无误, 到邮件服务器网站查找帮助。
        //
        //    PS_03: 仔细看log, 认真看log, 看懂log, 错误原因都在log已说明。
        transport.connect(myEmailAccount, myEmailPassword);

        // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(message, message.getAllRecipients());

        // 7. 关闭连接
        transport.close();
    }

    /**
     * 创建一封只包含文本的简单邮件
     *
     * @param session 和服务器交互的会话
     * @param sendMail 发件人邮箱
     * @param receiveMail 收件人邮箱
     * @return
     * @throws Exception
     */
    public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail) throws Exception {
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);     // 创建邮件对象

        /*
         * 也可以根据已有的eml邮件文件创建 MimeMessage 对象
         * MimeMessage message = new MimeMessage(session, new FileInputStream("MyEmail.eml"));
         */

        // 2. From: 发件人
        //    其中 InternetAddress 的三个参数分别为: 邮箱, 显示的昵称(只用于显示, 没有特别的要求), 昵称的字符集编码
        //    真正要发送时, 邮箱必须是真实有效的邮箱。
        message.setFrom(new InternetAddress(sendMail, "happygene", "UTF-8"));

        // 3. To: 收件人
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, "zls1", "UTF-8"));
        //    To: 增加收件人（可选）
        message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress("zls@cool-too.com", "zls2", "UTF-8"));
        //    Cc: 抄送（可选）
        message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress("zls@happy-gene.com", "chao-song", "UTF-8"));
        //    Bcc: 密送（可选）
        message.setRecipient(MimeMessage.RecipientType.BCC, new InternetAddress("lisongzhao0@163.com", "mi-chao", "UTF-8"));

        // 4. Subject: 邮件主题
        message.setSubject("HG心安报告", "UTF-8");

        // 5. Content: 邮件正文（可以使用html标签）
        message.setContent("<table style=\"border-collapse: collapse; border-spacing: 0; font-size: 16px; line-height: 1.5; margin: 0 auto; max-width: 680px; min-width: 300px; width: 100%\">\n" +
                "                        <tbody><tr>\n" +
                "                            <td style=\"-moz-hyphens: auto; -webkit-hyphens: auto; border-collapse: collapse !important; color: #222; font-family: 'Helvetica', 'Arial', sans-serif; font-size: 16px; hyphens: auto; margin: 0; padding: 0; word-break: break-word\">\n" +
                "                                <table width=\"100%\" style=\"border-collapse: collapse; border-spacing: 0\">\n" +
                "                                    <tbody><tr>\n" +
                "                                        <td style=\"-moz-hyphens: auto; -webkit-hyphens: auto; border-collapse: collapse !important; color: #222; font-family: 'Helvetica', 'Arial', sans-serif; font-size: 16px; hyphens: auto; margin: 0; padding: 0; word-break: break-word\">\n" +
                "                                            <div style=\"background: #fff; margin-bottom: 5%\">\n" +
                "    <img src=\"http://towerfiles.oss.aliyuncs.com/mail/mail-towerlogo.png\" style=\"-ms-interpolation-mode: bicubic; clear: both; display: block; float: none; height: 60px; margin: 5% auto 2%; max-width: 100%; outline: none; text-decoration: none; width: 60px\" align=\"none\">\n" +
                "    <h1 style=\"color: #222; font-family: 'Helvetica', 'Arial', sans-serif; font-size: 32px; font-weight: normal; margin: 0 0 0.2em; padding: 0; text-align: center\" align=\"center\">Tower Class</h1>\n" +
                "    <p style=\"color: #999; font-family: 'Helvetica', 'Arial', sans-serif; font-size: 20px; margin: 0 0 5%; padding: 0; text-align: center\" align=\"center\">组建你的团队并开展协作</p>\n" +
                "    <div style=\"border-bottom-color: #e8e8e8; border-bottom-style: solid; border-bottom-width: 1px; border-top-color: #e8e8e8; border-top-style: solid; border-top-width: 4px; height: 4px\"></div>\n" +
                "\n" +
                "    <p style=\"color: #222; font-family: 'Helvetica', 'Arial', sans-serif; font-size: 16px; margin: 5% 3%; padding: 0\">你好<br><br>\n" +
                "        我是彩程设计 CEO 沈学良，非常高兴你注册并使用 <a href=\"https://tower.im\" style=\"color: #2ba6cb; display: inline-block; text-decoration: none\" target=\"_blank\">Tower</a>。接下来的时间里，我会向你介绍 Tower 的基本使用方法，带领你和团队快速进入工作状态。\n" +
                "    </p>\n" +
                "    <p style=\"color: #222; font-family: 'Helvetica', 'Arial', sans-serif; font-size: 16px; margin: 5% 3%; padding: 0\">今天我主要讲述如何轻松组建你的团队，有效开展协作。</p>\n" +
                "</div>\n" +
                "\n" +
                "<hr style=\"background: #e8e8e8; border: none; color: #ddd; height: 1px; margin-bottom: 5%\">\n" +
                "\n" +
                "<div style=\"background: #fff; margin-bottom: 5%\">\n" +
                "    <h3 style=\"color: #222; font-family: 'Helvetica', 'Arial', sans-serif; font-size: 24px; margin: 0 0 5%; padding: 0; text-align: center\" align=\"center\">组建团队</h3>\n" +
                "    <p style=\"color: #222; font-family: 'Helvetica', 'Arial', sans-serif; font-size: 16px; margin: 5% 3%; padding: 0\">如果你是团队负责人并且大部分成员还未加入 Tower。毫无疑问，目前我们首先要做的就是把他们邀请进来。在 Tower 进入<b>「团队」</b>页面，点击<b>「添加新成员」</b>，即可邀请成员了。</p>\n" +
                "    <p style=\"color: #222; font-family: 'Helvetica', 'Arial', sans-serif; font-size: 16px; margin: 5% 3%; padding: 0\">我们提供三种轻松简单的邀请方式，满足你不同的使用需要。当然，我最爱「微信邀请」，扫码转发给团队成员，直接在微信上审核，方便极了！</p>\n" +
                "    <p style=\"color: #222; font-family: 'Helvetica', 'Arial', sans-serif; font-size: 16px; margin: 5% 3%; padding: 0; text-align: center\" align=\"center\">\n" +
                "        <img width=\"819\" src=\"http://towerfiles.oss-cn-hangzhou.aliyuncs.com/newsletter_static/1503/team-novice-mail-invite.png\" style=\"-ms-interpolation-mode: bicubic; clear: both; display: block; float: none; height: auto; margin: 0 auto; max-width: 100%; outline: none; text-align: center; text-decoration: none\" align=\"none\">\n" +
                "    </p>\n" +
                "</div>\n" +
                "\n" +
                "<hr style=\"background: #e8e8e8; border: none; color: #ddd; height: 1px; margin-bottom: 5%\">\n" +
                "\n" +
                "<div style=\"background: #fff; margin-bottom: 5%\">\n" +
                "    <h3 style=\"color: #222; font-family: 'Helvetica', 'Arial', sans-serif; font-size: 24px; margin: 0 0 5%; padding: 0; text-align: center\" align=\"center\">启动第一个项目</h3>\n" +
                "    <p style=\"color: #222; font-family: 'Helvetica', 'Arial', sans-serif; font-size: 16px; margin: 5% 3%; padding: 0\">团队组建完成，现在你可以启动一个项目了！它可以是软件开发的迭代、员工招聘、销售管理，甚至是旅行计划。不单有这么多玩法，你还可以利用「项目自定义」，创造特殊的项目，比如文档库、团队 BBS、团队网盘... 有很多团队的确在这么干。</p>\n" +
                "    <p style=\"color: #222; font-family: 'Helvetica', 'Arial', sans-serif; font-size: 16px; margin: 5% 3%; padding: 0; text-align: center\" align=\"center\">\n" +
                "        <img width=\"675\" src=\"http://towerfiles.oss-cn-hangzhou.aliyuncs.com/newsletter_static/1503/team-novice-mail-project.png\" style=\"-ms-interpolation-mode: bicubic; clear: both; display: block; float: none; height: auto; margin: 0 auto; max-width: 100%; outline: none; text-align: center; text-decoration: none\" align=\"none\">\n" +
                "        <span style=\"color: #999999; display: inline-block; font-size: 14px; font-style: italic; line-height: 2em; padding: 10px\">小技能：在「团队设置」中，你还能对项目模块进行排序和隐藏</span>\n" +
                "    </p>\n" +
                "\n" +
                "    <p style=\"color: #222; font-family: 'Helvetica', 'Arial', sans-serif; font-size: 16px; margin: 5% 3%; padding: 0; text-align: center\" align=\"center\">\n" +
                "        <img width=\"634\" src=\"http://towerfiles.oss-cn-hangzhou.aliyuncs.com/mail/team-novice-mail-type.png\" style=\"-ms-interpolation-mode: bicubic; clear: both; display: block; float: none; height: auto; margin: 0 auto; max-width: 100%; outline: none; text-align: center; text-decoration: none\" align=\"none\">\n" +
                "        <span style=\"color: #999999; display: inline-block; font-size: 14px; font-style: italic; line-height: 2em; padding: 10px\">创建项目时可以选择项目类型，你可以选择「敏捷项目」，<br>利用看板处理高度流程化的任务。</span>\n" +
                "    </p>\n" +
                "</div>\n" +
                "\n" +
                "<hr style=\"background: #e8e8e8; border: none; color: #ddd; height: 1px; margin-bottom: 5%\">\n" +
                "\n" +
                "<div style=\"background: #fff; margin-bottom: 5%\">\n" +
                "    <h3 style=\"color: #222; font-family: 'Helvetica', 'Arial', sans-serif; font-size: 24px; margin: 0 0 5%; padding: 0; text-align: center\" align=\"center\">团队协作，信息的传递非常重要</h3>\n" +
                "    <p style=\"color: #222; font-family: 'Helvetica', 'Arial', sans-serif; font-size: 16px; margin: 5% 3%; padding: 0\">当与你有关的工作内容有更新时，你能够及时收到来自 Tower 的通知。Tower 提供多种通知方式供你选择，覆盖所有的使用场景。在 Tower 右上角点击头像弹出菜单，或者在通知窗口的右上角点击设置按钮即可进入通知设置。</p>\n" +
                "    <p style=\"color: #222; font-family: 'Helvetica', 'Arial', sans-serif; font-size: 16px; margin: 5% 3%; padding: 0\">如果你长期待在电脑前，建议开启桌面通知，贴心的气泡会及时告之你新动态；当你需要移动办公，使用微信版会是个不错的选择， 点击通知即可处理任务；另外，我们的智能提醒，可以判断你的在线状态来调整通知的方式，保证通知渠道的畅通。</p>\n" +
                "    <p style=\"color: #222; font-family: 'Helvetica', 'Arial', sans-serif; font-size: 16px; margin: 5% 3%; padding: 0; text-align: center\" align=\"center\">\n" +
                "        <img width=\"722\" src=\"http://towerfiles.oss-cn-hangzhou.aliyuncs.com/newsletter_static/1503/team-novice-mail-notice.png\" style=\"-ms-interpolation-mode: bicubic; clear: both; display: block; float: none; height: auto; margin: 0 auto; max-width: 100%; outline: none; text-align: center; text-decoration: none\" align=\"none\">\n" +
                "    </p>\n" +
                "</div>\n" +
                "\n" +
                "<hr style=\"background: #e8e8e8; border: none; color: #ddd; height: 1px; margin-bottom: 5%\">\n" +
                "\n" +
                "<p style=\"color: #222; font-family: 'Helvetica', 'Arial', sans-serif; font-size: 16px; margin: 5% 3%; padding: 0\">\n" +
                "    我很想继续聊下去，不过今天就先聊到这里吧。明天，我将向你介绍如何用 Tower 来推动项目，把工作变得井井有条。如果你仍然有一些疑问，可以在\n" +
                "    <a href=\"https://tower.im/help\" style=\"color: #2ba6cb; display: inline-block; text-decoration: none\" target=\"_blank\">帮助中心</a>\n" +
                "    获得需要的解答或向我们提问 :)\n" +
                "</p>\n" +
                "\n" +
                "<p style=\"color: #222; font-family: 'Helvetica', 'Arial', sans-serif; font-size: 16px; margin: 5% 3%; padding: 0; text-align: right\" align=\"right\">\n" +
                "    <b style=\"font-size: 22px\">沈学良</b><br>\n" +
                "    <span style=\"color: #999; font-size: 14px; text-align: center\">彩程设计 CEO</span>\n" +
                "</p>\n" +
                "\n" +
                "                                        </td>\n" +
                "                                    </tr>\n" +
                "                                </tbody></table>\n" +
                "\n" +
                "                                <hr style=\"background: #e8e8e8; border: none; color: #ddd; height: 1px; margin: 5% 0\">\n" +
                "\n" +
                "                                <table width=\"100%\" style=\"border-collapse: collapse; border-spacing: 0\">\n" +
                "                                    <tbody><tr>\n" +
                "                                        <td colspan=\"2\" align=\"center\" style=\"-moz-hyphens: auto; -webkit-hyphens: auto; border-collapse: collapse !important; color: #222; font-family: 'Helvetica', 'Arial', sans-serif; font-size: 16px; hyphens: auto; margin: 0; padding: 0; word-break: break-word\">\n" +
                "                                            <p style=\"color: #999999; font-family: 'Helvetica', 'Arial', sans-serif; font-size: 12px; margin: 0 0 10px; padding: 0\">\n" +
                "                                                如有疑问请联系我们 ：<a href=\"mailto:tower@mycolorway.com\" style=\"color: #999999; text-decoration: underline\" target=\"_blank\">tower@mycolorway.com</a> 或进入<a href=\"https://tower.im/help\" target=\"_blank\" style=\"color: #999999; text-decoration: underline\">帮助中心</a>\n" +
                "                                            </p>\n" +
                "                                        </td>\n" +
                "                                    </tr>\n" +
                "                                </tbody></table>\n" +
                "                            </td>\n" +
                "                        </tr>\n" +
                "                \n" +
                "            \n" +
                "        \n" +
                "    </tbody></table>", "text/html;charset=UTF-8");

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
}
