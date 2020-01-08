package io.itman.admin.controller;


import io.itman.admin.filter.StatusValueFilter;
import io.itman.admin.service.ApiCategoriesService;
import io.itman.library.util.*;
import io.itman.model.ApiCategories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/APICategories")
public class ApiCategoriesController {
    String parentUrl ="/APICategories/";

    @Autowired
    private ApiCategoriesService apiCategoriesService;

    /**
     * 跳转到列表页面
     * @return
     */
    @GetMapping(value = "/index")
    public String index() {
        return (parentUrl + "index");
    }

    /**
     * grid请求接口
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("/DataList")
    public String DataList(@RequestParam Map<String, Object> map) {
        DataGridReturn dgr = apiCategoriesService.DataList(map);
        return JsonObjectUtils.objectToJson(dgr,new StatusValueFilter());
    }

    /**
     * @Author CES
     * @Description 树形结构
     * @Param [map]
     * @return java.util.List<java.util.Map>
     **/
    @ResponseBody
    @PostMapping("/treeDataList")
    public List<Map> treeDataList(@RequestParam Map<String, Object> map) {
        List<Map> list = TreeUtil.treeDataList(map,"getAPICategoriesList",0);
        return list;
    }

    /**
     * 跳转到编辑新增页面
     * @param ID
     * @param request
     * @return
     */
    @GetMapping(value = "/edit")
    public String edit(@RequestParam(value = "ID", defaultValue = "0") int ID, HttpServletRequest request) {
        request.setAttribute("id", ID);
        return (parentUrl + "edit");
    }

    /**
     * 编辑页面回显数据接口
     * @param id
     * @return
     */
    @GetMapping(value = "/Show")
    @ResponseBody
    public ApiCategories Show(@RequestParam(value = "id", defaultValue = "0") String id) {
        ApiCategories model = ApiCategories.dao.findById(id);
        if (model!=null&&model.getSort().equals(StringUtil.DEFAULT_SORT)){
            model.setSort(null);
        }
        return model;
    }

    /**
     * 保存修改操作
     * @param model
     * @return
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public JsonUtil save(ApiCategories model) {
        JsonUtil result = apiCategoriesService.save(model);
        return result;
    }

    /**
     * 删除操作
     * @param ID
     * @return
     */
    @PostMapping(value = "/delete")
    @ResponseBody
    public JsonUtil del(String ID) {
        JsonUtil result = apiCategoriesService.del(ID);
        return result;
    }
}
