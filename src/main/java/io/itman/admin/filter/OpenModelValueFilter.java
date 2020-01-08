package io.itman.admin.filter;

import com.alibaba.fastjson.serializer.ValueFilter;

/**
 * Describe
 *
 * @author Jxx
 * @date 2018/12/12
 */
public class OpenModelValueFilter implements ValueFilter {

    private String  key="openMode";


    @Override
    public Object process(Object object, String name, Object value) {
        if (key.equals(name)){
            if (value!=null&&value.equals(0)) {
                return "<span class=\'xbtag\'>无窗口打开</span>";
            } else if(value!=null&&value.equals(1)){
                return "<span class=\'xbtag xbtag-info\'>有交互的窗口</span>";
            }else if(value!=null&&value.equals(2)){
                return "<span class=\'xbtag xbtag-info\'>无交互的窗口</span>";
            }else if(value!=null&&value.equals(3)){
                return "<span class=\'xbtag xbtag-info\'>带确认的无窗口</span>";
            }else if(value!=null&&value.equals(4)){
                return "<span class=\'xbtag xbtag-info\'>新标签打开</span>";
            }else if(value!=null&&value.equals(5)){
                return "<span class=\'xbtag xbtag-info\'>表单提交打开</span>";
            }else if(value!=null&&value.equals(6)){
                return "<span class=\'xbtag xbtag-info\'>地址栏打开</span>";
            }
        }
        return  value;
    }
}
