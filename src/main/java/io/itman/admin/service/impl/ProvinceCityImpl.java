package io.itman.admin.service.impl;


import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import io.itman.admin.service.ProvinceCityCountyService;
import io.itman.library.util.DataGridReturn;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProvinceCityImpl implements ProvinceCityCountyService {
    @Override
    public DataGridReturn DataList(Map<String, Object> map) {
        SqlPara sqlPara = Db.getSqlPara("getProvinceCityCountyList", Kv.by("map", map));
        List<Record> rList=  Db.find(sqlPara);
        List<Map> list=new ArrayList<>();
        for (Record r:rList) {
            Map m=r.getColumns();
            if(r.getInt("parentId")!=0){
                m.put("_parentId",r.getInt("parentId"));
            }
            list.add(m);
        }
        DataGridReturn dgr=new DataGridReturn(list.size(), list);
        return dgr;
    }
}
