package io.itman.admin.filter;

import com.alibaba.fastjson.serializer.ValueFilter;

/**
 * Describe
 *
 * @author Jxx
 * @date 2018/12/12
 */
public class OSTypeValueFilter implements ValueFilter {

    private String  key="types";


    @Override
    public Object process(Object object, String name, Object value) {
        if (key.equals(name)) {
            if (value!=null&&value.equals(1)) {
                return "<span class=\'xbtag\'>iOS</span>";
            } else if (value.equals(2))  {
                return "<span class=\'xbtag xbtag-info\'>安卓</span>";
            }
        }
        return value;
    }
}
