package io.itman.admin.service.impl;

import io.itman.admin.service.GroovyScriptService;
import org.springframework.stereotype.Service;

@Service("groovyScriptService")
public class GroovyScriptServiceImpl implements GroovyScriptService {
    @Override
    public String testQuery(long id) {
        return "Test query success, id is " + id;
    }
}
