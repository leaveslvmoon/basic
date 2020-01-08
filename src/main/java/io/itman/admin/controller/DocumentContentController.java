package io.itman.admin.controller;


import io.itman.admin.filter.StatusValueFilter;
import io.itman.admin.service.DocumentContentService;
import io.itman.library.util.*;
import io.itman.library.vo.Tree;
import io.itman.model.DocumentCategories;
import io.itman.model.DocumentContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**

* @Description:    文档编辑、查看管理

* @Author:         CES

* @Version:        1.0

*/
@Controller
@RequestMapping("/admin/DocumentContent")
public class DocumentContentController {

    String parenturl = "/DocumentContent/";

    @Autowired
    DocumentContentService documentContentService;

    /**
     * @Author CES
     * @Description 跳转到列表页面
     * @Param [request, CategoriesID]
     * @return java.lang.String
     **/
    @GetMapping(value = "/index")
    public String contentList(HttpServletRequest request,String CategoriesID) {
        request.setAttribute("CategoriesID",CategoriesID);
        return (parenturl + "index");
    }

    /**
     * @Author CES
     * @Description 返回页面请求数据
     * @Param [page, rows, map, request]
     * @return cc.ahxb.util.DataGridReturn
     **/
    @ResponseBody
    @PostMapping("/DataList")
    public String DataList(int page, int rows, @RequestParam Map<String, Object> map, HttpServletRequest request) {
        if(null!=map.get("CategoriesID")){
            int CategoriesID = Integer.parseInt(map.get("CategoriesID").toString());
            List<DocumentCategories> documentCategoriesList = DocumentCategories.dao.find("select * from document_categories where FIND_IN_SET(parentId,getDocumentCcategories("+CategoriesID+"))");
            String types ="";
            for(DocumentCategories documentCategories : documentCategoriesList){
                types += ","+documentCategories.getId();
            }
            types = map.get("CategoriesID").toString()+types;
            map.put("types",types);
        }
        DataGridReturn datalist = CommonUtil.DataGridData(page, rows, "getDocumentContentList", map);
        return  JsonObjectUtils.objectToJson(datalist,new StatusValueFilter());
    }

    /**
     * @Author CES
     * @Description 返回 内容分类 树形结构数据
     * @Param
     * @return java.util.List<java.lang.Object>
     **/
    @RequestMapping(value = "/getClassTree")
    @ResponseBody
    public List<Object> getMenuTree(){
        TreeUtil modelTree = new TreeUtil();
        List<Object> commodityclassList = new ArrayList<Object>();
        List<Tree> list = documentContentService.buildTree();
        commodityclassList = modelTree.modelList(list);
        return commodityclassList;
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
        List<Map> list = TreeUtil.treeDataList(map,"getDocumentCategoriesList",1);
        return list;
    }

    /*
     * @Author CES
     * @Description 跳转新增编辑页面
     * @Param [ID, request]
     * @return java.lang.String
     **/
    @GetMapping(value = "/edit")
    public String edit(@RequestParam(value = "ID", defaultValue = "0") int ID, HttpServletRequest request) {
        if(ID!=0){
            DocumentContent model = DocumentContent.dao.findById(ID);
            request.setAttribute("contents",model.getContents());
        }
        request.setAttribute("id", ID);
        return (parenturl + "edit");
    }

    /**
     * @Author CES
     * @Description 编辑页面数据回显
     * @Param [id]
     * @return cc.ahxb.model.Content
     **/
    @GetMapping(value = "/Show")
    @ResponseBody
    public DocumentContent Show(@RequestParam(value = "id", defaultValue = "0") String id) {
        DocumentContent model = DocumentContent.dao.findById(id);
        if (model!=null&&model.getSort().equals(StringUtil.DEFAULT_SORT)){
            model.setSort(null);
        }
        return model;
    }

    /**
     * @Author CES
     * @Description 新增修改保存动作
     * @Param [request, model]
     * @return cc.ahxb.util.JsonUtil
     **/
    @PostMapping(value = "/save")
    @ResponseBody
    public JsonUtil save(HttpServletRequest request, DocumentContent model) {
        JsonUtil result = documentContentService.save(request,model);
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
        JsonUtil result = documentContentService.del(ID);
        return result;
    }

    /*
     * @Author CES
     * @Description 跳转新增编辑页面
     * @Param [ID, request]
     * @return java.lang.String
     **/
    @GetMapping(value = "/detail")
    public String detail(@RequestParam(value = "ID", defaultValue = "0") int ID, HttpServletRequest request) {
        if(ID!=0){
            DocumentContent model = DocumentContent.dao.findById(ID);
            request.setAttribute("contents",model.getContents());
        }
        request.setAttribute("id", ID);
        return (parenturl + "detail");
    }

}
