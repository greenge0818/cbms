/**
 * 合同变更页面
 * Created by lichaowei on 2016/8/9.
 */
$().ready(function () {
    orderTotal();
    setSelectValue();
    bindCity();
    $("#changeForm").verifyForm();

    // 触发统计
    $("#itemsdatabody").on("input", "input[name='costPrice'],input[name='dealPrice'],input[name='weight'],input[name='quantity']", function () {
        var currentRow = $(this).parents('tr:first');     // 获取当前行`
        calculationAmount(currentRow);
        orderTotal();
    });

    // 复制资源
    $("#itemsdatabody").on("click", "a[copy='copy']", function () {
        // 当前行
        var currentRow = $(this).parents('tr:first');
        // 复制当前行
        var cloneRow = $(currentRow).clone();
        // 获取省市value
        var provinceVal = 0, cityVal = 0;
        var temp_provinceVal = $(currentRow).find("[name='province']").val();
        if (temp_provinceVal != "") {
            provinceVal = parseInt(temp_provinceVal);
        }
        var temp_cityVal = $(currentRow).find("[name='city']").val();
        if (temp_cityVal != "") {
            cityVal = parseInt(temp_cityVal);
        }

        // 修改赋值
        var weightConceptVal = $(currentRow).find("[name='weightConcept']").val();
        $(cloneRow).find("[name='weightConcept']").val(weightConceptVal);

        var acceptDraftVal = $(currentRow).find("[name='acceptDraft']").val();
        $(cloneRow).find("[name='acceptDraft']").val("");
        $(cloneRow).attr("itemid","0");
        // 追加在当前行后面
        $(currentRow).after($(cloneRow));

        // 绑定省市联动
        bindRegionData($(cloneRow).find("[name='province']"), $(cloneRow).find("[name='city']"), null, provinceVal, cityVal);

        updateIndex();
        orderTotal();
    });

    // 删除资源
    $("#itemsdatabody").on("click", "a[delete='delete']", function () {
        var rowsSize = $("#itemsdatabody tr").size();
        var currentRow = $(this).parents('tr:first');
        var orderItemId = $(this).closest("tr").attr("itemid");//获取当前行详情id
        if (orderItemId == 0) {
        	 msg (rowsSize, currentRow);
        } else {
        	$.ajax({
                type: 'post',
                url: Context.PATH + "/order/changecontract/getPickedQuantity.html",
                data : {"orderItemId":orderItemId},
                success: function (result) {
                    if (result) {
                        if (result.success) {
                        	 msg (rowsSize, currentRow);
                        } else {
                            cbms.alert(result.data);
                        }
                    } else {
                        cbms.alert("查询订单详情失败！");
                    }
                }
            }); 
        }
    });
    
    function msg (rowsSize, currentRow) {
		// 只有一行数据时不能被删除
	    if (rowsSize > 1) {
	        cbms.confirm("确认删除本条资源？", null, function () {
	            $(currentRow).remove();
	            updateIndex();
	            orderTotal();
	        });
	    } else {
	        cbms.alert("不能全部删除！");
	    }
    }

    // 提交
    $("#submit").click(function(){
        var saveData = {};
        saveData['orderChange.orderId'] = $("#orderId").val();
        saveData['orderChange.deliveryType'] = $("#deliveryType").val();
        saveData['orderChange.transArea'] = $("#transArea").val();
        saveData['orderChange.deliveryEndDate'] = $("#endTime").val();
        saveData['orderChange.feeTaker'] = $("#feeTaker").val();
        saveData['orderChange.shipFee'] = $("#shipFee").val();
        saveData['orderChange.outboundTaker'] = $("#outboundTaker").val();
        saveData['orderChange.outboundFee'] = $("#outboundFee").val();
        saveData['orderChange.contractAddress'] = $("#contractAddress").val();

        var itemsIndex = 0;
        $("#itemsdatabody tr").each(function () {
            var row = $(this);
            var prefix = 'orderItemsChanges[' + itemsIndex + '].';
            saveData[prefix + 'orderItemId'] = $(row).attr("itemid");
            saveData[prefix + 'orderId'] = $("#orderId").val();
            saveData[prefix + 'sellerId'] = $(row).find("input[name='sellerId']").val();
            saveData[prefix + 'sellerName'] = $.trim($(row).find("label[name='sellerName']").text());
            saveData[prefix + 'departmentId'] = $(row).find("input[name='departmentId']").val();
            saveData[prefix + 'departmentName'] = $(row).find("label[name='departmentName']").text();
            saveData[prefix + 'contactId'] = $(row).find("input[name='contactId']").val();
            saveData[prefix + 'contactName'] = $(row).find("label[name='contactName']").text();
            saveData[prefix + 'nsortName'] = $.trim($(row).find("input[name='nsortName']").val());
            saveData[prefix + 'material'] = $.trim($(row).find("input[name='material']").val());
            saveData[prefix + 'spec'] = $.trim($(row).find("input[name='spec']").val());
            saveData[prefix + 'factory'] = $.trim($(row).find("input[name='factory']").val());
            saveData[prefix + 'city'] = $(row).find("select[name='city']").find("option:selected").text();
            saveData[prefix + 'warehouse'] = $.trim($(row).find("input[name='warehouse']").val());
            saveData[prefix + 'quantity'] = parseInt($.trim($(row).find("input[name='quantity']").val()));
            saveData[prefix + 'weight'] = cbms.convertFloat($(row).find("input[name='weight']").val());
            saveData[prefix + 'weightConcept'] = $(row).find("select[name='weightConcept']").val();
            saveData[prefix + 'costPrice'] = cbms.subStr($(row).find("input[name='costPrice']").val(), 2);
            saveData[prefix + 'dealPrice'] = cbms.subStr($(row).find("input[name='dealPrice']").val(), 2);
            saveData[prefix + 'amount'] = cbms.convertFloat(($(row).find("input[name='amount']").val()), 2);
            saveData[prefix + 'strappingNum'] = $.trim($(row).find("input[name='strappingNum']").val()); //捆包号
            var isPayedByAcceptDraft = $(row).find("select[name='isPayedByAcceptDraft']").val(); //是否使用银票
            var acceptDraftId = 0;  //银票票号id
            var acceptDraftCode = "";  //银票票号
            if (isPayedByAcceptDraft == undefined) {
                isPayedByAcceptDraft = 0;
            }
            // 若没有选择银票支付或者票号则为0，若选择了则为1
            if (isPayedByAcceptDraft != 0) {
                if (isPayedByAcceptDraft > 0) {
                    acceptDraftId = $.trim($(row).find("select[name='isPayedByAcceptDraft']").val());  //银票票号id
                    acceptDraftCode = $.trim($(row).find("select[name='isPayedByAcceptDraft']").find("option:selected").text());  //银票票号
                }
                isPayedByAcceptDraft = 1;  //是否使用银票0否1是
            }
            saveData[prefix + 'isPayedByAcceptDraft'] = isPayedByAcceptDraft;
            saveData[prefix + 'acceptDraftId'] = acceptDraftId;
            saveData[prefix + 'acceptDraftCode'] = acceptDraftCode;

            itemsIndex++;

        });

        cbms.confirm("确定提交吗？", null, function () {
            $(this).prop("disabled", true);
            cbms.loading();
            $.ajax({
                type: 'post',
                url: Context.PATH + "/order/changecontract/savechange.html",
                data: saveData,
                error: function (s) {
                    $("#submit").prop("disabled", false);
                    cbms.closeLoading();
                }
                , success: function (result) {
                    cbms.closeLoading();
                    if (result) {
                        if (result.success) {
                            cbms.gritter("合同修改完成，请等待审核！",true,function(){
                                window.location.href = Context.PATH + "/order/changecontract/changelist.html";
                            })
                        }
                        else {
                            $("#submit").prop("disabled", false);
                            cbms.alert(result.data);
                        }
                    } else {
                        $("#submit").prop("disabled", false);
                        cbms.alert("合同修改失败");
                    }
                }
            });
        });
    });
});


