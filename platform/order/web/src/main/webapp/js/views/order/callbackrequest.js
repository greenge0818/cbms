
/**
 * 付款申请单打回操作
 * @author lixiang
 */
$(document).on('click', '#callBack', function() {
	cbms.confirm("确定要打回待审核吗？", null, function() {
		cbms.loading();
		var requestId = $("#requestId").val();
		var orderId = $("#order_Id").val();
		$.ajax({
	        url: Context.PATH + '/order/query/callBack/pendingAudit.html',
	        type: "POST", 
	        data : {
				"requestId" : requestId,
				"orderId" : orderId
			},
	        success: function (result) {
	        	cbms.closeLoading();
	        	if (result && result.success) {
	        		if (result.type == 2) {
	        			cbms.alert("打回成功，等待服总审核！", function() {
							location.href = Context.PATH + "/order/query/secondpaysettlement2.html";
						});
	        		} else if (result.type == 3) {
	        			cbms.alert("打回成功，等待服总审核！", function() {
							location.href = Context.PATH + "/order/query/withdrawconfirm2.html";
						});
	        		} else if (result.type == 4) {
	        			cbms.alert("打回成功，等待服总审核！", function() {
							location.href = Context.PATH + "/order/query/printpayment.html";
						});
	        		}
	        	} else {
	        		if (result.type == 2) {
	        			cbms.alert(result.data, function() {
							location.href = Context.PATH + "/order/query/secondpaysettlement2.html";
						});
	        		} else if (result.type == 3) {
	        			cbms.alert(result.data, function() {
							location.href = Context.PATH + "/order/query/withdrawconfirm2.html";
						});
	        		} else if (result.type == 4) {
	        			cbms.alert(result.data, function() {
							location.href = Context.PATH + "/order/query/printpayment.html";
						});
	        		}
	        	}
	        }
	    });
	});
});
