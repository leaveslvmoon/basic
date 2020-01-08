package io.itman.admin.filter;

import com.alibaba.fastjson.serializer.ValueFilter;

/**
 * Describe
 *
 * @author Jxx
 * @date 2018/12/12
 */
public class IsForcedValueFilter implements ValueFilter {

    private String  key="isforced";
//    if (value == 1) {
//        return '<span class="xbtag">启用</span>';
//    } else {
//        return '<span class="xbtag xbtag-info">禁用</span>';
//    }

    @Override
    public Object process(Object object, String name, Object value) {
        if (key!=null&&key.equals(name)) {
            if (value!=null&&value.equals(1)) {
                return "<span class=\'xbtag\'>启用</span>";
            }else{
                return "<span class=\'xbtag xbtag-info\'>禁用</span>";
            }
        }
        return value;
    }
}
