function setTotalRow(){
	var totalreceipt = 0;
	var totalpayment=0;
	$("#secondpayment_datarow tr").each(function() {
		var tempreceipt = $(this).find("input[name='ys']").val();
		if ($.trim(tempreceipt) != "") {
			var receipt = parseFloat(tempreceipt);
			totalreceipt = totalreceipt.add(receipt);
		}
		var temppayment = $(this).find("input[name='yf']").val();
		if ($.trim(temppayment) != "") {
			var payment=parseFloat(temppayment);
			totalpayment = totalpayment.add(payment);
		}			
	});
	var footerrow=$("#secondpayment_footer").find("tr");
	footerrow.find("td").eq(3).text("应收合计："+formatMoney(Math.abs(totalreceipt),2)+"元");
	footerrow.find("td").eq(4).text("应付合计："+formatMoney(totalpayment,2)+"元");
}
