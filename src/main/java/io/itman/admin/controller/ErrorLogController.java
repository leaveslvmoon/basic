package io.itman.admin.controller;


import io.itman.admin.filter.DateValueFilter;
import io.itman.library.util.CommonUtil;
import io.itman.library.util.DataGridReturn;
import io.itman.library.util.JsonObjectUtils;
import io.itman.library.util.JsonUtil;
import io.itman.model.Errorlog;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**

* @Description:    错误日志管理

* @Author:         CES

* @UpdateDate:     2018/11/9 16:22

* @Version:        1.0

*/
@Controller
@RequestMapping("/admin/ErrorLog")
public class ErrorLogController {
    String parentUrl = "/ErrorLog/";

    /*
     * @Author CES
     * @Description 跳转列表页面
     * @Param []
     * @return java.lang.String
     **/
    @GetMapping(value = "index")
    public String showMenu() {
        return (parentUrl + "index");
    }

    /*
     * @Author CES
     * @Description 列表页面请求数据
     * @Param []
     * @return java.lang.String
     **/
    @ResponseBody
    @PostMapping("/DataList")
    public String DataList(int page, int rows, @RequestParam Map<String, Object> map) {
        DataGridReturn datalist = CommonUtil.DataGridData(page, rows, "getErrorLogList", map);
        return JsonObjectUtils.objectToJson(datalist,new DateValueFilter("time"));
    }

    /**
     * 跳转到编辑新增页面
     *
     * @param ID
     * @param request
     * @return
     */
    @GetMapping(value = "/detail")
    public String detail(@RequestParam(value = "ID", defaultValue = "0") int ID, HttpServletRequest request) {
        request.setAttribute("id", ID);
        return (parentUrl + "detail");
    }

    /**
     * 编辑页面回显数据接口
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/Show")
    @ResponseBody
    public Errorlog Show(@RequestParam(value = "id", defaultValue = "0") String id) {
        Errorlog model = Errorlog.dao.findById(id);

        return model;
    }

    /*
     * @Author CES
     * @Description 删除动作
     * @Param []
     * @return java.lang.String
     **/
    @PostMapping(value = "/delete")
    @ResponseBody
    public JsonUtil del(String ID) {
        JsonUtil result = new JsonUtil();
        for (String id : ID.split(",")) {
            Errorlog.dao.deleteById(id);
        }
        result.setMessage("删除成功");
        return result;
    }
}
