package io.itman.library.util;


/**
 * Demo class
 *
 * @author keriezhang
 * @date 2016/10/31
 */
public class MakeToken {

    /**
     * 生成接口传输的Token
     * 生成规则:
     * 第一步:用户名+时间戳ID加密后取前30位
     * 第二步:密文密码+时间戳值加密后取后30位
     * 然后第一步+第二步
     *
     * @param phone 用户名或手机号
     * @param pwd   密文密码
     * @param id    时间戳ID
     * @param val   时间戳值
     * @return
     */
    public static String getToken(String phone, String pwd, String id, String val) {
        String name = MD5.getMD5(phone + id).substring(0, 30);
        String last = MD5.getMD5(pwd + val).substring(MD5.getMD5(pwd + val).length() - 30);
        return (name + last).toUpperCase();
    }

    /**
     * AES加密  KEY生成
     * 生成规则:用户名+时间戳的值加密后取第2到第30位,前边拼接"KEY"
     *
     * @param phone 用户名或手机号
     * @param val   时间戳值
     * @return 32位字符串
     */
    public static String getKey(String phone, String val) {
        return "KEY" + MD5.getMD5(phone + val).substring(1, 30).toUpperCase();
    }

    /**
     * AES加密  IV生成
     * 生成规则:密文密码+时间戳ID加密后取第2到第15位,前边拼接"IV"
     *
     * @param pwd 密码
     * @param id  时间戳ID
     * @return 16位字符串
     */
    public static String getIv(String pwd, String id) {
        return "IV" + MD5.getMD5(pwd + id).substring(1, 15).toUpperCase();
    }

    public static void main(String[] args) {
//        System.out.println(getKey("13866668888", "75D775A6775B33F2175D662FA7AF56"));
//        System.out.println(getIv("123456", "10"));
        System.out.println(getToken("18815699357", "623A7CC4E83B681C3B6B6", "b17844f3-19f5-4b47-9569-35d162ebfaa5", "E4A9C831A3149FFA3CB675A37FE79D"));
    }
}
