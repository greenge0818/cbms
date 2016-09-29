/**
 * 审核折让单——不通过
 * @author lixiang
 */
$().ready(function() {
	$("#payBtnNoPass").click(function() {
		var html = $('#addreason').html();
		cbms.getDialog("不通过审核", html);
	});
	$(document).on("click", "#btnClose",function () {
        cbms.closeDialog();
    });
	var buyerAllowanceIds = $("*[name='buyerAllowance_id']").map(function(){return $(this).val()}).get().join(",");
	var allowanceType = $("#allowance_type").val();
	$(document).on('click', '#btncommit', function() {
		if(!setlistensSave("#form-horizontal")){
			return;		
		}
		var allowanceId = $("#allowance_id").val();
		var reason = $('#reason').val();// 理由
		cbms.closeDialog();
		cbms.loading();
		$.ajax({
			type : "POST",
			url : Context.PATH + "/allowance/notThrough/audit.html",
			data : {
				"allowanceId":allowanceId,
				"remark":reason,
				"buyerAllowanceIds":buyerAllowanceIds.toString()
			},
			success : function(result) {
				cbms.closeLoading();
				if (result && result.success) {
					cbms.alert("提交成功！", function() {
						if (allowanceType == "seller") {
							location.href = Context.PATH + "/allowance/list/seller.html";
						} else {
							location.href = Context.PATH + "/allowance/list/buyer.html";
						}
					});
				} else {
					if(result.data != null){
						cbms.closeDialog();
						cbms.alert(result.data, function() {
							if (allowanceType == "seller") {
								var url = Context.PATH + "/allowance/list/seller.html";
							} else {
								var url = Context.PATH + "/allowance/list/buyer.html";
							}
							location.replace(url);
						});
					}else {
						cbms.alert("提交失败！");
					}
				}
			},
			error : function(xhr, textStatus, errorThrown) {
				cbms.closeLoading();
			}
		});
	});
});