package com.prcsteel.platform.core.model.model;

import java.io.Serializable;

/**
 * 品名对应的材质信息
 *
 * @author peanut
 * @date 2016/8/18 9:45
 */
public class CategoryMaterials implements Serializable {

    /**
     * 品名名称
     */
    private String categoryName;

    /**
     * 品名uuid
     */
    private String categoryUuid;

    /**
     * 材质名称
     */
    private String materialsName;

    /**
     * 材质uuid
     */
    private String materialsUuid;

    public CategoryMaterials() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryUuid() {
        return categoryUuid;
    }

    public void setCategoryUuid(String categoryUuid) {
        this.categoryUuid = categoryUuid;
    }

    public String getMaterialsName() {
        return materialsName;
    }

    public void setMaterialsName(String materialsName) {
        this.materialsName = materialsName;
    }

    public String getMaterialsUuid() {
        return materialsUuid;
    }

    public void setMaterialsUuid(String materialsUuid) {
        this.materialsUuid = materialsUuid;
    }
}
