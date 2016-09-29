/**
 * Created by lixiang on 2016/3/22.
 */
$(document).ready(function() {
	$('#dynamic-table').DataTable({
		"dom" : 'lrTt<"bottom"p>i<"clear">',
		"pageLength": 100, //每页记录数
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "ajax": {
            "url" : Context.PATH + "/accountinfo/settlementsearch.html",
            "type" : "POST",
            data : function(d) {
                return $.extend({}, d, {
                	startTime:$("#startTime").val(),
                	endTime:$("#endTime").val(),
                	accountId:$("#department_id").val()//在这里实际就是部门id
                });
            },
            "dataSrc": function ( json ) {
                  return json.data;
                }
        },
        columns: [
          {data: 'serialTime',"mRender":function(e,t,f){return formatDay(e);}},
          {data: 'associationType'},
          {data: 'consignOrderCode'},
          {data: 'applyType'},
          {data: 'amount',"mRender":function(e,t,f){return formatMoney(e);}},
          {data: 'currentBalance',"mRender":function(e,t,f){return formatMoney(e);}},
          {data: 'applyerName'}]
	});
});

//搜索
$(document).on("click", "#searchBtn", function(){
	reloadList();
});

/**
 * 日期转换
 * @param data
 * @returns {String}
 */
function formatDay(data) {
    var dt = new Date(data);
    var time = dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate() + " " +
        ((dt.getHours()/1)<10?("0"+dt.getHours()):dt.getHours()) + ":" + ((dt.getMinutes()/1)<10?("0"+dt.getMinutes()):dt.getMinutes()) + ":" + ((dt.getSeconds()/1)<10?("0"+dt.getSeconds()):dt.getSeconds());
    return time;
}


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
		url : Context.PATH + "/accountinfo/settlementalapply.html",
		data : {
			"departmentId":o.departmentId,
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
		var departmentId = ($("#department_id").val());//部门Id
		var accountName = ($("#account_name").text());//客户名字
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
		var o = {"departmentId":departmentId, "deductionPay":deductionPay,"bankId":bankId,"balanceSecondSettlemento":balanceSecondSettlemento};
		cbms.confirm("确定申请提现吗？",o,sendAjaxFun);
		
	});
});