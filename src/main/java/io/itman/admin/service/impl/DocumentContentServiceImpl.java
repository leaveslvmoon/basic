package io.itman.admin.service.impl;


import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import io.itman.admin.service.DocumentContentService;
import io.itman.library.util.JsonUtil;
import io.itman.library.util.StringUtil;
import io.itman.library.vo.Tree;
import io.itman.model.DocumentContent;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentContentServiceImpl implements DocumentContentService {

    @Override
    public  List<Tree> buildTree() {
        List<Tree> treeList=new ArrayList<>();;
        List<Record> recordList = Db.find("select id, name text,parentId pid from document_categories where status = 1");
//        recordList.
        for(Record r:recordList){
            Tree tree =new Tree();
            tree.setId(r.get("id"));
            tree.setName(r.get("text"));
            tree.setpId(r.get("pid"));
            treeList.add(tree);
        }
        return treeList;
    }

    @Override
    public JsonUtil del(String ID) {
        JsonUtil result = new JsonUtil();
        for (String id : ID.split(",")) {
            DocumentContent.dao.deleteById(id);
        }
        result.setMessage("删除成功");
        return result;
    }

    @Override
    public JsonUtil save(HttpServletRequest request, DocumentContent model) {
        JsonUtil result = new JsonUtil();
        if(model.getSort() == null){
            model.setSort(StringUtil.DEFAULT_SORT);

        }
        if (model.getId() == 0) {
            model.save();
            result.setMessage("恭喜您，添加成功！");
        } else {
            model.update();
            result.setMessage("恭喜您，修改成功！");
        }
        return result;
    }
}
