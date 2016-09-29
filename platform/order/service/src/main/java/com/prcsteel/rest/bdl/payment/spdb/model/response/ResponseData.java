package com.prcsteel.rest.bdl.payment.spdb.model.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by kongbinheng on 2015/7/22.
 */
@XmlRootElement(name="ResponseData")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseData implements Serializable {

    private boolean success;
    private String message;
    private Body body;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