// 设置对应的选择值（select控件等）
function setSelectValue() {
    $("select[val]").each(function () {
        var value = $(this).attr("val");
        if (value != "") {
            $(this).val(value);
        }
    });

    $("select[text]").each(function () {
        var select=$(this);
        var text = $(select).attr("text");
        if (text != "") {
            var selIndex=0;
            $(select).find("option").each(function(){
                if($(this).text() == text) {
                    return false;
                }
                selIndex++;
            });
            $(select)[0].selectedIndex = selIndex;
        }
    });
}

// 计算单条资源总金额
function calculationAmount(row) {
    var dealPrice = 0, costPrice = 0, weight = 0, amount = 0, costAmount = 0;

    var temp_dealPrice = row.find("input[name='dealPrice']").val();
    var temp_costPrice = row.find("input[name='costPrice']").val();
    var temp_weight = row.find("input[name='weight']").val();

    dealPrice = parseFloat(cbms.subStr(temp_dealPrice,2));
    costPrice = parseFloat(cbms.subStr(temp_costPrice, 2));
    weight = parseFloat(cbms.convertFloat(temp_weight));
    amount = parseFloat(cbms.convertFloat((dealPrice.mul(weight)), 2));
    costAmount = parseFloat(cbms.convertFloat((costPrice.mul(weight)), 2));
    if (!isNaN(amount)) {
        $(row).find("input[name='amount']").val(amount);
        $(row).find("span[name='amounttext']").text(formatMoney(amount, 2));
    }
    if (!isNaN(costAmount)) {
        $(row).find("input[name='costamount']").val(costAmount);
        $(row).find("span[name='costamounttext']").text(formatMoney(costAmount, 2));
    }
}

