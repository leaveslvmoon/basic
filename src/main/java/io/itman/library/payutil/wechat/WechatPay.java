package io.itman.library.payutil.wechat;


import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPayConstants;
import io.itman.library.payutil.wechat.base.WXPay;
import io.itman.library.payutil.wechat.base.WXPayConfig;
import io.itman.library.payutil.wechat.base.WXPayUtil;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class WechatPay {

    /**
     * 支付异步回调地址
     */
    private static final String notify_url = "http://xnr.ahceshi.com/";
    /**
     * 退款异步回调地址
     */
    private static final String refund_notify_url = "http://xnr.ahceshi.com/api/order/wechat_refund_notify";

    /**
     * 微信统一下单
     * @param model
     * @param request
     * @param url 回调地址
     * @return
     * @throws Exception
     */
    public static Map<String, String> initPayInfo(WechatModel model, HttpServletRequest request, String url) throws Exception {
        WXPayConfig config = new MyConfig();
        WXPay wxpay = new WXPay(config);
        String total_fee = BigDecimal.valueOf(model.getMoney()).multiply(BigDecimal.valueOf(100))
                .setScale(0, BigDecimal.ROUND_HALF_UP).toString();
        Map<String, String> data = new HashMap<String, String>(7);
        data.put("body", model.getBody());
        data.put("out_trade_no", model.getNo());
        data.put("total_fee", total_fee);
        data.put("device_info", model.getDeviceInfo());
        data.put("spbill_create_ip", PayUtil.getRemoteAddrIp(request));
        data.put("notify_url", notify_url + url);
        data.put("trade_type", model.getTradeType());

        try {
            Map<String, String> resp = wxpay.unifiedOrder(data);

            // 二次签名，移动端不用再次签名
            Map<String, String> result = new HashMap<>(7);
            result.put("timestamp", WXPayUtil.getCurrentTimestamp() + "");
            result.put("appid", config.getAppID());
            result.put("partnerid", config.getMchID());
            result.put("package", "Sign=WXPay");
            result.put("noncestr", WXPayUtil.generateNonceStr());
            result.put("prepayid", resp.get("prepay_id"));
            String sign = WXPayUtil.generateSignature(result, config.getKey(), WXPayConstants.SignType.HMACSHA256);
            result.put("sign", sign);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询订单信息
     *
     * @param no 订单号
     * @return
     * @throws Exception
     */
    public static Map<String, String> query(String no) throws Exception {
        MyConfig config = new MyConfig();
        WXPay wxpay = new WXPay(config);

        Map<String, String> data = new HashMap<String, String>(1);
        data.put("out_trade_no", no);

        try {
            Map<String, String> resp = wxpay.orderQuery(data);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 申请退款
     *
     * @param model
     * @return
     * @throws Exception
     */
    public static Map<String, String> refund(WechatModel model) throws Exception {
        MyConfig config = new MyConfig();
        WXPay wxpay = new WXPay(config);

        String total_fee = BigDecimal.valueOf(model.getMoney()).multiply(BigDecimal.valueOf(100))
                .setScale(0, BigDecimal.ROUND_HALF_UP).toString();
        String refund_fee = BigDecimal.valueOf(model.getRefundMoney()).multiply(BigDecimal.valueOf(100))
                .setScale(0, BigDecimal.ROUND_HALF_UP).toString();
        Map<String, String> data = new HashMap<String, String>();
        data.put("out_trade_no", model.getNo());
        data.put("out_refund_no", model.getRefundNo());
        data.put("total_fee", total_fee);
        data.put("refund_fee", refund_fee);
        data.put("refund_desc", model.getRefundReason());
        try {
            Map<String, String> resp = wxpay.refund(data);
            System.out.println("refund ----------------------- " + JSON.toJSONString(resp));
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 退款订单查询
     *
     * @param no 订单号
     * @return
     * @throws Exception
     */
    public static Map<String, String> refundQuery(String no) throws Exception {
        MyConfig config = new MyConfig();
        WXPay wxpay = new WXPay(config);

        Map<String, String> data = new HashMap<String, String>(1);
        data.put("out_trade_no", no);

        try {
            Map<String, String> resp = wxpay.refundQuery(data);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 验证签名，支付成功通知时调用
     *
     * @param notifyMap
     * @return
     * @throws Exception
     */
    public static boolean checkSign(Map<String, String> notifyMap) throws Exception {
        //String notifyData = "...."; // 支付结果通知的xml格式数据

        MyConfig config = new MyConfig();
        WXPay wxpay = new WXPay(config);

        //Map<String, String> notifyMap = WXPayUtil.xmlToMap(notifyData);  // 转换成map

        if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
            // 签名正确
            // 进行处理。
            // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
            return true;
        } else {
            // 签名错误，如果数据里没有sign字段，也认为是签名错误
            return false;
        }
    }
}