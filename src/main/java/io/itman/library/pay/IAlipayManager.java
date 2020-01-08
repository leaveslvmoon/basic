package io.itman.library.pay;

import com.alipay.api.AlipayApiException;
import io.itman.library.payutil.alipay.AliPayModel;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Describe
 *
 * @author Jxx
 * @date 2018/12/7
 */
public interface IAlipayManager {
    /**
     * 预下单返回参数结果
     *
     * @param params
     * @param url    回调地址
     * @return
     */
    String initPayInfo(AliPayModel params, String url);
    /**
     * 解析支付宝回调参数
     *
     * @param request     HttpServletRequest
     * @param messageCode 是否乱码
     * @return
     * @throws AlipayApiException
     * @throws UnsupportedEncodingException
     */
    Map<String, String> analysisParams(HttpServletRequest request, boolean messageCode) throws AlipayApiException, UnsupportedEncodingException ;

    /**
     * 退款执行
     *
     * @param params
     * @return
     */
    Map<String, Object> refund(AliPayModel params);
    /**
     * 退款订单查询
     *
     * @param no 订单号
     * @return
     */
    Map<String, Object> refundQuery(String no);

}
