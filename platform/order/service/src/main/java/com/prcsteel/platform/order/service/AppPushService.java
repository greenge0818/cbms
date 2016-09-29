package com.prcsteel.platform.order.service;

/**
 * 
 * @author Green.Ge 10/09
 *
 */
public interface AppPushService {
	public void sendPushNoticfication(String alias, String deviceType,String title, String content) ;
	public void sendPushMessage(String alias,String content);
}
