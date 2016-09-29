/**
 * 取消审核折让单
 * @author lixiang
 */
$().ready(function() {
	$("#cancelAudit").click(function() {
		cbms.confirm("确定要取消审核通过吗？", null, function() {
			cbms.loading();
			var allowanceId = $("#allowance_id").val();
			var allowanceType = $("#allowance_type").val();
			var buyerAllowanceIds = $("*[name='buyerAllowance_id']").map(function(){return $(this).val()}).get().join(",");
			var state = "cancel";
			$.ajax({
				type : "POST",
				url : Context.PATH + "/allowance/cancel/rebate.html",
				data : {
					"allowanceId" : allowanceId,
					"buyerAllowanceIds" : buyerAllowanceIds.toString(),
					"state":state
				},
				success : function(result) {
					cbms.closeLoading();
					if (result && result.success) {
						cbms.alert("取消审核成功！", function() {
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
							cbms.alert("取消审核失败！");
						}
					}
				},
				error : function(xhr, textStatus, errorThrown) {
					cbms.closeLoading();
				}
			});
		});
	});
	
	//保存修改
	$("#saveAmountBtn").click(saveAmount);
	
	//监听修改
	$(document).on("keyup","input[name='allowance_money']", function(){
		amountInputChange($(this));
	});
	
});

var _items={};

function saveAmount(){
	
	var result = setlistensSave($("#buyer-allowance-table tbody tr"));
	if (!result) return;
	
	var param = null;
	var allowanceId = $("#allowance_id").val();
	for(var id in _items){
		if(!param)
			param = [];
		param.push({
			"id": id,
			"amount":_items[id].amount,
			"allowanceId":allowanceId
		});
	}
	
	//send save
	if(param){
		$.ajax({
			type : "POST",
			url : Context.PATH + "/allowance/updateallowanceamount.html",
			data : JSON.stringify(param),
			dataType:"json",
			contentType : 'application/json;charset=utf-8', //设置请求头信息  
			success:function(data){
				if(data.success)
					cbms.alert("更新成功！");
				else{
					cbms.alert(data.data);
				}
			}
		});
			
	}
	
}

function amountInputChange(input){
	//如果输入的不是数字，则不触发事件
	var parent = input.closest("td");
	
	parent.children("div").remove();
	
	var result = setlistensSave(parent);
	if (!result) return;
	
	var id = input.siblings("input[name='id']").val();
	_items[id] = {amount:input.val()};
}

