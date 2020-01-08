package io.itman.library.payutil.wechat;

import java.util.Calendar;
import java.util.Date;

public class WechatModel {

    private Double money;
    private String body;
    private String no;
    private String tradeType = "APP";
    private String timeExpire;             //订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010
    private String deviceInfo;          //设备终端Id


    private String refundNo;        //退款单号
    private Double refundMoney;      //退款金额
    private String refundReason;    //退款原因

    public WechatModel(){}

    // 统一下单实例化
    public WechatModel(Double money,String body,String no,String deviceInfo) {
        this.money = money;
        this.body = body;
        this.no = no;
        this.deviceInfo = deviceInfo;
    }

    // 退款实例化
    public WechatModel(Double money,String no,String refundNo,Double refundMoney,String refundReason) {
        this.money = money;
        this.no = no;
        this.refundNo = refundNo;
        this.refundMoney = refundMoney;
        this.refundReason = refundReason;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getTimeExpire() {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR,24);
        timeExpire = calendar.getTime().toString();
        return timeExpire;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public Double getRefundMoney() {
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
}
