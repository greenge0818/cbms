/**
 * 修改买家折让单（审核不通过后）
 * Created by lcw on 2015/11/19.
 */
$().ready(function () {
    $("#buyerForm").verifyForm();
    sumAmount();
    // 触发统计
    $("#dynamic-table").on("input", "select[name='amountSymbol']", function () {
        sumAmount();
    });

    $("#dynamic-table").on("input", "input[name='allowanceAmount']", function () {
        var value = parseFloat($.trim($(this).val()));
        if($.trim($(this).val()) === '' || isNaN(value) || value < 0){
            return false;
        }
        sumAmount();
    });

    $("#dynamic-table").on("blur", "input[name='allowanceAmount']", function () {
        if($.trim($(this).val()) === ''){
            $(this).val('0.00');
            sumAmount();
        }
    });

    $("#submit").click(function () {
        if (!setlistensSave())return false;
        cbms.confirm("确定提交审核？", null, function () {
            allowanceSubmit();
        });
    });

    /**
     * 关闭
     */
    $("#close").click(function () {
        cbms.confirm("确定要关闭该折让单吗？", null, function () {
            cbms.loading();
            $.ajax({
                type: "POST",
                url: Context.PATH + "/allowance/" + $("#allowanceId").val() + "/closeorder.html",
                data: {},
                success: function (result) {
                    cbms.closeLoading();
                    if (result && result.success) {
                        cbms.alert("关闭成功！", function () {
                            location.href = Context.PATH + "/allowance/list/buyer.html";
                        });
                    } else {
                        $("#close").prop("disabled", false);
                        if (result.data != null) {
                            cbms.alert(result.data);
                        } else {
                            cbms.alert("关闭失败！");
                        }
                    }
                },
                error: function (xhr, textStatus, errorThrown) {
                    cbms.closeLoading();
                    $("#close").prop("disabled", false);
                }
            });
        });
    });
});

function sumAmount() {
    var totalActualWeight = 0;
    var totalActualMoney = 0;
    var totalAllowanceMoney = 0;
    var totalUnAllowanceMoney = 0;
    $(".orderList-tbody > tr").each(function () {
        // 实提重量
        totalActualWeight = totalActualWeight + parseFloat($(this).find("input[name='actualWeight']").val());
        // 实提金额
        var actualMoney = parseFloat(parseFloat($(this).find("input[name='actualAmount']").val()).toFixed(2));
        totalActualMoney = totalActualMoney + actualMoney;
        // 折让金额
        var allowanceAmount = parseFloat(parseFloat($(this).find("input[name='allowanceAmount']").val()).toFixed(2));
        var allowanceMoney = parseFloat($(this).find("[name='amountSymbol']").val() + allowanceAmount.toFixed(2));
        $("input[name='allowanceMoney']",this).val(allowanceMoney);
        totalAllowanceMoney = totalAllowanceMoney + allowanceMoney;
        // 折后金额
        var unAllowanceMoney = actualMoney + allowanceMoney;
        totalUnAllowanceMoney = totalUnAllowanceMoney + unAllowanceMoney;
        $("input[name='discountedMoney']",this).val(unAllowanceMoney);
        $("span[name='discountedMoneyDisplay']",this).text(formatMoney(unAllowanceMoney,2));
    });
    $('#totalActualWeight').text(totalActualWeight.toFixed(6));
    $('#totalActualMoneyDisplay').text(formatMoney(totalActualMoney,2));
    $('#totalAllowanceMoneyDisplay').text(formatMoney(totalAllowanceMoney,2));
    $('#totalUnAllowanceMoneyDisplay').text(formatMoney(totalUnAllowanceMoney,2));
    $('#totalActualMoney').val(totalActualMoney.toFixed(2));
    $('#totalAllowanceMoney').val(totalAllowanceMoney.toFixed(2));
    $('#totalUnAllowanceMoney').val(totalUnAllowanceMoney.toFixed(2));
}

function allowanceSubmit() {
	var note = $("#allowance_note").val();
    var allowance = gather();
    if(!allowance){
       return;
    }

    var paramJson = JSON.stringify(allowance);
    cbms.loading();
    $.ajax({
        type: 'POST',
        dataType: "JSON",
        url: Context.PATH + "/allowance/savebuyeredit.html",
        data: {
            "id": $("#allowanceId").val(),
            "paramJson": paramJson,
            "allowanceType": "buyer",
            "status": "to_audit",
            "note" : note,
            "allowanceManner": $("#allowanceManner").val()
        },
        error: function (s) {
            cbms.closeLoading();
            $("#submit").prop("disabled", false);
        },
        success: function (result) {
            cbms.closeLoading();
            if (result) {
                if (result.success) {
                    cbms.alert("提交审核成功", function () {
                        window.location.href = Context.PATH + "/allowance/list/buyer.html";
                    });
                } else {
                    $("#submit").prop("disabled", false);
                    cbms.alert(result.data);
                }
            } else {
                cbms.alert("提交审核失败");
                $("#submit").prop("disabled", false);
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
        var allowanceWeight = $(this).attr("allowanceweight") || 0;
        var buyerName = $(this).attr("buyername");
        var buyerDeptId = $(this).attr("buyerdeptid");
        var buyerDeptName = $(this).attr("buyerdeptname");

        // 行参数
        var orderTime = $(this).find(".orderTime").text();
        var orderCode = $(this).find(".orderCode").text();
        var nsortName = $(this).find(".nsortName").text();
        var material = $(this).find(".material").text();
        var spec = $(this).find(".spec").text();
        var actualWeight = $(this).find("input[name='actualWeight']").val();
        var actualAmount = $(this).find("input[name='actualAmount']").val();
        var allowanceAmount = $(this).find("input[name='allowanceMoney']").val();
        if (allowanceAmount == 0 || allowanceAmount == '-0') {
            allowanceAmount = 0;
        }
        var discountedMoney = $(this).find("input[name='discountedMoney']").val();

        var allowanceManner = $("#allowanceManner").val();
        if(allowanceManner == "amount") {
        	if(Math.abs(allowanceAmount) < 0 || Math.abs(allowanceAmount) >= actualAmount ){
        		cbms.alert("金额必须大于等于0小于实提金额");
        		orderList = null;
                return false;
            }
        }else {
			if(Math.abs(allowanceAmount) < 0 || Math.abs(allowanceAmount) > actualAmount ){
				cbms.alert("金额必须大于等于0小于等于实提金额");
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
        orderObj.buyerName = buyerName;
        orderObj.nsortName = nsortName;
        orderObj.material = material;
        orderObj.spec = spec;
        orderObj.actualWeight = actualWeight;
        orderObj.allowanceWeight = allowanceWeight;
        orderObj.unAllowanceWeight = 0;
        orderObj.actualAmount = actualAmount;
        orderObj.allowanceAmount = allowanceAmount;
        orderObj.unAllowanceAmount = discountedMoney;

        orderList.push(orderObj);
    });
    return orderList;
}