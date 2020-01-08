package io.itman.library.util;

import java.util.List;
import java.util.Map;

/**
 *
 * 用于与前端交互传递的回执，前端已封装字段
 * @date 2017/12/15.
 * ajax  回执  封装类
 */
public class JsonUtil {

  //默认成功
  private boolean flag=true;
  private Integer result=1;
  private String message;
  private Object des;
  private List<Map> list;
  private List<Map> list2;
  private List<Map> list3;

  public List<Map> getList() {
    return list;
  }
  public void setList(List<Map> list) {
    this.list = list;
  }
  public List<Map> getList2() {
    return list2;
  }
  public void setList2(List<Map> list2) {
    this.list2 = list2;
  }
  public List<Map> getList3() {
    return list3;
  }
  public void setList3(List<Map> list3) {
    this.list3 = list3;
  }
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }
  public Integer getResult(){
    return result;
  };
  public void setResult(Integer result) {
    this.result = result;
  }
  public JsonUtil() {
  }
  public JsonUtil(Integer result, String message) {
    this.result = result;
    this.message = message;
  }

  public JsonUtil(boolean flag, String message,Integer result) {
    this.flag = flag;
    this.message = message;
    this.result = result;
  }

  /**restful 返回*/
  public static JsonUtil error(String message){
    return new JsonUtil(false,message,0);
  }
  public  static JsonUtil sucess(String message){
    return new JsonUtil(true,message,1);
  }


  /**restful 返回*/
//  public static JsonUtil error(String msg){
//    return new JsonUtil(false,msg);
//  }
//  public  static JsonUtil sucess(String msg){
//    return new JsonUtil(true,msg);
//  }

  public Object getDes() {
    return des;
  }

  public void setDes(Object des) {
    this.des = des;
  }

  public boolean isFlag() {
    return flag;
  }

  public void setFlag(boolean flag) {
    this.flag = flag;
  }
}
