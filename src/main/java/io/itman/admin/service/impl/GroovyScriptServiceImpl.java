package io.itman.admin.service.impl;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import io.itman.admin.service.GroovyScriptService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("groovyScriptService")
public class GroovyScriptServiceImpl implements GroovyScriptService {
    @Override
    public List<Record> testQuery(String sql) {
        List<Record> list= Db.find(sql);
        return list;
    }

    public List<Record> find(String sql){
        List<Record> list= Db.find(sql);
        return list;
    }
}
