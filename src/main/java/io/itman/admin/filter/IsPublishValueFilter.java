package io.itman.admin.filter;

import com.alibaba.fastjson.serializer.ValueFilter;

/**
 * Describe
 *
 * @author Jxx
 * @date 2018/12/12
 */
public class IsPublishValueFilter implements ValueFilter {

    private String  key="isPublish";

    @Override
    public Object process(Object object, String name, Object value) {
        if (key.equals(name)) {
            if (value!=null&&value.equals(1)) {
                return "<span class=\'xbtag\'>是</span>";
            } else  {
                return "<span class=\'xbtag xbtag-info\'>否</span>";
            }
        }
        return value;
    }
}
