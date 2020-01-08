package io.itman.admin.filter;

import com.alibaba.fastjson.serializer.ValueFilter;

/**
 * Describe
 *
 * @author Jxx
 * @date 2018/12/12
 */
public class PushTargetValueFilter implements ValueFilter {

    private String  key="mobile";

    @Override
    public Object process(Object object, String name, Object value) {
        if (key.equals(name)) {
            if (value!=null&&value!=null) {
                return "<span class=\'xbtag\'>全部用户</span>";
            }else{
                return "<span class=\'xbtag xbtag-info\'>"+ String.valueOf(value) +"</span>";
            }
        }
        return value;
    }
}
