package com.prcsteel.platform.order.model.dto;

import java.util.ArrayList;
import java.util.List;

import com.prcsteel.platform.order.model.model.InvoiceOutApply;

public class InvoiceOutApplyDto extends InvoiceOutApply {
	
	private String statusName;

	private List<InvoiceOutApplyDetailDto> detailList = new ArrayList<InvoiceOutApplyDetailDto>();
	
	public void setDetailList(List<InvoiceOutApplyDetailDto> detailList) {
		this.detailList = detailList;
	}

	public void addDetail(InvoiceOutApplyDetailDto detail) {
		this.detailList.add(detail);
	}

	public List<InvoiceOutApplyDetailDto> getDetailList() {
		return detailList;
	}

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

}
