package io.itman.admin.controller;


import io.itman.admin.filter.StatusValueFilter;
import io.itman.admin.service.RoleService;
import io.itman.admin.vo.RoleMenu;
import io.itman.admin.vo.User;
import io.itman.library.util.*;
import io.itman.model.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**

* @Description:    java类作用描述

* @Author:         CES

* @Version:        1.0

*/
@Controller
@RequestMapping("/admin/role")
public class RoleController {
    @Autowired
    RoleService roleService;
    String parentUrl = "/Role/";

    /**
     * @Author CES
     * @Description 跳转列表页面
     * @Param [request]
     * @return java.lang.String
     **/
    //    @Auth(value = 2)
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
        User user = (User) RequestUtil.getSession().getAttribute("user");
        map.put("id",user.getAdministrator().getRoleId());
        DataGridReturn datalist = CommonUtil.DataGridData(page, rows, "getRoleList", map);
        return JsonObjectUtils.objectToJson(datalist,new StatusValueFilter());
    }

    /**
     * @Author CES
     * @Description 跳转新增、编辑页面
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
     * @Description 编辑页面回显
     * @Param [id]
     * @return cc.ahxb.model.SysRole
     **/
    @GetMapping(value = "/Show")
    @ResponseBody
    public SysRole Show(@RequestParam(value = "id", defaultValue = "0") String id) {
        SysRole model = SysRole.dao.findById(id);
        return model;
    }


    /**
     * @Author CES
     * @Description 编辑页面角色 数据回显
     * @Param []
     * @return java.util.List<java.util.Map>
     **/
    @PostMapping(value = "/RoleMenu")
    @ResponseBody
    public List<Map> RoleMenu() {
        List<Map> modelList = MyDb.find("select id as value , name as text  from sys_menu");
        return modelList;
    }

    /**
     * @Author CES
     * @Description 新增修改保存动作
     * @Param [model]
     * @return cc.ahxb.util.JsonUtil
     **/
    @PostMapping(value = "/save")
    @ResponseBody
    public JsonUtil save(SysRole model) {
        JsonUtil result = new JsonUtil();
        if (model.getId() == 0) {
            User user = (User)RequestUtil.getSession().getAttribute("user");
            model.setCreateBy(user.getAdministrator().getRoleId());
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
        JsonUtil result = roleService.del(ID);
        return result;
    }

    /**
     * @Author CES
     * @Description 跳转分配权限页面
     * @Param [ID, request]
     * @return java.lang.String
     **/
    @GetMapping(value = "/permissions")
    public String permissions(String ID, HttpServletRequest request) {
        roleService.goPermissions(ID, request);
        return (parentUrl + "permissions");
    }

    /**
     * @Author CES
     * @Description 新增修改保存角色权限
     * @Param [request, roleMenu]
     * @return cc.ahxb.util.JsonUtil
     **/
    @PostMapping(value = "/saveRoleMenu")
    @ResponseBody
    public JsonUtil saveRoleMenu(HttpServletRequest request, RoleMenu roleMenu) {
        //取roleId
        int roleId = Integer.parseInt(request.getParameter("ID"));
        JsonUtil result = roleService.result(roleId, roleMenu);
        return result;
    }

}
