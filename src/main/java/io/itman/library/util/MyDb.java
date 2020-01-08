package io.itman.library.util;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库封装相关工具类
 * @author 叶子旺
 */
public class MyDb {

    public static Map findFirst(String sql,Object... paras){
        Record record= Db.findFirst(sql,paras);
        if(null!=record){
            return record.getColumns();
        }else{
            return new HashMap();
        }


    }

    public static List<Map> find(String sql){
        List<Record> recordList= Db.find(sql);
        List<Map> list=new ArrayList<>();
        for (Record r:recordList) {
            Map m=r.getColumns();
            list.add(m);
        }
        return list;
    }

    public static List<Map> find(String sql,Object... paras){

        List<Record> recordList= Db.find(sql,paras);
        List<Map> list=new ArrayList<>();
        for (Record r:recordList) {
            Map m=r.getColumns();
            list.add(m);
        }
        return list;
    }

    public static List<Map> find(SqlPara sqlPara){
        List<Record> recordList= Db.find(sqlPara);
        List<Map> list=new ArrayList<>();
        for (Record r:recordList) {
            Map m=r.getColumns();
            list.add(m);
        }
        return list;
    }




}
