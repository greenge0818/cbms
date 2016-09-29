package com.prcsteel.platform.order.persist.receipt;

import java.util.List;

import com.prcsteel.platform.order.model.query.FphcQuery;
import com.prcsteel.platform.order.model.receipt.Fphc;

/**
 * Created by Rolyer on 2015/9/24.
 */
public interface FphcDao {
    public int insert(Fphc fphc);
    
	public List<Fphc> syncSelect(FphcQuery query);
}
