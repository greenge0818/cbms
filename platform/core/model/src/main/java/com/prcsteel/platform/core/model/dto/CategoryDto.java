package com.prcsteel.platform.core.model.dto;

import com.prcsteel.platform.core.model.model.CategoryGroup;

/**
 * Created by chenchen on 2015/8/6.
 */
public class CategoryDto {
		private Integer id;
	    private String uuid;
	    private String name;
	    private Integer priority;
	    private CategoryGroup inner_group;
	    private CategoryGroup outer_group;
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getUuid() {
			return uuid;
		}
		public void setUuid(String uuid) {
			this.uuid = uuid;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Integer getPriority() {
			return priority;
		}
		public void setPriority(Integer priority) {
			this.priority = priority;
		}
		public CategoryGroup getInner_group() {
			return inner_group;
		}
		public void setInner_group(CategoryGroup inner_group) {
			this.inner_group = inner_group;
		}
		public CategoryGroup getOuter_group() {
			return outer_group;
		}
		public void setOuter_group(CategoryGroup outer_group) {
			this.outer_group = outer_group;
		}
	    

}
