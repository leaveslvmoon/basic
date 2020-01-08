package io.itman.admin.filter;

import com.alibaba.fastjson.serializer.ValueFilter;

/**
 * Describe
 *
 * @author Jxx
 * @date 2018/12/12
 */
public class SexValueFilter implements ValueFilter {

    private String  key="sex";

    @Override
    public Object process(Object object, String name, Object value) {
        if (key.equals(name)) {
            if (value!=null&&value.equals(0)) {
                return "保密";
            }
            if (value!=null&&value.equals(1)) {
                return "男";
            }
            if (value!=null&&value.equals(2)) {
                return "女";
            }
        }
        return value;
    }
}
