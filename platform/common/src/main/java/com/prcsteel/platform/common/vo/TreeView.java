package com.prcsteel.platform.common.vo;

/**
 * 树状结构类
 *
 * Created by rolyer on 15-7-22.
 */
public class TreeView {

    /**
     * 节点号
     */
    private long id;
    /**
     * 父节点
     */
    private long pId;
    /**
     * 名称
     */
    private String name;
    /**
     * 是否自动展开
     */
    private boolean open = true;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getpId() {
        return pId;
    }

    public void setpId(long pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
