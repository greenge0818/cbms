var _dtQutotionOrderItems;
//全局变量
var _tableSellerIds = {
	selectedIds:[], //数据行的卖家id行
    selectedtds:[] //数据行的卖家id行
}


$(document).ready(function() {
	
	if( QuotationPage.ID ==""){
		cbms.alert("当前报价信息为空，请先生成报价单！！！");
		return;
	}
	
	var url = Context.PATH + "/smartmatch/quotation/getQuotationInfo.html";
	_dtQutotionOrderItems = jQuery("#dynamic-table").DataTable({
		"processing" : false,
		"serverSide" : false,
		"searching" : false,
		"ordering" : false,
		"paging" : false,
		"bInfo" : false,
		"autoWidth": true,
		"ajax" : {
			"url" : url,
			"type" : "POST",
			data : function(d) {
				return $.extend({}, d, {
					 id:QuotationPage.ID
				});
			}
		},
		columns : [
	     {data : 'sellerName'},
	     {data : 'categoryName'},
	     {data : 'categoryName'},
		 {data : 'categoryName'},
		 {data : 'materialName'},
		 {data : 'spec'},
		 {data : 'factoryName'},
		 {data : 'cityName'},
		 {data : 'warehouseName'},
		 {data : 'quantity'},
		 {data : 'weight',render:function (e) {return e.toFixed(3);}},
		 {data : 'weightConcept'},
		 {data : 'dealPrice'},
		 {data : 'costPrice'},
		 {data : 'totalAmount'},
		 {data : 'totalAmount'},
		 {defaultContent:""}
		],
		footerCallback: function (row, data, start, end, display) {
			//$("#dynamic-table th").css("background-color","#E87716").css("color","#000000");
			
			
        },
        fnDrawCallback:function(aData){
        	orderTotal();
        },
        fnRowCallback: function (nRow, aData, iDataIndex) {
            // operation button
            $('td:last', nRow).html(generateOptHtml(nRow,aData));
            $('td:eq(1)', nRow).html(generateDeptHtml(aData));
            $('td:eq(2)', nRow).html(generateContactHtml(aData));
            //$('td:eq(9)', nRow).html(generateQuantityHtml(aData));
            $('td:eq(11)', nRow).html(generateWpHtml(aData));
            $('td:eq(12)', nRow).html(generateDetHtml(aData));
            $('td:eq(13)', nRow).html(generateCostHtml(aData));
            
            $('td:eq(14)', nRow).html(generateAmountHtml(aData,'amount'));
            $('td:eq(15)', nRow).html(generateAmountHtml(aData,'costamount'));
        },
		
		
	});

	//tab点击内容切换
	$(document).on("click", ".quotation-li", function(){
		$(this).closest("ul").find("li").removeClass("active");
		$(this).closest("li").addClass("active");

		if($(this).closest("li").hasClass("current")){
			$(".currentQuotation").removeClass("none");
			$(".historyQuotation").addClass("none");
		}else{
			$(".historyQuotation").removeClass("none");
			$(".currentQuotation").addClass("none");
		}
	});

    //表格表头颜色
	$(".dynamic-table th").css("background-color","#E87716").css("color","#000000");

	//底部按钮处理
	$(document).on("click", ".btn-event", function(){
		var url;
		//返回
		if($(this).hasClass("returnPage")){
			url=Context.PATH +"/smartmatch/inquiryorder/create.html?id="+QuotationPage.purchaseOrderId;
		}
		//开单
		if($(this).hasClass("openOrder")){

			cbms.loading();
			$.ajax({
				type: 'post',
				data: {
					"quotationOrderId":QuotationPage.ID   //报价单id
				},
				url: Context.PATH + "/smartmatch/purchaseorder/preopen.html",
				error: function (s) {
					cbms.closeLoading();
				},
				success: function (result) {
					cbms.closeLoading();
					if(result.success) {
						if(result.success){
							var requrimentcode = result.data;
							url=QuotationPage.orderDomain+"/order/"+QuotationPage.purchaseOrderId+"/create.html";
							if(requrimentcode != null){
								url+='?requirementCode='+requrimentcode;
							}
							window.location.href=url;
						}else{
							cbms.alert(result.data);
						}
					}else{
						cbms.alert(result.data);
					}
				}
			});

		}
		//推送
		if($(this).hasClass("push")){
			cbms.loading();
			$.ajax({
				type: 'post',
				data: {
					"quotationOrderId":QuotationPage.ID   //报价单id
				},
				url: Context.PATH + "/smartmatch/purchaseorder/push.html",
				error: function (s) {
					cbms.closeLoading();
				},
				success: function (result) {
					cbms.closeLoading();
					if(result.success) {
						utils.showMsg(result.data, null, null, 2000);
						cbms.closeDialog();
					}else{
						cbms.alert(result.data);
					}
				}
			});
		}
		//继续询价
		if($(this).hasClass("continueInquiry")){
			url=Context.PATH +"/smartmatch/purchaseorder/created.html?id="+QuotationPage.purchaseOrderId;
		}

		if(url){
			window.location.href=url;
		}
	});

	//关闭
	$(document).on("click", ".closed", function () {
		var ele = '<div class="dialog-m" id="dialog">' +
			'<p><label>关闭原因　　：</label><select id="reasonSelect"><option value="0">价格不合适</option><option value="1">没找到货</option>' +
			'<option value="2">交货地不合适</option><option value="3">货不全</option><option value="4">其他</option></select></p>' +
			'<p id="reasonDiv"><label>输入其他原因：</label><textarea id="reason"></textarea></p>' +
			'<div class="btn-bar text-center"><button id="commit" type="button" class="btn btn-primary btn-sm">关闭</button>&nbsp;<button type="button" class="btn btn-default btn-sm" id="cancel">取消</button></div></div>';
		cbms.getDialog("采购单关闭", ele);
		
		$("#reasonDiv").hide();

		$("#reasonSelect").change(function () {
			var select = $('#reasonSelect').children('option:selected').val();
			if (select != 4) {
				$("#reasonDiv").hide();
			} else {
				$("#reasonDiv").show();
			}
		});
		$("#dialog").on("click", "#cancel", function () {
			cbms.closeDialog();
		});

		$("#dialog").on("click", "#commit", function () {
			var selected = $("#reasonSelect").children("option:selected");
			var reason;
			if($(selected).val() == 4) {
				reason = $("#reason").val();
			}else{
				reason = $(selected).html();
			}
			if(reason == ""){
				utils.showMsg("请填写关闭理由", null, "error", 2000);
				return false;
			}
			$.ajax({
				type: 'post',
				url: Context.PATH + "/smartmatch/purchaseorder/"+ QuotationPage.purchaseOrderId +"/close.html",
				data: {
					reason: reason
				},
				error: function (s) {
				},
				success: function (result) {
					if(result.success) {
						utils.showMsg("关闭成功", null, null, 2000);
						cbms.closeDialog();
						window.location.href=Context.PATH +"/smartmatch/purchaseorder/list.html?tabIndex=2";
					}else{
						cbms.alert(result.data);
					}
				}
			});
		});
	});

	//改派
	$(document).on("click", ".change", function () {
		$("#reassign_purchase_orderid").val(QuotationPage.purchaseOrderId);
		cbms.getDialog("改派理由","#reassign_dialog");
	});
	$(document).on("change", "#assign_reason_select", function () {
		var select = $(this).val();
		if (select != '其他') {
			$("#assign_reason_panel").hide();
		} else {
			$("#assign_reason_panel").show();
		}
	});

	$(document).on("click", "#btnreassign", function () {
		var reason = "";
		var checkReason = $("#assign_reason_select").val();
		if (checkReason == '其他') {
			reason = $("#reassign_reason_other_text").val();
			if (reason == '') {
				cbms.alert("请填写改派理由！");
				return false;
			}
		}
		else {
			reason = checkReason;
		}
		var order_id = $("#reassign_purchase_orderid").val();
		var url = Context.PATH + '/smartmatch/purchaseorder/orderReassign.html';
		$.post(url, {id: order_id, remark: reason}, function (re) {
			if (re.success) {
				location.href = Context.PATH + '/smartmatch/purchaseorder/list.html';
			}
			else {
				cbms.alert(re.data);
			}
		});
	});

	$(document).on("click", "#reassign_cancel", function () {
		cbms.closeDialog();
	});
});


