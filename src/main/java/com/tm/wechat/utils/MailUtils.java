package com.tm.wechat.utils;


import com.tm.wechat.config.MailProperties;
import com.tm.wechat.utils.commons.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Created by LEO on 16/10/13.
 */
@Component
public class MailUtils {

    @Autowired
    private MailProperties mailProperties;

    public void sendAuthMail(AuthMailRecord authMailRecord) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String uploadDate = sdf.format(new Date());
        // 邮件主题
        String subject = authMailRecord.getLeaseCompanyName() + "委托授权书说明_"
                + authMailRecord.getName() + "_" + authMailRecord.getPlate() + "_" + uploadDate;
        // 文件存放路径
        String url = authMailRecord.getUrl();
        //1. 用于存放 SMTP 服务器地址等参数
        Properties properties = new Properties();
        // 主机地址
        properties.setProperty("mail.smtp.host", mailProperties.getHost());
        // 邮件协议
        properties.setProperty("mail.transport.protocol", mailProperties.getProtocol());
        // 认证
        properties.setProperty("mail.smtp.auth", mailProperties.getAuth());
        // 端口
        properties.setProperty("mail.smtp.port", mailProperties.getPort());

        // 2. 创建session
        Session session = Session.getDefaultInstance(
                properties, new MailAuthenticator(mailProperties.getAddress(), mailProperties.getPwd()));
        // 开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
        session.setDebug(true);

        // 3. 创建邮件
        // 创建邮件对象
        MimeMessage message = new MimeMessage(session);

        // 邮件的标题
        message.setSubject(subject);
        // 邮件发送日期
        message.setSentDate(new Date());
        // 指明邮件的发件人
        message.setFrom(new InternetAddress(mailProperties.getAddress()));

        // 指明邮件的收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(authMailRecord.getEmail()));

        // 指明邮件的抄送人
        message.setRecipient(Message.RecipientType.CC, new InternetAddress(mailProperties.getCc()));

        // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
        Multipart multipart = new MimeMultipart();

        // 添加邮件正文
        BodyPart contentBodyPart = new MimeBodyPart();
        // 邮件内容
        String result =
                "<html lang=\"zh-CN\" data-ng-app=\"app\">" +
                        "<head>" +
                        "<style type=\"text/css\" rel=\"stylesheet\">body { -webkit-overflow-scrolling: touch} " +
                        ".newsWrapper{padding: 10px;line-height: 28px; background: #fff;}" +
                        ".newsWrapper p{font-size: 16px;}" +
                        ".newsWrapper img{text-align: center}" +
                        ".newsWrapper h1,.newsWrapper h2,.newsWrapper h3,.newsWrapper h4,.newsWrapper h5,.newsWrapper h6{line-height: 24px;}" +
                        ".newsWrapper hr{border: 1px dashed #ccc;}" +
                        "</style>" +
                        "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\" />" +
                        "<link rel=\"stylesheet\" href=\"http://wx.xftm.com/css/app.min.css\" type=\"text/css\" />" +
                        "</head>" +
                        "<body><div class=\"newsWrapper\">" +
                        "<div><p>致：</p></div> " +
                        "<div>" +
                        "<p>&nbsp;&nbsp;鉴于我公司租赁客户 KHXM （姓名）未按照与我公司签署的租赁合同按时偿还合同下的债务。根据其与我公司签订的租赁合同相关约定，我司有权以任何合法方式处置租赁车辆，现我公司要求在贵公司同意下述承诺前提下，依法依规回收车辆：</p>" +
                        "</div> " +
                        "<div><p>&nbsp;&nbsp;&nbsp;&nbsp;一、向该客户进行合法催收；</p></div> " +
                        "<div><p>&nbsp;&nbsp;&nbsp;&nbsp;二、通过合法途径控制并回收租赁车辆，车辆信息为： CPHM  车架号为：  CJHM   颜色为：  CLYS  车型为：CLXH；</p></div> " +
                        "<div><p>&nbsp;&nbsp;&nbsp;&nbsp;三、确保租赁车辆中的财物的安全和完整性；</p></div> " +
                        "<div><p>&nbsp;&nbsp;&nbsp;&nbsp;四、在车辆收回后第一时间将车辆交至我公司指定的地点，并做好交接手续；</p></div> " +
                        "<div><p>&nbsp;&nbsp;&nbsp;&nbsp;五、贵公司的服务费用由我司工作人员统一结算；</p></div> " +
                        "<div><p>&nbsp;&nbsp;&nbsp;&nbsp;授权书详见附件。</p></div> " +
                        "<div><p align=\"right\">WTGSMC</p></div> " +
                        "</div></body></html>";
        result = writeContent(result, authMailRecord);
        contentBodyPart.setContent(result, "text/html;charset=UTF-8");
        multipart.addBodyPart(contentBodyPart);


        // 添加附件
        if (url != null && !"".equals(url)) {
            BodyPart attachmentBodyPart = new MimeBodyPart();
            // 根据附件url获取文件,
            attachmentBodyPart.setDataHandler(new DataHandler(new URL(url)));
            //MimeUtility.encodeWord可以避免文件名乱码
            attachmentBodyPart.setFileName(MimeUtility.encodeWord("收车公司授权书.pdf"));
            multipart.addBodyPart(attachmentBodyPart);
        }
        // 邮件的文本内容
        message.setContent(multipart);

        // 4. 发送邮件,Transport每次发送成功程序帮忙关闭
        Transport.send(message, message.getAllRecipients());
    }

    /**
     * 替换邮件模板内容
     * @param content
     * @param authMailRecord
     * @return
     */
    private String writeContent(String content,AuthMailRecord authMailRecord){
        content = content.replace("KHXM", CommonUtils.isNotNull(authMailRecord.getName()) ? authMailRecord.getName() : "-");
        content = content.replace("CPHM",CommonUtils.isNotNull(authMailRecord.getPlate()) ? authMailRecord.getPlate() : "-");
        content = content.replace("CJHM",CommonUtils.isNotNull(authMailRecord.getVehicleIdentifyNum()) ? authMailRecord.getVehicleIdentifyNum() : "-");
        content = content.replace("CLYS",CommonUtils.isNotNull(authMailRecord.getVehicleColor()) ? authMailRecord.getVehicleColor() : "-");
        content = content.replace("CLXH",CommonUtils.isNotNull(authMailRecord.getVehicleType()) ? authMailRecord.getVehicleType() : "-");
        content = content.replace("WTGSMC",CommonUtils.isNotNull(authMailRecord.getLeaseCompanyName()) ? authMailRecord.getLeaseCompanyName() : "");
        return content;
    }
}
