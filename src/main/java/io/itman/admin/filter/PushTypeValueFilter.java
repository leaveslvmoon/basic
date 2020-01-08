package io.itman.admin.filter;

import com.alibaba.fastjson.serializer.ValueFilter;

/**
 * Describe
 *
 * @author Jxx
 * @date 2018/12/12
 */
public class PushTypeValueFilter implements ValueFilter {

    private String  key="pushType";


    @Override
    public Object process(Object object, String name, Object value) {
        if (key.equals(name)) {
            if (value!=null&&value.equals(1)) {
                return "<span class=\'xbtag\'>广播</span>";
            } else if (value!=null&&value.equals(2))  {
                return "<span class=\'xbtag xbtag-info\'>单发</span>";
            }
        }
        return value;
    }
}
