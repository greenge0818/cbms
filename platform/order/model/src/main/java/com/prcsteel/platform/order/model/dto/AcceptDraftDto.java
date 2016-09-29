package com.prcsteel.platform.order.model.dto;

import com.prcsteel.platform.order.model.model.AcceptDraft;

/**
 * @author dengxiyan
 * @version V1.0
 * @Title: AcceptDraftDto
 * @Package com.prcsteel.platform.order.model.dto
 * @Description:
 * @date 2015/11/12
 */
public class AcceptDraftDto extends AcceptDraft {
    private String oldStatus;

    public AcceptDraftDto() {
    }

    public AcceptDraftDto(Long id, String status, String reason, String lastUpdatedBy, Integer modificationNumber, String oldStatus) {
        super(id, reason, status, lastUpdatedBy, modificationNumber);
        this.oldStatus = oldStatus;
    }

    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus;
    }

    public String getOldStatus() {

        return oldStatus;
    }
}