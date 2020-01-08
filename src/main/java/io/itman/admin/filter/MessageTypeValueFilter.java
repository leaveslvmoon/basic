package io.itman.admin.filter;

import com.alibaba.fastjson.serializer.ValueFilter;

/**
 * Describe
 *
 * @author Jxx
 * @date 2018/12/12
 */
public class MessageTypeValueFilter implements ValueFilter {

    private String  key="messageType";


    @Override
    public Object process(Object object, String name, Object value) {
        if (key.equals(name)) {
            if (value!=null&&value.equals(1)) {
                return "<span class=\'xbtag\'>系统消息</span>";
            } else if (value!=null&&value.equals(2)) {
                return "<span class=\'xbtag xbtag-info\'>交易消息</span>";
            } else if (value!=null&&value.equals(3)){
                return "<span class=\'xbtag xbtag-info\'>其他消息</span>";
            }
        }
        return value;
    }
}
