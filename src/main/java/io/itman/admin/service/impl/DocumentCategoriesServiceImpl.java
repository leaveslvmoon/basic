package io.itman.admin.service.impl;



import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import io.itman.admin.service.DocumentCategoriesService;
import io.itman.library.util.DataGridReturn;
import io.itman.library.util.JsonUtil;
import io.itman.library.util.StringUtil;
import io.itman.model.Contentcategories;
import io.itman.model.DocumentCategories;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DocumentCategoriesServiceImpl implements DocumentCategoriesService {
    /**
     * @Author CES
     * @Description 新增同级分类不重复
     * @Param [name, parentId]
     * @return int
     **/
    public int number(String name,int parentId) {
        List<Contentcategories> contentcategories = Contentcategories.dao.find("select * from document_categories where name = '"+name+"'and parentId = "+parentId+ "");
        if(contentcategories.size()>0){
            return 0;
        }
        return 1;
    }

    /**
     * @Author CES
     * @Description 修改同级分类不重复
     * @Param [id, name, parentId]
     * @return int
     **/
    public int number(int id, String name,int parentId) {
        List<Contentcategories> protocolList = Contentcategories.dao.find("select * from document_categories where name = '"+name+"'and parentId = "+parentId+ " and id != "+id+"");
        if(protocolList.size()>0){
            return 0;
        }
        return 1;
    }


    @Override
    public DataGridReturn DataList(Map<String, Object> map) {
        SqlPara sqlPara = Db.getSqlPara("getDocumentCategoriesList", Kv.by("map", map));
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

    @Override
    public JsonUtil save(DocumentCategories model) {
        String name = model.getName();
        if (model.getSort()==null){
            model.setSort(StringUtil.DEFAULT_SORT);
        }
        JsonUtil result = new JsonUtil();
        int parentId ;
        if (model.getId() == 0) {
            if(null==model.getParentId()){
                model.setParentId(0);
                parentId = 0;
            }else {
                parentId = model.getParentId();
            }
            int number = this.number(name,parentId);
            if(number==0){
                result.setResult(0);
                result.setMessage("分类名称重复");
                return result;
            }
            model.save();
            result.setMessage("恭喜您，添加成功！");
        } else {
            int number = this.number(model.getId(),model.getName(),model.getParentId());
            if(number==0){
                result.setResult(0);
                result.setMessage("分类名称重复");
                return result;
            }
            model.update();
            result.setMessage("恭喜您，修改成功！");
        }
        return result;
    }

    @Override
    public JsonUtil del(String ID) {
        JsonUtil result = new JsonUtil();
        try {
            for (String id : ID.split(",")) {
                DocumentCategories.dao.deleteById(id);
            }
            result.setMessage("删除成功");
        } catch (Exception e) {
            result.setMessage("删除失败");
            result.setResult(0);
            e.printStackTrace();
        }
        return result;
    }
}
