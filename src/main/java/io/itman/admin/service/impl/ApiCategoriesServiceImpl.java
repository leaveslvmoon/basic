package io.itman.admin.service.impl;


import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import io.itman.admin.service.ApiCategoriesService;
import io.itman.library.util.DataGridReturn;
import io.itman.library.util.JsonUtil;
import io.itman.library.util.StringUtil;
import io.itman.model.ApiCategories;
import io.itman.model.ApiContent;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ApiCategoriesServiceImpl implements ApiCategoriesService {
    public int number(String name) {
        List<ApiCategories> apiCategoriesList = ApiCategories.dao.find("select * from api_categories where name = '"+name+"'");
        if(apiCategoriesList.size()>0){
            return 0;
        }
        return 1;
    }

    public int number(String name, int id) {
        List<ApiCategories> apiCategoriesList = ApiCategories.dao.find("select * from api_categories where name = '"+name+"' and id != "+id+"");
        if(apiCategoriesList.size()>0){
            return 0;
        }
        return 1;
    }

    @Override
    public DataGridReturn DataList(Map<String, Object> map) {
        SqlPara sqlPara = Db.getSqlPara("getAPICategoriesList", Kv.by("map", map));
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
    public JsonUtil save(ApiCategories model) {
        String name = model.getName();
        if (model.getSort() == null) {
            model.setSort(StringUtil.DEFAULT_SORT);
        }
        JsonUtil result = new JsonUtil();
        if (model.getId() == 0) {
            if(null==model.getParentId()){
                model.setParentId(0);
            }
            int number = this.number(name);
            if(number==0){
                result.setResult(0);
                result.setMessage("分类名称重复");
                return result;
            }
            model.save();
            result.setMessage("恭喜您，添加成功！");
        } else {
            int number = this.number(model.getName(),model.getId());
            if(number==0){
                result.setResult(0);
                result.setMessage("分类名称重复");
                return result;
            }
            if(model.getStatus()==0){
                List<Record> parentList = Db.find("select * from api_content where type = "+model.getId()+"");
                if(parentList.size()>0){
                    result.setResult(0);
                    result.setMessage("该分类已被接口占用，不能禁用！");
                    return result;
                }
                String ids = "";
                List<Record> recordList = Db.find("select * from api_categories where FIND_IN_SET(parentId,getApiCategories("+model.getId()+"))");
                for(Record record : recordList){
                    List<ApiContent> apiContentList = ApiContent.dao.find("select * from api_content where  type = "+record.getInt("id")+"");
                    if(apiContentList.size()>0){
                        result.setResult(0);
                        result.setMessage("该分类下子级分类被接口占用，不能禁用！");
                        return result;
                    }
                    ids =","+ record.getInt("id");
                }
                ids = model.getId()+ids;
                Db.update("UPDATE api_categories SET status = 0 WHERE parentId in ("+ids+")");
            }
            model.update();
            result.setMessage("恭喜您，修改成功！");
        }
        return result;
    }

    @Override
    public JsonUtil del(String ID) {
        JsonUtil result = new JsonUtil();
            for (String id : ID.split(",")) {
                List<Record> parentList = Db.find("select * from api_content where type = "+id+"");
                if(parentList.size()>0){
                    result.setResult(0);
                    result.setMessage("该分类已被接口占用，不能删除！");
                    return result;
                }
                List<Integer> ids = new ArrayList<>();
                ids.add(Integer.parseInt(id));
                List<Record> recordList = Db.find("select * from api_categories where FIND_IN_SET(parentId,getApiCategories("+id+"))");
                for(Record record : recordList){
                    List<ApiContent> apiContentList = ApiContent.dao.find("select * from api_content where  type = "+record.getInt("id")+"");
                    if(apiContentList.size()>0){
                        result.setMessage("该分类下子级分类被接口占用，不能删除！");
                        result.setResult(0);
                        return result;
                    }
                    ids.add(record.getInt("id"));
                }
                ApiCategories.dao.deleteById(id);
                for(Integer i:ids){
                    ApiContent.dao.deleteById(i);
                }
            }
            result.setMessage("删除成功");
        return result;
    }
}
