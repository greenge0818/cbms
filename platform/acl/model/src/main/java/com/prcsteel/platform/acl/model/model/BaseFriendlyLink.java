package com.prcsteel.platform.acl.model.model;

public class BaseFriendlyLink {
    private Integer id;

    private Long url;

    private String title;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUrl() {
        return url;
    }

    public void setUrl(Long url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }
}