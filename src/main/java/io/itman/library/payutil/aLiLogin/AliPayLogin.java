package io.itman.library.payutil.aLiLogin;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 支付宝登录
 *
 * @author oliver
 * @since 2017-12-20
 */
public class AliPayLogin {

    private static final String apiName = "com.alipay.account.auth";
    private static final String APP_ID = "2018090561232445";
    private static final String P_ID = "2088231578995564";
    private static final String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCDxQzDrpPUOLhJcDJ/MwJhrxng6U9UILAPrqzR12i1jLt+/AsBx4mmMKp1klpJzIWqhrWAF5ZOuvj7OdTky4aEvC/wtrp/P6MgecI8W4f2dv78jRYkAeaVS6fBsOdfuxXES8ZOuZ/W47XSn2EKIW5AnaPeb1nUENd34acMHx82pUKRX3x3cuV0IOG2z+e0PwdqCCKU2mKv4WHoW5vVjNU5CBPJedliEWJtYaSqeQgRAJ9jE0jET4g0QHQ4gN2eNlUwDfmpU3LowEgTn8baJh48Zrjz+qjozUIzp6rJjqR22wg5H4600B5xMvLpdyuN1a5oH68Dw2Cb4bYzXuqJejAlAgMBAAECggEAd0L9zo5PliDQCwfVbHMKkMcesQX5mrcLIuaB3FsOlmMNwFz2yWGEIDqjzU3tEiZeRH0wAQNYM2Hev+MT4RCka7a33TJFka5TQq1xM5fUcyo4lvRLOcPOowmaiCBJCcgWZ1f94Hr5RFFM25kNB1JCq5ECI+y8bMfhfMtrmfkBEz8oZ0GcxOdlYyfj8WHcl4UaPszH/V0650DKonBAXFsX5QxS85N4kJ58ejivDfZg71IXs+30p2+A84lctpadvaNP3jPksTc1FoBMT0P5Y7NpxQ0Wvyidb2Qs5ivjoq/aL06X1w6+br0NIM56FxVtHV0PQJkEss649tMFXavdIAAYcQKBgQDSvLlBVM7CdWSTh+o/+hRZJLxUDpOGMKadk7peo6qofn9UPH//v69MFdfOt2PNrzz2f08CGtfv6G+6QKthv/NFPNaL5N2iguq+qzeZqTzElhB9BfgHJqRkE1sEKnK/9XpeXYJo+9+yeHUYwwFv365Kjsvg/+UzeBTD5cFewpKXKwKBgQCgElccxniE792/poZwScqegnKRhf7NbIRsCACJ4Gb7RKiDVgzojK/jlZQM2mysGzZy4X9+71TtncohdntMTmaixPOSIwM+ZKvnAyNg9zI/WwEFwYH/yuNURGlEEg0lVVXKjDmrNuVJkyXVtO/5kzal9fvKPDN54vWDy/YhS+Wt7wKBgBxOc0feP2OJZzgV4pYzXjZXI+DakGBO+qJh7H+31j2JuEq1UbAlrEM5D+LZaVOAfVzS3ub2ehjkjNVkyGB56xBayqrB1C93DFcOIorGW75ANfjFK3qyaxRyMSCPHxi/vW8del3lrzeGzJvF3dSMJBopsiMH3F6ce5WQr/ethQk/AoGAAzO6uGGkAYzG09Gh+NcRDZCweAbFLRjRoVObpyZtCPO7pzzubyx5TEUuu/8Wk27L7QpjwYdKHKK+a2LyXnlnBjSIZtD6fKII6ksexw7K24eXgqT3U8WcPJBNxsXRq4CEgdPCR5GGjq054YeIEWcHfdRo4AfKQ9LtdGB9IPditdMCgYEAyQFJRyq7BgGuMMbkNxSghv4JWpvII3KRU2ywbQ26VYTSRTWfrtt1cz/Bwx7M3QW0Ok9SMvX0VcRpPiKGJ/jVGybMkNfeB6JQFq65Doi9FHNjp0WALBgmIzbwL5DvXq0oWXA+e09JeExm6J/nS+Dg11KwzF2mroBVVAEunwXYAAo=";
    private static final String CHARSET = "UTF-8";
    private static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjSfi39+wGe+FzI/sZxL/jTiMxugx495uVbfo4rRjwzV6x4oynGiU0vO+rvmxRWpfgEWqsnm4tBbIk2BuQ8Fc8K8PrzMeWCqo8gskxQfuc1XU51NVjCfp04hK9r7T/GFi6hNUv7gfQyaQQrt42ODSIay7vU55rMd20Ot+TtyVMln+xki5KhW2cPAvGmz7xavPugPQgBwG9aube83JCbu6yF9bimgd/eI4L8IQ3I8GyXiY1K9DC0JRvrvelVTDyJ2tBtYJBAf3JpU2iVy7zfjib5HJZ98/cE06663gzJiRknB+EweKjtjzlRALkA46hKrnYV2vZVCVUxN8c5RaqOKDRQIDAQAB";
    private static final String SIGN_TYPE = "RSA2";
    private static final String notify_utl = "http://xnr.ahceshi.com/api/order/alipay_notify";
    private static final String server_url = "https://openapi.alipay.com/gateway.do";

    /**
     * 返回APP的调用信息
     *
     * @param targetId
     * @return
     * @throws AlipayApiException
     * @throws UnsupportedEncodingException
     */
    public static String appCallInfo(String targetId) throws AlipayApiException, UnsupportedEncodingException {
        SortedMap<String, String> map = new TreeMap<>();
        map.put("apiname", apiName);
        map.put("method", "alipay.open.auth.sdk.code.get");
        map.put("app_id", APP_ID);
        map.put("app_name", "mc");
        map.put("biz_type", "openservice");
        map.put("pid", P_ID);
        map.put("product_id", "APP_FAST_LOGIN");
        map.put("scope", "kuaijie");
        map.put("target_id", targetId);
        map.put("auth_type", "AUTHACCOUNT");
        map.put("sign_type", "RSA");
        String signStr = AlipaySignature.getSignContent(map);
        String sign = AlipaySignature.rsaSign(signStr, APP_PRIVATE_KEY, CHARSET);
        return signStr + "&sign=" + URLEncoder.encode(sign, CHARSET);
    }

    public static String getALiPayUserId(String authCode) throws AlipayApiException {
        //获得初始化的AliPayClient
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, "RSA2");
        //创建API对应的request类
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setGrantType("authorization_code");
        request.setCode(authCode);
        //通过aliPayClient调用API，获得对应的response类
        AlipaySystemOauthTokenResponse response = alipayClient.execute(request);
        String aLiPayUserId = response.getUserId();
        return aLiPayUserId;
    }

    public static Map getALiPayUserInfo(String authCode) throws AlipayApiException {
        //获得初始化的AliPayClient
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, "RSA2");
        //创建API对应的request类
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setGrantType("authorization_code");
        request.setCode(authCode);
        //通过aliPayClient调用API，获得对应的response类
        AlipaySystemOauthTokenResponse response = alipayClient.execute(request);
        //根据token获取用户信息
        AlipayUserInfoShareRequest request1 = new AlipayUserInfoShareRequest();
        AlipayUserInfoShareResponse userInfo = alipayClient.execute(request1, response.getAccessToken());
        Map userInfoMap = new HashMap(3);
        userInfoMap.put("headUrl", userInfo.getAvatar());
        userInfoMap.put("nickName", userInfo.getNickName());
        userInfoMap.put("userId", userInfo.getUserId());
        userInfoMap.put("openId", authCode);
        return userInfoMap;
    }
}
