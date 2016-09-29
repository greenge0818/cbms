/**
 * 计算服务中心二次结算储备金日报金额合计
 * @lixiang
 */
function setTotalRow(){
	var totalYesterdayAmount = 0;//昨日余额
	var totalAmountAdd = 0;//今日增加
	var totalAmountReduce = 0;//今日减少
	var totalTodayAmount = 0;//今日余额
	$("#secondpayment_datarow tr").each(function() {
		var yesterdayAmount = $(this).find("input[name='ys']").val();
		if ($.trim(yesterdayAmount) != "") {
			var yesterdayMoney = parseFloat(yesterdayAmount);
			totalYesterdayAmount += yesterdayMoney ;
		}	
		var amountAdd = $(this).find("input[name='tdd']").val();
		if ($.trim(amountAdd) != "") {
			var addMoney =parseFloat(parseFloat(amountAdd).toFixed(2));
			totalAmountAdd += addMoney ;
		}
		var amountReduce = $(this).find("input[name='trr']").val();
		if ($.trim(amountReduce) != "") {
			var reduceMoney = parseFloat(parseFloat(amountReduce).toFixed(2));
			totalAmountReduce += reduceMoney ;
		}	
		var todayAmount = $(this).find("input[name='ts']").val();
		if ($.trim(todayAmount) != "") {
			var todayMoney = parseFloat(todayAmount);
			totalTodayAmount += todayMoney ;
		}	
	});
	var footerrow=$("#secondpayment_footer").find("tr");
	footerrow.find("td").eq(2).text("昨日总余额："+formatMoney(totalYesterdayAmount,2)+"元");
	footerrow.find("td").eq(3).text("当日增加："+formatMoney(totalAmountAdd,2)+"元");
	footerrow.find("td").eq(4).text("当日减少："+formatMoney(Math.abs(totalAmountReduce),2)+"元");
	footerrow.find("td").eq(5).text("当日余额："+formatMoney(totalTodayAmount,2)+"元");
}
