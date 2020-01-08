package io.itman.admin.filter;

import com.alibaba.fastjson.serializer.ValueFilter;
import io.itman.library.util.DateUtil;

import java.util.Date;

/**
 * Describe
 *
 * @author Jxx
 * @date 2018/12/12
 */
public class DateValueFilter implements ValueFilter {

    private String  key="date";

    public DateValueFilter(String key) {
        this.key=key;
    }

    @Override
    public Object process(Object object, String name, Object value) {
        if (key.equals(name)){
            if (value instanceof Date){
                return DateUtil.parseDateToStr((Date) value);
            }
        }
        return value;
    }
}
