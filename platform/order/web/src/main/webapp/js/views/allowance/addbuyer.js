var _sellerDt;

$().ready(function () {
	$("#allowanceManner").val("amount");
	
	$("#allowanceFrom").verifyForm();
	changeAllowanceManner();
	initAmountAndWeight();
	sumWeightAndAmount();
	$('.orderList-tbody').on("change", ".weightSymbol", function (){
		sumWeightAndAmount();
    });
	$('.orderList-tbody').on("input", ".weightValue", function (){
		sumWeightAndAmount();
    });
	$('.orderList-tbody').on("change", ".amountSymbol", function (){
		sumWeightAndAmount();
    });
	$('.orderList-tbody').on("input", ".amountValue", function (){
		sumWeightAndAmount();
    });
	
    // 提交审核
    $(document).on("click", "#allowanceSubmit", function(){
    	if(!CheckData()) {
    		return;
    	};
        cbms.confirm("确认提交审核？", null, function () {
        	allowanceSave("to_audit","提交审核");
        });
    });
    // 保存
    $(document).on("click", "#allowanceSave", function(){
    	if(!CheckData()) {
    		return;
    	};
        cbms.confirm("确认保存？", null, function () {
        	allowanceSave("to_submit","保存");
        });
    });
    //失去焦点 如果输入为空 默认为0
    $('.orderList-tbody').on("blur", ".weightValue", function (){
        if($.trim($(this).val()) === ''){
            $(this).val('0.000000');
            sumWeightAndAmount();
        }
    });

    //失去焦点 如果输入为空 默认为0
    $('.orderList-tbody').on("blur", ".amountValue", function (){
        if($.trim($(this).val()) === ''){
            $(this).val('0.00');
            sumWeightAndAmount();
        }
    });

	
});

function sumWeightAndAmount() {
	var totalActualWeight = 0;
	var totalAllowanceWeight = 0;
	var totalUnAllowanceWeight = 0;
	var totalActualAmount = 0;
	var totalAllowanceAmount = 0;
	var totalUnAllowanceAmount = 0;
	$(".orderList-tbody > tr").each(function () {
		var actualAmount = parseFloat($(this).find(".actualAmount").text().replace(/,/g,""))
		totalActualAmount = totalActualAmount + actualAmount;
		var allowanceAmount = parseFloat($(this).find(".amountSymbol").val()+$(this).find(".amountValue").val().replace(/,/g,""));
		$(this).find(".allowanceAmount").text(allowanceAmount);
		totalAllowanceAmount = totalAllowanceAmount + allowanceAmount;
		var unAllowanceAmount = actualAmount+allowanceAmount;
		$(this).find(".unAllowanceAmount").text(formatMoney(unAllowanceAmount,2));
		totalUnAllowanceAmount = totalUnAllowanceAmount +unAllowanceAmount;
		
		var actualWeight = parseFloat($(this).find(".actualWeight").text().replace(/,/g,""));
		totalActualWeight = totalActualWeight + actualWeight;
		var allowanceWeight = parseFloat($(this).find(".weightSymbol").val()+$(this).find(".weightValue").val().replace(/,/g,""));
		$(this).find(".allowanceWeight").text(allowanceWeight);
		totalAllowanceWeight = totalAllowanceWeight + allowanceWeight;
		var unAllowanceWeight = actualWeight+allowanceWeight;
		$(this).find(".unAllowanceWeight").text(unAllowanceWeight.toFixed(6));
		totalUnAllowanceWeight = totalUnAllowanceWeight + unAllowanceWeight;
	});
	$('#totalActualWeight').text(totalActualWeight.toFixed(6));
	$('#totalAllowanceWeight').text(totalAllowanceWeight.toFixed(6));
	$('#totalUnAllowanceWeight').text(totalUnAllowanceWeight.toFixed(6));
	$('#totalActualAmount').text(formatMoney(totalActualAmount,2));
	$('#totalAllowanceAmount').text(formatMoney(totalAllowanceAmount,2));
	$('#totalUnAllowanceAmount').text(formatMoney(totalUnAllowanceAmount,2));
}

function initAmountAndWeight() {
	var allowanceManner = $("#allowanceManner").val();
	$(".weightSymbol").val("-");
	$(".amountSymbol").val("-");
	$(".orderList-tbody > tr").each(function () {
		$(this).find(".weightValue").val(0);
		$(this).find(".amountValue").val(0);
	});
}

function CheckData() {
	if (!setlistensSave())return false;
	return true;
}

function allowanceSave(status,msg) {
	var allowance = gather();
	if(!allowance){
		return;
	}
	var note = $("#allowance_note").val();
    var paramJson = JSON.stringify(allowance);
    cbms.loading();
    $.ajax({
        type: 'POST',
        dataType:"JSON",
        url: Context.PATH + "/allowance/savebuyallowance.html",
        data: {
            "paramJson" : paramJson,
            "allowanceType" : "buyer",
            "status" : status,
            "note" : note,
            "allowanceManner" : $("#allowanceManner").val()
        },
        error: function (s) {
            cbms.closeLoading();
        },
        success: function (result) {
            cbms.closeLoading();
            if (result) {
                if (result.success) {
                        cbms.alert(msg+"成功", function(){
                            window.location.href = Context.PATH + "/allowance/list/buyer.html";
                    });
                } else {
                	cbms.alert(result.data);
                }
            } else {
                    cbms.alert(msg+"失败");
            }
        }
    });
}

