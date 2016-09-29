package com.prcsteel.platform.order.model.query;

import java.util.List;

public class InvoiceOutBackQuery {
	private Integer id;
	private String djh;
	private String sortBy; //sort by
	private Integer useful;
	private List<String> djhs;
	
	public String getDjh() {
		return djh;
	}
	
	public InvoiceOutBackQuery setDjh(String djh) {
		this.djh = djh;
		return this;
	}

	public List<String> getDjhs() {
		return djhs;
	}

	public InvoiceOutBackQuery setDjhs(List<String> djhs) {
		this.djhs = djhs;
		return this;
	}

	public String getSortBy() {
		return sortBy;
	}

	public InvoiceOutBackQuery setSortBy(String sortBy) {
		this.sortBy = sortBy;
		return this;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public InvoiceOutBackQuery setUseful(Integer useful) {
		this.useful = useful;
		return this;
	}

	public Integer getUseful() {
		return useful;
	}
	
}
