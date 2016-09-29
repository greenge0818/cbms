package com.prcsteel.cxfrs.service.bdl.model.history;

import com.prcsteel.platform.common.utils.BeanXMLMapping;

import java.util.Vector;

public class CMS {
	private Vector eb;

	public CMS() {
		eb = new Vector();
	}

	public CMS(Vector eb) {
		this.eb = eb;
	}

	public Eb[] getEb() {
		Eb[] ebArray = new Eb[eb.size()];

		return (Eb[]) eb.toArray(ebArray);
	}

	public void setEb(Eb[] neweb) {
		eb = new Vector();
		for (Eb element : neweb) {
			eb.addElement(element);
		}
	}

	public boolean addEb(Eb aEb) {
		return eb.add(aEb);
	}

	public String toXML() {
		return BeanXMLMapping.toXML(this);
	}

	public static Object fromXML(String xml) {
		return BeanXMLMapping.fromXML(xml, CMS.class);
	}

	public static void main(String[] argc) throws Exception {
		/*
		 * ListOfCustomer il = new ListOfCustomer(); ArrayList invList = new
		 * ArrayList(); Customer inv = new Customer();
		 * inv.setCInvAddCode("10001"); inv.setCInvCCode("12");
		 * inv.setCInvcName("安佳淡奶油"); inv.setCInvCode("1111"); Customer inv2 =
		 * new Customer(); inv2.setCInvAddCode("10001");
		 * inv2.setCInvCCode("12"); inv2.setCInvcName("安佳11油");
		 * inv2.setCInvCode("1111"); invList.add(inv); invList.add(inv2);
		 * ListOfCustomer ilist = new ListOfCustomer(); ilist.addCustomer(inv);
		 * ilist.addCustomer(inv2); System.out.println(ilist.toXML());
		 */
		/*
		 * String xmlInv =
		 * "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><ListOfCustomer>" +
		 * "<customer><CInvAddCode>10001</CInvAddCode><CInvCCode>12</CInvCCode><CInvCode>1111</CInvCode>"
		 * +
		 * "<CInvcName>安佳淡奶油</CInvcName></customer><customer><CInvAddCode>10001</CInvAddCode>"
		 * +
		 * "<CInvCCode>12</CInvCCode><CInvCode>1111</CInvCode><CInvcName>安佳11油</CInvcName>"
		 * + "</customer></ListOfCustomer>";
		 */
		// String xmlInv =
		// "<?xml version=\"1.0\" encoding=\"GB2312\"?><Out><Rd><contractNo>01001</contractNo><portno>金麦兄弟（北京）食品有限公司</portno><opType>BPFC</opType><payAccNo></payAccNo><payAccNameCN></payAccNameCN><signDate></signDate><repReserved3></repReserved3><repReserved4></repReserved4></Rd><Rd><contractNo>01002</contractNo><portno>金麦兄弟（北京）食品有限公司</portno><opType>BPFC</opType><payAccNo></payAccNo><payAccNameCN></payAccNameCN><signDate></signDate><repReserved3></repReserved3><repReserved4></repReserved4></Rd></Out>";
		//		String file = "/Users/ericlee/Projects/PaymentService/src/PersonPayRes.xml";
		//		String content = "";
		//		try {
		//			// Read the entire contents of sample.txt
		//			content = FileUtils.readFileAsString(file);
		//			// For shake of this example we show the file content here.
		//			System.out.println("File content: " + content);
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		}
		//
		//		CMS ivl = (CMS) CMS.fromXML(content);
		//		System.out.println(ivl.getEb()[0].getOut()[0].getRd().length);
		//		System.out.println(ivl.getEb()[0].getOut()[0].getRd()[0].getCurrType());
		//		System.out.println(ivl.getEb()[0].getPub()[0].getCIS());

	}

}
