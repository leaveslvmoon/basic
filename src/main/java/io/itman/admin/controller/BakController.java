package io.itman.admin.controller;


import com.alibaba.fastjson.JSON;
import io.itman.admin.filter.DateValueFilter;
import io.itman.library.exception.MyException;
import io.itman.library.util.DataGridReturn;
import io.itman.library.util.FileUtil;
import io.itman.library.util.JsonUtil;
import io.itman.library.util.MySQLDatabaseBackup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/admin/bak")
public class BakController  {
    @Value("${rm.sqlBak}")
    private String bakPath;

    @Value("${rm.hostIP}")
    private String hostIP;
    @Value("${spring.datasource.username}")
    private String userName;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${rm.dataBase}")
    private String dataBase;

    @GetMapping("index")
    public String toListUI(){

        return "/Bak/index";
    }

    /**
     *
     * @return
     */
    @PostMapping("/DataList")
    @ResponseBody
    public String findAllBak(){
        List<Map> list=new ArrayList<>();
        try {
            list= FileUtil.loopTraversal(bakPath,1);
            long tmp = 0;
            // 时间排序
            Collections.sort(list, (o1, o2) -> (((Date)o2.get("createTime")).compareTo((Date)o1.get("createTime"))));
        }catch (Exception e){
            e.printStackTrace();
        }
        DataGridReturn dgr=new DataGridReturn(list.size(), list);
        return JSON.toJSONString(dgr,new DateValueFilter("createTime"));

    }

    /**
     * 备份数据库
     * @return
     */
    @PostMapping("/doBak")
    @ResponseBody
    public JsonUtil doBak(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String fileName=df.format(new Date()).replace("-","").replace(" ","").replace(":","")+".sql";
        try {
            Boolean flag= MySQLDatabaseBackup.exportDatabaseTool(hostIP,userName,password,bakPath,fileName,dataBase);
        }catch (Exception e){
            e.printStackTrace();
        }
        JsonUtil j=new JsonUtil();
        j.setMessage("备份成功");
        return j;
    }

    @PostMapping("/reBak")
    @ResponseBody
    public JsonUtil reBak(String name){
        Boolean flag=MySQLDatabaseBackup.restore(hostIP,userName,password,dataBase,bakPath,name);
        JsonUtil j=new JsonUtil();
        if(!flag){
            j.setMessage("还原失败");
            j.setResult(1);
            return j;
        }
        j.setMessage("还原成功");
        return j;
    }

    @GetMapping("downLoad")
    public void downLoad(String ID,HttpServletResponse res){
        String fileName = ID;
        res.setHeader("content-type", "application/octet-stream");
        res.setContentType("application/octet-stream");
        res.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = res.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(new File(bakPath
                    + fileName)));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("success");
    }

    /**
     * 删除log
     *
     * @param
     * @return
     */
    @PostMapping(value = "delete")
    @ResponseBody
    public JsonUtil del(String ID) {
        JsonUtil j = new JsonUtil();
        String[] ids = ID.split(",");
        String msg = "删除成功";
        try {
            for (String name : ids) {
                File file=new File(bakPath+name);
                if(file.exists()){
                    file.delete();
                }else {
                    msg = "文件不存在";
                }
            }
        } catch (MyException e) {
            msg = "删除失败";
        }
        j.setMessage(msg);
        return j;
    }
}
