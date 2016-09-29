package com.prcsteel.platform.account.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

/**
 * Created by dengxiyan on 2016/1/18.
 */
public class ContactQuery extends PagedQuery{
    private Long parentId;
    private String deptName;
    private Long deptId;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }
}
