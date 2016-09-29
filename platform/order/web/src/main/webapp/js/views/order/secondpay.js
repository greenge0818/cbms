/**
 * Created by lixiang on 2015/7/27.
 */
function reloadfun(isf){
	if(isf){
		$.gritter.add({
			title: '',
			text: '申请抵扣成功！',
			class_name: 'gritter-item-wrapper gritter-info gritter-center gritter-light'
		});
		setTimeout(function(){location.reload()},3000);
	}

}
function sendAjaxFunc(o){
	cbms.loading();
	$.ajax({
		type : "POST",
		url : Context.PATH + "/order/addSecondPay.html",
		data : {
			"balance":o.balance,
			"id":o.id,
			"deductionPay":o.deductionPay
		},
		success : function(result) {
			cbms.closeLoading();
			if (result && result.success) {
				$.gritter.add({
					title: '',
					text: '申请抵扣成功！',
					class_name: 'gritter-item-wrapper gritter-info gritter-center gritter-light'
				});
				setTimeout(function(){location.reload()},3000);
			} else {
				cbms.alert(result.data);
			}
			
		},
		error : function(xhr, textStatus, errorThrown) {
			cbms.closeLoading();
		}
	});
}
$().ready(function() {
	$("#payBtn").click(function() {
		var forms = setlistensSave("#createForm");
		if (!forms)return;
		var id = ($("#department_id").val());//部门Id
		var balance = parseFloat(cbms.convertFloat($("#balance").val(),2));// 账户余额
		var balanceSecondSettlemento = parseFloat(cbms.convertFloat($("#balanceSecondSettlemento").val(),2));//二次结算账户余额
		var deductionPay = parseFloat(cbms.convertFloat($("#amount").val(),2));// 申请抵扣二次结算账户欠款金额
		if (deductionPay > balance) {
			cbms.alert("您申请抵扣的金额不能大于资金账户余额！");
			return;
		}
		if (deductionPay.add(balanceSecondSettlemento) > 0) {
			cbms.alert("您申请抵扣的金额不能大于二次结算欠款！");
			return;
		}
		if (deductionPay== 0) {
			cbms.alert("您申请抵扣的金额不能为零！");
			return;
		}
		var o = {"id":id,"balance":balance,"deductionPay":deductionPay}
		cbms.confirm("确定抵扣吗？",o,sendAjaxFunc);
		
	});
});
