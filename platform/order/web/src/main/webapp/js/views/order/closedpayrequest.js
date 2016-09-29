/**
 * create by lixiang
 * 关闭付款申请单
 */
//付款申请单如审核不通过的状态下，可以申请关闭
$(document).on('click', '#closed', function() {
	closed();
});

function closed() {
	var url = Context.PATH + '/order/paymentmanager/closedpayment.html';
	var requestId = $("#requestId").val();
	$.post(url, {
		requestId : requestId
	}, function(re) {
		if (re.success) {
			cbms.alert("申请关闭成功！", function() {
				var url = "";
				if (re.status == "CONFIRMED" || re.status == "CONFIRMEDPAY") {
					url = Context.PATH + "/order/paymentmanager/payment.html";
				} else {
					url = Context.PATH + "/order/paymentmanager/request.html";
				}
				location.replace(url);
			});
		}else{
				cbms.alert(re.data);
		}
	});
}