function pageTotal(api) {
    var weight = 5,  amount= 7;
    var total = {
        pageTotalWeight: pageTotalColumn(api, weight),
        pageTotalAmount: pageTotalColumn(api, amount)
    }
    return total;
}



$(document).on("click", ".confirm", function(){
	$.ajax({
		type: "POST",
		url: Context.PATH + "/smartmatch/quotation/confirm.html",
		data: {
			id : QuotationPage.ID
		},
		dataType: "json",
		success: function (response) {
			if(response.success){
				location.href = Context.PATH + "/smartmatch/purchaseorder/list.html";
			}else{
				utils.showMsg(response.data, null, "error", 2000);
			}
		}
	});
});


/**
 * 操作列的html代码生成
 * @param id
 * @returns {String}
 */
function generateOptHtml(row,aData) {
	//记录当前的TD的id
	row.id = "_data_tr_"+aData.sellerId+"_"+aData.departmentId+"_"+aData.status;
	$(row).attr("qid",aData.id);
	//判断是否是相同的卖家和部门，如果是，合并操作列的单元格
	var idStr = "oper_bill_td_"+aData.sellerId+"_"+aData.departmentId+"_"+aData.status;
	var index = _tableSellerIds.selectedIds.indexOf(idStr);
	if(index!=-1){//存在相同的卖家
		idStr = "del_" + idStr;
		var tdEle = _tableSellerIds.selectedtds[index];
		tdEle.rowSpan ++;//合并当前单元格
		//删除当前单元格子
		row.deleteCell(16);
		return;
	}else{//不存在相同的卖家
		_tableSellerIds.selectedIds.push(idStr);
		_tableSellerIds.selectedtds.push(row.cells[16]);
		row.cells[16].rowSpan = "1";

	}
	row.cells[16].id = idStr;
	var optHtml = '<div id="q_openOrder_'+aData.sellerId+'" class="fa-hover">';
	
	if(aData.status == '1'){
		optHtml += "<span>已开单</span>";
	}else{
		var openOrderStr ="开单";
		optHtml += "<a id='q_openOrder' href='javascript:void(0);' target='_blank' title='"+openOrderStr+"'>"+openOrderStr+"</a>";
	}
	optHtml += "</div>";

	return optHtml;
}
/**
 * 部门列的html代码生成
 * @param id
 * @returns {String}
 */
