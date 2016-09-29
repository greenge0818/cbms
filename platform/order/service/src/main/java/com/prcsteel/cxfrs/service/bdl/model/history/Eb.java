package com.prcsteel.cxfrs.service.bdl.model.history;

import com.prcsteel.platform.common.utils.BeanXMLMapping;

import java.util.Vector;

public class Eb {

	private Vector out;
	private Vector pub;

	public Pub[] getPub() {
		Pub[] pubArray = new Pub[pub.size()];

		return (Pub[]) pub.toArray(pubArray);
	}

	public void setPub(Pub[] newPub) {
		pub = new Vector();
		for (Pub element : newPub) {
			pub.addElement(element);
		}
	}

	public boolean addPub(Pub aPub) {
		return pub.add(aPub);
	}

	public Eb() {
		out = new Vector();
		pub = new Vector();
	}

	public Eb(Vector out, Vector pub) {
		this.out = out;
		this.pub = pub;
	}

	public Out[] getOut() {
		Out[] outArray = new Out[out.size()];

		return (Out[]) out.toArray(outArray);
	}

	public void setOut(Out[] newOut) {
		out = new Vector();
		for (Out element : newOut) {
			out.addElement(element);
		}
	}

	public boolean addOut(Out aOut) {
		return out.add(aOut);
	}

	public String toXML() {
		return BeanXMLMapping.toXML(this);
	}

	public static Object fromXML(String xml) {
		return BeanXMLMapping.fromXML(xml, Eb.class);
	}

}
