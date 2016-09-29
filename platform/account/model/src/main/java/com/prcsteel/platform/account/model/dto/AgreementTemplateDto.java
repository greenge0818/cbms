package com.prcsteel.platform.account.model.dto;

import java.util.Date;

/**
 * Created by dengxiyan on 2016/4/1.
 */
public class AgreementTemplateDto {

    /**
     * 当前公司id
     */
    private Long currentCompanyId;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * 合同模板内容（包含html）
     */
    private String content;

    /**
     * 打印次数
     */
    private Long printTimes;

    /**
     * 上次打印时间
     */
    private Date lastPrintDate;

    /**
     * 模板id
     */
    private Long templateId;

    /**
     * 数据库中的条码
     */
    private String barCode;


    public Long getCurrentCompanyId() {
        return currentCompanyId;
    }

    public void setCurrentCompanyId(Long currentCompanyId) {
        this.currentCompanyId = currentCompanyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getPrintTimes() {
        return printTimes;
    }

    public void setPrintTimes(Long printTimes) {
        this.printTimes = printTimes;
    }

    public Date getLastPrintDate() {
        return lastPrintDate;
    }

    public void setLastPrintDate(Date lastPrintDate) {
        this.lastPrintDate = lastPrintDate;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }


    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
}
