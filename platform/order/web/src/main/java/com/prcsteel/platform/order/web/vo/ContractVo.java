package com.prcsteel.platform.order.web.vo;

/**
 * Created by rolyer on 15-8-12.
 */
public class ContractVo {
    private String title;
    private boolean isBuyer;
    private String companyName;
    private String content;
    private int number;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isBuyer() {
        return isBuyer;
    }

    public void setBuyer(boolean isBuyer) {
        this.isBuyer = isBuyer;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
