package com.prcsteel.platform.core.persist.dao;

import com.prcsteel.platform.core.model.model.BillSequence;

public interface BillSequenceDao {

    public int insert(BillSequence billSequence);

    public BillSequence query(BillSequence billSequence);

    public int update(BillSequence billSequence);
    
    public int updateCurrentValueById(Long id);

    public int updateById(Long id);

}