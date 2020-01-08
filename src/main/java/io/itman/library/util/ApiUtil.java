package io.itman.library.util;

import com.alibaba.fastjson.JSON;

import java.util.Map;

/**
 * Created with IDEA
 *
 * @author: Ycq
 * @Date: 2018/9/5
 * @Time: 17:14
 */
public class ApiUtil {
    private AesEncryption aesOperator = AesEncryption.getInstance();

    /**
     * 默认成功
     */
    private String message = "恭喜您,操作成功";
    /**
     * 返回数据
     */
    private String data;
    /**
     * 1成功 0失败 -1需要登录
     */
    private int result = 1;
    /**
     * 0明文 1密文
     */
    private int isAes = 0;

    public ApiUtil() {
    }

    public ApiUtil(String message, String data, int result, int isAes) {
        this.message = message;
        this.data = data;
        this.result = result;
        this.isAes = isAes;
    }

    /**
     * 默认加密状态
     *
     * @param message
     * @param data
     * @param result
     */
    public ApiUtil(String message, String data, int result, Map keyAndIV) {
        try {
            this.message = message;
            if (keyAndIV == null) {
                this.message = "数据无法加密，请联系管理员";
                this.data = null;
                this.result = 0;
                this.isAes = 0;
            } else {
                String returnJson;
                if (data != null) {
                    returnJson = JSON.toJSON(data).toString();
                } else {
                    returnJson = null;
                }
                this.data = aesOperator.encrypt(returnJson, keyAndIV.get("iv").toString(), keyAndIV.get("key").toString());
                this.isAes = 1;
            }
            this.result = result;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getIsaes() {
        return isAes;
    }

    public void setIsaes(int isAes) {
        this.isAes = isAes;
    }
}
