package io.itman.library.util;


import io.itman.library.vo.Tree;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台管理菜单树工具类
 */
public class MenuTreeUtil {

    public static Map<String,Object> mapArray = new LinkedHashMap<String, Object>();
    public List<Tree> menuCommon;
    public List<Object> list = new ArrayList<Object>();

    public List<Object> menuList(List<Tree> menu){
        this.menuCommon = menu;
        for (Tree x : menu) {
            Map<String,Object> mapArr = new LinkedHashMap<String, Object>();
            if(x.getpId()==0){
                mapArr.put("id", x.getId());
                mapArr.put("text", x.getName());
//                mapArr.put("pid", x.getpId());
                mapArr.put("iconCls",x.getIconCls());
                mapArr.put("children", menuChild(x.getId()));
                list.add(mapArr);
            }
        }
        return list;
    }

    public List<?> menuChild(int id){
        List<Object> lists = new ArrayList<Object>();
        for(Tree a:menuCommon){
            Map<String,Object> childArray = new LinkedHashMap<String, Object>();
            if(a.getpId() == id){
                childArray.put("id", a.getId());
                childArray.put("text", a.getName());
//                childArray.put("pid", a.getpId());
                childArray.put("children", menuChild(a.getId()));
                lists.add(childArray);
            }
        }
        return lists;
    }

}
