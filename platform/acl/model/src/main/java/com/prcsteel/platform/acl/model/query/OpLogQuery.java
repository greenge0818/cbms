package com.prcsteel.platform.acl.model.query;

import com.prcsteel.platform.common.query.PagedQuery;
import com.prcsteel.platform.common.enums.QueryOrderDirection;

import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author zhoukun
 */
public class OpLogQuery extends PagedQuery {

	/**
	 * 将前端的排序字段与DB中的字段对应起来
	 * @author prcsteel
	 *
	 */
	public enum OrderField{
		created("created"),operationLevelDesc("operationLevelValue");
		String fieldName;
		OrderField(String fieldName){
			this.fieldName = fieldName;
		}
		@Override
		public String toString(){
			return fieldName;
		}
	}
	
	String userName;
	
	String opType;
	
	String opLevel;
	
	Date beginDate;
	
	Date endDate;
	
	OrderField orderBy = OrderField.created;
	
	QueryOrderDirection orderDirection = QueryOrderDirection.asc;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOpType() {
		return opType;
	}

	public void setOpType(String opType) {
		this.opType = opType;
	}

	public String getOpLevel() {
		return opLevel;
	}

	public void setOpLevel(String opLevel) {
		this.opLevel = opLevel;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public OrderField getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(OrderField orderBy) {
		this.orderBy = orderBy;
	}

	public QueryOrderDirection getOrderDirection() {
		return orderDirection;
	}

	public void setOrderDirection(QueryOrderDirection orderDirection) {
		this.orderDirection = orderDirection;
	}

	public void setEndDate(Date endDate) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(endDate);
		ca.add(Calendar.DAY_OF_MONTH, 1);
		ca.add(Calendar.MILLISECOND, -1);
		this.endDate = ca.getTime();
	}
	
	
}
