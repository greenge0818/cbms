package com.prcsteel.platform.core.model.dto;

/**
 * Created by rabbit on 2015/8/6.
 */
public class CategoryInfoDto {
	private Integer id;
	private String uuid;
	private String name;
	private String groupUuid;
	private String isEcShow;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroupUuid() {
		return groupUuid;
	}

	public void setGroupUuid(String groupUuid) {
		this.groupUuid = groupUuid;
	}

	public String getIsEcShow() {
		return isEcShow;
	}

	public void setIsEcShow(String isEcShow) {
		this.isEcShow = isEcShow;
	}

}
