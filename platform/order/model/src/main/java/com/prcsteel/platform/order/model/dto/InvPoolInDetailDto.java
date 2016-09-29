package com.prcsteel.platform.order.model.dto;

public class InvPoolInDetailDto {
	private Long poolInId;
	
	private String nsortName;
	
	private String material;
	
	private String spec;

	public Long getPoolInId() {
		return poolInId;
	}

	public void setPoolInId(Long poolInId) {
		this.poolInId = poolInId;
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