function gather() {
	var orderList = [];
	$(".orderList-tbody > tr").each(function () {
		var orderObj = {};
		// 行属性
		var orderId = $(this).attr("orderid");
		var orderDetailId = $(this).attr("orderdetailid");
		var contractCode = $(this).attr("contractcode");
		var buyerId = $(this).attr("buyerid");
		var sellerId = $(this).attr("sellerid");
		var sellerName = $(this).attr("sellername");
		var totalWeight = $(this).attr("totalweight");
		var totalAmount = $(this).attr("totalamount");
		var totalActualWeight = $(this).attr("totalactualweight");
		var totalActualAmount = $(this).attr("totalactualamount");
		var totalQuantity = $(this).attr("totalquantity");
		var buyerName = $(this).attr("buyername");
		var buyerDeptId = $(this).attr("buyerdeptid");
		var buyerDeptName = $(this).attr("buyerdeptname");

		// 行参数
		var orderTime = $(this).find(".orderTime").text();
		var orderCode = $(this).find(".orderCode").text();
		var nsortName = $(this).find(".nsortName").text();
		var material = $(this).find(".material").text();
		var spec = $(this).find(".spec").text();
		var actualWeight = $(this).find(".actualWeight").text();
		var allowanceWeight = $(this).find(".allowanceWeight").text();
		if(allowanceWeight == 0 || allowanceWeight == '-0') {
			allowanceWeight = 0;
		}
		var unAllowanceWeight = $(this).find(".unAllowanceWeight").text().replace(/,/g,"");
		var actualAmount = $(this).find(".actualAmount").text();
		var allowanceAmount = $(this).find(".allowanceAmount").text();
		if(allowanceAmount == 0 || allowanceAmount == '-0') {
			allowanceAmount = 0;
		}
		var unAllowanceAmount = $(this).find(".unAllowanceAmount").text().replace(/,/g,"");
		
		var allowanceManner = $("#allowanceManner").val();
        if(allowanceManner == "amount") {
        	if(Math.abs(allowanceAmount) < 0 || Math.abs(allowanceAmount) > actualAmount ){
        		cbms.alert("金额必须大于等于0小于实提金额");
        		orderList = null;
                return false;
            }
        }else if(allowanceManner == "weight") {
			if(Math.abs(allowanceWeight) < 0 || Math.abs(allowanceWeight) > actualWeight ){
				cbms.alert("重量必须大于等于0小于实提重量");
				orderList = null;
				 return false;
            }
		}else {
			if(Math.abs(allowanceAmount) < 0 || Math.abs(allowanceAmount) > actualAmount ){
				cbms.alert("金额必须大于等于0小于等于实提金额");
				orderList = null;
				 return false;
            }
			if(Math.abs(allowanceWeight) < 0 || Math.abs(allowanceWeight) > actualWeight ){
				cbms.alert("重量必须大于等于0小于等于实提重量");
				orderList = null;
				 return false;
            }
		}
		orderObj.orderId = orderId;
		orderObj.orderDetailId = orderDetailId;
		orderObj.contractCode = contractCode;
		orderObj.buyerId = buyerId;
		orderObj.sellerId = sellerId;
		orderObj.sellerName = sellerName;
		orderObj.totalWeight = totalWeight;
		orderObj.totalAmount = totalAmount;
		orderObj.totalActualWeight = totalActualWeight;
		orderObj.totalActualAmount = totalActualAmount;
		orderObj.totalQuantity = totalQuantity;
		orderObj.buyerName = buyerName;
		orderObj.buyerDeptId = buyerDeptId;
		orderObj.buyerDeptName = buyerDeptName;

		orderObj.orderTime = orderTime;
		orderObj.orderCode = orderCode;
		orderObj.nsortName = nsortName;
		orderObj.material = material;
		orderObj.spec = spec;
		orderObj.actualWeight = actualWeight;
		orderObj.allowanceWeight = allowanceWeight;
		orderObj.unAllowanceWeight = unAllowanceWeight;
		orderObj.actualAmount = actualAmount;
		orderObj.allowanceAmount = allowanceAmount;
		orderObj.unAllowanceAmount = unAllowanceAmount;
		
		orderList.push(orderObj);
	});
	return orderList;
}

function changeAllowanceManner() {
	var allowanceManner = $("#allowanceManner").val();
	if(allowanceManner == 'amount') {
		$(".allowanceMannerWeight").hide();
		$(".allowanceMannerAmount").show();
	}else if(allowanceManner == 'weight') {
		$(".allowanceMannerWeight").show();
		$(".allowanceMannerAmount").hide();
	}else if(allowanceManner == 'all') {
		$(".allowanceMannerWeight").show();
		$(".allowanceMannerAmount").show();
	}
}


