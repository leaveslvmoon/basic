package io.itman.library.util;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 后台向前台返回JSON，用于easyui的datagrid
 * @author 叶子旺
 *
 */
@Getter
@Setter
public class DataGridReturn {
    /**
     * 总记录数
     */
    private int total;

    /**
     *  每行记录
     */
    private List rows;

    public DataGridReturn(int total, List rows) {
        this.total = total;
        this.rows = rows;
    }



}