package io.itman.admin.service.impl;


import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import io.itman.admin.service.ApiContentService;
import io.itman.library.util.JsonUtil;
import io.itman.library.vo.Tree;
import io.itman.model.ApiContent;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ApiContentServiceImpl implements ApiContentService {

    @Override
    public  List<Tree> buildTree() {
        List<Tree> treeList=new ArrayList<>();;
        List<Record> recordList = Db.find("select id, name text,parentId pid from api_categories where status = 1");
        for(Record r:recordList){
            Tree tree =new Tree();
            tree.setName(r.get("text"));
            tree.setId(r.get("id"));
            tree.setpId(r.get("pid"));
            treeList.add(tree);
        }
        return treeList;
    }

    @Override
    public JsonUtil save(ApiContent model) {
        model.setUpdateTime(new Date());
        JsonUtil result = new JsonUtil();
        if (model.getId() == 0) {
            int number = this.number(model.getName());
            if(number==0){
                result.setResult(0);
                result.setMessage("内容名称重复");
            }
            model.save();
            result.setMessage("恭喜您，添加成功！");
        } else {
            int number = this.number(model.getName(),model.getId());
            if(number==0){
                result.setResult(0);
                result.setMessage("内容名称重复");
            }
            model.update();
            result.setMessage("恭喜您，修改成功！");
        }
        return result;
    }

    public int number(String name) {
        List<ApiContent> apiContentList = ApiContent.dao.find("select * from api_content where name = '"+name+"'");
        if(apiContentList.size()>0){
            return 0;
        }
        return 1;
    }

    public int number(String name, int id) {
        List<ApiContent> apiContentList = ApiContent.dao.find("select * from api_content where name = '"+name+"' and id != "+id+"");
        if(apiContentList.size()>0){
            return 0;
        }
        return 1;
    }
}
