package com.prcsteel.platform.kuandao.service;

import java.io.Serializable;

public interface SendMsg {
	/**
	 * @param dto
	 */
	public void send(final Serializable dto,final String queue);
	/**
	 * @param str
	 */
	public void send(final String str,final String queue);
	
	/**
	 * @param dto
	 */
	public void send(final Serializable dto);
	/**
	 * @param str
	 */
	public void send(final String str);
}
