package io.itman.library.util;



import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * Aes加密
 *
 * @author: Ycq
 * @Date: 2018/9/5
 * @Time: 17:21
 */
public class AesEncryption {
    private static AesEncryption instance = null;

    private AesEncryption() {

    }

    /**
     * 单例模式
     *
     * @return
     */
    public static AesEncryption getInstance() {
        if (instance == null) {
            instance = new AesEncryption();
        }
        return instance;
    }

    /**
     * 加密
     *
     * @param sSrc
     * @param ivParameter
     * @param sKey
     * @return
     * @throws Exception
     */
    public String encrypt(String sSrc, String ivParameter, String sKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = sKey.getBytes();
        SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
        // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        Base64.Encoder encoder = Base64.getEncoder();
        String string =encoder.encodeToString(encrypted);
//        String string = new Base64.Encoder().encode(encrypted);
        /*由于Base64每76个字符换行一次，所以去掉\r与\n，由于安卓没有\r，所以两个分别去除*/
        return string.replaceAll("\r", "").replaceAll("\n", "");
    }

    /**
     * 解密
     *
     * @param sSrc
     * @param ivParameter
     * @param sKey
     * @return
     * @throws Exception
     */
    public String decrypt(String sSrc, String ivParameter, String sKey) throws Exception {
        try {
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            // 先用base64解密
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] encrypted1 = decoder.decode(sSrc);
//            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }

    public String makeKEY(String name, String timeStamp) {
        //String timeStamp = "75D775A6775B33F2175D662FA7AF56";//时间戳的值，从缓存中获取
        //MD5用户名+时间戳值
        String toMD5 = MD5.getMD5(name + timeStamp);
        //KEY+MD5后的值取第2-30（包含）然后转大写
        return ("key" + toMD5.substring(1, 30)).toUpperCase();
    }

    public String makeIV(String passWord, String timeStampId) {
        //String timeStampId = "10";//时间戳ID，从缓存中获取
        //MD5密码密文+时间戳值
        String toMD5 = MD5.getMD5(passWord + timeStampId);
        //iv+MD5后的值取第2-15（包含）然后转大写
        return ("iv" + toMD5.substring(1, 15)).toUpperCase();
    }


    public static void main(String[] args) throws Exception {

        //加密字符串
        String str = "{\"orderId\":64}";
        String enString = AesEncryption.getInstance().encrypt(str, "IV94A98C0DC42B91", "KEY20DF82E8697127948AE87B22B5F33");

        //解密字符串
        str = "v6YMXLSzG7D9PLhwNxkKqw==";
        String deString = AesEncryption.getInstance().decrypt(str, "IV0D7D9F75ECC8FD", "KEY2A1CCAD42E78855BE9683BEDE000A");
        System.out.println("解密后的字串是：" + deString);
        System.out.println("加密后的字串是：" + enString);
    }


}
