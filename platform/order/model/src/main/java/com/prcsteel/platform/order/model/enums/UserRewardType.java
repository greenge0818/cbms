package com.prcsteel.platform.order.model.enums;

/**
 * @Title: Status.java
 * @Package com.prcsteel.cbms.persist.model
 * @author  rabbit
 * @date 2015-9-17 17:47:12
 * @version V1.0
 */

public enum UserRewardType {
	buyerTempLocalReward("buyer_temp_local_reward", "本服务中心临采买家管理员提成"),
	buyerConsignLocalReward("buyer_consign_local_reward", "本服务中心代运营买家管理员提成"),
	buyerTempRangeReward("buyer_temp_range_reward", "跨服务中心临采买家管理员提成"),
	buyerConsignRangeReward("buyer_consign_range_reward","跨服务中心代运营买家管理员提成"),
	sellerTempLocalReward("seller_temp_local_reward", "本服务中心临采卖家管理员提成"),
	sellerConsignLocalReward("seller_consign_local_reward", "本服务中心代运营卖家管理员提成"),
	sellerTempRangeReward("seller_temp_range_reward", "跨服务中心临采卖家管理员提成"),
	sellerConsignRangeReward("seller_consign_range_reward","跨服务中心代运营卖家管理员提成"),
	newBuyerReward("new_buyer_reward", "新发展买家用户提成"),
	newSellerReward("new_seller_reward", "新发展卖家用户提成");
	// 成员变量
	private String code;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	// 构造方法
	private UserRewardType(String code, String name) {
		this.code = code;
		this.name = name;
	}
}
