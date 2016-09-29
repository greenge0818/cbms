package com.prcsteel.platform.smartmatch.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

/**
 * Created by caochao on 2016/7/5.
 */
public class CategoryNameQuery extends PagedQuery {
    private String categoryName;
    private String aliasName;
    private String ecDisplay;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getEcDisplay() {
        return ecDisplay;
    }

    public void setEcDisplay(String ecDisplay) {
        this.ecDisplay = ecDisplay;
    }
}