// 订单统计
function orderTotal() {
    var totalQuantity = 0, totalWeight = 0, totalCostAmount = 0, totalDealAmount = 0;

    var trSize = $("#itemsdatabody tr").size();
    for (var i = 0; i < trSize; i++) {
        var quantity = 0, weight = 0, dealPrice = 0, costPrice = 0, dealAmount = 0, costAmount = 0;

        var current = $("#itemsdatabody tr").eq(i);
        var temp_quantity = $.trim(current.find("input[name='quantity']").val());
        var temp_weight = $.trim(current.find("input[name='weight']").val());
        var temp_deal_price = $.trim(current.find("input[name='dealPrice']").val());
        var temp_cost_price = $.trim(current.find("input[name='costPrice']").val());

        if (temp_quantity != "") {
            quantity = parseInt(temp_quantity);
        }
        if (temp_weight != "") {
            weight = parseFloat(cbms.convertFloat(temp_weight));
        }
        if (temp_deal_price != "") {
            dealPrice = parseFloat(cbms.convertFloat(temp_deal_price, 2));
        }
        if (temp_cost_price != "") {
            costPrice = parseFloat(cbms.convertFloat(temp_cost_price, 2));
        }
        //修改生产问题 ，订单总金额计算错误
        dealAmount = parseFloat(cbms.convertFloat((dealPrice.mul(weight)), 2));
        costAmount = parseFloat(cbms.convertFloat((costPrice.mul(weight)), 2));
        totalQuantity += quantity;
        totalWeight = totalWeight.add(weight);
        totalDealAmount = totalDealAmount.add(dealAmount);
        totalCostAmount = totalCostAmount.add(costAmount);
    }
    if (isNaN(totalQuantity))
        totalQuantity = 0;
    if (isNaN(totalWeight))
        totalWeight = 0;
    if (isNaN(totalDealAmount))
        totalDealAmount = 0;

    $('#totalQuantity').text(totalQuantity);
    $("#totalWeight").val(cbms.convertFloat(totalWeight));
    $("#totalWeightText").text(formatMoney(totalWeight,6));
    $("#totalDealAmountText").text(formatMoney(totalDealAmount, 2));
    $("#totalCostAmountText").text(formatMoney(totalCostAmount, 2));
    $("#totalDealAmount").val(totalDealAmount);
    $("#totalCostAmount").val(totalCostAmount);

    $("#totalAmount").val(totalDealAmount);

}

// 更新table的序列值
function updateIndex() {
    var rows = $("#itemsdatabody tr");
    var rowSize = $(rows).size();
    for (var i = 0; i < rowSize; i++) {
        $(rows).eq(i).find("td").eq(0).text(i + 1);
    }
}

// 绑定省市数据
function bindCity() {
    var rows = $("#itemsdatabody tr");
    var rowSize = $(rows).size();
    for (var i = 0; i < rowSize; i++) {
        var row = rows[i];
        // 绑定省市数据
        var province = $(row).find("select[name='province']");
        var city = $(row).find("select[name='city']");
        var cityName = $(city).attr("city");
        if (cityName != undefined && cityName != "") {
            var cityInfo = getCity(cityName);
            if (cityInfo != null) {
                // 绑定省市联动数据
                bindRegionData(province, city, null, cityInfo.provinceId, cityInfo.id);
            }
        }
        else {
            bindRegionData(province, city);
        }
    }
}
