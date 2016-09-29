/**
 * Created by lixiang on 2015/7/29.
 */
$().ready(function() {
	$(".payBtnCofirm").click(function() {
		var button = $(this);
		var bankAccountTime = $("#bankAccountTime").val(); //银行出账时间
		if("" == bankAccountTime){
			cbms.alert("银行出账时间不能为空");
			return false;
		}
		cbms.confirm("确定付款吗？", null, function() {
			cbms.loading();
			var requestId = $("#request_id").val();// 申请单Id
			var accountId = $("#account_id").val();// 客户Id
			var totalAmount = $("#total_amount").val();// 付款金额
			var paymentBank = button.attr("vtype");
			$.ajax({
				type : "POST",
				url : Context.PATH + "/order/query/updconfirm.html",
				data : {
					"requestId" : requestId,
					"accountId" : accountId,
					"totalAmount" : totalAmount,
					"paymentBank" : paymentBank,
					"bankAccountTime" : bankAccountTime
				},
				success : function(result) {
					cbms.closeLoading();
					if (result && result.success) {
						cbms.alert("付款成功！");
						setTimeout(function() {
							history.go(-1);
						}, 3000);

					} else {
						if(result.data != null){
							cbms.alert(result.data, function() {
								var url = Context.PATH + "/order/query/secondpaysettlement.html";
								location.replace(url);
							});
						}else {
							cbms.alert("付款失败！");
						}
					}
				},
				error : function(xhr, textStatus, errorThrown) {
					cbms.closeLoading();
				}
			});

		});
	});
});
