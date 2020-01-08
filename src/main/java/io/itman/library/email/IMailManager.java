package io.itman.library.email;
/**
 * Describe
 *
 * @author Gao
 * @date 2018/12/3
 */
public interface IMailManager {
    /**
     * 发送简单邮件
     * @param to 目的用户
     * @param suject 主题
     * @param content 内容
     * @throws Exception
     */
    void sendSimpleMail(String to, String suject, String content) throws  Exception;

    /**
     * 发送HTML邮件
     * @param to 目的用户
     * @param suject 主题
     * @param content 内容
     * @throws Exception
     */
    void sendHTMLMail(String to, String suject, String content) throws  Exception;

    /**
     *
     * 发送带附件的邮件
     *  mailSender.send(message);  是一个阻塞方法，建议放入子线程中，异步调用
     * @param to 目的用户
     * @param suject 目的用户
     * @param content 内容
     * @param filePath 附件地址
     * @throws Exception
     */
    void sendAttachmentMail(String to, String suject, String content, String filePath) throws  Exception;

    /**
     * 发送带图片的邮件  mailSender.send(message);  是一个阻塞方法，建议放入子线程中，异步调用
     * @param to
     * @param suject
     * @param content
     * @param rscPath
     * @param rscId
     * @throws Exception
     */
    void sendInLiResourceMail(String to, String suject, String content, String rscPath, String rscId) throws  Exception;
}
