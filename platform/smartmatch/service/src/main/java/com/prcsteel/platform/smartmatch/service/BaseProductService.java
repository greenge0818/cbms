package com.prcsteel.platform.smartmatch.service;


import java.util.List;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.smartmatch.model.dto.ResourceDto;

/**
 * 基础资源、价格保存专用接口
 * @author afeng
 * @date 2016/06/30 15:54:58
 *
 */
public interface BaseProductService {
	
	/**
	 * 基础资源、价格保存
	 * @param baseProductQuery
	 * @param user
	 * @return
	 */
    public boolean saveBaseProduct(ResourceDto resourceDto, User user);
    
    /**
     * 删除基础资源
     * @param resourceList
     */
    public void deletProduct(List<ResourceDto> resourceList);

}
