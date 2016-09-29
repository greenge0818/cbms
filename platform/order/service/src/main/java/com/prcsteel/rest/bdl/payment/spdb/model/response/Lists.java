package com.prcsteel.rest.bdl.payment.spdb.model.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.io.Serializable;
import java.util.Vector;

/**
 * Created by ericlee on 7/18/15.
 */
@XmlRootElement(name="Lists")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({List.class})
public class Lists implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -2347137120135735976L;

    private Vector list;

    public Lists() {
        list = new Vector();
    }

    public Lists(Vector list) {
        this.list = list;
    }


    public List[] getList() {
        List[] listArray = new List[list.size()];
        return (List[]) list.toArray(listArray);
    }

    public void setList(List[] newList) {
        list = new Vector();
        for (int i = 0; i < newList.length; i++) {
            list.addElement(newList[i]);
        }
    }

    public boolean addList(List aList) {
        return list.add(aList);
    }

}
