package io.itman.library.payutil.alipay;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 支付宝支付工具类
 *
 * @author Administrator
 */
public class AlipayPay {
    private static final String APP_ID = "2018090561232445";
    private static final String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCDxQzDrpPUOLhJcDJ/MwJhrxng6U9UILAPrqzR12i1jLt+/AsBx4mmMKp1klpJzIWqhrWAF5ZOuvj7OdTky4aEvC/wtrp/P6MgecI8W4f2dv78jRYkAeaVS6fBsOdfuxXES8ZOuZ/W47XSn2EKIW5AnaPeb1nUENd34acMHx82pUKRX3x3cuV0IOG2z+e0PwdqCCKU2mKv4WHoW5vVjNU5CBPJedliEWJtYaSqeQgRAJ9jE0jET4g0QHQ4gN2eNlUwDfmpU3LowEgTn8baJh48Zrjz+qjozUIzp6rJjqR22wg5H4600B5xMvLpdyuN1a5oH68Dw2Cb4bYzXuqJejAlAgMBAAECggEAd0L9zo5PliDQCwfVbHMKkMcesQX5mrcLIuaB3FsOlmMNwFz2yWGEIDqjzU3tEiZeRH0wAQNYM2Hev+MT4RCka7a33TJFka5TQq1xM5fUcyo4lvRLOcPOowmaiCBJCcgWZ1f94Hr5RFFM25kNB1JCq5ECI+y8bMfhfMtrmfkBEz8oZ0GcxOdlYyfj8WHcl4UaPszH/V0650DKonBAXFsX5QxS85N4kJ58ejivDfZg71IXs+30p2+A84lctpadvaNP3jPksTc1FoBMT0P5Y7NpxQ0Wvyidb2Qs5ivjoq/aL06X1w6+br0NIM56FxVtHV0PQJkEss649tMFXavdIAAYcQKBgQDSvLlBVM7CdWSTh+o/+hRZJLxUDpOGMKadk7peo6qofn9UPH//v69MFdfOt2PNrzz2f08CGtfv6G+6QKthv/NFPNaL5N2iguq+qzeZqTzElhB9BfgHJqRkE1sEKnK/9XpeXYJo+9+yeHUYwwFv365Kjsvg/+UzeBTD5cFewpKXKwKBgQCgElccxniE792/poZwScqegnKRhf7NbIRsCACJ4Gb7RKiDVgzojK/jlZQM2mysGzZy4X9+71TtncohdntMTmaixPOSIwM+ZKvnAyNg9zI/WwEFwYH/yuNURGlEEg0lVVXKjDmrNuVJkyXVtO/5kzal9fvKPDN54vWDy/YhS+Wt7wKBgBxOc0feP2OJZzgV4pYzXjZXI+DakGBO+qJh7H+31j2JuEq1UbAlrEM5D+LZaVOAfVzS3ub2ehjkjNVkyGB56xBayqrB1C93DFcOIorGW75ANfjFK3qyaxRyMSCPHxi/vW8del3lrzeGzJvF3dSMJBopsiMH3F6ce5WQr/ethQk/AoGAAzO6uGGkAYzG09Gh+NcRDZCweAbFLRjRoVObpyZtCPO7pzzubyx5TEUuu/8Wk27L7QpjwYdKHKK+a2LyXnlnBjSIZtD6fKII6ksexw7K24eXgqT3U8WcPJBNxsXRq4CEgdPCR5GGjq054YeIEWcHfdRo4AfKQ9LtdGB9IPditdMCgYEAyQFJRyq7BgGuMMbkNxSghv4JWpvII3KRU2ywbQ26VYTSRTWfrtt1cz/Bwx7M3QW0Ok9SMvX0VcRpPiKGJ/jVGybMkNfeB6JQFq65Doi9FHNjp0WALBgmIzbwL5DvXq0oWXA+e09JeExm6J/nS+Dg11KwzF2mroBVVAEunwXYAAo=";
    private static final String CHARSET = "UTF-8";
    private static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjSfi39+wGe+FzI/sZxL/jTiMxugx495uVbfo4rRjwzV6x4oynGiU0vO+rvmxRWpfgEWqsnm4tBbIk2BuQ8Fc8K8PrzMeWCqo8gskxQfuc1XU51NVjCfp04hK9r7T/GFi6hNUv7gfQyaQQrt42ODSIay7vU55rMd20Ot+TtyVMln+xki5KhW2cPAvGmz7xavPugPQgBwG9aube83JCbu6yF9bimgd/eI4L8IQ3I8GyXiY1K9DC0JRvrvelVTDyJ2tBtYJBAf3JpU2iVy7zfjib5HJZ98/cE06663gzJiRknB+EweKjtjzlRALkA46hKrnYV2vZVCVUxN8c5RaqOKDRQIDAQAB";
    private static final String SIGN_TYPE = "RSA2";
    private static final String NOTIFY_UTL = "http://xnr.ahceshi.com/";
    private static final String SERVER_URL = "https://openapi.alipay.com/gateway.do";

