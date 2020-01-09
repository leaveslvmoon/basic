package io.itman.admin.controller;

import io.itman.admin.filter.DateValueFilter;
import io.itman.admin.filter.RoleTypeValueFilter;
import io.itman.admin.vo.User;
import io.itman.library.util.*;
import io.itman.model.SysRole;
import io.itman.model.SysSqlmodel;
import jdk.jshell.JShell;
import jdk.jshell.SnippetEvent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/SqlModel")
public class SqlModelController {
    String parentUrl = "/SqlModel/";

    @GetMapping(value = "/index")
    public String index() {
        return (parentUrl + "index");
    }

    @ResponseBody
    @PostMapping("/DataList")
    public String DataList(int page, int rows, @RequestParam Map<String, Object> map) {


        DataGridReturn datalist = CommonUtil.DataGridData(page, rows, "getSqlModelList", map);
        return JsonObjectUtils.objectToJson(datalist);
    }

    /**
     * @Author
     * @Description 跳转新增、修改页面
     **/
    @GetMapping(value = "/edit")
    public String edit(@RequestParam(value = "ID", defaultValue = "0") int ID, HttpServletRequest request) {
        request.setAttribute("id", ID);
        return (parentUrl + "edit");
    }

    /**
     * @Author
     * @Description 编辑页面回显
     **/
    @GetMapping(value = "/Show")
    @ResponseBody
    public SysSqlmodel Show(@RequestParam(value = "id", defaultValue = "0") String id) {
        SysSqlmodel model = SysSqlmodel.dao.findById(id);
        return model;
    }

    /**
     * @Author
     * @Description 新增修改保存动作
     **/
    @PostMapping(value = "/save")
    @ResponseBody
    public JsonUtil save(SysSqlmodel model) {
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

    @GetMapping(value = "/runcode")
    @ResponseBody
    public String runcode(String code,HttpServletRequest request) {


        JShell jShell=JShell.builder().build();
        jShell.addToClasspath("/Users/yeziwang/.m2/repository/com/jfinal/jfinal/4.8/jfinal-4.8.jar");
        jShell.addToClasspath("/Users/yeziwang/Documents/G-workspace/basic/target/classes");
        List<SnippetEvent> events = jShell.eval(code);
        for (SnippetEvent e : events) {
            System.out.println(e.status());
            //System.out.println(e.exception().getMessage());
        }
        return "";
    }

}
