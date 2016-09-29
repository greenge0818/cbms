package com.prcsteel.platform.account.model.dto;

import java.math.BigDecimal;

/**
 * @author zhoucai@prcsteel.com
 * @version V1.0
 * @Description: 客户销售记录Dto
 * @date 2016/3/25
 */
public class SellerTradeDto {
	//件数
	private Long id;
	//卖家名称
    private String sellerName;
    //卖家id
    private String sellerId;
	//买家名称
    private String accountName;
    //买家客户id
    private String accountId;
    //订单生成日期
    private String creatTime;
	//部门名称
    private String departName;

	//卖家部门名称
	private String sellerDepartName;
    //部门id
    private String departId;
    //状态
    private String status;
	//件数
    private Long quantity;
    //总金额
    private BigDecimal sumAmount;
	//总重量
    private BigDecimal sumWeight;
	//实提总总量
    private BigDecimal weight;
	//实提总金额
    private BigDecimal amount;
	//订单号
    private String orderId;
    //操作员名称
    private String userName;
    
    private Integer start;
    private Integer length;
	//项目名称
	private String projectName;
	//项目Id
	private Long projectId;

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getSellerDepartName() {
		return sellerDepartName;
	}

	public void setSellerDepartName(String sellerDepartName) {
		this.sellerDepartName = sellerDepartName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the sellerId
	 */
	public String getSellerId() {
		return sellerId;
	}

	/**
	 * @param sellerId the sellerId to set
	 */
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	/**
	 * @return the accountId
	 */
	public String getAccountId() {
		return accountId;
	}

	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	/**
	 * @return the quantity
	 */
	public Long getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the sumAmount
	 */
	public BigDecimal getSumAmount() {
		return sumAmount;
	}

	/**
	 * @param sumAmount the sumAmount to set
	 */
	public void setSumAmount(BigDecimal sumAmount) {
		this.sumAmount = sumAmount;
	}

	/**
	 * @return the weight
	 */
	public BigDecimal getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

   

    

    public BigDecimal getSumWeight() {
        return sumWeight;
    }

    public void setSumWeight(BigDecimal sumWeight) {
        this.sumWeight = sumWeight;
    } 

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

   

	/**
	 * @return the accountName
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * @param accountName the accountName to set
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	/**
	 * @return the creatTime
	 */
	public String getCreatTime() {
		return creatTime;
	}

	/**
	 * @param creatTime the creatTime to set
	 */
	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}

	/**
	 * @return the departName
	 */
	public String getDepartName() {
		return departName;
	}

	/**
	 * @param departName the departName to set
	 */
	public void setDepartName(String departName) {
		this.departName = departName;
	}

	/**
	 * @return the departId
	 */
	public String getDepartId() {
		return departId;
	}

	/**
	 * @param departId the departId to set
	 */
	public void setDepartId(String departId) {
		this.departId = departId;
	}
}


