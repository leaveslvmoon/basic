package io.itman.admin.controller;


import com.jfinal.plugin.activerecord.Db;
import io.itman.admin.filter.DateValueFilter;
import io.itman.admin.filter.StatusValueFilter;
import io.itman.admin.service.AdministratorService;
import io.itman.admin.vo.User;
import io.itman.library.util.*;
import io.itman.model.SysAdministrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**

* @Description:    用户管理

* @Author:         CES

* @UpdateDate:     2018/11/8 16:27

* @UpdateRemark:   修改代码风格，增加可读性

* @Version:        1.0

*/
@Controller
@RequestMapping("/admin/SysAdministrator")
public class AdministratorController {

    String parentUrl = "/Administrator/";

    @Autowired
    AdministratorService administratorService;

    /**
     * @Author CES
     * @Description 跳转列表页面
     * @Param [request]
     * @return java.lang.String
     **/
    @GetMapping(value = "/index")
    public String index() {
        return (parentUrl + "index");
    }

    /**
     * @Author CES
     * @Description 返回列表请求数据
     * @Param [page, rows, map]
     * @return cc.ahxb.util.DataGridReturn
     **/
    @ResponseBody
    @PostMapping("/DataList")
    public String DataList(int page, int rows, @RequestParam Map<String, Object> map) {
        System.out.println("测试jrebel");
        User user = (User) RequestUtil.getSession().getAttribute("user");
        map.put("id",user.getAdministrator().getId());
        DataGridReturn datalist = CommonUtil.DataGridData(page, rows, "getSysAdministratorList", map);
        return JsonObjectUtils.objectToJson(datalist,new StatusValueFilter(),new DateValueFilter("loginDate"));
    }

    /**
     * @Author CES
     * @Description 跳转新增、修改页面
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
     * @Description 修改页面回显
     * @Param [id]
     * @return cc.ahxb.model.SysAdministrator
     **/
    @GetMapping(value = "/Show")
    @ResponseBody
    public SysAdministrator Show(@RequestParam(value = "id", defaultValue = "0") String id) {
        SysAdministrator model = SysAdministrator.dao.findFirst("SELECT id,userName,'******' as userPsd,realName,mobile,roleId,psdErrorCount,status FROM sys_administrator WHERE id ="+id+"");

        return model;
    }

    /**
     * @Author CES
     * @Description 修改页面返回角色数据
     * @Param []
     * @return java.util.List<java.util.Map>
     **/
    @PostMapping(value = "/SysAdministratorRole")
    @ResponseBody
    public List<Map> administratorRole() {
        List<Map> modelList = MyDb.find("select id as value , name as text  from sys_role where status = 1");
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
    public JsonUtil save(SysAdministrator model) {
        JsonUtil result = administratorService.save(model);
        return result;
    }


    /**
     * @Author CES
     * @Description 删除动作，假删除
     * @Param [ID]
     * @return cc.ahxb.util.JsonUtil
     **/
    @PostMapping(value = "/delete")
    @ResponseBody
    public JsonUtil del(String ID) {
        JsonUtil result = new JsonUtil();
        for (String id : ID.split(",")) {
            Db.update("update sys_administrator set isDelete = 1  where  id  = ?", id);
        }
        result.setMessage("删除成功");
        return result;
    }

}
