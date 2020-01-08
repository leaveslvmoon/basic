package io.itman.admin.filter;

import com.alibaba.fastjson.serializer.ValueFilter;

/**
 * Describe
 *
 * @author Jxx
 * @date 2018/12/12
 */
public class AdvertisTypeValueFilter implements ValueFilter {

    private String  key="type";

    @Override
    public Object process(Object object, String name, Object value) {
        if (key.equals(name)) {
            if (value!=null&&value.equals(0)) {
                return "<span class=\'xbtag\'>分类</span>";
            } else if (value!=null&&value.equals(1)) {
                return "<span class=\'xbtag xbtag-info\'>商品详情</span>";
            } else if (value!=null&&value.equals(2)) {
                return "<span class=\'xbtag xbtag-info\'>H5页面</span>";
            } else if (value!=null&&value.equals(3)) {
                return "<span class=\'xbtag xbtag-info\'>关键字</span>";
            }
        }
        return value;
    }
}
