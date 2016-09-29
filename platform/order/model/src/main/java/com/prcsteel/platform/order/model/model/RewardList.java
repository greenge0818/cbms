package com.prcsteel.platform.order.model.model;

import com.prcsteel.platform.acl.model.model.Reward;

import java.util.List;

public class RewardList  {
   private List<Reward> allOrgReward;
   
   private String orgName;
   
   private Long orgId;

public List<Reward> getAllOrgReward() {
	return allOrgReward;
}

public void setAllOrgReward(List<Reward> allOrgReward) {
	this.allOrgReward = allOrgReward;
}

public String getOrgName() {
	return orgName;
}

public void setOrgName(String orgName) {
	this.orgName = orgName;
}

public Long getOrgId() {
	return orgId;
}

public void setOrgId(Long orgId) {
	this.orgId = orgId;
}


   
   


   
    
}