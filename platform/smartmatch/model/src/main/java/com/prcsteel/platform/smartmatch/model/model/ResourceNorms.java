package com.prcsteel.platform.smartmatch.model.model;

import java.util.Date;

public class ResourceNorms {
    private Long id;

    private Long resourceId;

    private String normUuid;

    private String value;

    private Integer priority;

    private Date created;

    private String createdBy;

    private String tmp;

    public ResourceNorms(){}
    
	public ResourceNorms(Long resourceId, String normUuid, String value, Integer priority,
			Date created, String createdBy) {
		super();
		this.resourceId = resourceId;
		this.normUuid = normUuid;
		this.value = value;
		this.priority=priority;
		this.created = created;
		this.createdBy = createdBy;
	}
	
	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getNormUuid() {
        return normUuid;
    }

    public void setNormUuid(String normUuid) {
        this.normUuid = normUuid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }
}