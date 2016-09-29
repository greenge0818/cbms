package com.prcsteel.platform.common.query;

import java.util.List;

/**
 * 
 * @author zhoukun
 */
public class PagedResult<T> {

	private PagedQuery query;
	
	private Long totalRows;
	
	List<T> data;
	
	public Long getTotalPages() {
		if(query != null && query.getLength() > 0){
			return totalRows / query.getLength() + ((totalRows % query.getLength() > 0) ? 1 : 0);
		}
		return totalRows;
	}
	
	public Long getRecordsTotal(){
		return totalRows;
	}
	public Long getRecordsFiltered() {
        return totalRows;
    }
	public PagedQuery getQuery() {
		return query;
	}

	public void setQuery(PagedQuery query) {
		this.query = query;
	}

	public Long getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(Long totalRows) {
		this.totalRows = totalRows;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

}
