package io.itman.admin.controller;


import io.itman.admin.filter.DateValueFilter;
import io.itman.admin.filter.IsForcedValueFilter;
import io.itman.admin.filter.OSTypeValueFilter;
import io.itman.admin.filter.StatusValueFilter;
import io.itman.library.util.CommonUtil;
import io.itman.library.util.DataGridReturn;
import io.itman.library.util.JsonObjectUtils;
import io.itman.library.util.JsonUtil;
import io.itman.model.SysAppversion;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**

* @Description:    APP版本控制

* @Author:         CES

* @Version:        1.0

*/
@Controller
@RequestMapping("/admin/SysAppVersion")
public class SysAppVersionController {

    String parentUrl = "/AppVersion/";

    /**
     * @Author CES
     * @Description 跳转列表页面
     * @Param []
     * @return java.lang.String
     **/
    @GetMapping(value = "/index")
    public String index() {
        return (parentUrl + "index");
    }

    /**
     * @Author CES
     * @Description 列表页面请求数据
     * @Param [page, rows, map]
     * @return cc.ahxb.util.DataGridReturn
     **/
    @ResponseBody
    @PostMapping("/DataList")
    public String DataList(int page, int rows, @RequestParam Map<String, Object> map) {
        DataGridReturn datalist = CommonUtil.DataGridData(page, rows, "getAppVersionContentList", map);
        return JsonObjectUtils.objectToJson(datalist,new OSTypeValueFilter(),new DateValueFilter("updateTime"),new StatusValueFilter(),new IsForcedValueFilter());
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
        SysAppversion appversion = SysAppversion.dao.findById(ID);
        request.setAttribute("appversion", appversion);
        return (parentUrl + "edit");
    }

    /**
     * @Author CES
     * @Description编辑页面数据回显
     * @Param [id]
     * @return cc.ahxb.model.SysAppversion
     **/
    @GetMapping(value = "/Show")
    @ResponseBody
    public SysAppversion Show(@RequestParam(value = "id", defaultValue = "0") String id) {
        SysAppversion model = SysAppversion.dao.findById(id);
        return model;
    }
    /**
     * @Author CES
     * @Description 新增修改保存动作
     * @Param [model]
     * @return cc.ahxb.util.JsonUtil
     **/
    @PostMapping(value = "/save")
    @ResponseBody
    public JsonUtil save(SysAppversion model) {
        JsonUtil result = new JsonUtil();
        if (model.getId() == 0) {
            model.save();
            result.setMessage("恭喜您，添加成功！");
        } else {
            model.update();
            result.setMessage("恭喜您，修改成功！");
        }
        return result;
    }

    /**
     * @Author CES
     * @Description 删除动作
     * @Param [ID]
     * @return cc.ahxb.util.JsonUtil
     **/
    @PostMapping(value = "/delete")
    @ResponseBody
    public JsonUtil del(String ID) {
        JsonUtil result = new JsonUtil();
        for (String id : ID.split(",")) {
            SysAppversion.dao.deleteById(id);
        }
        result.setMessage("删除成功");
        return result;
    }

}
