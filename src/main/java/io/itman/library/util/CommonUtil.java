package io.itman.library.util;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 通用工具类
 * @author 叶子旺
 *
 */
public class CommonUtil <M extends Model > {

    /**
     * easyuiDataGrid返回数据工具类
     * @param page dataGrid传过来的参数
     * @param rows dataGrid传过来的参数
     * @param sqlKey jfinal sql管理的key
     * @param map 前台传过的所有参数（统一封装在map中）
     * @return
     */
    public static DataGridReturn DataGridData(int page, int rows,String sqlKey, Map<String,Object> map){
        SqlPara sqlPara = Db.getSqlPara(sqlKey, Kv.by("map", map));
        Page<Record> p= Db.paginate(page, rows, sqlPara);
        List<Map> list=new ArrayList<>();
        for (Record r:p.getList()) {
            Map m=r.getColumns();
            list.add(m);
        }
        DataGridReturn dgr=new DataGridReturn(p.getTotalRow(), list);
        return dgr;

    }

    /**
     * 生成单号
     * @param prefix  前缀
     * @param identification    订单号标示
     * @return
     */
    public static String getNO(String prefix,String identification) {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(prefix);
        String str = new SimpleDateFormat("yyMMddHHmm").format(new Date());
        stringBuilder.append(str);
        stringBuilder.append(identification);
        return stringBuilder.toString()+"";
    }





}
