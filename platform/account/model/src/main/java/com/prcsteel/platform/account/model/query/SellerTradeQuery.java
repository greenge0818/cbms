package com.prcsteel.platform.account.model.query;


import com.prcsteel.platform.account.model.dto.OrderStatusDto;

import java.util.List;

/**
 * @author zhoucai@prcsteel.com
 * @version V1.0
 * @Description: 销售记录query
 * @date 2016/3/27
 */
public class SellerTradeQuery {
	//卖家名称
    private String sellerName;
    
  //卖家id
    private String sellerId;
    //买家名称
    private String accountName;
    
    //买家Id
    private String accountId;
    
    //部门名称
    private String departName;
    //部门id
    private String departId;
    //项目id
    private Long projectId;
    //状态列表
    private List<String> statusList;
    //状态列表
    private List<String> orderList;

    private List<OrderStatusDto> orderStatusList;

    //结构id
    private Long orgId;
    //查询开始时间
    private String strStartTime;
    //查询结束时间
    private String strEndTime;
    //用户id
    private String userId;
    private Integer start;
    private Integer length;


    //项目名称/修改后项目名称
    private String projectName;
    //前projectId
    private Long beforeId;

    public List<String> getOrderList() {
        return orderList;
    }

    public Long getBeforeId() {
        return beforeId;
    }

    public void setBeforeId(Long beforeId) {
        this.beforeId = beforeId;
    }

    public void setOrderList(List<String> orderList) {
        this.orderList = orderList;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }



    public List<String> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<String> statusList) {
        this.statusList = statusList;
    }


    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }
    
    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getStrStartTime() {
        return strStartTime;
    }

    public void setStrStartTime(String strStartTime) {
        this.strStartTime = strStartTime;
    }

    public String getStrEndTime() {
        return strEndTime;
    }

    public void setStrEndTime(String strEndTime) {
        this.strEndTime = strEndTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public List<OrderStatusDto> getOrderStatusList() {
        return orderStatusList;
    }

    public void setOrderStatusList(List<OrderStatusDto> orderStatusList) {
        this.orderStatusList = orderStatusList;
    }
}


