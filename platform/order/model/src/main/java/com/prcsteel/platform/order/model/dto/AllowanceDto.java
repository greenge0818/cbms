package com.prcsteel.platform.order.model.dto;

import com.prcsteel.platform.order.model.model.Allowance;

/**
 * Created by caochao on 2015/11/20.
 */
public class AllowanceDto extends Allowance {
    private boolean buyerGenerate;
    
    private int departmentCount;

    public int getDepartmentCount() {
		return departmentCount;
	}

	public void setDepartmentCount(int departmentCount) {
		this.departmentCount = departmentCount;
	}

	public boolean isBuyerGenerate() {
        return buyerGenerate;
    }

    public void setBuyerGenerate(boolean buyerGenerate) {
        this.buyerGenerate = buyerGenerate;
    }
}
