package com.prcsteel.platform.account.model.query;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * @author zhoucai@prcsteel.com
 * @version V1.0
 * @Description: 信用额度分组查询
 * @date 2016/4/7
 */
public class CustGroupingQuery {

    //分组   id
    private Long groupingInforId;

    //分组Name
    private String groupingInforName;
    //公司˾id
    private Long accountId;
    //公司名称
    private String accountName;
    //公司信用额度
    private BigDecimal creditLimit;
    //公司可用信用额度
    private BigDecimal creditLimitUsed;

    //公司待审核已申请信用额度
    private BigDecimal creditLimitAudit;

    //分组待审核已申请信用额度
    private BigDecimal limitAudit;

    //分组信用额度
    private BigDecimal limit;
    //分组可用信用额度
    private BigDecimal limitUsed;
    //状态
    private String status;

    //用户id
    private long userId;

    //用户名称
    private String userName;

    //添加公司，公司申请额度列表
    private List<BigDecimal> creditLimitList;


    //添加公司，公司申请id列表
    private List<Long> accountIdList;

    //添加公司，是否自动还款
    private List<Integer> isShowAutoList;

    //添加公司，公司申请公司名称列表
    private List<String> departNameList;

    //添加公司，公司申请公司部门id列表
    private List<Long> departIdList;


    //添加公司，公司申请公司部名称列表
    private List<String> accountNameList;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Integer> getIsShowAutoList() {
        return isShowAutoList;
    }

    public void setIsShowAutoList(List<Integer> isShowAutoList) {
        this.isShowAutoList = isShowAutoList;
    }

    public List<String> getDepartNameList() {
        return departNameList;
    }

    public void setDepartNameList(List<String> departNameList) {
        this.departNameList = departNameList;
    }

    public List<Long> getDepartIdList() {
        return departIdList;
    }

    public void setDepartIdList(List<Long> departIdList) {
        this.departIdList = departIdList;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public BigDecimal getCreditLimitAudit() {
        return creditLimitAudit;
    }

    public void setCreditLimitAudit(BigDecimal creditLimitAudit) {
        this.creditLimitAudit = creditLimitAudit;
    }

    public BigDecimal getLimitAudit() {
        return limitAudit;
    }

    public void setLimitAudit(BigDecimal limitAudit) {
        this.limitAudit = limitAudit;
    }

    public Long getGroupingInforId() {
        return groupingInforId;
    }

    public String getGroupingInforName() {
        return groupingInforName;
    }

    public void setGroupingInforName(String groupingInforName) {
        this.groupingInforName = groupingInforName;
    }

    public void setGroupingInforId(Long groupingInforId) {
        this.groupingInforId = groupingInforId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getCreditLimitUsed() {
        return creditLimitUsed;
    }

    public void setCreditLimitUsed(BigDecimal creditLimitUsed) {
        this.creditLimitUsed = creditLimitUsed;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public BigDecimal getLimitUsed() {
        return limitUsed;
    }

    public void setLimitUsed(BigDecimal limitUsed) {
        this.limitUsed = limitUsed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<BigDecimal> getCreditLimitList() {
        return creditLimitList;
    }

    public void setCreditLimitList(List<BigDecimal> creditLimitList) {
        this.creditLimitList = creditLimitList;
    }

    public List<Long> getAccountIdList() {
        return accountIdList;
    }

    public void setAccountIdList(List<Long> accountIdList) {
        this.accountIdList = accountIdList;
    }

    public List<String> getAccountNameList() {
        return accountNameList;
    }

    public void setAccountNameList(List<String> accountNameList) {
        this.accountNameList = accountNameList;
    }
}