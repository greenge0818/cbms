package com.prcsteel.platform.order.service.impl;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.order.service.AppPushService;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.DeviceType;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.IosAlert;
import cn.jpush.api.push.model.notification.Notification;

/**
 * Created by Green on 15-9-15.
 */
@Service("AppPushService")
public class AppPushServiceImpl implements AppPushService{
	protected static final Logger logger = LoggerFactory.getLogger(AppPushServiceImpl.class);
	@Value("${app.appKey}")
	private String appKey;
	@Value("${app.masterSecret}")
	private String masterSecret;
	static JPushClient jpushClient;
	
	private JPushClient getJPushClient(){
		if(jpushClient==null){
			jpushClient=new JPushClient(masterSecret, appKey, 3);
		}
		return jpushClient;
	}
	
	public void sendPushNoticfication(String alias, String deviceType,String title, String content) {
		// For push, all you need do is to build PushPayload object.
		PushPayload payload_android = buildPushObjectAndroidAliasTitleAlert(alias, title, content);
		if(DeviceType.Android.value().equals(deviceType)){
			try {
//				PushResult result = 
				getJPushClient().sendPush(payload_android);
//				logger.info("Got android result - " + result);
			} catch (APIConnectionException e) {
				logger.error("android Connection error. Should retry later. ", e);
			} catch (APIRequestException e) {
				logger.error("Error response from JPush server. Should review and fix it. ", e);
	            logger.info("HTTP Status: " + e.getStatus());
	            logger.info("Error Code: " + e.getErrorCode());
	            logger.info("Error Message: " + e.getErrorMessage());
	            logger.info("Msg ID: " + e.getMsgId());
			}
		}else if(DeviceType.IOS.value().equals(deviceType)){
			IosAlert alert = IosAlert.newBuilder().setTitleAndBody(title, content).setActionLocKey("PLAY").build();
			try {
//				PushResult result = 
//				getJPushClient().sendIosNotificationWithAlias(alert, new HashMap<String, String>(),
//						alias);
				PushPayload payload = buildPushObjectIosAliasAlert(alert,alias);
				PushResult result = getJPushClient().sendPush(payload);
//				PushPayload payload = buildPushObjectIosAliasAlert(alias, content);
//				getJPushClient().sendPush(payload);
//				logger.info("Got IOS result - " + result);
			} catch (APIConnectionException e) {
				logger.error("IOS Connection error. Should retry later. ", e);
			} catch (APIRequestException e) {
				logger.error("Error response from JPush server. Should review and fix it. ", e);
	            logger.info("HTTP Status: " + e.getStatus());
	            logger.info("Error Code: " + e.getErrorCode());
	            logger.info("Error Message: " + e.getErrorMessage());
	            logger.info("Msg ID: " + e.getMsgId());
			}
		}else{
			logger.error("未找到匹配发送的设备类型");
		}
	}

	public void sendPushMessage(String alias,String content){
		JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);
		PushPayload payload = buildPushObjectIosAudienceMoreMessageWithExtras(alias,content);
		try {
//			PushResult result = 
					jpushClient.sendPush(payload);
//			logger.info("Got android result - " + result);
		} catch (APIConnectionException e) {
			logger.error("Connection error. Should retry later. ", e);
		} catch (APIRequestException e) {
			
		}
	}
	public PushPayload buildPushObjectAndroidAliasTitleAlert(String alias, String title, String content) {
		return PushPayload.newBuilder().setPlatform(Platform.android()).setAudience(Audience.alias(alias))
				.setNotification(Notification.android(content, title, null)).build();
	}

	public PushPayload buildPushObjectIosAliasAlert(IosAlert alert,String alias) {
		return PushPayload.newBuilder().setPlatform(Platform.ios()).setAudience(Audience.alias(alias))
				.setNotification(Notification.ios(alert, new HashMap<String,String>())).setOptions(Options.newBuilder().setApnsProduction(true).build()).build();
	}

	public PushPayload buildPushObjectIosAudienceMoreMessageWithExtras(String alise,String content) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.newBuilder()
//                        .addAudienceTarget(AudienceTarget.tag("tag1", "tag2"))
                        .addAudienceTarget(AudienceTarget.alias(alise))
                        .build())
                .setMessage(Message.newBuilder()
                        .setMsgContent(content)
//                        .addExtra("from", "JPush")
                        .build())
                .build();
    }
}
