package io.itman.library.util;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailUtil {

    private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);

    // 邮件发送协议
    private final static String PROTOCOL = "smtp";
    // 是否要求身份认证
    private final static String IS_AUTH = "true";
    // 是否启用调试模式（启用调试模式可打印客户端与服务器交互过程时一问一答的响应消息）
    private final static String IS_ENABLED_DEBUG_MOD = "true";
    // SMTP邮件服务器默认端口
    private final static String PORT = "25";

    /**
     * 发送邮件工具类:通过qq邮件发送,因为具有ssl加密,采用的是smtp协议
     *
     * @param mailServer       邮件服务器的主机名:如 "smtp.qq.com"
     * @param loginAccount     登录邮箱的账号:如 "165468745@qq.com"
     * @param loginAuthCode    登录qq邮箱时候需要的授权码:可以进入qq邮箱,账号设置那里"生成授权码"
     * @param sender           发件人
     * @param recipients       收件人:支持群发
     * @param emailSubject     邮件的主题
     * @param emailContent     邮件的内容
     * @param emailContentType 邮件内容的类型,支持纯文本:"text/plain;charset=utf-8";,带有Html格式的内容:"text/html;charset=utf-8"
     * @return
     */
    public static boolean sendEmail(String mailServer, final String loginAccount, final String loginAuthCode, String sender, String[] recipients,
                                    String emailSubject, String emailContent, String emailContentType) {
        boolean res = true;
        try {
            //跟smtp服务器建立一个连接
            Properties p = new Properties();
            //设置邮件服务器主机名
            //p.setProperty("mail.host", mailServer);
            //p.setProperty("mail.smtp.port", PORT);
            //发送服务器需要身份验证,要采用指定用户名密码的方式去认证
            p.setProperty("mail.smtp.auth", IS_AUTH);
            //发送邮件协议名称
            p.setProperty("mail.transport.protocol", PROTOCOL);
            //------
            //网易的smtp服务器地址
            p.put("mail.smtp.host", "smtp.163.com");
            //SSLSocketFactory类的端口
            p.put("mail.smtp.socketFactory.port", "465");
            //SSLSocketFactory类
            p.put("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            p.put("mail.smtp.auth", "true");
            //网易提供的ssl加密端口,QQ邮箱也是该端口
            p.put("mail.smtp.port", "465");

            //------
            p.setProperty("mail.debug", IS_ENABLED_DEBUG_MOD);
            //使用 STARTTLS安全连接（此句必加）
            p.setProperty("mail.smtp.starttls.enable", "true");

            /*--------------------------------华丽的分割线----------------------------------------*/
            // 建立会话
            Session session = Session.getInstance(p);

            //声明一个Message对象(代表一封邮件),从session中创建
            MimeMessage msg = new MimeMessage(session);

            String nick="";
            try {
                nick=javax.mail.internet.MimeUtility.encodeText("itman论坛");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //邮件封装发件人
            msg.setFrom(new InternetAddress(nick+" <"+sender+">"));
            // 收件人
            InternetAddress[] receptientsEmail = new InternetAddress[recipients.length];
            for (int i = 0; i < recipients.length; i++) {
                receptientsEmail[i] = new InternetAddress(recipients[i]);
            }
            //多个收件人
            msg.setRecipients(Message.RecipientType.TO, receptientsEmail);
            //3邮件内容:主题、内容
            msg.setSubject(emailSubject);
            msg.setContent(emailContent, emailContentType);//发html格式的文本

             /*--------------------------------华丽的分割线----------------------------------------*/
            // 邮件服务器进行验证
            Transport tran = session.getTransport("smtp");

            // *配置发送者的邮箱账户名和密码
            tran.connect(mailServer, loginAccount, loginAuthCode);

            //发送动作
            tran.sendMessage(msg, msg.getAllRecipients());
            logger.info("邮件发送成功!---发送人：{},收件人{}。", sender, receptientsEmail.toString());
            res = true;
        } catch (Exception e) {
            logger.info("邮件发送失败: " + e.getMessage());
            res = false;
        }
        return res;
    }

    public static void main(String[] args) {
        boolean res= sendEmail("smtp.163.com", "leaveslvmoon@163.com", "leaves541015", "leaveslvmoon@163.com", new String[]{ "1264836157@qq.com"}, "mail测试标题", "测试内容：祝您生活愉快!","text/html;charset=utf-8");
        logger.info("\n发送结果:"+res);
    }

}

