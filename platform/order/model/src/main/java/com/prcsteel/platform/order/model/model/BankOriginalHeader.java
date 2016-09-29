package com.prcsteel.platform.order.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class BankOriginalHeader {
    private Long id;

    private String reqAcctno;

    private Date reqBegindate;

    private Date reqEnddate;

    private Integer reqQuerynumber;

    private Integer reqBeginnumber;

    private BigDecimal reqTransamount;

    private String reqSubaccount;

    private String reqSubacctname;

    private Integer resTotalcount;

    private String resAcctno;

    private String resAcctname;

    private Integer resCurrency;

    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;

    private Integer modificationNumber;

    private String rowId;

    private String parentRowId;

    private String ext1;

    private String ext2;

    private String ext3;

    private Integer ext4;

    private Integer ext5;

    private Integer ext6;

    private Date ext7;

    private Long ext8;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReqAcctno() {
        return reqAcctno;
    }

    public void setReqAcctno(String reqAcctno) {
        this.reqAcctno = reqAcctno == null ? null : reqAcctno.trim();
    }

    public Date getReqBegindate() {
        return reqBegindate;
    }

    public void setReqBegindate(Date reqBegindate) {
        this.reqBegindate = reqBegindate;
    }

    public Date getReqEnddate() {
        return reqEnddate;
    }

    public void setReqEnddate(Date reqEnddate) {
        this.reqEnddate = reqEnddate;
    }

    public Integer getReqQuerynumber() {
        return reqQuerynumber;
    }

    public void setReqQuerynumber(Integer reqQuerynumber) {
        this.reqQuerynumber = reqQuerynumber;
    }

    public Integer getReqBeginnumber() {
        return reqBeginnumber;
    }

    public void setReqBeginnumber(Integer reqBeginnumber) {
        this.reqBeginnumber = reqBeginnumber;
    }

    public BigDecimal getReqTransamount() {
        return reqTransamount;
    }

    public void setReqTransamount(BigDecimal reqTransamount) {
        this.reqTransamount = reqTransamount;
    }

    public String getReqSubaccount() {
        return reqSubaccount;
    }

    public void setReqSubaccount(String reqSubaccount) {
        this.reqSubaccount = reqSubaccount == null ? null : reqSubaccount.trim();
    }

    public String getReqSubacctname() {
        return reqSubacctname;
    }

    public void setReqSubacctname(String reqSubacctname) {
        this.reqSubacctname = reqSubacctname == null ? null : reqSubacctname.trim();
    }

    public Integer getResTotalcount() {
        return resTotalcount;
    }

    public void setResTotalcount(Integer resTotalcount) {
        this.resTotalcount = resTotalcount;
    }

    public String getResAcctno() {
        return resAcctno;
    }

    public void setResAcctno(String resAcctno) {
        this.resAcctno = resAcctno == null ? null : resAcctno.trim();
    }

    public String getResAcctname() {
        return resAcctname;
    }

    public void setResAcctname(String resAcctname) {
        this.resAcctname = resAcctname == null ? null : resAcctname.trim();
    }

    public Integer getResCurrency() {
        return resCurrency;
    }

    public void setResCurrency(Integer resCurrency) {
        this.resCurrency = resCurrency;
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
        this.createdBy = createdBy == null ? null : createdBy.trim();
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy == null ? null : lastUpdatedBy.trim();
    }

    public Integer getModificationNumber() {
        return modificationNumber;
    }

    public void setModificationNumber(Integer modificationNumber) {
        this.modificationNumber = modificationNumber;
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

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1 == null ? null : ext1.trim();
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2 == null ? null : ext2.trim();
    }

    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3 == null ? null : ext3.trim();
    }

    public Integer getExt4() {
        return ext4;
    }

    public void setExt4(Integer ext4) {
        this.ext4 = ext4;
    }

    public Integer getExt5() {
        return ext5;
    }

    public void setExt5(Integer ext5) {
        this.ext5 = ext5;
    }

    public Integer getExt6() {
        return ext6;
    }

    public void setExt6(Integer ext6) {
        this.ext6 = ext6;
    }

    public Date getExt7() {
        return ext7;
    }

    public void setExt7(Date ext7) {
        this.ext7 = ext7;
    }

    public Long getExt8() {
        return ext8;
    }

    public void setExt8(Long ext8) {
        this.ext8 = ext8;
    }
}