package io.itman.library.util;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import io.itman.library.vo.MenuTree;
import io.itman.library.vo.Tree;

import java.util.*;

/**
 * 树型结构相关工具类
 */
public class TreeUtil {

    public static Map<String,Object> mapArray = new LinkedHashMap<String, Object>();
    public List<Tree> modelCommon;
    public List<MenuTree> treeModelCommon;
    public List<Object> list = new ArrayList<Object>();
    public List<List<Object>> treeList = new ArrayList<List<Object>>();

    public List<Object> modelList(List<Tree> model){
        this.modelCommon = model;
        for (Tree x : model) {
            Map<String,Object> mapArr = new LinkedHashMap<String, Object>();
            if(x.getpId()==0){
                mapArr.put("id", x.getId());
                mapArr.put("text", x.getName());
//                mapArr.put("pid", x.getpId());
                mapArr.put("children", modelChild(x.getId()));
                list.add(mapArr);
            }
        }
        return list;
    }

    public List<?> modelChild(int id){
        List<Object> lists = new ArrayList<Object>();
        for(Tree a:modelCommon){
            Map<String,Object> childArray = new LinkedHashMap<String, Object>();
            if(a.getpId() == id){
                childArray.put("id", a.getId());
                childArray.put("text", a.getName());
//                childArray.put("pid", a.getpId());
                childArray.put("children", modelChild(a.getId()));
                lists.add(childArray);
            }
        }
        return lists;
    }



    public static List<Map> treeDataList(Map<String, Object> map,String sql,int needTop) {
        SqlPara sqlPara = Db.getSqlPara(sql, Kv.by("map", map));
        List<Record> rList=  Db.find(sqlPara);
        List<Map> list=new ArrayList<>();
        if(1==needTop){
            Map root=new HashMap();
            root.put("name","顶级");
            root.put("id",0);
            list.add(root);
        }

        for (Record r:rList) {
            Map m=r.getColumns();
            m.put("_parentId",r.getInt("parentId"));
            list.add(m);
        }
        return list;
    }


    public List<List<Object>> treeModelList(List<MenuTree> model){
        this.treeModelCommon = model;
        for (MenuTree x : model) {
            Map<String,Object> mapArr = new LinkedHashMap<String, Object>();
            if(x.getpId()==0){
                List<Object> list1 = new ArrayList<>();
                mapArr.put("id", x.getId());
                //mapArr.put("text", x.getName());
                mapArr.put("title",x.getTitle());
                mapArr.put("pid", x.getpId());
                //mapArr.put("url", x.getUrl());
                mapArr.put("href",x.getUrl());
                //mapArr.put("state", x.getState());
                mapArr.put("isCurrent",x.getCurrent());
                //mapArr.put("iconCls", x.getIconCls());
                mapArr.put("icon",x.getIcon());
                mapArr.put("menu", treeModelChild(x.getId()));
                list1.add(mapArr);
                treeList.add(list1);
            }
        }
        return treeList;
    }

    public List<?> treeModelChild(int id){
        List<Object> lists = new ArrayList<Object>();
        for(MenuTree a:treeModelCommon){
            Map<String,Object> childArray = new LinkedHashMap<String, Object>();
            if(a.getpId() == id){
                childArray.put("id", a.getId());
                //childArray.put("text", a.getName());
                //childArray.put("text", a.getName());
                childArray.put("title",a.getTitle());
                childArray.put("pid", a.getpId());
                //childArray.put("url", a.getUrl());
                childArray.put("href",a.getUrl());
                //childArray.put("state", a.getState());
                childArray.put("isCurrent",a.getCurrent());
                //childArray.put("iconCls", a.getIconCls());
                childArray.put("icon",a.getIcon());
                //查看是否有子菜单
                Record record = Db.findFirst("select count(1) as count from sys_rolemenu where parentId = ? ", a.getId());
                if(record.getInt("count")>0){
                    List<?> objects = treeModelChild(a.getId());
                    childArray.put("children",objects);
                }else {
                    childArray.put("children",new ArrayList<>());
                }
                lists.add(childArray);
            }
        }
        return lists;
    }
}
