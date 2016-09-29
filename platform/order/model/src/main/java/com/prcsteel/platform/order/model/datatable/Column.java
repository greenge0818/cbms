package com.prcsteel.platform.order.model.datatable;


/**
 * @author dengxiyan
 * @version V1.0
 * @Description: jquery data table column 用于动态表头
 * @date 2015/12/9
 */
public class Column {

    public static final String TEXT_RIGHT = "text-right";
    public static final String TEXT_CENTER = "text-center";
    public static final String TEXT_LEFT = "text-left";

    /** 对象数据name */
    private String data;
    
    private String name;

    private String sClass = "text-right";

    /** 渲染方法名 */
    private String render;

    private String index;//用于导出的数据列 var+i

    public Column() {}

    public Column(String data) {
        this.data = data;
    }

    public Column(String data, String sClass,String index,String name) {
        this.data = data;
        this.sClass = sClass;
        this.index = index;
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getsClass() {
        return sClass;
    }

    public void setsClass(String sClass) {
        this.sClass = sClass;
    }

    public String getRender() {
        return render;
    }

    public void setRender(String render) {
        this.render = render;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}

