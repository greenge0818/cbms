package com.prcsteel.platform.account.service;

import com.prcsteel.platform.account.model.enums.GlobalIdModule;

/**
 * 
 * @author zhoukun
 */
public interface GlobalIdService {

	Long getId(GlobalIdModule module);
	
}
