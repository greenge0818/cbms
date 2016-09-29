/**
 * Created by lixiang on 2015/7/27.
 */
function reloadfun(isf){
	if(isf){
		$.gritter.add({
			title: '',
			text: '申请付款成功！',
			class_name: 'gritter-item-wrapper gritter-info gritter-center gritter-light'
		});
		setTimeout(function(){location.reload()},3000);
	}

}
function sendAjaxFun(o){
	cbms.loading();
	var accountId = $("#accountId").val();
	var accountName = $("#accountName").val();
	$.ajax({
		type : "POST",
		url : Context.PATH + "/order/addSecondPayAmount.html",
		data : {
			"id":o.id,
			"deductionPay":o.deductionPay,
			"bankId":o.bankId,
			"accountId":accountId,
			"accountName":accountName,
			"balanceSecondSettlemento":o.balanceSecondSettlemento
		},
		success : function(result) {
			cbms.closeLoading();
			if (result && result.success) {
				$.gritter.add({
					title: '',
					text: '申请提现成功，等待审核！',
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
	$("#payBtnOther").click(function() {
		var forms = setlistensSave("#createForm");
		if (!forms)return;
		var id = ($("#department_id").val());//部门Id
		var accountName = ($("#account_name").text());//客户名字
		//var balance = parseFloat(cbms.convertFloat($("#balanceOther").val(),2));// 账户余额
		var balanceSecondSettlemento = parseFloat(cbms.convertFloat($("#balanceSecondSettlementoOther").val(),2));//二次结算账户余额
		var deductionPay = parseFloat(cbms.convertFloat($("#payAmount").val(),2));// 提现金额
		var bankId = ($("#bankList").val());//银行ID
		if (deductionPay > balanceSecondSettlemento) {
			cbms.alert("您申请提现的金额不能大于二次结算账户余额！");
			return;
		}
		if (deductionPay== 0) {
			cbms.alert("您申请提现的金额不能为零！");
			return;
		}
		if(bankId==null){
			cbms.alert("请选择提现银行！");
			return;
		}
		var o = {"id":id, "deductionPay":deductionPay,"bankId":bankId,"balanceSecondSettlemento":balanceSecondSettlemento};
		cbms.confirm("确定申请提现吗？",o,sendAjaxFun);
		
	});
});























