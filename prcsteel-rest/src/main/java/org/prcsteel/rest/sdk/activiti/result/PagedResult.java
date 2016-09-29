package org.prcsteel.rest.sdk.activiti.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

/**
 * 
 * @author zhoukun
 */
public class PagedResult<T extends Object> implements Serializable {

	private static final long serialVersionUID = -1108229796902569577L;

	private Integer total;
	
	private Integer start;
	
	private String sort;
	
	private String order;
	
	private Integer size;
	
	private List<T> data;

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public List<T> getData(Class<T> clzz) {
		if(data != null && data.size() > 0 && isMap(data.get(0))){
			List<T> nlist = new ArrayList<>(data.size());
			Gson gson = new Gson();
			for (T t : data) {
				nlist.add(gson.fromJson(gson.toJson(t), clzz));
			}
			data = nlist;
		}
		return data;
	}
	
	private boolean isMap(Object obj){
		return LinkedTreeMap.class.isInstance(obj) || LinkedHashMap.class.isInstance(obj);
	}

	public void setData(List<T> data) {
		this.data = data;
	}
	
}
