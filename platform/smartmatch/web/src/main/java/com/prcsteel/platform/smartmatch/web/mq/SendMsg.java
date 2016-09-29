package com.prcsteel.platform.smartmatch.web.mq;

import java.io.Serializable;


/**
 * @author xiaohl
 *
 */
public interface SendMsg {
	/**
	 * @param dto
	 */
	public void send(final Serializable dto);
	/**
	 * @param str
	 */
	public void send(final String str);
	
    void sendWithTrack(String str, String busiCode, int busiId);
    
}
