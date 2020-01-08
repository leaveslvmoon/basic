package io.itman.admin.controller;


import io.itman.admin.filter.StatusValueFilter;
import io.itman.admin.service.MenuService;
import io.itman.admin.vo.SysmenubuttonList;
import io.itman.library.util.DataGridReturn;
import io.itman.library.util.JsonObjectUtils;
import io.itman.library.util.JsonUtil;
import io.itman.library.util.StringUtil;
import io.itman.model.SysMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**

* @Description:    java类作用描述

* @Author:         CES

* @UpdateDate:     2018/11/8 16:05

* @UpdateRemark:   修改代码风格，增加可读性

* @Version:        1.0

*/

@Controller
@RequestMapping("/admin/menu")
public class SysMenuController {
    @Autowired
    private MenuService menuService;

    String parentUrl = "/menu/";

    /**
     * 跳转到列表页面
     */
    @GetMapping(value = "/index")
    public String index(HttpServletRequest request) {
        return (parentUrl + "index");
    }

    /**
     * grid请求接口
     *
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("/DataList")
    public String DataList(@RequestParam Map<String, Object> map) {
        DataGridReturn datalist = menuService.DataList(map);
        return JsonObjectUtils.objectToJson(datalist,new StatusValueFilter());
    }

    /**
     * 跳转到编辑新增页面
     *
     * @param ID
     * @param request
     * @return
     */
    @GetMapping(value = "/edit")
    public String edit(@RequestParam(value = "ID", defaultValue = "0") int ID, HttpServletRequest request) {
        menuService.edit(ID, request);
        return (parentUrl + "edit");
    }

    /**
     * 编辑页面回显数据接口
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/Show")
    @ResponseBody
    public SysMenu Show(@RequestParam(value = "id", defaultValue = "0") String id) {
        SysMenu model = SysMenu.dao.findById(id);
        if (model!=null&&model.getSort().equals(StringUtil.DEFAULT_SORT)){
            model.setSort(null);
        }
        return model;
    }

    /**
     * 保存修改操作
     *
     * @param model
     * @return
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public JsonUtil save(SysMenu model, SysmenubuttonList sysmenubuttonList) {
        JsonUtil result = menuService.save(model, sysmenubuttonList);
        return result;
    }

    /**
     * 删除操作
     *
     * @param ID
     * @return
     */
    @PostMapping(value = "/delete")
    @ResponseBody
    public JsonUtil del(String ID) {
        JsonUtil result = menuService.del(ID);
        return result;
    }

    /**
     * 显示树形结构
     *
     * @return
     */
    @RequestMapping(value = "/getMenuTree")
    @ResponseBody
    public List<Object> getMenuTree() {
        List<Object> menuList = menuService.getMenuTree();
        return menuList;
    }

    /**
     * 跳转增加按钮页面
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/MenuButton")
    public String MenuButton(String id, HttpServletRequest request) {
        menuService.MenuButton(id, request);
        return (parentUrl + "menuButton");
    }

}
