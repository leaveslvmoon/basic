package io.itman.admin.controller;


import io.itman.admin.filter.StatusValueFilter;
import io.itman.admin.service.MacWhitelistService;
import io.itman.library.util.CommonUtil;
import io.itman.library.util.DataGridReturn;
import io.itman.library.util.JsonObjectUtils;
import io.itman.library.util.JsonUtil;
import io.itman.model.MacWhitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**

* @Description:    mac白名单管理

* @Author:         CES

* @Version:        1.0

*/
@Controller
@RequestMapping("/admin/macWhitelist")
public class MacWhitelistController {

    @Autowired
    private MacWhitelistService macWhitelistService;

    String parentUrl = "/macWhitelist/";

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
        DataGridReturn datalist = CommonUtil.DataGridData(page, rows, "getMacWhitelist", map);
        return JsonObjectUtils.objectToJson(datalist,new StatusValueFilter());
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
    public MacWhitelist Show(@RequestParam(value = "id", defaultValue = "0") String id) {
        MacWhitelist model = MacWhitelist.dao.findById(id);
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
    public JsonUtil save(MacWhitelist model) {
        JsonUtil result = macWhitelistService.save(model);
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
            MacWhitelist.dao.deleteById(id);
        }
        result.setMessage("删除成功");
        return result;
    }

}
