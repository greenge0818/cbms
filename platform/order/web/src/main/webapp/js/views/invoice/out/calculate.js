function setTotalRow(){
	var totalreceipt = 0;
	var totalWeight = 0;
	var totalAmount = 0;
	var totalCode = 0;
	$("#secondpayment_datarow tr").each(function() {
		var balanceSecondSettlement = $(this).find("input[name='ys']").val();
		if ($.trim(balanceSecondSettlement) != "") {
			var receipt = parseFloat(parseFloat(balanceSecondSettlement).toFixed(2));
			totalreceipt += receipt ;//结算应收款合计
		}	
		var Weight = $(this).find("td").eq(2).text();
		if ($.trim(Weight) != "") {
			var WeightSum = parseFloat(parseFloat(Weight).toFixed(4));
			totalWeight += WeightSum ;//本次开票重量合合计
		}
		var amount = $(this).find("input[name='je']").val();
		if ($.trim(amount) != "") {
			var amountSum =  parseFloat(parseFloat(amount).toFixed(2));
			totalAmount += amountSum ;//本次开票金额合计
		}	
		var code = $(this).find("td").eq(4).text();
		if ($.trim(code) != "") {
			var codeSum =  parseFloat(code);
			totalCode += codeSum ;//发票总数量合计
		}	
	});
	var footerrow=$("#secondpayment_footer").find("tr");
	footerrow.find("td").eq(1).text(formatMoney(totalreceipt,2));
	footerrow.find("td").eq(2).text(totalWeight.toFixed(4));
	footerrow.find("td").eq(3).text(formatMoney(totalAmount,2));
	footerrow.find("td").eq(4).text(totalCode);
}
