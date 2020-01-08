package io.itman.library.payutil.alipay;

import java.text.DecimalFormat;


/**
 * 支付宝预下单所需参数
 *
 * @author keriezhang
 * @date 2016/10/31
 */
public class AliPayModel {
    /**
     * 主体内容
     */
    private String body;

    /**
     * 标题
     */
    private String title;

    /**
     * 订单号
     */
    private String no;

    /**
     * 过期时间  1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。
     */
    private String timeoutExpress = "24h";

    /**
     * 订单金额  元  保留两位小数
     */
    private Double money;

    /**
     * 退款单号
     */
    private String refundNo;

    /**
     * 退款金额
     */
    private Double refundMoney;

    /**
     * 退款原因
     */
    private String refundReason;

    public AliPayModel(){}

    /**
     * 预支付
     * @param body
     * @param title
     * @param no
     * @param money
     */
    public AliPayModel(String body, String title, String no, Double money) {
        this.body = body;
        this.title = title;
        this.no = no;
        this.money = money;
    }

    /**
     * 退款
     * @param no
     * @param refundNo
     * @param refundMoney
     * @param refundReason
     */
    public AliPayModel(String no, String refundNo, Double refundMoney, String refundReason) {
        this.no = no;
        this.refundNo = refundNo;
        this.refundMoney = refundMoney;
        this.refundReason = refundReason;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getTimeoutExpress() {
        return timeoutExpress;
    }

    public Double getMoney() {
        DecimalFormat df = new DecimalFormat("#.00");
        money = Double.parseDouble(df.format(money));
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Double getRefundMoney() {
        DecimalFormat df = new DecimalFormat("#.00");
        refundMoney = Double.parseDouble(df.format(refundMoney));
        return refundMoney;
    }

    public void setRefundMoney(Double refundMoney) {
        this.refundMoney = refundMoney;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }
}
