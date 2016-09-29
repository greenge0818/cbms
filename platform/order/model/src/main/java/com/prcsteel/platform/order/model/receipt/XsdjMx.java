package com.prcsteel.platform.order.model.receipt;

import java.math.BigDecimal;

/**
 * 销售单据明细
 *
 * Created by Rolyer on 2015/9/25.
 */
public class XsdjMx {
    private Long id;            //编号
    private String djh;         //单据号
    private String spmc;        //商品名称
    private String jldw;        //计量单位
    private String ggxh;        //规格型号
    private BigDecimal slian;   //数量
    private BigDecimal sl;      //税率
    private BigDecimal hsje;    //含税金额
    private BigDecimal hsdj;    //含税单价
    private BigDecimal zkje;    //折扣金额

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

    public String getSpmc() {
        return spmc;
    }

    public void setSpmc(String spmc) {
        this.spmc = spmc;
    }

    public String getJldw() {
        return jldw;
    }

    public void setJldw(String jldw) {
        this.jldw = jldw;
    }

    public String getGgxh() {
        return ggxh;
    }

    public void setGgxh(String ggxh) {
        this.ggxh = ggxh;
    }

    public BigDecimal getSlian() {
        return slian;
    }

    public void setSlian(BigDecimal slian) {
        this.slian = slian;
    }

    public BigDecimal getSl() {
        return sl;
    }

    public void setSl(BigDecimal sl) {
        this.sl = sl;
    }

    public BigDecimal getHsje() {
        return hsje;
    }

    public void setHsje(BigDecimal hsje) {
        this.hsje = hsje;
    }

    public BigDecimal getHsdj() {
        return hsdj;
    }

    public void setHsdj(BigDecimal hsdj) {
        this.hsdj = hsdj;
    }

    public BigDecimal getZkje() {
        return zkje;
    }

    public void setZkje(BigDecimal zkje) {
        this.zkje = zkje;
    }
}