function generateDeptHtml(aData) {
    var depts = aData.departments;
    var optHtml = '<select id="seller_department"';
    if(aData.status == '1'){
    	optHtml += ' disabled="disabled"';
    }
    optHtml += '>';
	for (var i = 0; i < depts.length; i++) {
		optHtml += '<option value="'+depts[i].id+'"'; 
		if(depts[i].id == aData.departmentId){
			optHtml += 'selected="selected"';			
		}
			
		optHtml += '>'+depts[i].name+'</option>';
		 
	}
	optHtml += '</select>';
	return optHtml;
}

function generateContactHtml(aData) {
    var depts = aData.departments;
    var optHtml = '<select id="seller_contact"';
    if(aData.status == '1'){
    	optHtml += ' disabled="disabled"';
    }
    optHtml += '>';
	for (var i = 0; i < depts.length; i++) {
		for(var j = 0; j < depts[i].contacts.length;j++){
			optHtml += '<option value="'+depts[i].contacts[j].id+'"'; 
			if(aData.contactId == depts[i].contacts[j].id){
				optHtml += 'selected="selected"';			
			}
			optHtml += '>'+depts[i].contacts[j].name+'</option>';
		}
		 
	}
	optHtml += '</select>';
	return optHtml;
}
/**
 * 计重方式列的html代码生成
 * @param id
 * @returns {String}
 */
function generateWpHtml(aData) {
	var weightConcept = aData.weightConcept;
	var wpvalues = new Array("理计","磅计","抄码");
	var optHtml = '<select id="_select_weightConcept" val="'+weightConcept+'">';
	for (var i = 0; i < wpvalues.length; i++) {
		optHtml += '<option value="'+wpvalues[i]+'"';
		if(weightConcept == wpvalues[i]){
			optHtml += 'selected="selected"';
		}
		optHtml += '>'+wpvalues[i]+'</option>';
	}
	optHtml += '</select>';
	return optHtml;
}
/**
 * 销售价格列的html代码生成
 * @param id
 * @returns {String}
 */
