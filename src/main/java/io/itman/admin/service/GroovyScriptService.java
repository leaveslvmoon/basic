package io.itman.admin.service;

import com.jfinal.plugin.activerecord.Record;

import java.util.List;

public interface GroovyScriptService {
    public List<Record> testQuery(String sql);
}
