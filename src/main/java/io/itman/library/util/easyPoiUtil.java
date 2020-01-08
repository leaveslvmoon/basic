package io.itman.library.util;

import cn.afterturn.easypoi.entity.vo.MapExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class easyPoiUtil {

    /*
     * @Author CES
     * @Description //一级标题导出
     * @Param [title, entity, sheetName, modelMap, request, response]
     * @return void
     **/
    public static void export(String title, List<ExcelExportEntity> entity, List<Record>recordList, String sheetName, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response){
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for(Record record:recordList){
            Map<String, Object> map = record.getColumns();
            list.add(map);
        }
        ExportParams params = new ExportParams( title,sheetName);
        modelMap.put(MapExcelConstants.MAP_LIST, list);
        modelMap.put(MapExcelConstants.ENTITY_LIST, entity);
        modelMap.put(MapExcelConstants.PARAMS, params);
        modelMap.put(MapExcelConstants.FILE_NAME, "EasypoiMapExcelViewTest");
        PoiBaseView.render(modelMap, request, response, MapExcelConstants.EASYPOI_MAP_EXCEL_VIEW);
    }

    /*
     * @Author CES
     * @Description //二级标题导出
     * @Param [title, secondTitle, entity, recordList, sheetName, modelMap, request, response]
     * @return void
     **/
    public static void export(String title,String secondTitle, List<ExcelExportEntity> entity, List<Record>recordList, String sheetName, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response){
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for(Record record:recordList){
            Map<String, Object> map = record.getColumns();
            list.add(map);
        }
        ExportParams params = new ExportParams( title,  secondTitle,  sheetName);
        modelMap.put(MapExcelConstants.MAP_LIST, list);
        modelMap.put(MapExcelConstants.ENTITY_LIST, entity);
        modelMap.put(MapExcelConstants.PARAMS, params);
        modelMap.put(MapExcelConstants.FILE_NAME, "EasypoiMapExcelViewTest");
        PoiBaseView.render(modelMap, request, response, MapExcelConstants.EASYPOI_MAP_EXCEL_VIEW);
    }
    
    /*
     * @Author CES
     * @Description //按sal导出
     * @Param [title, secondTitle, entity, sql, sheetName, modelMap, request, response]
     * @return void
     **/
    public static void export(String title,String secondTitle, List<ExcelExportEntity> entity, String sql, String sheetName, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response){
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Record> recordList = Db.find(sql);
        for(Record record:recordList){
            Map<String, Object> map = record.getColumns();
            list.add(map);
        }
        ExportParams params = new ExportParams( title,  secondTitle,  sheetName);
        modelMap.put(MapExcelConstants.MAP_LIST, list);
        modelMap.put(MapExcelConstants.ENTITY_LIST, entity);
        modelMap.put(MapExcelConstants.PARAMS, params);
        modelMap.put(MapExcelConstants.FILE_NAME, "EasypoiMapExcelViewTest");
        PoiBaseView.render(modelMap, request, response, MapExcelConstants.EASYPOI_MAP_EXCEL_VIEW);
    }




    public  void  downloadByPoiBaseView(ModelMap modelMap, HttpServletRequest request,
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

}
