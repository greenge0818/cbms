package com.prcsteel.platform.core.model.model;

import java.util.Date;

public class CategoryAlias {
    private Long id;

    private Long categoryId;

    private String categoryName;

    private String aliasName;

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

    public CategoryAlias setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public CategoryAlias setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public CategoryAlias setCategoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public String getAliasName() {
        return aliasName;
    }

    public CategoryAlias setAliasName(String aliasName) {
        this.aliasName = aliasName;
        return this;
    }

    public Date getCreated() {
        return created;
    }

    public CategoryAlias setCreated(Date created) {
        this.created = created;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public CategoryAlias setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public CategoryAlias setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public CategoryAlias setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public Integer getModificationNumber() {
        return modificationNumber;
    }

    public CategoryAlias setModificationNumber(Integer modificationNumber) {
        this.modificationNumber = modificationNumber;
        return this;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getParentRowId() {
        return parentRowId;
    }

    public void setParentRowId(String parentRowId) {
        this.parentRowId = parentRowId;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3;
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