package com.prcsteel.platform.order.web.support;
/**
 * 
 * @author zhoukun
 */
public class ProjectEnv {

	String projectVersion;
	
	String requiredDbVersion;

	public String getProjectVersion() {
		return projectVersion;
	}

	public void setProjectVersion(String projectVersion) {
		this.projectVersion = projectVersion;
	}

	public String getRequiredDbVersion() {
		return requiredDbVersion;
	}

	public void setRequiredDbVersion(String requiredDbVersion) {
		this.requiredDbVersion = requiredDbVersion;
	}
	
	
}
