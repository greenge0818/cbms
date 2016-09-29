package com.prcsteel.platform.kuandao.persist.dao;

import com.prcsteel.platform.kuandao.model.model.BillSequence;

public interface KuandaoSequenceDao {

	 public int insert(BillSequence billSequence);

	    public BillSequence query(BillSequence billSequence);

	    public int update(BillSequence billSequence);
	    
	    public int updateCurrentValueById(Long id);

	    public int updateById(Long id);

}
