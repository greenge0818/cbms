package com.prcsteel.platform.kuandao.service;

import com.prcsteel.platform.kuandao.model.dto.KuandaoDepositJournalDto;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBResponseResult;

public interface RefundService {
	
	SPDBResponseResult refundFromBank(KuandaoDepositJournalDto dto);
}