function generateDetHtml(aData) {
	var dealPrice = aData.dealPrice;
	dealPrice = cbms.convertFloat(dealPrice,2);
	var optHtml = '<input type="text" name="dealPrice_inp" value="'+dealPrice+'" must="1" verify="numeric" class="d-text"/>'
	return optHtml;
}

/**
 * 采购价格列的html代码生成
 * @param id
 * @returns {String}
 */
function generateCostHtml(aData) {
	var costPrice = aData.costPrice;
	costPrice = cbms.convertFloat(costPrice,2);
	var optHtml = '<input type="text" name="costPrice_inp" value="'+costPrice+'" must="1" verify="numeric" class="d-text"/>'
	return optHtml;
}

/**
 * 采购价格列的html代码生成
 * @param id
 * @returns {String}
 */
function generateQuantityHtml(aData) {
	var quantity = aData.quantity;
	var optHtml = '<input type="text" name="quantity_inp" value="'+quantity+'" must="1" verify="numeric" class="d-text"/>'
	return optHtml;
}
/**
 * 总价的生成	
 * @param id
 * @returns {String}
 */
function generateAmountHtml(aData,cloumn){
	var htmlstr ='';
	if(cloumn == 'costamount'){
		var price = aData.costPrice*aData.weight;
		price = cbms.convertFloat(price,2);
		htmlstr += '<input type="hidden" name="costamount" value="'+price+'"><span name="costamounttext">'+price+'</span>';
	}else if(cloumn = 'amount'){
		var price = aData.dealPrice*aData.weight;
		price = cbms.convertFloat(price,2);
		htmlstr += '<input type="hidden" name="amount" value="'+price+'"><span name="amounttext">'+price+'</span>';
	}
	
	return htmlstr;
}

//计算单条资源总金额
function calculationAmount(row) {
    var dealPrice = 0, costPrice = 0, weight = 0, amount = 0, costAmount = 0;

    var temp_dealPrice = row.find("input[name='dealPrice_inp']").val();
    var temp_costPrice = row.find("input[name='costPrice_inp']").val();
    //var temp_weight = row.find("input[name='weight']").val();
    var temp_weight = $(row).find("td").eq(10).text();
    
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
    
    
    //根据缓存中的数据对象中的值
	_dtQutotionOrderItems.row(row).data().dealPrice = dealPrice;
	_dtQutotionOrderItems.row(row).data().costPrice = costPrice;
}

// 触发统计
$("#dynamic-table").on("input", "input[name='costPrice_inp'],input[name='dealPrice_inp']", function () {
    var currentRow = $(this).parents('tr:first');     // 获取当前行
    calculationAmount(currentRow);
    orderTotal();

});

