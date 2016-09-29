package com.prcsteel.platform.common.utils;

import java.math.BigDecimal;

/**
 * 
 * @author zhoukun
 */
public class CbmsNumberUtil {

	public static int WeightDigit = 6;//默认值6
	
	public static int MoneyDigit = 2;//默认值2
	
	public void setWeightDigit(int weightDigit){
		CbmsNumberUtil.WeightDigit = weightDigit;
	}
	
	public void setMoneyDigit(int moneyDigit){
		CbmsNumberUtil.MoneyDigit = moneyDigit;
	}
	
	public static double formatWeight(BigDecimal weight){
		if(weight == null){
			return  0;
		}
		return weight.setScale(WeightDigit, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static double formatWeight(BigDecimal weight,int digit){
		if(weight == null){
			return  0;
		}
		return weight.setScale(digit, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static double formatWeight(Double weight,int digit){
		if(weight == null){
			return  0;
		}
		return formatWeight(new BigDecimal(weight),digit);
	}

	public static double formatWeight(Double weight){
		if(weight == null){
			return  0;
		}
		return formatWeight(new BigDecimal(weight));
	}

	public static double formatMoney(BigDecimal money){
		if(money == null){
			return 0;
		}
		return money.setScale(MoneyDigit, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	public static double formatMoney(Double money){
		if(money == null){
			return 0;
		}
		return formatMoney(new BigDecimal(money));
	}
	
	public static BigDecimal buildWeight(double weight){
		return new BigDecimal(weight).setScale(WeightDigit, BigDecimal.ROUND_HALF_UP);
	}
	
	public static BigDecimal buildMoney(double money){
		return new BigDecimal(money).setScale(MoneyDigit, BigDecimal.ROUND_HALF_UP);
	}
	public static BigDecimal buildMoney(BigDecimal money){
		if (money == null) {
			return BigDecimal.ZERO;
		}
		return money.setScale(MoneyDigit, BigDecimal.ROUND_HALF_UP);
	}
}
