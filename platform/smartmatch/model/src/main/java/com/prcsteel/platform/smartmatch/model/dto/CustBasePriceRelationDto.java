package com.prcsteel.platform.smartmatch.model.dto;

import java.util.List;

import com.prcsteel.platform.smartmatch.model.model.CustBasePriceRelation;
/**
 * 基价设置DTO
 * @author caosulin
 *
 */
public class CustBasePriceRelationDto extends CustBasePriceRelation {

	private List<CustBasePriceRelationDetailDto> details = null;

	public List<CustBasePriceRelationDetailDto> getDetails() {
		return details;
	}

	public void setDetails(List<CustBasePriceRelationDetailDto> details) {
		this.details = details;
	}

}
