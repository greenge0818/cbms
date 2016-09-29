package com.prcsteel.platform.order.service.impl;

import javax.annotation.Resource;

import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.order.service.SysConfigService;
import com.prcsteel.platform.acl.service.SysSettingService;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.acl.model.model.SysSetting;

/**
 * 
 * @author zhoukun
 */
@Service("sysConfigService")
public class SysConfigServiceImpl implements SysConfigService {

	@Resource
	SysSettingService sysSettingService;
	
	@Override
	public int getInt(String key) {
		String val = getString(key);
		try{
			return Integer.parseInt(val);
		}catch(Exception e){
			throw new BusinessException(Constant.EXCEPTIONCODE_SYSTEM, "Bad system config.Key:" + key);
		}
	}

	@Override
	public double getDouble(String key) {
		String val = getString(key);
		try{
			return Double.parseDouble(val);
		}catch(Exception e){
			throw new BusinessException(Constant.EXCEPTIONCODE_SYSTEM, "Bad system config.Key:" + key);
		}
	}

	@Override
	public String getString(String key) {
		SysSetting set = sysSettingService.queryBySettingType(key);
		if(set == null){
			throw new BusinessException(Constant.EXCEPTIONCODE_SYSTEM, "System config missing.Key:" + key);
		}
		return set.getSettingValue();
	}

	@Override
	public long getLong(String key) {
		String val = getString(key);
		try{
			return Long.parseLong(val);
		}catch(Exception e){
			throw new BusinessException(Constant.EXCEPTIONCODE_SYSTEM, "Bad system config.Key:" + key);
		}
	}

}
