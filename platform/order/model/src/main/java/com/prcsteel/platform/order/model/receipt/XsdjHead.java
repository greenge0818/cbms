package com.prcsteel.platform.order.model.receipt;

import java.util.Date;

/**
 * 销售单据头
 *
 * Created by Rolyer on 2015/9/25.
 */
public class XsdjHead {
    private Long id;            //编号
    private String khsh;        //客户税号（专票必须）;
    private String khmc;        //客户名称;
    private String khdz;        //客户地址（专票必须）
    private String khdh;        //客户电话（专票必须）
    private String khyh;        //客户银行帐号（专票必须）
    private String bz;          //备注
    private Date djrq;          //单据日期;
    private String djh;         //单独号（必需是唯一值）
    private String djzl;        //发票种类：中文：“专用发票”和“普通发票”
    private String dubz;        //读取标志: “0”—未读取 “1”—已读取
    private String kpjh;        //开票机号（0-主机，1-分1，…）

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKhsh() {
        return khsh;
    }

    public void setKhsh(String khsh) {
        this.khsh = khsh;
    }

    public String getKhmc() {
        return khmc;
    }

    public void setKhmc(String khmc) {
        this.khmc = khmc;
    }

    public String getKhdz() {
        return khdz;
    }

    public void setKhdz(String khdz) {
        this.khdz = khdz;
    }

    public String getKhdh() {
        return khdh;
    }

    public void setKhdh(String khdh) {
        this.khdh = khdh;
    }

    public String getKhyh() {
        return khyh;
    }

    public void setKhyh(String khyh) {
        this.khyh = khyh;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public Date getDjrq() {
        return djrq;
    }

    public void setDjrq(Date djrq) {
        this.djrq = djrq;
    }

    public String getDjh() {
        return djh;
    }

    public void setDjh(String djh) {
        this.djh = djh;
    }

    public String getDjzl() {
        return djzl;
    }

    public void setDjzl(String djzl) {
        this.djzl = djzl;
    }

    public String getDubz() {
        return dubz;
    }

    public void setDubz(String dubz) {
        this.dubz = dubz;
    }

    public String getKpjh() {
        return kpjh;
    }

    public void setKpjh(String kpjh) {
        this.kpjh = kpjh;
    }
}
