package com.prcsteel.donet.iv.finance.model.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by lcw36 on 2015/9/8.
 */
@XmlRootElement(name = "GetAmountByPhoneResult", namespace = "http://tempuri.org/")
@XmlAccessorType(XmlAccessType.FIELD)
public class CbmsAmountList {

    @XmlElement(name = "CbmsAmount", namespace = "http://tempuri.org/")
    private List<CbmsAmount> cbmsAmounts;

    public List<CbmsAmount> getCbmsAmount() {
        return cbmsAmounts;
    }

    public void setCbmsAmount(List<CbmsAmount> cbmsAmounts) {
        this.cbmsAmounts = cbmsAmounts;
    }

}
