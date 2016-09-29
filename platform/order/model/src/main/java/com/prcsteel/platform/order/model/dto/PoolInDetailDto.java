package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.prcsteel.platform.order.model.model.PoolInDetail;

/**
 * Created by lcw on 2015/8/2.
 */
public class PoolInDetailDto extends PoolInDetail {
	private String accountName;
	
	private String code;
	
	private String contractCode;
	
	private Date creationTime;

    private Long sellerId;
    
    private String sellerName;
    
    private BigDecimal allowanceWeight;
    
    private BigDecimal allowanceAmount;

	private Integer totalDepartment;

	private Long departmentId;

	private String departmentName;
    
    public BigDecimal getAllowanceWeight() {
		return allowanceWeight;
	}

	public void setAllowanceWeight(BigDecimal allowanceWeight) {
		this.allowanceWeight = allowanceWeight;
	}

	public BigDecimal getAllowanceAmount() {
		return allowanceAmount;
	}

	public void setAllowanceAmount(BigDecimal allowanceAmount) {
		this.allowanceAmount = allowanceAmount;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	
	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

	public Integer getTotalDepartment() {
		return totalDepartment;
	}

	public void setTotalDepartment(Integer totalDepartment) {
		this.totalDepartment = totalDepartment;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
}
