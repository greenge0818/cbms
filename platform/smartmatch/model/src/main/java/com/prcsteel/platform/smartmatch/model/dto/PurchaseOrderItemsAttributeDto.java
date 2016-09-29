package com.prcsteel.platform.smartmatch.model.dto;

import com.prcsteel.platform.smartmatch.model.model.PurchaseOrderItemAttributes;

/**
 * Created by myh on 2015/12/10.
 */
public class PurchaseOrderItemsAttributeDto extends PurchaseOrderItemAttributes {
    private String name;
    private String options;
    private String type;
    private String uuid;
    private Long attributeId;

    public Long getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(Long attributeId) {
		this.attributeId = attributeId;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
