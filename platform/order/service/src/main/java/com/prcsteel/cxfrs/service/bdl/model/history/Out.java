package com.prcsteel.cxfrs.service.bdl.model.history;

import com.prcsteel.platform.common.utils.BeanXMLMapping;

import java.util.Vector;

public class Out implements java.io.Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -2347137120135735976L;
	/*<AccNo>1202020419900157535</AccNo>
	<AccName>鸡侧妒绅位挠扛桃低审它霞帜吉四烯况</AccName>
	<CurrType>RMB</CurrType>
	<BeginDate>20120525</BeginDate>
	<EndDate>20150530</EndDate>
	<MinAmt>0</MinAmt>
	<MaxAmt>1000000</MaxAmt>
	<NextTag>2015-04-02-16.33.42.954943</NextTag>
	<TotalNum>20</TotalNum>
	<RepReserved1></RepReserved1>
	<RepReserved2></RepReserved2>*/
	private String accNo;
	private String accName;
	private String currType;
	private String areaCode;
	private String nextTag;
	private String totalNum;
	private String repReserved1;
	private String repReserved2;
	private Vector rd;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	public String getCurrType() {
		return currType;
	}

	public void setCurrType(String currType) {
		this.currType = currType;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getNextTag() {
		return nextTag;
	}

	public void setNextTag(String nextTag) {
		this.nextTag = nextTag;
	}

	public String getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}

	public String getRepReserved1() {
		return repReserved1;
	}

	public void setRepReserved1(String repReserved1) {
		this.repReserved1 = repReserved1;
	}

	public String getRepReserved2() {
		return repReserved2;
	}

	public void setRepReserved2(String repReserved2) {
		this.repReserved2 = repReserved2;
	}

	public void setRd(Vector rd) {
		this.rd = rd;
	}

	public Out() {
		rd = new Vector();
	}

	public Out(Vector rd) {
		this.rd = rd;
	}

	public Rd[] getRd() {
		Rd[] rdArray = new Rd[rd.size()];

		return (Rd[]) rd.toArray(rdArray);
	}

	public void setRd(Rd[] newRd) {
		rd = new Vector();
		for (Rd element : newRd) {
			rd.addElement(element);
		}
	}

	public boolean addRd(Rd aRd) {
		return rd.add(aRd);
	}

	public String toXML() {
		return BeanXMLMapping.toXML(this);
	}

	public static Object fromXML(String xml) {
		return BeanXMLMapping.fromXML(xml, Out.class);
	}

}
