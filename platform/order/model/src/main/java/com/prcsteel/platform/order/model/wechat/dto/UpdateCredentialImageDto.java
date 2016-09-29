package com.prcsteel.platform.order.model.wechat.dto;

import java.util.List;

import com.prcsteel.platform.order.model.dto.CredentialImageDto;

/**
 * @author tuxming
 * @date 20160525
 */
public class UpdateCredentialImageDto {
	List<CredentialImageDto> updates;
	List<Long> removes;
	public List<CredentialImageDto> getUpdates() {
		return updates;
	}
	public void setUpdates(List<CredentialImageDto> updates) {
		this.updates = updates;
	}
	public List<Long> getRemoves() {
		return removes;
	}
	public void setRemoves(List<Long> removes) {
		this.removes = removes;
	}
}	
