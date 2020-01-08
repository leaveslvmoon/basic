package io.itman.admin.controller;


import io.itman.admin.filter.DateValueFilter;
import io.itman.admin.service.ApiContentService;
import io.itman.library.util.*;
import io.itman.library.vo.Tree;
import io.itman.model.ApiContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**

* @Description:    API内容管理

* @Author:         CES

* @Version:        1.0

*/
@Controller
@RequestMapping("/admin/ApiContent")
public class ApiContentController {

    String parentUrl = "/ApiContent/";

    @Autowired
    ApiContentService apiContentService;

    /**
     * @Author CES
     * @Description 跳转api左侧树
     * @Param []
     * @return java.lang.String
     **/
    @GetMapping(value = "/ApiContentList")
    public String index() {
        return (parentUrl + "ApiContentList");
    }

    /**
     * @Author CES
     * @Description 跳转列表页面
     * @Param [request, CategoriesID]
     * @return java.lang.String
     **/
    @GetMapping(value = "/index")
    public String contentList(HttpServletRequest request,String CategoriesID) {
        request.setAttribute("CategoriesID",CategoriesID);
        return (parentUrl + "index");
    }

    /**
     * @Author CES
     * @Description 列表页面回显数据
     * @Param [page, rows, map]
     * @return cc.ahxb.util.DataGridReturn
     **/
    @ResponseBody
    @PostMapping("/DataList")
    public String DataList(int page, int rows, @RequestParam Map<String, Object> map) {
        DataGridReturn datalist = CommonUtil.DataGridData(page, rows, "getApiContentList", map);
        return JsonObjectUtils.objectToJson(datalist,new DateValueFilter("updateTime"));
    }

    /**
     * @Author CES
     * @Description 跳转新增、编辑页面
     * @Param [ID, request]
     * @return java.lang.String
     **/
    @GetMapping(value = "/edit")
    public String edit(@RequestParam(value = "ID", defaultValue = "0") int ID, HttpServletRequest request) {
        if(ID!=0){
            ApiContent apiContent = ApiContent.dao.findById(ID);
            request.setAttribute("apiContent",apiContent);
        }
        request.setAttribute("id", ID);
        return (parentUrl + "edit");
    }

    /**
     * @Author CES
     * @Description 编辑页面数据回显
     * @Param [id]
     * @return cc.ahxb.model.ApiContent
     **/
    @GetMapping(value = "/Show")
    @ResponseBody
    public ApiContent Show(@RequestParam(value = "id", defaultValue = "0") String id) {
        ApiContent model = ApiContent.dao.findById(id);

        return model;
    }

    /**
     * @Author CES
     * @Description 树形数据请求接口
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
     * @Author CES
     * @Description 返回树形结构数据
     * @Param [request, response]
     * @return java.util.List<java.lang.Object>
     **/
    @RequestMapping(value = "/getClassTree")
    @ResponseBody
    public List<Object> getMenuTree() throws Exception {
        TreeUtil modelTree = new TreeUtil();
        List<Tree> list = apiContentService.buildTree();
        List<Object> commodityclassList = new ArrayList<Object>();
        commodityclassList = modelTree.modelList(list);
        return commodityclassList;
    }


    /**
     * @Author CES
     * @Description 新增修改保存动作
     * @Param [model]
     * @return cc.ahxb.util.JsonUtil
     **/
    @PostMapping(value = "/save")
    @ResponseBody
    public JsonUtil save(ApiContent model) {
        JsonUtil result = apiContentService.save(model);
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
            ApiContent.dao.deleteById(id);
        }
        result.setMessage("删除成功");
        return result;
    }

}
