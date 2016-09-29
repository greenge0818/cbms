/**
 * 计算折让详情单金额
 * @lixiang
 */

window.onload=function(){ 
	setTotalRow();
} 

function setTotalRow(){
	var allowanceManner = $("#allowance_manner").val();
	var totalActualWeight = 0;//实提重量合计
	var totalAllowanceWeight = 0;//折扣重量合计
	var totalDiscountedWeight = 0;//折后重量合计
	var totalActualAmount = 0;//实提金额合计
	var totalAllowanceAmount = 0;//折扣金额合计
	var totalDiscountedAmount = 0;//折后金额合计

	$(".secondpayment_datarow").each(function(){
		totalActualWeight = 0;//实提重量合计
		totalAllowanceWeight = 0;//折扣重量合计
		totalDiscountedWeight = 0;//折后重量合计
		totalActualAmount = 0;//实提金额合计
		totalAllowanceAmount = 0;//折扣金额合计
		totalDiscountedAmount = 0;//折后金额合计
		
		$(this).find("tr").each(function() {
			var actualWeight = $(this).find("input[name='actual_weight']").val();
			if ($.trim(actualWeight) != "") {
				totalActualWeight += parseFloat(parseFloat(actualWeight).toFixed(4));
			}	
			var actualAmount = $(this).find("input[name='actual_money']").val();
			if ($.trim(actualAmount) != "") {
				totalActualAmount += parseFloat(parseFloat(actualAmount).toFixed(2));
			}
			
			if (allowanceManner == "all" || allowanceManner == "amount") {
				var allowanceAmount = $(this).find("input[name='allowance_money']").val();
				if ($.trim(allowanceAmount) != "") {
					totalAllowanceAmount += parseFloat(parseFloat(allowanceAmount).toFixed(2));
				}	
				var discountedAmount = $(this).find("input[name='discounted_money']").val();
				if ($.trim(discountedAmount) != "") {
					totalDiscountedAmount += parseFloat(parseFloat(discountedAmount).toFixed(2));
				}
			}
			if (allowanceManner == "all" || allowanceManner == "weight") {
				var allowanceWeight = $(this).find("input[name='allowance_weight']").val();
				if ($.trim(allowanceWeight) != "") {
					totalAllowanceWeight += parseFloat(parseFloat(allowanceWeight).toFixed(6));
				}	
				var discountedWeight = $(this).find("input[name='discounted_weight']").val();
				if ($.trim(discountedWeight) != "") {
					totalDiscountedWeight += parseFloat(parseFloat(discountedWeight).toFixed(6));
				} 
			}
		});
		var footerrow=$(this).next().find("tr");		
		footerrow.find("td").eq(6).text(formatMoney(totalActualWeight,6));//实提重量
		if (allowanceManner == "all") {
			footerrow.find("td").eq(9).text(formatMoney(totalActualAmount,2));//实提金额
			footerrow.find("td").eq(10).text(formatMoney(totalAllowanceAmount,2));//折扣金额
			footerrow.find("td").eq(11).text(formatMoney(totalDiscountedAmount,2));//折后金额
			footerrow.find("td").eq(7).text(formatMoney(totalAllowanceWeight,6));//折扣重量
			footerrow.find("td").eq(8).text(formatMoney(totalDiscountedWeight,6));//折后重量
		}
		if (allowanceManner == "amount") {
			footerrow.find("td").eq(7).text(formatMoney(totalActualAmount,2));//实提金额
			footerrow.find("td").eq(8).text(formatMoney(totalAllowanceAmount,2));//折扣金额
			footerrow.find("td").eq(9).text(formatMoney(totalDiscountedAmount,2));//折后金额
		}
		if (allowanceManner == "weight") {
			footerrow.find("td").eq(7).text(formatMoney(totalAllowanceWeight,6));//折扣重量
			footerrow.find("td").eq(8).text(formatMoney(totalDiscountedWeight,6));//折后重量
			footerrow.find("td").eq(9).text(formatMoney(totalActualAmount,2));//实提金额
		}
	});
}
