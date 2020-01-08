package io.itman.admin.controller;


import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import io.itman.admin.filter.StatusValueFilter;
import io.itman.admin.service.ProvinceCityCountyService;
import io.itman.library.util.DataGridReturn;
import io.itman.library.util.JsonObjectUtils;
import io.itman.library.util.JsonUtil;
import io.itman.library.util.StringUtil;
import io.itman.model.Provincecitycounty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

* @Description:    区域管理

* @Author:         CES

* @Version:        1.0

*/
@Controller
@RequestMapping("/admin/provinceCityCounty")
public class ProvinceCityCountyController {
    String parenturl ="/ProvinceCityCounty/";
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ProvinceCityCountyService provinceCityCountyService;

    /**
     * @Author CES
     * @Description 跳转列表页面
     * @Param []
     * @return java.lang.String
     **/
    @GetMapping(value = "/index")
    public String index() {
        return (parenturl + "index");
    }

    /**
     * @Author CES
     * @Description  列表页面请求数据接口
     * @Param [map]
     * @return cc.ahxb.util.DataGridReturn
     **/
    @ResponseBody
    @PostMapping("/DataList")
    public String DataList(@RequestParam Map<String, Object> map) {
        DataGridReturn dgr = provinceCityCountyService.DataList(map);
        return JsonObjectUtils.objectToJson(dgr,new StatusValueFilter());
    }

    /**
     * @Author CES
     * @Description 跳转编辑页面
     * @Param [ID, request]
     * @return java.lang.String
     **/
    @GetMapping(value = "/edit")
    public String edit(@RequestParam(value = "ID", defaultValue = "0") int ID, HttpServletRequest request) {
        request.setAttribute("id", ID);
        return (parenturl + "edit");
    }

    /**
     * @Author CES
     * @Description 编辑页面数据回显接口
     * @Param [ID, request]
     * @return java.lang.String
     **/
    @GetMapping(value = "/Show")
    @ResponseBody
    public Provincecitycounty Show(@RequestParam(value = "id", defaultValue = "0") String id) {
        Provincecitycounty model = Provincecitycounty.dao.findById(id);
        if (model!=null&&model.getSort().equals(StringUtil.DEFAULT_SORT)){
            model.setSort(null);
        }
        return model;
    }

    /**
     * @Author CES
     * @Description 区域树请求数据接口
     * @Param [map]
     * @return java.util.List<java.util.Map>
     **/
    @ResponseBody
    @PostMapping("/treeDataList")
    public List<Map> treeDataList(@RequestParam Map<String, Object> map) {
        SqlPara sqlPara = Db.getSqlPara("getProvinceCityCountyList", Kv.by("map", map));
        List<Record> rList=  Db.find(sqlPara);
        List<Map> list=new ArrayList<>();
        Map root=new HashMap();
        root.put("name","顶级");
        root.put("id",0);
        list.add(root);
        for (Record r:rList) {
            Map m=r.getColumns();
            m.put("_parentId",r.getInt("parentId"));
            list.add(m);
        }
        return list;
    }

    /**
     * @Author CES
     * @Description 新增、修改保存动作
     * @Param [model]
     * @return cc.ahxb.util.JsonUtil
     **/
    @PostMapping(value = "/save")
    @ResponseBody
    public JsonUtil save(Provincecitycounty model) {
        JsonUtil result = new JsonUtil();

        if (model!=null){
            model.setSort(StringUtil.DEFAULT_SORT);
        }
        if (model.getId() == 0) {
            if (null == model.getParentId()) {
                model.setParentId(0);
            }
            model.save();
            result.setMessage("恭喜您，添加成功！");
        } else {
            model.update();
            result.setMessage("恭喜您，修改成功！");
        }
        stringRedisTemplate.delete("provinceCityCounty");//根据key删除缓存
        return result;
    }

    /**
     * @Author CES
     * @Description //删除动作
     * @Param [ID]
     * @return cc.ahxb.util.JsonUtil
     **/
    @PostMapping(value = "/delete")
    @ResponseBody
    public JsonUtil del(String ID) {
        JsonUtil result = new JsonUtil();
        try {
            for (String id : ID.split(",")) {
                Provincecitycounty.dao.deleteById(id);
            }
            result.setMessage("删除成功");
        } catch (Exception e) {
            result.setMessage("删除失败");
            result.setResult(0);
            e.printStackTrace();
        }
        stringRedisTemplate.delete("provinceCityCounty");//根据key删除缓存
        return result;
    }
}
