package com.prcsteel.platform.order.model.receipt;

/**
 * Created by Rolyer on 2015/9/24.
 */
public class Fphc {
    private Long id;
    private String djh;
    private String fphm;
    private String kprq;
    private Integer zf;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDjh() {
        return djh;
    }

    public void setDjh(String djh) {
        this.djh = djh;
    }

    public String getFphm() {
        return fphm;
    }

    public void setFphm(String fphm) {
        this.fphm = fphm;
    }

    public String getKprq() {
        return kprq;
    }

    public void setKprq(String kprq) {
        this.kprq = kprq;
    }

    public Integer getZf() {
        return zf;
    }

    public void setZf(Integer zf) {
        this.zf = zf;
    }
}
