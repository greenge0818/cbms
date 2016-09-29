/**
 * Created by lixiang on 2015/7/29.
 */
$().ready(function() {
	$("#payBtnPass").click(function() {
		cbms.confirm("确定审核通过吗？", null, function() {
			cbms.loading();
			var requestId = $("#request_id").val();// 申请单id
			var requesterId = $("#requester_id").val();
			
			$.ajax({
				type : "POST",
				url : Context.PATH + "/order/query/updsecondpayauditskip.html",
				data : {
					"requestId" : requestId,
					"requesterId" : requesterId
				},
				success : function(result) {
					cbms.closeLoading();
					if (result && result.success) {
						cbms.alert("审核成功！", function() {
							location.href = Context.PATH + "/order/query/secondpayaudit.html";
						});
					} else {
						if(result.data!= null){
							cbms.alert(result.data, function() {
								var url = Context.PATH + "/order/query/secondpayaudit.html";
								location.replace(url);
							});
						}else {
							cbms.alert("审核失败！");
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
