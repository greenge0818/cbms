package com.prcsteel.donet.iv.finance.model.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by lcw36 on 2015/9/8.
 */
@XmlRootElement(name = "GetAcountListResult", namespace = "http://tempuri.org/")
@XmlAccessorType(XmlAccessType.FIELD)
public class DealDetailList {

    @XmlElement(name = "DealDetail", namespace = "http://tempuri.org/")
    private List<DealDetail> dealDetails;

    public List<DealDetail> getDealDetails() {
        return dealDetails;
    }

    public void setDealDetails(List<DealDetail> dealDetails) {
        this.dealDetails = dealDetails;
    }
}