package io.itman.library.push;

import io.itman.library.push.android.AndroidBroadcast;
import io.itman.library.push.ios.IOSBroadcast;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Administrator
 */
@Component
public interface IPushManager {



	/**
	 * 发送 Android手机 默认类型的通知，默认行为为点击打开App
	 * @param ticker  通知栏提示文字
	 * @param title   通知标题
	 * @param text    通知文字描述
	 * @throws Exception
	 */
	 void sendAndroidBroadcastNotification(String ticker, String title, String text) throws Exception ;


	/**
	 * 发送 Android手机 消息透传
	 * @param custom  必须为json 字符串
	 * @throws Exception
	 */
	 void sendAndroidBroadcastMessage(String custom) throws Exception ;

	/**
	 * 发送针对Android用户的全局广播
	 * @param androidBroadcast
	 * @throws Exception
	 */
	 void sendAndroidBroadcast(AndroidBroadcast androidBroadcast) throws Exception ;


	/**
	 *  Android手机 单发推送消息
	 * @param ticker  通知栏提示文字
	 * @param title 通知标题
	 * @param text  通知文字描述
	 * @param deviceToken  用户设备识别号
	 * @throws Exception
	 */
	 void sendAndroidUnicast(String ticker, String title, String text, String deviceToken) throws Exception ;

	/**
	 * 批量发送 Android手机 推送，根据deviceToken列表
	 * @param ticker  通知栏提示文字
	 * @param title 通知标题
	 * @param text  通知文字描述
	 * @param deviceTokens  用户设备识别号列表
	 */
	 void sendAndroidUnicastByList(String ticker, String title, String text, List<String> deviceTokens) ;



	/**
	 * 发送针对IOS 设备的广播
	 * @param content 显示内容alert
	 * @param customizedField 自定义字段
	 * @param contentAvailable  是否静默发送
	 * @throws Exception
	 */
	 void sendIOSBroadcast(String content, String customizedField, boolean contentAvailable) throws Exception ;

	/**
	 * 发送针对IOS 设备的广播
	 * @param content  显示内容alert
	 * @param customizedfield  自定义字段
	 * @throws Exception
	 */
	 void sendIOSBroadcast(String content, String customizedfield) throws Exception ;


	/**
	 * 发送针对IOS 设备的静默广播
	 * @param content  显示内容alert
	 * @throws Exception
	 */
	 void sendIOSBroadcastContentAvailable(String content) throws Exception;

	/**
	 * 发送针对IOS 设备的广播
	 * @param broadcast
	 * @throws Exception
	 */
	 void sendIOSBroadcast(IOSBroadcast broadcast) throws Exception ;

	/**
	 * 发送针对IOS 设备的广播
	 * @param content
	 * @throws Exception
	 */
	 void sendIOSBroadcast(String content) throws Exception ;


	/**
	 * 单点发送IOS设备的推送
	 * @param content
	 * @param deviceToken
	 * @throws Exception
	 */
	 void sendIOSUnicast(String content, String deviceToken) throws Exception ;



}
