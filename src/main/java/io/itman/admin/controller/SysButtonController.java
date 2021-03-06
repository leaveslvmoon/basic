package io.itman.admin.controller;


import io.itman.admin.filter.ButtonValueFilter;
import io.itman.admin.filter.OpenModelValueFilter;
import io.itman.admin.filter.StatusValueFilter;
import io.itman.admin.service.SysButtonService;
import io.itman.library.util.*;
import io.itman.model.SysButton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**

* @Description:    按钮管理

* @Author:         CES

* @Version:        1.0

*/
@Controller
@RequestMapping("/admin/button")
public class SysButtonController {

    @Autowired
    private SysButtonService sysButtonService;

    String parentUrl = "/Button/";

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
     * @Description 列表请求数据
     * @Param [page, rows, map]
     * @return cc.ahxb.util.DataGridReturn
     **/
    @ResponseBody
    @PostMapping("/DataList")
    public String DataList(int page, int rows, @RequestParam Map<String, Object> map) {
        DataGridReturn datalist = CommonUtil.DataGridData(page, rows, "getButtonList", map);
        return JsonObjectUtils.objectToJson(datalist,new ButtonValueFilter(),new StatusValueFilter(),new OpenModelValueFilter());
    }

    /**
     * @Author CES
     * @Description 跳转新增编辑页面
     * @Param [ID, request]
     * @return java.lang.String
     **/
    @GetMapping(value = "/edit")
    public String edit(@RequestParam(value = "ID", defaultValue = "0") int ID, HttpServletRequest request) {
        request.setAttribute("id", ID);
        SysButton sysButton = SysButton.dao.findById(ID);
        request.setAttribute("sysButton",sysButton);
        return (parentUrl + "edit");
    }

    /**
     * @Author CES
     * @Description 编辑页面数据回显
     * @Param [id]
     * @return cc.ahxb.model.SysButton
     **/
    @GetMapping(value = "/Show")
    @ResponseBody
    public SysButton Show(@RequestParam(value = "id", defaultValue = "0") String id) {
        SysButton model = SysButton.dao.findById(id);
        if (model!=null&&model.getSort().equals(StringUtil.DEFAULT_SORT)){
            model.setSort(null);
        }
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
    public JsonUtil save(SysButton model) {
        JsonUtil result = sysButtonService.save(model);
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
        JsonUtil result = sysButtonService.del(ID);
        return result;
    }

}
