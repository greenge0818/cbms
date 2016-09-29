/**
 * Created by lixiang on 2015/8/2.
 */
$().ready(function() {
	$("#payBtnNoPass").click(function() {
		var requestId = $('#request_id').val();
		$.ajax({
			type : "POST",
			url : Context.PATH + "/order/query/find/requset/status.html",
			data : {
				"requestId":requestId
			},
			success : function(result) {
				if (result.data != null) {
					cbms.alert(result.data, function() {
						var url = Context.PATH + "/order/query/secondpayaudit.html";
						location.replace(url);
					});
				} else {
					var html = $('#addreason').html();
					cbms.getDialog("不通过审核", html);
				}
			},
		});
		
	});
	$(document).on("click", "#btnClose",function () {
        cbms.closeDialog();
    });
	$(document).on('click', '#btncommit', function() {
		var requestId = $('#request_id').val();
		var reason = $('#reason').val();// 理由
		var accountId = $('#account_id').val();//客户ID 
		var totalAmount = $('#total_amount').val();//申请金额
		if(!setlistensSave("#form-horizontal")){
			return;		
		}
		cbms.loading();
		$.ajax({
			type : "POST",
			url : Context.PATH + "/order/query/updsecondpayauditskipno.html",
			data : {
				"requestId":requestId,
				"reason":reason,
				"accountId":accountId,
				"totalAmount":totalAmount
			},
			success : function(result) {
				cbms.closeLoading();
				if (result && result.success) {
					$.gritter.add({
						title: '',
						text: '提交成功！',
						class_name: 'gritter-item-wrapper gritter-info gritter-center gritter-light'
					});
					setTimeout(function(){location.href="secondpayaudit.html"},3000);	
				} else {
					if(result.data != null){
						cbms.alert(result.data);
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
