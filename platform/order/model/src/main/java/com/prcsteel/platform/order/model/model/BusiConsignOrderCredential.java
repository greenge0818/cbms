package com.prcsteel.platform.order.model.model;

import java.util.Date;

public class BusiConsignOrderCredential {
    private Long id;

    private String credentialCode;
    private Integer credentialNum; //凭证总页数
    
    private String type;

    private String name;

    private Integer printNum;  //打印单子的次数
    private Integer printCodeNumber; //打印凭证号的次数1

    private String printIp;
    private String printCodeIp;

    private String status;

    private String note;

    private Boolean isDeleted;
    
    private Date created;

	private Date printDate;
	private String printDateStr;
	
	private Date printCodeDate;
	private String printCodeDateStr;
	
    private String printedBy;
    private String printCodeBy;

    private Date submitDate;

    private String submitedBy;

    private Date auditDate;

    private String auditBy;

    
    private Boolean isBillBuyercert;
    private String uploadStatus;  //凭证上传状态 
    

    public Boolean getIsBillBuyercert() {
		return isBillBuyercert;
	}

	public void setIsBillBuyercert(Boolean isBillBuyercert) {
		this.isBillBuyercert = isBillBuyercert;
	}

	private String rowId;


    private String parentRowId;

    public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCredentialCode() {
        return credentialCode;
    }

    public void setCredentialCode(String credentialCode) {
        this.credentialCode = credentialCode == null ? null : credentialCode.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getPrintNum() {
        return printNum;
    }

    public void setPrintNum(Integer printNum) {
        this.printNum = printNum;
    }

    public String getPrintIp() {
        return printIp;
    }

    public void setPrintIp(String printIp) {
        this.printIp = printIp == null ? null : printIp.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getPrintDate() {
        return printDate;
    }

    public void setPrintDate(Date printDate) {
        this.printDate = printDate;
    }

    public String getPrintedBy() {
        return printedBy;
    }

    public void setPrintedBy(String printedBy) {
        this.printedBy = printedBy == null ? null : printedBy.trim();
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public String getSubmitedBy() {
        return submitedBy;
    }

    public void setSubmitedBy(String submitedBy) {
        this.submitedBy = submitedBy == null ? null : submitedBy.trim();
    }

    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    public String getAuditBy() {
        return auditBy;
    }

    public void setAuditBy(String auditBy) {
        this.auditBy = auditBy == null ? null : auditBy.trim();
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId == null ? null : rowId.trim();
    }

    public String getParentRowId() {
        return parentRowId;
    }

    public void setParentRowId(String parentRowId) {
        this.parentRowId = parentRowId == null ? null : parentRowId.trim();
    }

	public Integer getPrintCodeNumber() {
		return printCodeNumber;
	}

	public void setPrintCodeNumber(Integer printCodeNumber) {
		this.printCodeNumber = printCodeNumber;
	}

	public String getPrintDateStr() {
		return printDateStr;
	}

	public void setPrintDateStr(String printDateStr) {
		this.printDateStr = printDateStr;
	}

	public String getPrintCodeIp() {
		return printCodeIp;
	}

	public void setPrintCodeIp(String printCodeIp) {
		this.printCodeIp = printCodeIp;
	}

	public Date getPrintCodeDate() {
		return printCodeDate;
	}

	public void setPrintCodeDate(Date printCodeDate) {
		this.printCodeDate = printCodeDate;
	}

	public String getPrintCodeDateStr() {
		return printCodeDateStr;
	}

	public void setPrintCodeDateStr(String printCodeDateStr) {
		this.printCodeDateStr = printCodeDateStr;
	}

	public String getPrintCodeBy() {
		return printCodeBy;
	}

	public void setPrintCodeBy(String printCodeBy) {
		this.printCodeBy = printCodeBy;
	}

	public Integer getCredentialNum() {
		return credentialNum;
	}

	public void setCredentialNum(Integer credentialNum) {
		this.credentialNum = credentialNum;
	}

	public String getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(String uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

	
}