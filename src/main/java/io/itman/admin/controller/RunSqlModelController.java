package io.itman.admin.controller;

import com.alibaba.fastjson.JSON;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.*;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;
import com.jfinal.template.Template;
import io.itman.admin.direct.ButtonDirective;
import io.itman.library.util.MyDb;
import io.itman.model.SysConnection;
import io.itman.model.SysSqlmodel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/runSqlModel")
public class RunSqlModelController {

    @RequestMapping("/run")
    @ResponseBody
    public String run(String code,@RequestParam Map<String, Object> map){
        SysSqlmodel sysSqlmodel= SysSqlmodel.dao.findFirst("select * from sys_sqlmodel where code =?",code);
        if(sysSqlmodel.getType()==0){//返回json数据
            int connectionId=sysSqlmodel.getConnectionId();
            SysConnection sysConnection= SysConnection.dao.findById(connectionId);
            Config config= DbKit.getConfig(sysConnection.getAlias());
            if(null==config){
                DruidPlugin dp = new DruidPlugin(sysConnection.getUrl(), sysConnection.getUserName(), sysConnection.getPassWord());
                ActiveRecordPlugin arp = new ActiveRecordPlugin(sysConnection.getAlias(),dp);
                dp.start();
                arp.start();
            }

            String sqlTpl= sysSqlmodel.getSql();
            String sql= getSql(sqlTpl, map);
            List<Record> recordList= Db.use(sysConnection.getAlias()).find(sql);
            List<Map> list=new ArrayList<>();
            for (Record r:recordList) {
                Map m=r.getColumns();
                list.add(m);
            }
            return JSON.toJSONString(list);
        }else{//返回页面

            Engine engine = Engine.use();
            engine.setDevMode(true);
            Kv kv = Kv.by("key", 123);
            Template template=engine.getTemplateByString(sysSqlmodel.getTpl());
            String str = template.renderToString(kv);
            return str;

        }



    }

    public String getSql(String sqlTpl, Map<String,Object> map){
        Engine engine = Engine.use();
        engine.setDevMode(true);
        Kv kv = new Kv();
        for(Map.Entry<String, Object> entry : map.entrySet()){
            String mapKey = entry.getKey();
            Object mapValue = entry.getValue();
            System.out.println(mapKey+":"+mapValue);
            kv.set(mapKey,mapValue);
        }

        Template template=engine.getTemplateByString(sqlTpl);
        String str = template.renderToString(kv);
        return str;
    }
}
