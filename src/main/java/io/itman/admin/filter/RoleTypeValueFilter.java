package io.itman.admin.filter;

import com.alibaba.fastjson.serializer.ValueFilter;

/**
 * Describe
 *
 * @author Jxx
 * @date 2018/12/12
 */
public class RoleTypeValueFilter implements ValueFilter {

    private String  key="roleType";

    @Override
    public Object process(Object object, String name, Object value) {
        if (key.equals(name)) {
            if (value!=null&&value.equals(1)) {
                return "<span class=\'xbtag\'>管理员</span>";
            } else  {
                return "<span class=\'xbtag xbtag-info\'>会员</span>";
            }
        }
        return value;
    }
}
