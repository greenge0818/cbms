package com.prcsteel.platform.account.model.query;


/**
 * @author zhoucai@prcsteel.com
 * @version V1.0
 * @Description: 项目查询
 * @date 2016/3/30
 */
public class OrderRelationQuery {
	//项目名称
    private String projectName;
    
  //项目id
    private Long projectId;
    //项目id
    private Long beforeId;
    //项目状态
    private Long status;
    
  //操作用户名称
    private String userName;
    //操作用户id
    private String userId;

    //公司id
    private Long companyId;

    public Long getBeforeId() {
        return beforeId;
    }

    public void setBeforeId(Long beforeId) {
        this.beforeId = beforeId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}


