package com.prcsteel.platform.kuandao.service;

import com.prcsteel.platform.kuandao.model.dto.KuandaoGuaranteedPaymentsDto;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBResponseResult;

public interface MCDBService {
	public SPDBResponseResult paymentGuaranteed(KuandaoGuaranteedPaymentsDto dto);
}
