package com.prcsteel.platform.order.model.dto;

import java.util.List;

public class PoolOutDetailDto {
	private List<Long> poolOutIds;
	
	private String nsortName;
	
	private String material;
	
	private String spec;

	public List<Long> getPoolOutIds() {
		return poolOutIds;
	}

	public void setPoolOutIds(List<Long> poolOutIds) {
		this.poolOutIds = poolOutIds;
	}

	public String getNsortName() {
		return nsortName;
	}

	public void setNsortName(String nsortName) {
		this.nsortName = nsortName;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}
	
}
