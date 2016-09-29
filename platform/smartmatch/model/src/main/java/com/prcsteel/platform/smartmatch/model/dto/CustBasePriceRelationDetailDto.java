package com.prcsteel.platform.smartmatch.model.dto;

import com.prcsteel.platform.smartmatch.model.model.CustBasePriceRelationDetail;
/**
 * 基价设置明细DTO
 * @author caosulin
 *
 */
public class CustBasePriceRelationDetailDto extends CustBasePriceRelationDetail {
	 
	private String spec;

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}
	 
}
