package io.itman.admin.filter;

import com.alibaba.fastjson.serializer.ValueFilter;
import io.itman.model.SysButton;

import java.util.Map;

/**
 * Describe
 *
 * @author Jxx
 * @date 2018/12/12
 */
public class ButtonValueFilter implements ValueFilter {

    private String  key="icon";


    @Override
    public Object process(Object object, String name, Object value) {

        if (object instanceof Map &&name.equals(key)){
            Map object1 = (Map) object;
            return  "<div type=\'button\' class=\'xbButton\'><i class=\'fa "
                    +String.valueOf(value)
                    +"\'></i> <span>"
                    +String.valueOf(object1.get("name"))
                    +"</span></div>";
        }
        if (object instanceof SysButton &&name.equals(key)){
            SysButton object1 = (SysButton) object;
            return  "<div type=\'button\' class=\'xbButton\'><i class=\'fa "
                    +String.valueOf(value)
                    +"\'></i> <span>"
                    +String.valueOf(object1.getName())
                    +"</span></div>";
        }
        return  value;
    }
}