//订单统计
function orderTotal() {
    var totalQuantity = 0, totalWeight = 0, totalResourceAmount = 0, shipFee = 0, totalAmount = 0;

    var trSize = $("#dynamic-table tbody tr").size();
    for (var i = 0; i < trSize; i++) {
        var quantity = 0, weight = 0, dealPrice = 0, amount = 0;

        var current = $("#dynamic-table tbody tr").eq(i);
        var temp_quantity = $.trim(current.find("td").eq(9).text());
        var temp_weight = $.trim(current.find("td").eq(10).text());
        var temp_price = $.trim(current.find("input[name='dealPrice_inp']").val());

        if (temp_quantity != "") {
            quantity = parseInt(temp_quantity);
        }
        if (temp_weight != "") {
            weight = parseFloat(cbms.convertFloat(temp_weight));
        }
        if (temp_price != "") {
            dealPrice = parseFloat(cbms.convertFloat(temp_price, 2));
        }
        //修改生产问题 ，订单总金额计算错误
        amount = parseFloat(cbms.convertFloat((dealPrice.mul(weight)), 2));
        totalQuantity += quantity;
        totalWeight = totalWeight.add(weight);
        totalResourceAmount = totalResourceAmount.add(amount);
    }
    var temp_shipFee = $("#shipFee").val();
    shipFee = parseFloat(cbms.convertFloat(temp_shipFee, 2));

    if (isNaN(totalQuantity))
        totalQuantity = 0;
    if (isNaN(totalWeight))
        totalWeight = 0;
    if (isNaN(totalResourceAmount))
        totalResourceAmount = 0;
    if (isNaN(shipFee))
        shipFee = 0;

    $('#totalQuantity').text(totalQuantity);
    $("#totalWeight").text(cbms.convertFloat(totalWeight));
    $("#totalResourceAmount").text(formatMoney(totalResourceAmount, 2));

    totalAmount = parseFloat(cbms.convertFloat((totalResourceAmount.add(shipFee)), 2));
    $("#totalAmount").val(totalAmount);

}
//开单链接点击
$(document).on("click", "#q_openOrder", function(){
	//modify by zhoucai@prcsteel.com 2016-8-18
	var requirementCode = $('#requirementCode').val();
	
	var tId = $(this).closest('tr').attr("id");
	var trArray = $("tr[id='"+tId+"']");
	var selectIds = new Array();
	for(var i = 0 ; i < trArray.length ; i++){
		var qid = $(trArray[i]).attr("qid");
		selectIds.push(qid);
	}
	var selectStr = selectIds.join("_");
	
	var forms = setlistensSave("#quo_info_form");
	if (!forms)return;
	//开单前调用后台保存当前的报价单详情信息
	var allData = _dtQutotionOrderItems.rows().data();
	var datas = "[";
	for(var i=0;i<allData.length;i++){
		datas += JSON.stringify(allData[i]);
		datas +=",";
	}
	datas = datas.substring(0,datas.length-1);
	datas += "]";
	var isSuccess = true;
	$.ajax({
        type : "POST",
        url : Context.PATH + "/smartmatch/quotation/preOpenBill.html",
        data : datas,
        contentType:"application/json",
        dataType : "json",
        success : function(response) {
            if (response.success) {
            	//跳转到开单页面进行开单操作。
            	//http://localhost:8006/order/order/224_225/createByQuotationItems.html
        		var url = QuotationPage.orderDomain+'/order/'+selectStr+'/createByQuotationItems.html?requirementCode='+requirementCode;
        		window.location.href=url;
            }else{
            	isSuccess = false;
            	cbms.closeLoading();
            	cbms.alert("保存当前报价单详情失败！");
            }
        }
    });
	
});


$(document).on("change", "#seller_contact", function(){
	var row = $(this).parents('tr:first');     // 获取当前行
	var contactId = $(this).find("option:selected").val();
	_dtQutotionOrderItems.row(row).data().contactId = contactId;
	
});


$(document).on("change", "#seller_department", function(){
	var row = $(this).parents('tr:first');     // 获取当前行
	var deptId = $(this).find("option:selected").val();
	 //根据缓存中的数据对象中的值
	_dtQutotionOrderItems.row(row).data().departmentId = deptId;
	
	//如果是已开单修改部门，则不做处理
	var row_status = _dtQutotionOrderItems.row(row).data().status;
	if(row_status == '1'){
		return;	
	}
	
	//开单前调用后台保存当前的报价单详情信息
	var allData = _dtQutotionOrderItems.rows().data();
	var datas = "[";
	for(var i=0;i<allData.length;i++){
		datas += JSON.stringify(allData[i]);
		datas +=",";
	}
	datas = datas.substring(0,datas.length-1);
	datas += "]";
	
	$.ajax({
        type : "POST",
        url : Context.PATH + "/smartmatch/quotation/preOpenBill.html",
        data : datas,
        contentType:"application/json",
        dataType : "json",
        success : function(response) {
            if (response.success) {
            	//保存成功
            }else{
            	isSuccess = false;
            	cbms.closeLoading();
            	cbms.alert("保存当前卖家部门失败！");
            }
            
            _tableSellerIds.selectedIds=[];
            _tableSellerIds.selectedtds=[];
            //重置所有单元格子
            var trs =  $("#dynamic-table tbody").children();
            for(var ii = 0; ii<trs.length;ii++){
            	if(trs[ii].cells.length == 16){
            		trs[ii].insertCell(16);
            	}
            }
            _dtQutotionOrderItems.draw();
            
        }
    });
});


$(document).on("change", "#_select_weightConcept", function(){
	var row = $(this).parents('tr:first');     // 获取当前行
	var wp =  $(this).find("option:selected").val();
	 //根据缓存中的数据对象中的值
	_dtQutotionOrderItems.row(row).data().weightConcept = wp;
});

