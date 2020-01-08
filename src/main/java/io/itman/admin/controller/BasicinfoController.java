package io.itman.admin.controller;


import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import io.itman.library.util.JsonUtil;
import io.itman.model.SysBasicinfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/basicinfo")
public class BasicinfoController {

    String parentUrl = "/basicinfo/";
    /**
     * 基本信息列表展示
     *
     * @return
     */
    @GetMapping(value = "/index")
    public String index() {
        return (parentUrl + "basicinfoEdit");
    }

    /**
     * 基本信息添加修改信息保存
     *
     * @param model
     * @return
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public JsonUtil save(SysBasicinfo model, String SMSParameter1, String SMSParameter2, String SMSParameter3) {
        JsonUtil result = new JsonUtil();
        String smsParameter = SMSParameter1 + "@@@" + SMSParameter2 + "@@@" + SMSParameter3;
        model.setSmsParameter(smsParameter);
         if(null==model.getIp()){
            model.setIp(0);
        }
        if(null==model.getMac()){
            model.setMac(0);
        }
        model.update();
        result.setMessage("恭喜您，修改成功！");
        return result;
    }

    private String smsProvider1;

    /**
     * 基本数据信息
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/Show")
    @ResponseBody
    public SysBasicinfo Show(HttpServletRequest request) {
        SysBasicinfo model = SysBasicinfo.dao.findFirst("select * from sys_basicinfo");
        smsProvider1 = (model.getSmsProvider());
        return model;
    }

    /**
     * 获取短信接口数据
     * @param smsID
     * @return
     */
    @PostMapping(value = "/SMS")
    @ResponseBody
    public String sms(String  smsID) {
        if (smsID == null||"".equals(smsID)) {
            smsID = smsProvider1;
        }
        String result = "";
        Record sms = Db.findFirst("select smsParameter from sys_basicinfo where smsProvider = ?", smsID);

        try {
        /*   if( sms.get("sms_parameter")){

           }*/
            String sms_parameter = sms.get("smsParameter");
            String[] strs = sms_parameter.split("@@@");
            String SMSParameter1 ="";
            String SMSParameter2="";
            String SMSParameter3="";
            if(strs[0]!=null&&strs.length>0){
                SMSParameter1 = strs[0];
            }
            if(strs[1]!=null&&strs.length>1){
                SMSParameter2 = strs[1];
            }
            if(strs.length>2){
                if(smsID.equals("aldy")&&strs[2]!=null){
                    SMSParameter3 = strs[2];
                }
            }

            if ("lsm".equals(smsID)) {
                result = ("<tr>\n" +
                        "<td width=\"200\"  align=\"right\"><span class=\"Red\">*</span> API KEY：</td>\n" +
                        "<td>\n" +
                        "<input name=\"SMSParameter1\" class=\"inputs\" data-options=\"height:40,required:true,validType:['length[1,100]']\" value='" + SMSParameter1 + "' type=\"text\" />\n" +
                        "<span class=\"Hui\">会员中心>>短信>>触发发送页面可查看</span>\n" +
                        "</td>\n" +
                        "</tr>\n" +
                        "<tr>\n" +
                        "<td align=\"right\"><span class=\"Red\">*</span> 短信签名：</td>\n" +
                        "<td>\n" +
                        "<input name=\"SMSParameter2\" class=\"inputs\" data-options=\"height:40,required:true,validType:['length[1,100]']\" value='" + SMSParameter2 + "' type=\"text\" />\n" +
                        "<span class=\"Hui\">常用公司名称简写，4-5个字符为宜，不宜过长</span>\n" +
                        "</td>\n" +
                        "</tr>");
            } else if ("aldy".equals(smsID)) {
                result = ("<tr>\n" +
                        "<td width=\"200\" align=\"right\"><span class=\"Red\">*</span> API KEY：</td>\n" +
                        "<td>\n" +
                        "<input name=\"SMSParameter1\" class=\"inputs\" data-options=\"height:40,required:true,validType:['length[1,100]']\" value='" + SMSParameter1 + "' type=\"text\" />\n" +
                        "<span class=\"Hui\">阿里大于分配给应用的AppKey</span>\n" +
                        "</td>\n" +
                        "</tr>\n" +
                        "<tr>\n" +
                        "<td align=\"right\"><span class=\"Red\">*</span> API Secret：</td>\n" +
                        "<td>\n" +
                        "<input name=\"SMSParameter3\" class=\"inputs\" data-options=\"height:40,required:true,validType:['length[1,100]']\" value='" + SMSParameter3 + "' type=\"text\" />\n" +
                        "<span class=\"Hui\">阿里大于分配给应用的AppSecret</span>\n" +
                        "</td>\n" +
                        "</tr>\n" +
                        "<tr>\n" +
                        "<td align=\"right\"><span class=\"Red\">*</span> 短信签名：</td>\n" +
                        "<td>\n" +
                        "<input name=\"SMSParameter2\" class=\"inputs\" data-options=\"height:40,required:true,validType:['length[1,100]']\"  value='" + SMSParameter2 + "' type=\"text\" />\n" +
                        "<span class=\"Hui\">常用公司名称简写，4-5个字符为宜，不宜过长</span>\n" +
                        "</td>\n" +
                        "</tr>\n");
            }
            return result;
        } catch (Exception e) {

            if ("lsm".equals(smsID)) {
                result = ("<tr>\n" +
                        "<td width=\"200\"  align=\"right\"><span class=\"Red\">*</span> API KEY：</td>\n" +
                        "<td>\n" +
                        "<input name=\"SMSParameter1\" data-options=\"height:40,required:true,validType:['length[1,100]']\" class=\"inputs\"  type=\"text\" />\n" +
                        "<span class=\"Hui\">会员中心>>短信>>触发发送页面可查看</span>\n" +
                        "</td>\n" +
                        "</tr>\n" +
                        "<tr>\n" +
                        "<td align=\"right\"><span class=\"Red\">*</span> 短信签名：</td>\n" +
                        "<td>\n" +
                        "<input name=\"SMSParameter2\" data-options=\"height:40,required:true,validType:['length[1,100]']\" class=\"inputs\"  type=\"text\" />\n" +
                        "<span class=\"Hui\">常用公司名称简写，4-5个字符为宜，不宜过长</span>\n" +
                        "</td>\n" +
                        "</tr>");
            } else if ("aldy".equals(smsID)) {
                result = ("<tr>\n" +
                        "<td width=\"200\" align=\"right\"><span class=\"Red\">*</span> API KEY：</td>\n" +
                        "<td>\n" +
                        "<input name=\"SMSParameter1\" data-options=\"height:40,required:true,validType:['length[1,100]']\" class=\"inputs\"  type=\"text\" />\n" +
                        "<span class=\"Hui\">阿里大于分配给应用的AppKey</span>\n" +
                        "</td>\n" +
                        "</tr>\n" +
                        "<tr>\n" +
                        "<td align=\"right\"><span class=\"Red\">*</span> API Secret：</td>\n" +
                        "<td>\n" +
                        "<input name=\"SMSParameter3\" data-options=\"height:40,required:true,validType:['length[1,100]']\" class=\"inputs\"  type=\"text\" />\n" +
                        "<span class=\"Hui\">阿里大于分配给应用的AppSecret</span>\n" +
                        "</td>\n" +
                        "</tr>\n" +
                        "<tr>\n" +
                        "<td align=\"right\"><span class=\"Red\">*</span> 短信签名：</td>\n" +
                        "<td>\n" +
                        "<input name=\"SMSParameter2\" data-options=\"height:40,required:true,validType:['length[1,100]']\" class=\"inputs\"  type=\"text\" />\n" +
                        "<span class=\"Hui\">常用公司名称简写，4-5个字符为宜，不宜过长</span>\n" +
                        "</td>\n" +
                        "</tr>\n");
                e.printStackTrace();
            }

        }
        return result;
    }
}
