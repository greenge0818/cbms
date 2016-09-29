/**
 * 审核折让单
 * @author lixiang
 */
$().ready(function() {
	$("#payBtnPass").click(function() {
		cbms.confirm("确定审核通过吗？", null, function() {
			cbms.loading();
			var allowanceId = $("#allowance_id").val();
			var buyerAllowanceIds = $("*[name='buyerAllowance_id']").map(function(){return $(this).val()}).get().join(",");
			var allowanceType = $("#allowance_type").val();
			$.ajax({
				type : "POST",
				url : Context.PATH + "/allowance/update/rebate.html",
				data : {
					"allowanceId" : allowanceId,
					"buyerAllowanceIds" : buyerAllowanceIds.toString()
				},
				success : function(result) {
					cbms.closeLoading();
					if (result && result.success) {
						cbms.alert("审核成功！", function() {
							if (allowanceType == "seller") {
								location.href = Context.PATH + "/allowance/list/seller.html";
							} else {
								location.href = Context.PATH + "/allowance/list/buyer.html";
							}
						});
					} else {
						if(result.data!= null){
							cbms.alert(result.data, function() {
								if (allowanceType == "seller") {
									var url = Context.PATH + "/allowance/list/seller.html";
								} else {
									var url = Context.PATH + "/allowance/list/buyer.html";
								}
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
	
	$(".img-box").click(function () {
		var src = $("#turnImg").attr("src");
		renderImg(src);
	});
});
