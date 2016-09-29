package com.prcsteel.platform.kuandao.service;

import com.prcsteel.platform.kuandao.model.dto.KuandaoAccountDto;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBResponseResult;

public interface MemeberService {
	
	/**
	 * 会员开户
	 * @param kuandaoAccountDto 会员账号信息
	 * @return
	 */
	SPDBResponseResult regiterMemeber(KuandaoAccountDto kuandaoAccountDto);
	
	/**
	 * 修改会员信息
	 * @param kuandaoAccountDto 会员账号信息
	 * @return
	 */
	SPDBResponseResult modifyMemeber(KuandaoAccountDto kuandaoAccountDto);
	
	/**
	 * 删除会员
	 * @param memeberCode 会员编号
	 * @return
	 */
	SPDBResponseResult deleteMemeber(String memeberCode);
	
	/**
	 * 会员信息查询
	 * @param memeberCode 会员编号
	 * @return
	 */
	SPDBResponseResult queryMemeberInfo(String memeberCode);

	/**
	 * 会员 绑定通知反馈报文生成
	 * @param code
	 * @return
	 */
	String generateResponse(String memeberCode, String status);


}