    /**
     * 预下单返回参数结果
     *
     * @param params
     * @param url    回调地址
     * @return
     */
    public static String initPayInfo(AliPayModel params, String url) {
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(SERVER_URL, APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(params.getBody());
        model.setSubject(params.getTitle());
        model.setOutTradeNo(params.getNo());
        model.setTimeoutExpress(params.getTimeoutExpress());
        model.setTotalAmount(params.getMoney() + "");
        request.setBizModel(model);
        request.setNotifyUrl(NOTIFY_UTL + url);
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            //System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
            return response.getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析支付宝回调参数
     *
     * @param request     HttpServletRequest
     * @param messageCode 是否乱码
     * @return
     * @throws AlipayApiException
     * @throws UnsupportedEncodingException
     */
    public static Map<String, String> analysisParams(HttpServletRequest request, boolean messageCode) throws AlipayApiException, UnsupportedEncodingException {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>(1);
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            if (messageCode) {
                valueStr = new String(valueStr.getBytes("ISO-8859-1"), CHARSET);
            }
            params.put(name, valueStr);
        }
        //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        boolean flag = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, CHARSET, SIGN_TYPE);
        if (flag) {
            return params;
        }
        return null;
    }

    /**
     * 退款执行
     *
     * @param params
     * @return
     */
    public static Map<String, Object> refund(AliPayModel params) {
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(SERVER_URL, APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(params.getNo());
        model.setOutRequestNo(params.getRefundNo());
        model.setRefundAmount(params.getRefundMoney() + "");
        model.setRefundReason(params.getRefundReason());
        request.setBizModel(model);
        // 返回支付宝退款信息
        Map<String, Object> resultMap = new HashMap<>(5);
        AlipayTradeRefundResponse aliPayResponse;
        try {
            aliPayResponse = alipayClient.execute(request);
            if (aliPayResponse.isSuccess()) {
                resultMap.put("out_trade_no", aliPayResponse.getOutTradeNo());
                // 用户的登录id
                resultMap.put("buyer_logon_id", aliPayResponse.getBuyerLogonId());
                // 退款支付时间
                resultMap.put("gmt_refund_pay", aliPayResponse.getGmtRefundPay());
                // 买家在支付宝的用户id
                resultMap.put("buyer_user_id", aliPayResponse.getBuyerUserId());
                resultMap.put("refundType", "success");
            } else {
                resultMap.put("refundType", "fail");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 退款订单查询
     *
     * @param no 订单号
     * @return
     */
    public static Map<String, Object> refundQuery(String no) {
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(SERVER_URL, APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(no);
        // 返回支付宝退款信息
        Map<String, Object> resultMap = new HashMap<>(4);
        AlipayTradeQueryResponse aliPayResponse;
        try {
            aliPayResponse = alipayClient.execute(request);
            if (aliPayResponse.isSuccess()) {
                resultMap.put("out_trade_no", aliPayResponse.getOutTradeNo());
                // 用户的登录id
                resultMap.put("buyer_logon_id", aliPayResponse.getBuyerLogonId());
                // 买家在支付宝的用户id
                resultMap.put("buyer_user_id", aliPayResponse.getBuyerUserId());
                resultMap.put("refundType", "success");
            } else {
                resultMap.put("refundType", "fail");
            }
            System.out.println(JSON.toJSONString(aliPayResponse));
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return resultMap;
    }
}
