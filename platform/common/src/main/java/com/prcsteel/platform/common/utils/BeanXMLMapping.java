package com.prcsteel.platform.common.utils;

import com.wutka.jox.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BeanXMLMapping {

	/**
	 * Retrieves a bean object for the received XML and matching bean class
	 */
	public static Object fromXML(String xml, Class className) {
		ByteArrayInputStream xmlData = new ByteArrayInputStream(xml.getBytes());
		JOXBeanInputStream joxIn = new JOXBeanInputStream(xmlData);
		try {
			return (Object) joxIn.readObject(className);
		} catch (IOException exc) {
			exc.printStackTrace();
			return null;
		} finally {
			try {
				xmlData.close();
				joxIn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Returns an XML document String for the received bean
	 */
	public static String toXML(Object bean) {
		ByteArrayOutputStream xmlData = new ByteArrayOutputStream();
		JOXBeanOutputStream joxOut = new JOXBeanOutputStream(xmlData);
		try {
			joxOut.writeObject(beanName(bean), bean);
			return new String(xmlData.toByteArray(), "GBK");
		} catch (IOException exc) {
			exc.printStackTrace();
			return null;
		} finally {
			try {
				xmlData.close();
				joxOut.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Find out the bean class name
	 */
	private static String beanName(Object bean) {
		String fullClassName = bean.getClass().getName();
		String classNameTemp = fullClassName.substring(
				fullClassName.lastIndexOf(".") + 1, fullClassName.length());
		return classNameTemp.substring(0, 1) + classNameTemp.substring(1);
	}
}