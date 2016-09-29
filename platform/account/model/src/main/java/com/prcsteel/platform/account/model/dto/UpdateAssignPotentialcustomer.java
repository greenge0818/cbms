package com.prcsteel.platform.account.model.dto;

import java.util.List;

import com.prcsteel.platform.account.model.model.AccountContact;

/**
 * @author tuxianming
 * @date 20160711
 */
public class UpdateAssignPotentialcustomer {
	List<AccountContact> updates;
	List<Long> removes;
	public List<AccountContact> getUpdates() {
		return updates;
	}
	public void setUpdates(List<AccountContact> updates) {
		this.updates = updates;
	}
	public List<Long> getRemoves() {
		return removes;
	}
	public void setRemoves(List<Long> removes) {
		this.removes = removes;
	}
	
}	
