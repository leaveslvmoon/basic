package io.itman.library.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;

import java.util.List;

/**
 * Json 相关工具类
 */
public class JsonObjectUtils {


    /**
     * 将对象转换成json字符串。
     * <p>Title: pojoToJson</p>
     * <p>Description: </p>
     * @param data
     * @return
     */
    public static String objectToJson(Object data) {
        return JSON.toJSONString(data);
    }

    public static String objectToJson(Object data, SerializeFilter serializeFilter) {
        return JSON.toJSONString(data,serializeFilter);
    }

    public static String objectToJson(Object data, SerializeFilter... filters) {
        return JSON.toJSONString(data,filters);
    }

    /**
     * 将json结果集转化为对象
     * 
     * @param jsonData json数据
     * @param beanType 对象中的object类型
     * @return
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        return JSON.parseObject(jsonData,beanType);
    }


    
    /**
     * 将json数据转换成pojo对象list
     * <p>Title: jsonToList</p>
     * <p>Description: </p>
     * @param jsonData
     * @param beanType
     * @return
     */
    public static <T>List<T> jsonToList(String jsonData, Class<T> beanType) {
    	return JSON.parseArray(jsonData,beanType);
    }
    
}
