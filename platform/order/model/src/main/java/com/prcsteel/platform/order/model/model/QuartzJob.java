package com.prcsteel.platform.order.model.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Rolyer on 2016/7/6.
 */
public class QuartzJob implements Serializable {

    private static final long serialVersionUID = 2382887870535250988L;

    private Integer id;

    private String jobName;

    private String status;

    private Date lastUpdated;

    private Date previousExecutedTime;

    private Date created;

    private String description;

    public QuartzJob() {
    }

    public QuartzJob(String jobName, String status, Date lastUpdated, Date previousExecutedTime, Date created, String description) {
        this.jobName = jobName;
        this.status = status;
        this.lastUpdated = lastUpdated;
        this.previousExecutedTime = previousExecutedTime;
        this.created = created;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Date getPreviousExecutedTime() {
        return previousExecutedTime;
    }

    public void setPreviousExecutedTime(Date previousExecutedTime) {
        this.previousExecutedTime = previousExecutedTime;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
