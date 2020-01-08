package io.itman.library.util;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

//Map转化为bean
public class MapToVoUtil {
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public static <T> Object refelctBean(Map map , T t){
        Object obj = null;
        Class clazz = t.getClass();
        try {
            obj = clazz.newInstance();
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if(method.getName().startsWith("set")){
                    String key = method.getName().replace("set", "");
                    key = key.substring(0, 1).toLowerCase().concat(key.substring(1));
                    Object value = map.get(key);
                    if(value==null || value.equals("N/A")) {
                        continue;
                    }
                    Class<?>[]  paramType = method.getParameterTypes();
                    //根据参数类型执行对应的set方法给vo赋值
                    if(paramType[0] == String.class){
                        method.invoke(obj, String.valueOf(value));
                        continue;
                    }else if(paramType[0] == BigDecimal.class){
                        method.invoke(obj, new BigDecimal(value.toString()));
                        continue;
                    }else if(paramType[0] == Double.class){
                        method.invoke(obj, Double.parseDouble(value.toString()));
                        continue;
                    }else if(paramType[0] == Date.class){
                        method.invoke(obj, StringToDate(value.toString()));
                        continue;
                    }else if(paramType[0] == int.class || paramType[0] == Integer.class){
                        method.invoke(obj, Integer.valueOf(value.toString()));
                        continue;
                    }else if(paramType[0] == Boolean.class){
                        method.invoke(obj, Boolean.parseBoolean(value.toString()));
                        continue;
                    }else if(paramType[0] == char.class || paramType[0] == Character.class){
                        method.invoke(obj, value.toString().charAt(0));
                        continue;
                    }
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return obj;
    }

    //字符串转日期
    public static Date  StringToDate(String str){
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    //日期转字符串
    public static String DateToString(Date date){
        String str = null;
        str = sdf.format(date);
        return str;
    }
}