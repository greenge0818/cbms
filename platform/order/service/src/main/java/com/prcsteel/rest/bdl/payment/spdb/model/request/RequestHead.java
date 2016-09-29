package com.prcsteel.rest.bdl.payment.spdb.model.request;

/**
 * Created by kongbinheng on 2015/7/13.
 */
//@XmlRootElement(name="RequestHead")
public class RequestHead {

    private String transCode;
    private String signFlag;
    private String packetID;
    private String masterID;
    private String timeStamp;

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    public String getSignFlag() {
        return signFlag;
    }

    public void setSignFlag(String signFlag) {
        this.signFlag = signFlag;
    }

    public String getPacketID() {
        return packetID;
    }

    public void setPacketID(String packetID) {
        this.packetID = packetID;
    }

    public String getMasterID() {
        return masterID;
    }

    public void setMasterID(String masterID) {
        this.masterID = masterID;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}

