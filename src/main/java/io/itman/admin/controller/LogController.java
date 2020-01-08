package io.itman.admin.controller;


import cn.afterturn.easypoi.entity.vo.MapExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import io.itman.admin.filter.DateValueFilter;
import io.itman.admin.filter.RoleTypeValueFilter;
import io.itman.library.util.CommonUtil;
import io.itman.library.util.DataGridReturn;
import io.itman.library.util.JsonObjectUtils;
import io.itman.library.util.JsonUtil;
import io.itman.model.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
日志管理
 */
@Controller
@RequestMapping("/admin/log")
public class LogController {
    String parentUrl = "/Log/";

    @GetMapping(value = "index")
    public String showMenu() {
        return (parentUrl+"index");
    }

    @ResponseBody
    @PostMapping("/DataList")
    public String DataList(int page, int rows, @RequestParam Map<String, Object> map) {
        DataGridReturn datalist = CommonUtil.DataGridData(page, rows, "getLogList", map);
        return JsonObjectUtils.objectToJson(datalist,new DateValueFilter("datetime"),new RoleTypeValueFilter());
    }

    @PostMapping(value = "/delete")
    @ResponseBody
    public JsonUtil del(String ID) {
        JsonUtil result = new JsonUtil();
        for (String id : ID.split(",")) {
            Log.dao.deleteById(id);
        }
        result.setMessage("删除成功");
        return result;
    }

    /**如果上面的方法不行,可以使用下面的用法
     * 同样的效果,只不过是直接问输出了,不经过view了
     * @param
     * @param request
     * @param response
     */

    @RequestMapping("load")
    public void downloadByPoiBaseView(ModelMap modelMap, HttpServletRequest request,
                                      HttpServletResponse response) {
        List<ExcelExportEntity> entity = new ArrayList<ExcelExportEntity>();
        entity.add(new ExcelExportEntity("菜单名称", "name",30));
        entity.add(new ExcelExportEntity("父级分类ID", "parentId"));
        entity.add(new ExcelExportEntity("菜单访问URL", "url"));
        entity.add(new ExcelExportEntity("排序", "sort"));
        entity.add(new ExcelExportEntity("菜单图标", "icon",80));

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Record> recordList = Db.find("select * from sys_menu");
        for(Record record:recordList){
            Map<String, Object> map = record.getColumns();
            list.add(map);
        }
        ExportParams params = new ExportParams( "一级标题",  "二级标题",  "表名");
        modelMap.put(MapExcelConstants.MAP_LIST, list);
        modelMap.put(MapExcelConstants.ENTITY_LIST, entity);
        modelMap.put(MapExcelConstants.PARAMS, params);
        modelMap.put(MapExcelConstants.FILE_NAME, "EasypoiMapExcelViewTest");
        PoiBaseView.render(modelMap, request, response, MapExcelConstants.EASYPOI_MAP_EXCEL_VIEW);

    }



    @RequestMapping("download")
    public String download(ModelMap modelMap) {
        List<ExcelExportEntity> entity = new ArrayList<ExcelExportEntity>();
        ExcelExportEntity excelentity = new ExcelExportEntity("姓名", "name");
        excelentity.setNeedMerge(true);
        entity.add(excelentity);
        entity.add(new ExcelExportEntity("性别", "sex"));
        excelentity = new ExcelExportEntity(null, "students");
        entity.add(excelentity);

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        for (int i = 0; i < 10; i++) {
            map = new HashMap<String, Object>();
            map.put("name", "1" + i);
            map.put("sex", "2" + i);

            List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
            tempList.add(map);
            tempList.add(map);
            map.put("students", tempList);

            list.add(map);
        }

        ExportParams params = new ExportParams("2412312", "测试", ExcelType.XSSF);
        params.setFreezeCol(2);
        modelMap.put(MapExcelConstants.MAP_LIST, list);
        modelMap.put(MapExcelConstants.ENTITY_LIST, entity);
        modelMap.put(MapExcelConstants.PARAMS, params);
        modelMap.put(MapExcelConstants.FILE_NAME, "EasypoiMapExcelViewTest");
        return MapExcelConstants.EASYPOI_MAP_EXCEL_VIEW;

    }




}
