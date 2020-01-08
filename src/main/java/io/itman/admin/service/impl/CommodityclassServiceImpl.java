package io.itman.admin.service.impl;


import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import io.itman.admin.service.CommodityclassService;
import io.itman.library.vo.Tree;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommodityclassServiceImpl implements CommodityclassService {

    @Override
    public  List<Tree> buildTree() {
        List<Tree> treeList=new ArrayList<>();;
        List<Record> recordList = Db.find("select id, name text,parentId pid from commodityclass");
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
}
