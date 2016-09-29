/**
 * Created by lcw on 2015/8/3.
 */
jQuery(function ($) {
	var invoiceId = $("#invoiceId").val();
	var url = Context.PATH + "/invoice/in/getstepdata.html";
	$.post(url,{invoiceId: invoiceId}, function (re) {
		if (re.success) {
			var stepmainlist = [], steppaylist = [];
			//stepindexmain 上一行 ，下一行stepindexpay
	        var stepindexmain = 0, stepindexpay = 0;
	        for (var i in re.data) {
	        	var tempcontent = re.data[i].operatorName + "(" + re.data[i].operatorRoleName + ")" + re.data[i].operatorMobile + "<br>";
	        	if (re.data[i].created != null) {
	        		var date = new Date(re.data[i].created);
	        		if (typeof(date) != "undefined") {
	                       var datestr = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
	                       tempcontent += datestr;
	                }
	        	}
	            var stepitem = {
	                title: re.data[i].invoiceInStatusName,
	                content: tempcontent
	            };
	        	if (re.data[i].invoiceInStatus == "ROLLBACK") {
                   steppaylist.push(stepitem);
                   if (re.data[i].created != null) {
                       stepindexpay++;
                   }
               } else {
                   stepmainlist.push(stepitem);
                   if (re.data[i].created != null) {
                       stepindexmain++;
                   }
               }
	        }
	        var stepmaindata = $.parseJSON(JSON.stringify(stepmainlist));
            var steppaydata = $.parseJSON(JSON.stringify(steppaylist));
            $(".ystep4").loadStep({
                size: "large",
                color: "blue",
                steps: stepmaindata
            });
//            $(".ystep2").loadStep({
//                vertical: "2",
//                size: "large",
//                color: "blue",
//                steps: steppaydata
//            });

            if (stepindexpay > 0) {
                $(".ystep-vertical").find("span").addClass("ystep-active-highlight");
            }
            $(".ystep4").setStep(stepindexmain);
//            for (var i in re.data) {
//            	if (re.data[i].invoiceInStatus == "ROLLBACK") {
//            		if (re.data[i].created != null) {
//            			$(".ystep2").setStep(-2);
//            		}
//            	}
//            	if (re.data[i].invoiceInStatus == "ALREADY" && re.data[i].invoiceInStatus == "ROLLBACK") {
//	            	if (re.data[i].created != null) {
//	            		$(".ystep2").setStep(-1);
//	            	}
//	            }
//            }
		}
	});
});

var tableId = "dynamic-table";  // table
var filterChar = ["详见"];
var _stataus_related = "<span style='color:Green;'>已关联</span>";
var _status_unrelated= "<span style='color:Red;'>待关联</span>";
var allowAmountDeviation = 0.01;
var allowWeightDeviation = 0;
var _currentMaxDate = 0;
var _weightFixedLength = 6;
var _amountFixedLength = 2;
var _orderItemOrginal = {};//{'1':{weight:1,amount:1}}
var _canceledRelatedOrderItemIds = [];//被取消关联的ids
var _relateTableColumns = {
		getTd : function(row,column){
			return $(column.filter,row);
		},
		getTh : function(row,column){
			return $('th:eq(' + column.index + ')',row);
		},
		choose : {
			index : 0,
			filter : 'td:eq(0)'
		},
		orderDateTime : {
			index : 1,
			filter : 'td:eq(1)'
		},
		orderNumber : {
			index : 2,
			filter : 'td:eq(2)'
		},
		contractCode : {
			index : 3,
			filter : 'td:eq(3)'
		},
		buyerName : {
			index : 4,
			filter : 'td:eq(4)'
		},
		nsortName : {
			index : 5,
			filter : 'td:eq(5)'
		},
		materials : {
			index : 6,
			filter : 'td:eq(6)'
		},
		spec : {
			index : 7,
			filter : 'td:eq(7)'
		},
		costPrice : {
		index : 8,
		filter : 'td:eq(8)'
	},
		unInvoiceWeight : {
			index : 9,
			filter : 'td:eq(9)'
		},
		unInvoiceAmount : {
			index : 10,
			filter : 'td:eq(10)'
		},
		increaseWeight : {
			index : 11,
			filter : 'td:eq(11)'
		},
		increaseAmount : {
			index : 12,
			filter : 'td:eq(12)'
		}
};
var _uniqIdIndex = 10;
function generateId(prefix){
	if(!prefix){
		prefix = "uniq_id_";
	}
	_uniqIdIndex++;
	var timestamp = new Date().getTime() + _uniqIdIndex;
	var id = prefix + timestamp;
	for(;$("#" + id).size()>0;id += ++_uniqIdIndex){
	}
	return id;
}
$(document).ready(function () {
	allowAmountDeviation = parseFloat($("#allowAmountDeviation").val());
	allowWeightDeviation = parseFloat($("#allowWeightDeviation").val());
	filterChar = $("#filterText").val().split(";");
	if(isInEditModel() && !isInReceivedStatus()){
		//$("#areaCode,#invoiceCode,#invoiceDate").attr("disabled","disabled").attr("readonly","readonly");
		$("#addNewRow").addClass("none");
	}
	if(isInEditModel() && !isFinanceChecked()){
		$("#allowanceChk").attr("disabled","disabled").attr("readonly","readonly");
	}
	var inAllowanceId = $("#inAllowanceId").val();
	if (inAllowanceId != "" && inAllowanceId != "0") {
		getAllowanceDetail();
	}

    var table = $("#" + tableId);

    table.on("blur", "input[name='nsortNameComb']", function () {
        var currentVal = $.trim($(this).val());
        // 货物或应税劳务合计不能有详见清单字样
        for(var i=0;i<filterChar.length;i++){
        	if(currentVal.indexOf(filterChar[i]) >= 0){
        		$(this).val("");
        		cbms.alert("‘货物或应税劳务、服务名称’ 不能包含 ‘" + filterChar[i] + "’ 字样");
        		break;
        	}
        }
        currentVal = currentVal.replace(/\t/g,' ').replace(/\s+/g,' ');
        $(this).val(currentVal);
    });

    table.on("focus", "input[name='weight'],input[name='noTaxAmount'],input[name='taxAmount']", function () {
        var currentVal = convertFloat($(this).val());
        if (currentVal == 0) {
            $(this).val("");
        }
    });
    
    $("#invoiceAmount").focus(function(){
    	var currentVal = convertFloat($(this).val());
        if (currentVal == 0) {
            $(this).val("");
        }
    });

    table.on("blur", "input[name='weight'],input[name='noTaxAmount'],input[name='taxAmount']", function () {
        var currentVal = $.trim($(this).val());
        if (currentVal == "") {
            $(this).val("0");
        }
    });

    // 计算价税合计事件
    table.on("input", "input[name='weight'],input[name='noTaxAmount'],input[name='taxAmount']", function () {
        var currentRow = $(this).parent().parent();
        var verify = calculateAmount(currentRow);
        if (verify == true) {
            invoiceTotal();
        }
    });

    // 修改
    table.on("click", "button[name='edit']", function () {
        var currentRow = $(this).closest("tr");
        var statusValue = currentRow.find("input[name='status']").val();
        if(getRelatedOrderItemIds(currentRow).length > 0){
            cbms.confirm("修改会导致关联取消，是否继续？",null,function(){
            	$("#invoiceAmount").removeAttr("disabled").removeAttr("readonly");
				$("input[name=relatedOrderItemIds]",currentRow).val("");
            	if(!isInEditModel() || isInReceivedStatus()){
            		// 在新增模式下，需要清空掉关联关系
					addToCancelList(currentRow);
            		currentRow.prop("releationCanceled",true);
            	}
            	setToEdit(currentRow);

				//修改设置状态为待关联
				$("span[name=status]",currentRow).html(_status_unrelated);
            });
        }else{
        	setToEdit(currentRow);
        	$("#invoiceAmount").removeAttr("disabled").removeAttr("readonly");
        }
    });

    // 复制
    table.on("click", "button[name='copy']", function () {
        var currentRow = $(this).closest('tr');// 当前行
        if (isEmptyRow(currentRow)) {
            cbms.alert("请先输入当前行数据！");
            return;
        }
        // 复制当前行
        var crow = currentRow.clone();
        
        //删除备注行，并且给已经备注高度+1
        handlerMemoInput(crow);
        
        currentRow.after(crow);
        crow.attr("id",generateId("inoice_row_"));
        $("select[name=typeOfSpec]",crow).val($("select[name=typeOfSpec]",currentRow).val());
        $("input[name=relatedOrderItemIds]",crow).val("");
        $("span[name=status]",crow).html(_status_unrelated);
        $("button[name=relate]",crow).removeClass("none");
        $("button[name=cancelelation]",crow).addClass("none");
		$("input[name=invoiceDetailId]",crow).val("");//清空
        setToEdit(crow);
        invoiceTotal();
    });
    
    // 添加新行
    table.on("click","#addNewRow",function(){
    	var nr = $("#firstRow").clone();
    	
    	//删除备注这一行
    	handlerMemoInput(nr);
    	
    	nr.appendTo("#dynamic-table-body");
    	nr.attr("id",generateId("inoice_row_"));
    	$("input",nr).val("");
    	$("span[name=price]",nr).text("0");
    	$("span[name=amount]",nr).text("0");
    	$("span[name=status]",nr).html(_status_unrelated);
		$("select[name=typeOfSpec]",nr).find("option[value='rebate']").remove();
    });

    // 删除
    table.on("click", "button[name='delete']", function () {
        var rowsSize = $("#dynamic-table tbody tr").size();
        if(rowsSize <= 1){
        	cbms.alert("不能全部删除！");
        	return;
        }
        var currentRow = $(this).closest('tr');
        
        //删除的时候，备注不用删除
        //如果删除行里面有备注行，则另存一份副本
        //将备注放入到下一行，并且高度-1
        var memoTd = currentRow.find(".memoTd");
        var memoVal = null;
        if(memoTd && memoTd.length>0){
        	memoVal = memoTd.find(".memo").val();
        	memoTd = memoTd.clone();
        }
        
        function handlerRemoveMemo(){
        	//处理备注td不为空，则说明删除行里面包含了备注td，刚将备注td加入到第一个tr里面，并将tr行高-1， textarea行高-1
            //如果备注td为空，则直接：将tr行高-1， textarea行高-1，
            if(memoTd && memoTd.length>0){
            	$("#dynamic-table-body tr").eq(0).append(memoTd);
            }else{
            	memoTd = $("#dynamic-table-body .memoTd");
            }
            
            var rowspan = memoTd.attr("rowspan");
        	memoTd.attr("rowspan", rowspan-1);
        	
        	var memotextarea = memoTd.find(".memo");
        	var rows= memotextarea.attr("rows");
        	memotextarea.attr("rows", rows-1);
        	if(memoVal)
        		memotextarea.val(memoVal);
        }
        
        // 只有一行数据时不能被删除
        // 空行直接删除
        if (isEmptyRow(currentRow)) {
            $(currentRow).remove();
            handlerRemoveMemo();
        } else {
            cbms.confirm("确认删除本条资源？", null, function () {
            	addToCancelList(currentRow);
                $(currentRow).remove();
                invoiceTotal();
                handlerRemoveMemo();
            });
        }
        invoiceTotal();
        
    });
    //查看关联
    table.on("click", "button[name='viewRelate']", function () {
    	var currentRow = $(this).closest("tr");
    	cbms.getDialog("查看关联","#assignDialog");
    	copyRowToRelateContainer(currentRow);
		$("#assign_searchBar,#assign_controlBar").addClass("none");
		if(getRelatedOrderItemIds(currentRow).length>0){
	    	$("#assign_searchBar input[type=text],#selectDepartment").val("");
			bindAssignOrderItems(_canceledRelatedOrderItemIds);
		}
    });
    // 关联
    table.on("click", "button[name='relate']", function () {
    	
    	//用来检测品名，和别名不为空
    	if (!setlistensSave("#dynamic-table-body tr td:first"))return;
    	
    	var currentRow = $(this).closest("tr");
    	/*if($.trim($("input[name=spec]",currentRow).val()) == ""){
    		cbms.alert("请先输入“规格型号”");
    		return;
    	}*/
		if(isNaN($.trim($("input[name=weight]",currentRow).val()))){
			cbms.alert("请输入正确的“数量”");
			return;
		}
		if(isNaN($.trim($("input[name=noTaxAmount]",currentRow).val()))){
			cbms.alert("请输入正确的“金额”");
			return;
		}
		if(isNaN($.trim($("input[name=taxAmount]",currentRow).val()))){
			cbms.alert("请输入正确的“税额”");
			return;
		}
    	if($.trim($("#invoiceDate").val()) == ''){
    		cbms.alert("请选择开票日期");
    		return;
    	}
    	cbms.getDialog("关联交易单<span class='red'>(未结算的交易单不显示)</span>","#assignDialog");
    	copyRowToRelateContainer(currentRow);
		$("#assign_searchBar,#assign_controlBar").removeClass("none");
		//设置下拉框默认选择的部门
		var depName = $("#depName").text();
		if(depName !=""){
	    	$("#selectDepartment option").each(function(){
	 		   if($(this).text() == depName){
	 		      $(this).attr('selected', 'selected');
	 		   }
	 		});
		}
    	bindAssignOrderItems(_canceledRelatedOrderItemIds);
    });

    // 取消关联
    table.on("click", "button[name=cancelelation]", function () {
    	var btn = $(this);
		var currentRow = btn.closest("tr");
    	cbms.confirm("确定要取消该记录的关联吗？",null,function(){
			addToCancelList(currentRow);
    		$("input[name=relatedOrderItemIds]",currentRow).val("");
        	$("span[name=status]",currentRow).html(_status_unrelated);
        	currentRow.prop("releationCanceled",true);
        	btn.addClass("none");
        	btn = null;
        	setToEdit(currentRow);
    	});
    });
    // 规则类型选择类型变化
    table.on("change", "select[name=typeOfSpec]", function () {
    	var currentRow = $(this).closest("tr");
    	var currVal = $(this).val();
    	var cs = $(this).attr("cstate");
    	if(isRebateRow(currentRow)){
    		if(cs != "rebating"){
        		setRebateRow(currentRow);
        		$(this).attr("cstate","rebating");
    		}
    	}else{
    		if(cs == "rebating"){
        		setNormalRow(currentRow);
        		$(this).attr("cstate","normal");
    		}
    	}
    });
    
    // 保存发票信息
    $("#submitInvoice").click(function () {
		//add by wangxianjun 若卖家设置成进项票黑名单，控制进项票不能提交
		$.ajax({
			type: 'post',
			url: Context.PATH + "/invoice/in/checksellersubmit.html",
			data: {
				sellerName: $("#sellerName").text()
			},
			error: function (s) {
				cbms.alert("提交进项票，服务器出错！");
			}
			, success: function (result) {
				if ($("#invoiceId").val() != '' && result && !result.success) {
					cbms.alert(result.data);
				} else {
					cbms.confirm("确定提交吗？", null, function () {
						if (!setlistensSave("#pageContent"))return;

						if ($("#dynamic-table-body tr").length == 0) {
							cbms.alert("请录入进项发票详细信息 ！");
							return;
						}
						var checkedDate = new Date($("#invoiceDate").val()).getTime();
						if (checkedDate < _currentMaxDate) {
							cbms.alert("开票日期早于关联订单的开单日期，请修改开票日期或重新关联交易单！");
							return;
						}
						var verify = true;
						var currentRow;
						var weight = 0, noTaxAmount = 0, taxAmount = 0, amount = 0;
						var totalAmount = 0;
						var invoiceDetails = [];
						var invoiceData = {
							'invoiceIn.areaCode': $.trim($("#areaCode").val()),
							'invoiceIn.sellerId': $("#sellerId").val(),
							'invoiceIn.sellerName': $("#sellerName").text(),
							'invoiceIn.departmentId': $("#departmentId").val(),
							'invoiceIn.departmentName': $("#departmentName").val(),
							'invoiceIn.code': $.trim($("#invoiceCode").val()),
							'invoiceIn.invoiceDate': $.trim($("#invoiceDate").val()),
							'invoiceIn.id': $("#invoiceId").val(),
							'invoiceIn.memo': $("#dynamic-table-body .memo").val()
						};

						var detailIndex = 0;
						$("#dynamic-table-body tr").each(function () {
							currentRow = $(this);
							weight = parseFloat(convertFloat(currentRow.find("input[name='weight']").val()).toFixed(_weightFixedLength));
							noTaxAmount = parseFloat(convertFloat(currentRow.find("input[name='noTaxAmount']").val()).toFixed(_amountFixedLength));
							taxAmount = parseFloat(convertFloat(currentRow.find("input[name='taxAmount']").val()).toFixed(_amountFixedLength));
							amount = parseFloat((noTaxAmount + taxAmount).toFixed(_amountFixedLength));
							if ($(currentRow).attr("id") == "allowanceRow") {
								totalAmount -= amount;
								return true;
							}
							// 重量和金额不能一个为0，一个不为0
							if (weight > 0 && ( noTaxAmount == 0 || taxAmount == 0)
								|| noTaxAmount > 0 && ( weight == 0 || taxAmount == 0)
								|| taxAmount > 0 && ( noTaxAmount == 0 || weight == 0)) {
								cbms.alert("重量和金额不能一个为0，一个不为0 ！");
								verify = false;
								return false;
							}

							// 编辑模式下（即财务审核时）不需要关联也可以提交、编辑模式下（即内勤在修改发票信息时）需要全部关联才可提交
							if ((!isInEditModel() || isInWaitStatus() || isInReceivedStatus()) && getRelatedOrderItemIds(currentRow).length == 0) {
								cbms.alert("需要全部关联后才可提交！");
								verify = false;
								return false;
							}
							var nsortNameComb = $.trim($("input[name=nsortNameComb]", currentRow).val());
							if (!nsortNameComb || nsortNameComb == '') {
								cbms.alert("‘货物或应税劳务、服务名称’不能为空");
								verify = false;
								return false;
							}
							totalAmount += noTaxAmount + taxAmount;//累加价税合计
							totalAmount = parseFloat(totalAmount.toFixed(_amountFixedLength));
							var nsortNameCombArr = getNsortMaterialsArray(nsortNameComb);
							var prefix = 'invoiceDetails[' + detailIndex + '].';
							invoiceData[prefix + 'invoiceDetailId'] = $("input[name=invoiceDetailId]", currentRow).val();
							invoiceData[prefix + 'nsortNameComb'] = nsortNameComb;
							invoiceData[prefix + 'nsortName'] = nsortNameComb;
							invoiceData[prefix + 'material'] = nsortNameCombArr[1];
							invoiceData[prefix + 'typeOfSpec'] = $("select[name=typeOfSpec]", currentRow).val();
							invoiceData[prefix + 'spec'] = $("input[name=spec]", currentRow).val();
							invoiceData[prefix + 'weight'] = $("input[name=weight]", currentRow).val();
							invoiceData[prefix + 'noTaxAmount'] = $("input[name=noTaxAmount]", currentRow).val();
							invoiceData[prefix + 'taxAmount'] = $("input[name=taxAmount]", currentRow).val();

							invoiceData[prefix + 'categoryName'] = $("input[name=categoryName]", currentRow).val();
							invoiceData[prefix + 'categoryId'] = $("input[name=categoryId]", currentRow).val();
							invoiceData[prefix + 'aliasId'] = $("input[name=aliasId]", currentRow).val();

							var roIds = getRelatedOrderItemIds(currentRow);
							for (var i = 0; i < roIds.length; i++) {
								var rop = prefix + "orderItems[" + i + "].";
								invoiceData[rop + 'orderId'] = roIds[i].orderId;
								invoiceData[rop + 'orderitemId'] = roIds[i].orderitemId;
								invoiceData[rop + 'increaseWeight'] = roIds[i].increaseWeight;
								invoiceData[rop + 'increaseAmount'] = roIds[i].increaseAmount;
								invoiceData[rop + 'originalWeight'] = parseFloat(roIds[i].originalWeight.toFixed(_weightFixedLength));
								invoiceData[rop + 'originalAmount'] = parseFloat(roIds[i].originalAmount.toFixed(_amountFixedLength));
							}

							detailIndex++;
						});
						if (verify == false) {
							return;
						}
						var invoiceAmount = parseFloat(convertFloat($.trim($("#invoiceAmount").val())).toFixed(_amountFixedLength));
						var invoiceDate = $.trim($("#invoiceDate").val());

						if (invoiceAmount == 0) {
							cbms.alert("发票价税合计不能为0！");
							$("#invoiceAmount").focus();
							return;
						}

						if (invoiceAmount != totalAmount) {
							cbms.alert("价税合计输入有误，请检查并修改！");
							$("#invoiceAmount").focus();
							return;
						}
						invoiceData["invoiceIn.totalAmount"] = totalAmount;
						// 折让单相关
						var allowanceRow = $("#allowanceRow");
						if (allowanceRow && allowanceRow.length > 0) {
							var allowanceNoTaxAmount = 0 - parseFloat(convertTextToFloat($("input[name='noTaxAmount']", allowanceRow).val()).toFixed(_weightFixedLength));
							var allowanceTaxAmount = 0 - parseFloat(convertTextToFloat($("input[name='taxAmount']", allowanceRow).val()).toFixed(_weightFixedLength));
							invoiceData['invoiceInAllowance.cargoName'] = $("input[name='nsortNameComb']", allowanceRow).val();
							invoiceData['invoiceInAllowance.noTaxAmount'] = allowanceNoTaxAmount;
							invoiceData['invoiceInAllowance.taxAmount'] = allowanceTaxAmount;
							invoiceData['invoiceInAllowance.amount'] = allowanceNoTaxAmount + allowanceTaxAmount;
							invoiceData['invoiceInAllowance.relationStatus'] = 'hasrelation';

							var allowanceStatus = $("span[name=status]", allowanceRow).text();
							if ('待关联' == allowanceStatus) {
								invoiceData['invoiceInAllowance.relationStatus'] = 'toberelation';
							}

							var allowanceitemsStr = $(allowanceRow).attr("allowanceitems");
							if (allowanceitemsStr != null && allowanceitemsStr != "") {
								var allowanceItems = $.parseJSON(allowanceitemsStr);
								for (var i = 0; i < allowanceItems.length; i++) {
									var item = 'inAllowanceItems[' + i + '].';
									invoiceData[item + 'allowanceId'] = allowanceItems[i].allowanceId;
									invoiceData[item + 'orderItemId'] = allowanceItems[i].orderItemId;
									invoiceData[item + 'allowanceOrderDetailItemId'] = allowanceItems[i].allowanceOrderDetailItemId;
									invoiceData[item + 'allowanceAmount'] = parseFloat(allowanceItems[i].allowanceAmount.toFixed(_weightFixedLength));
								}
							}
						}
						invoiceData.isCheck = isInEditModel() && !isInReceivedStatus() && (_pageContext._from != "send");
						cbms.loading();
						$.ajax({
							type: 'post',
							url: Context.PATH + "/invoice/in/saveinvoice.html",
							data: invoiceData,
							error: function (s) {
								cbms.closeLoading();
							}
							, success: function (result) {
								cbms.closeLoading();
								if (result) {
									if (result.success) {
										cbms.alert("发票信息提交成功", function () {
											if (isInEditModel()) {
												if (isInReceivedStatus()) {
													window.location.href = Context.PATH + "/invoice/in/send.html";
												} else {
													window.location.href = Context.PATH + "/invoice/in/confirm.html";
												}
											} else {
												window.location.href = Context.PATH + "/invoice/in/awaits.html";
											}
										});
									} else {
										cbms.alert(result.data);
									}
								} else {
									cbms.alert("录入发票失败");
								}
							}
						});
					});
				}
			}
		});
    });

    $(document.body).on("click","#assign_cancelSelect",function(){//取消选择
    	$("#relateTable input:checked").each(function(){
    		$(this).prop("checked",false);
    		computeRowIncrease($(this).closest("tr"));
    	});
    	computeRemain();
    });
    $(document.body).on("click","#assign_sysSuggestion",function(){//系统推荐
    	$("#relateTable input:checked").each(function(){
    		$(this).prop("checked",false);
    		computeRowIncrease($(this).closest("tr"));
    	});
    	$("#relateTable input[matched=true]").each(function(){
    		$(this).prop("checked",true);
    		var currentRow = $(this).closest("tr");
    		_relateTableColumns.getTd(currentRow, _relateTableColumns.increaseWeight).text($(this).attr("matched_iwt"));
    		_relateTableColumns.getTd(currentRow, _relateTableColumns.increaseAmount).text($(this).attr("matched_iat"));
    	});
    	computeRemain();
    });
    $(document.body).on("click","#assign_enter",function(){
    	var rmWeight = parseFloat(convertTextToFloat($.trim($("#assign_remainWeight").text())).toFixed(_weightFixedLength));
    	var rmAmount = parseFloat(convertTextToFloat($.trim($("#assign_remainAmount").text())).toFixed(_amountFixedLength));
    	if(Math.abs(rmWeight) > allowWeightDeviation){
    		cbms.alert("剩余到票重量未分配完，无法关联");
    		return;
    	}
    	if(Math.abs(rmAmount) > allowAmountDeviation){
    		cbms.alert("剩余到票金额未分配完，无法关联");
    		return;
    	}
    	var targetRow = $("#" + $("#assign_currentRowId").val());
    	var ids = [];
    	$("#relateTableBody input:checked").each(function(){
    		var currentRow = $(this).closest("tr");
    		
    		ids.push({
    			orderId:$(this).attr("orderId"),
    			orderitemId:$(this).val(),
    			increaseWeight:convertTextToFloat(_relateTableColumns.getTd(currentRow, _relateTableColumns.increaseWeight).text()),
    			increaseAmount:convertTextToFloat(_relateTableColumns.getTd(currentRow, _relateTableColumns.increaseAmount).text()),
    			originalAmount:convertTextToFloat($("td:first",currentRow).attr("originalAmount")),
    			originalWeight:convertTextToFloat($("td:first",currentRow).attr("originalWeight"))
    		});
    		var cr = new Date($.trim(_relateTableColumns.getTd(currentRow, _relateTableColumns.orderDateTime).text())).getTime();
    		if(cr > _currentMaxDate){
    			_currentMaxDate = cr;
    		}
    	});
    	$("input[name=relatedOrderItemIds]",targetRow).val(JSON.stringify(ids));
    	$("span[name=status]",targetRow).html(_stataus_related);

		setToReadonly(targetRow, "assign_enter");
    	$("button[name='cancelelation']",targetRow).removeClass("none");

		//关联成功后隐藏“关联”按钮、显示"修改"、"查看关联"按钮
		$("button[name='relate']",targetRow).addClass("none");
		$("button[name='edit']",targetRow).removeClass("none");
		$("button[name='viewRelate']",targetRow).removeClass("none");

    	cbms.closeDialog();
    });
    $(document.body).on("click","#relateTableBody input[type=checkbox]",function(){
    	var currentRow = $(this).closest("tr");

		if(isChangeContract($(currentRow).attr("alterstatus"))){
			cbms.alert("该订单在变更中，请变更成功后再关联！");
			$(this).prop("checked",false);
			return;
		}

		computeRowIncrease(currentRow);
    	computeRemain();

    });
    $(document.body).on("click","#assign_doSearch",function(){// 搜索
		var purchasePrice = $.trim($("#assign_price").val());
		if (purchasePrice != "" && isNaN(purchasePrice)) {
			cbms.alert("请输入正确的“采购单价”");
			return;
		}
		var actualWeight = $.trim($("#assign_weight").val());
		if (actualWeight != "" && isNaN(actualWeight)) {
			cbms.alert("请输入正确的“重量”");
			return;
		}
    	assignOrderItemsTable.ajax.reload();
    });
//    $(document.body).on("click","#assign_clearCondition",function(){// 清空搜索条件
//    	$("#assign_contract,#assign_nsort,#assign_materials,#assign_spec,#assign_beginDate").val("");
//    });
    
    //查找是否进入编辑状态
    if(isInEditModel()){
    	initEditData();
    }else{
    	var row = $("#firstRow").clone().attr("id",generateId("invoice_in_")).appendTo("#dynamic-table-body");
    	$("select[name=typeOfSpec]",row).find("option[value='rebate']").remove();
    }

	// 显示折让行
	$("#allowanceChk").click(function(){
		if ($(this).prop("checked") == true) {
			var row = $("#firstRow").clone();
			
	        //删除备注行，并且给已经备注高度+1
			handlerMemoInput(row);

			var memo = $("#dynamic-table-body .memoTd");
			
			var value = memo.find(".memo").val();
			
			var m = memo.clone();
			memo.remove();
			
			m.find(".memo").val(value);
			row.append(m);
			
			row.prependTo("#dynamic-table-body");
			row.attr("id", "allowanceRow");
			$("input", row).val("");
			$("span[name=price]", row).text("0");
			$("span[name=amount]", row).text("0");
			$("span[name=status]", row).html(_status_unrelated);

			$("select[name=typeOfSpec]", row).val("rebate");
			$("select[name=typeOfSpec]", row).change().attr("disabled", "disabled");

			$("button[name=relate]", row).siblings().addClass("none");
			$("button[name=relate]", row).removeClass("none").attr("name", "allowanceRelate");
			$("button[name='cancelelation']", row).attr("name", "allowanceCancel");
			$("button[name='edit']", row).attr("name", "allowanceEdit");
			$("button[name='viewRelate']", row).attr("name", "allowanceViewRelate");
		}
		else {
			var allowanceRow = $("#allowanceRow");
			
			var memoTd = allowanceRow.find(".memoTd");
			var memoVal =  memoTd.find(".memo").val();
			var memoTdNew = allowanceRow.find(".memoTd").clone();
			
			$("#allowanceRow").remove();
			
        	$("#dynamic-table-body tr").eq(0).append(memoTdNew);
            var rowspan = memoTdNew.attr("rowspan");
            memoTdNew.attr("rowspan", rowspan-1);
        	
        	var memotextarea = memoTdNew.find(".memo");
        	var rows= memotextarea.attr("rows");
        	memotextarea.attr("rows", rows-1);
        	if(memoVal)
        		memotextarea.val(memoVal);
			
		}
		// 取消已关联数据，防止数据异常
		var invoiceRows = table.find("tr[id!='allowanceRow']");
		invoiceRows.each(function () {
			var currentRow = $(this);
			addToCancelList(currentRow);
			$("input[name=relatedOrderItemIds]", currentRow).val("");
			$("span[name=status]", currentRow).html(_status_unrelated);
			currentRow.prop("releationCanceled", true);
			$("input[name=cancelelation]", currentRow).addClass("none");
			setToEdit(currentRow);
			$("#invoiceAmount").removeAttr("disabled").removeAttr("readonly");
			if(isInWaitStatus()){
				$("button[name=relate]",currentRow).removeClass("none");
			}
		});
	});
	// 关联折让
	table.on("click", "button[name='allowanceRelate']", function () {
		var currentRow = $(this).closest("tr");
		var nsortNameComb = $.trim($("input[name=nsortNameComb]",currentRow).val());
		var noTaxAmount = $.trim($("input[name=noTaxAmount]",currentRow).val());
		var taxAmount = $.trim($("input[name=taxAmount]",currentRow).val());
		if(nsortNameComb==""){
			cbms.alert("请输入折让信息");
			return;
		}
		if(isNaN(parseFloat(noTaxAmount))){
			cbms.alert("请输入正确的“金额”");
			return;
		}
		if(isNaN(parseFloat(taxAmount))){
			cbms.alert("请输入正确的“税额”");
			return;
		}
		cbms.getDialog("关联折让单&nbsp;&nbsp;&nbsp;&nbsp;<span>" + $("#sellerName").text() + "</span>", "#allowanceDialog");
		copyRowToAllowanceContainer(currentRow);
		bindAllowanceDetailItems();
		$("#allowanceCancel,#allowanceEnter").removeClass("none");
	});
	// 修改折让
	table.on("click", "button[name='allowanceEdit']", function () {
		var currentRow = $(this).closest("tr");
		if ($(currentRow).attr("allowanceitems").length > 0) {
			cbms.confirm("修改会导致关联取消，是否继续？", null, function () {
				$("#invoiceAmount").removeAttr("disabled").removeAttr("readonly");
				$(currentRow).attr("allowanceitems", "");
				setToAllowanceEdit(currentRow);
				//修改设置状态为待关联
				$("span[name=status]", currentRow).html(_status_unrelated);
			});
		} else {
			setToAllowanceEdit(currentRow);
		}
	});
	// 查看折让关联
	table.on("click", "button[name='allowanceViewRelate']", function () {
		var currentRow = $(this).closest("tr");
		cbms.getDialog("查看关联折让单","#allowanceDialog");
		copyRowToAllowanceContainer(currentRow);
		bindAllowanceDetailItems();
	});
	// 取消折让关联
	table.on("click", "button[name=allowanceCancel]", function () {
		var btn = $(this);
		var currentRow = btn.closest("tr");
		cbms.confirm("确定要取消折让单的关联吗？",null,function(){
			$(currentRow).attr("allowanceitems","");
			$("span[name=status]",currentRow).html(_status_unrelated);
			btn = null;
			setToAllowanceEdit(currentRow);
		});
	});
	// 折让行选中
	$(document.body).on("click","#allowanceTableBody input[type=checkbox]",function(){
		computeAllowanceRemain();
	});

	// 弹层取消折让关联
	$(document.body).on("click", "#allowanceCancel", function () {
		$("#allowanceTable input:checked").each(function(){
			$(this).prop("checked",false);
		});
		computeAllowanceRemain();
	});
	// 弹层确认折让关联
	$(document.body).on("click","#allowanceEnter",function(){
		var rmAmount = parseFloat(convertTextToFloat($.trim($("#allowanceRemainAmount").text())).toFixed(_amountFixedLength));
		if(rmAmount != 0){
			cbms.alert("剩余折让金额未分配完，无法关联");
			return;
		}
		var targetRow = $("#allowanceRow");
		var ids = [];
		$("#allowanceTableBody input:checked").each(function(){
			var currentRow = $(this).closest("tr");
			ids.push({
				//id:$(this).attr("id"), // id
				allowanceId:$(this).attr("allowanceId"), // 折让id
				orderItemId:$(this).attr("orderItemId"), // 订单详情id
				allowanceOrderDetailItemId:$(this).attr("allowanceOrderDetailItemId"), // 折让详情id
				allowanceAmount:parseFloat(convertTextToFloat($.trim($("span[name='allowanceAmount']",currentRow).text())).toFixed(_amountFixedLength))
			});
		});
		if (ids.length == 0) {
			cbms.alert("请选择要关联的折让单详情");
			return;
		}
		$(targetRow).attr("allowanceitems",JSON.stringify(ids));
		$("span[name=status]",targetRow).html(_stataus_related);

		setToReadonly(targetRow);

		//关联成功后隐藏“关联”按钮、显示"修改"、"查看关联"按钮
		$("button[name='allowanceRelate']",targetRow).addClass("none");
		$("button[name='allowanceEdit']",targetRow).removeClass("none");
		$("button[name='allowanceCancel']",targetRow).removeClass("none");
		$("button[name='allowanceViewRelate']",targetRow).removeClass("none");

		cbms.closeDialog();
	});
	
	
	//by tuxianming
	//显示分类列表
	var categoryInput;  //当品名输入框得到焦点的时候，会被赋值
	var aliasInput; //当别名输入框值发生变化的时候，会被赋值
	
	$("#dynamic-table").on("focus", ".categoryName", function(){
		categoryInput = $(this);
		var inputHeight = 35;
		categoryInputPoint = categoryInput.offset();
		var popCategories = $("#categories-layout");
		popCategories.css("top", (categoryInputPoint.top+inputHeight)+"px").css("left",categoryInputPoint.left+"px")
		popCategories.show();
		
	});
	
	//关闭分类列表
	$(document.body).on("click", function(e){
//		var ele = $(e.toElement)
		var ele = $(e.target)
		
		if(ele.hasClass('category-label')){  //当点中每个品名的时候
			categoryInput.val(ele.text());
			categoryInput.siblings("input[name='categoryId']").val(ele.attr("categoryId"));
			$("#categories-layout").hide();
		}else if(ele.hasClass("exist-category")){ //选中下别名下拉框值里面的label时，填充别名，并填充品名，隐藏下拉框
			aliasInput.val(ele.attr("aliasName"));
			aliasInput.siblings("input[name='categoryName']").val(ele.attr("categoryName"));
			aliasInput.siblings("input[name='categoryId']").val(ele.attr("categoryId"));
			aliasInput.siblings("input[name='aliasId']").val(ele.attr("aliasId"));
			$("#search-categories").hide();
			return ;
			
		}else if(ele.hasClass("categoryName")==false){
			//当点中的不是输入分类框的时候，即点除输入分类框的的任意地方的时候，都关闭
			$("#categories-layout").hide();
		}else {
			$("#search-categories").hide();
		}
	});

	//动态搜索品类关联，并显示 
//	<div class="search-categories" id="search-categories">
//	<ul class="list-group">	
//			<li><a class="exist-category list-group-item" categoryId='1111' href="javascript:;">dfave</a></li>
//		</ul>
//	</div>
	
	var searchCategories = "<div class='search-categories' id='search-categories'><ul class='list-group'></ul></div>";
	$(document.body).append(searchCategories);
	$(document.body).on("keyup", "input[name='nsortNameComb']", function(){
		//得到值，
		aliasInput = $(this);
		var aliasName = aliasInput.val();
		//提交到后台查询数据库
		//加载数据
		var listNode = $("#search-categories ul");
		$.post(Context.PATH + "/invoice/in/loadcategoryaliaslikealias.html", {aliasName: aliasName, rnd: new Date().getTime()}, function(data){
			console.log(data);
			
			if(data.success){
				//初始化下别名下拉框列表
				listNode.empty();
				
				var categories =  data.data;
				if(categories){
					for(var i=0; i<categories.length; i++){
						var category = categories[i];
						var row = "<li><a class='exist-category list-group-item' " +
							"aliasId='"+category.id+
							"' categoryId='"+category.categoryId+
							"' categoryName='"+category.categoryName+
							"' aliasName='"+category.aliasName+"' href='javascript:;'>"+
							category.aliasName+" - "+category.categoryName+"</a></li>";
						listNode.append(row);
					}
					
					var inputHeight = 35;
					var aliasInputPoint = aliasInput.offset();
					var popSearchCategories = $("#search-categories");
					popSearchCategories.css("top", (aliasInputPoint.top+inputHeight)+"px")
						.css("left",aliasInputPoint.left+"px")
						.show();
				}
				
			}
		});
		
		//如果有返回结果则构建显示列表，并显示 
	});
	
	
});

function isInEditModel(){//是否处于编辑模式
	return $.trim($("#invoiceId").val())!="";
}
function getRelatedOrderItemIds(currentRow){
	var val = $("input[name=relatedOrderItemIds]",currentRow).val();
	if(val && val != ""){
		return $.parseJSON(val);
	}
	return [];
}
function getNsortMaterialsArray(nsortNameComb){
	var res = nsortNameComb.split(/\s+/);
	if(res.length == 2){
		return res;
	}else{
		return [nsortNameComb,''];
	}
}
function copyRowToRelateContainer(currentRow){
	var nsortNameComb = getNsortMaterialsArray($("input[name=nsortNameComb]",currentRow).val());
//	$("#assign_nsort").val(nsortNameComb[0]);
//	$("#assign_materials").val(nsortNameComb[1]);
//	$("#assign_spec").val($.trim($("input[name=spec]",currentRow).val()));
	$("#assign_endDate").val($("#invoiceDate").val());

	$("#assign_invoiceDate").text($("#invoiceDate").val());
	$("#assign_sellerName").text($("#sellerName").text());
	$("#assign_cargoName").text($("input[name=nsortNameComb]",currentRow).val());
	$("#assign_cargoSpec").text($("input[name=spec]",currentRow).val());
	$("#assign_cargoWeight").text(convertTextToFloat($("input[name=weight]",currentRow).val()).toFixed(_weightFixedLength));
	$("#assign_priceTaxAmount").text(convertTextToFloat($("span[name=amount]",currentRow).text()).toFixed(_amountFixedLength));
	$("#assign_remainWeight").text($("#assign_cargoWeight").text());
	$("#assign_remainAmount").text($("#assign_priceTaxAmount").text());

	// $("#assign_price").text($("span[name='price']",currentRow).text());//单价
	var taxPrice = 0.0000000;//含税单价=价税合计金额/数量
	var weight = parseFloat(convertTextToFloat($("input[name=weight]",currentRow).val()).toFixed(_weightFixedLength));
	var taxAmount = parseFloat(convertTextToFloat($("span[name=amount]",currentRow).text()).toFixed(_amountFixedLength));
	if(weight > 0){
		taxPrice = (1.0 * taxAmount) / (1.0 * weight);
	}
	$("#assign_priceTax").text(taxPrice.toFixed(7));
	
	// 设置当前行ID
	if(!currentRow.attr("id")){
		currentRow.attr("id",generateId("inoice_row_"));
	}
	$("#assign_currentRowId").val(currentRow.attr("id"));
}
function copyRowToAllowanceContainer(currentRow){
	var amount = 0 - convertTextToFloat($("span[name=amount]", currentRow).text()).toFixed(_amountFixedLength);
	$("#allowanceCargoName").text($("input[name=nsortNameComb]",currentRow).val());
	$("#allowancePriceTaxAmount,#allowanceRemainAmount").text(formatMoney(amount, 2));

	// 设置当前行ID
	if(!currentRow.attr("id")){
		currentRow.attr("id","allowanceRow");
	}
	$("#allowanceCurrentRowId").val(currentRow.attr("id"));
}
function setRebateRow(currentRow){
	$("input[name=nsortNameComb]",currentRow).val("");//.attr("disabled","disabled")
	$("input[name=spec]",currentRow).val("").attr("disabled","disabled");
	$("input[name=weight]",currentRow).val("").attr("disabled","disabled");
	$("input[name=noTaxAmount]",currentRow).before("<span>-</span>");
	$("input[name=taxAmount]",currentRow).before("<span>-</span>");
	$("span[name=amount]",currentRow).before("<span>-</span>");
	$("span[name=status]",currentRow).text("-");
	$("span[name=price]",currentRow).text("-");
	$("button[name=relate]",currentRow).addClass("none");
	$("button[name=cancelelation]",currentRow).addClass("none");
}

function setNormalRow(currentRow){
	$("input[name=nsortNameComb]",currentRow).val("").removeAttr("disabled");
	$("input[name=spec]",currentRow).val("").removeAttr("disabled");
	$("input[name=weight]",currentRow).val("").removeAttr("disabled");
	$("input[name=noTaxAmount]",currentRow).prev().remove();
	$("input[name=taxAmount]",currentRow).prev().remove();
	$("span[name=amount]",currentRow).prev().remove();
	$("span[name=status]",currentRow).html(_status_unrelated);
	$("span[name=price]",currentRow).text("0");
	$("button[name=relate]",currentRow).removeClass("none");
}

function initEditData(){
	var invoiceId = $("#invoiceId").val();
	// 加载数据
	$.getJSON(Context.PATH + "/invoice/in/detail.html",{invoiceId : invoiceId},function(re){
		if(re.success){
			var details = re.data.details;
			if(details){
				for (var i=0;i<details.length;i++) {
					bindItem(details[i], re.data.invoiceIn.status);
				}
			}
			invoiceTotal();
		}else{
			cbms.alert("读取发票详情失败！");
		}
	});
	if(isInEditModel() && _pageContext.hasCheckPermission && !isInReceivedStatus()){
		$("#invoiceAmount").attr("disabled","disabled").attr("readonly","readonly");
	}
}

//status：用来判断memo是不是可修改， status的值为： SENT,AWAITS，RECEIVED, assign_enter为可修改
function setToReadonly(row, status){
	$("input[name=invoiceDetailId]",row).attr("disabled","disabled").attr("readonly","readonly");
	$("input[name=nsortNameComb]",row).attr("disabled","disabled").attr("readonly","readonly");
	$("select[name=typeOfSpec]",row).attr("disabled","disabled").attr("readonly","readonly");
	$("input[name=spec]",row).attr("disabled","disabled").attr("readonly","readonly");
	$("input[name=weight]",row).attr("disabled","disabled").attr("readonly","readonly");
	$("input[name=noTaxAmount]",row).attr("disabled","disabled").attr("readonly","readonly");
	$("input[name=taxAmount]",row).attr("disabled","disabled").attr("readonly","readonly");
	$(".categoryName",row).attr("disabled","disabled");
	if(isInReceivedStatus() || isInWaitStatus() || status == 'assign_enter')
		$(".memo",row).removeAttr("disabled").removeAttr("readonly");
	else
		$(".memo",row).attr("disabled","disabled").attr("readonly","readonly");
		
}
function setToEdit(row){
	$("input[name=invoiceDetailId]",row).removeAttr("disabled").removeAttr("readonly");
	$("input[name=nsortNameComb]",row).removeAttr("disabled").removeAttr("readonly");
	$("select[name=typeOfSpec]",row).removeAttr("disabled").removeAttr("readonly");
	$("input[name=spec]",row).removeAttr("disabled").removeAttr("readonly");
	$("input[name=weight]",row).removeAttr("disabled").removeAttr("readonly");
	$("input[name=noTaxAmount]",row).removeAttr("disabled").removeAttr("readonly");
	$("input[name=taxAmount]",row).removeAttr("disabled").removeAttr("readonly");
	
	if(isInSentStatus==false)
		$(".memo",row).removeAttr("disabled").removeAttr("readonly");
	
	$(".categoryName",row).removeAttr("disabled");
	
	/*$("input[name=relatedOrderItemIds]",row).val("");
	$("span[name=status]",row).html(_status_unrelated);*/

	$("button[name=edit]",row).addClass("none");
	$("button[name=cancelelation]",row).addClass("none");
	$("button[name=viewRelate]",row).addClass("none");
	if(isInEditModel() && !isInReceivedStatus()){
		$("button[name=relate]",row).addClass("none");
	}else{
		$("button[name=relate]",row).removeClass("none");
	}
}
function setToAllowanceEdit(row){
	$("input[name=nsortNameComb]",row).removeAttr("disabled").removeAttr("readonly");
	$("input[name=noTaxAmount]",row).removeAttr("disabled").removeAttr("readonly");
	$("input[name=taxAmount]",row).removeAttr("disabled").removeAttr("readonly");
	if(isInEditModel() && !isInReceivedStatus()){
		$("button",row).addClass("none");
	}else{
		$("button[name='allowanceRelate']",row).removeClass("none").siblings().addClass("none");
	}
}
function isFinanceChecked(){
	return _pageContext.checkTotalAmount && parseFloat(_pageContext.checkTotalAmount) > 0;
}
function isInWaitStatus(){
	return _pageContext.invoiceStatus && _pageContext.invoiceStatus=="WAIT";
}

// 待确认
function isInSentStatus(){
	return _pageContext.invoiceStatus && _pageContext.invoiceStatus=="SENT";
}

//待寄出
function isInReceivedStatus(){
	return _pageContext.invoiceStatus && _pageContext.invoiceStatus=="RECEIVED";
}

function addToCancelList(currentRow){
	var tag = currentRow.prop("releationTag");
	if(tag && tag =='true'){
		_canceledRelatedOrderItemIds = _canceledRelatedOrderItemIds.concat(getRelatedOrderItemIds(currentRow));
		currentRow.removeProp("releationTag");
	}
}

//删除备注行，并且给已经备注高度+1
//备注输入框只有一个。，这个处理就是保证一直只有一个。
function handlerMemoInput(row){

	var memoTd = $("#dynamic-table-body").find(".memoTd");
	var memoTextarea = null;
	
	if(memoTd && memoTd.length>0){
		//保留原来已经有的memo，删除row里面的memo
		row.find(".memoTd").remove();
		memoTextarea = $("#dynamic-table-body").find(".memo");
	}else{
		memoTd = row.find(".memoTd");
		memoTextarea = row.find(".memoTd");
	}
	
	//合并表格+1
	if(memoTd && memoTd.length>0){
    	var rowspan = memoTd.attr("rowspan") || 1;
    	memoTd.attr("rowspan", (rowspan/1)+1);
    }
    
	//memo 的textarea的行数+1
    if(memoTextarea && memoTextarea.length>0){
    	var rows = memoTextarea.attr("rows") || 2;
    	memoTextarea.attr("rows", (rows/1)+1);
    }
	
}

//status：用来判断memo是不是可修改， status的值为： SENTA，WAITS，RECEIVED为可修改
function bindItem(item, status){
	var row = $("#firstRow").clone();
	
	//处理备注，如果是第一行，则添加备注，如果不是第一行，则增加备注的高度
	handlerMemoInput(row);
	
	row = row.appendTo("#dynamic-table-body");
	var isChecked = isFinanceChecked();
	$("input[name=invoiceDetailId]",row).val(item.id);
	$("input[name=nsortNameComb]",row).val(item.nsortNameComb);
	$("input[name='categoryName']", row).val(item.categoryName);
	$("input[name='categoryId']", row).val(item.categoryId);
	$("input[name='aliasId']", row).val(item.aliasId);
	
	$("select[name=typeOfSpec]",row).val(item.typeOfSpec).find("option[value='rebate']").remove();
	$("input[name=spec]",row).val(item.spec);
	$("input[name=weight]",row).val(isChecked ? item.checkWeight : item.weight);
	$("input[name=noTaxAmount]",row).val(isChecked ? item.checkNoTaxAmount : item.noTaxAmount);
	$("input[name=taxAmount]",row).val(isChecked ? item.checkTaxAmount : item.taxAmount);
	if(item.detailOrderItems && item.detailOrderItems.length>0){
		$("span[name=status]",row).html(_stataus_related);
		row.prop("dbHasReleation",'true');
		row.prop("releationTag",'true');
	}else{
		$("span[name=status]",row).html(_status_unrelated);
		row.prop("dbHasReleation",'false');
	}
	var releatedIds = [];
	for(var i=0;i<item.detailOrderItems.length;i++){
		var doi = item.detailOrderItems[i];
		releatedIds.push({
			orderId : doi.orderId,
			orderitemId : doi.orderitemId,
			increaseWeight : doi.weight,
			increaseAmount : doi.amount - doi.allowanceAmount,
			originalWeight : 0,
			originalAmount : 0
		});
	}
	$("input[name=relatedOrderItemIds]",row).val(JSON.stringify(releatedIds));
	
	if(isChecked && isInWaitStatus()){// 已经过财务确认，无法再修改
		$("button[name=edit]",row).addClass("none");
	}else{
		$("button[name=edit]",row).removeClass("none");
	}

	//编辑模式下不是待寄出状态
	if(isInEditModel() && !isInReceivedStatus()){
		$("button[name=copy]",row).addClass("none");
		$("button[name=delete]",row).addClass("none");
	}

	if(getRelatedOrderItemIds(row).length > 0){
		$("button[name=viewRelate]",row).removeClass("none");
		$("button[name=relate]",row).addClass("none");
		if(_pageContext.relationStatus=="hasrelation"){
			// 财务确认时需要显示取消关联按钮
			$("button[name=cancelelation]",row).removeClass("none");
		}
	}else{
		$("button[name=viewRelate]",row).addClass("none");
		$("button[name=relate]",row).removeClass("none");
	}
	
	row.attr("id",generateId("invoice_in_"));
	calculateAmount(row);
	setToReadonly(row, status);
}
function isRebateRow(row){
	return $("select[name=typeOfSpec]",row).val() == "rebate";
}
// 计算价税合计
function calculateAmount(row) {
    var noTaxAmount = convertFloat(row.find("input[name='noTaxAmount']").val());//金额
    var taxAmount = convertFloat(row.find("input[name='taxAmount']").val());//税额
    noTaxAmount = parseFloat(noTaxAmount.toFixed(_amountFixedLength));
    taxAmount = parseFloat(taxAmount.toFixed(_amountFixedLength));
    var amount = parseFloat((taxAmount + noTaxAmount).toFixed(_amountFixedLength));//价税合计=金额+税额
    $("span[name='amount']",row).text(formatMoney(amount, 2));
    if(isRebateRow(row)){
    	$("span[name='price']",row).text("-");
	}else{
		var weight = parseFloat(convertFloat($("input[name='weight']",row).val()).toFixed(_weightFixedLength));//数量
	    var price = 0;
	    if(weight > 0){
	        price = (1.0 * noTaxAmount) / (1.0 * weight);//单价=金额/数量
	    }
	    $("span[name='price']",row).text(formatMoney(price, 7));
	}
    return true;
}

// 金额和税额统计计算
function invoiceTotal() {
    // 重置统计数据
    totalNoTaxAmount = 0;
    totalTaxAmount = 0;
    var table = $("#dynamic-table-body");
    $("tr",table).each(function () {
        var currentRow = $(this);
        var weight = convertFloat(currentRow.find("input[name='weight']").val());
        var noTaxAmount = convertFloat(currentRow.find("input[name='noTaxAmount']").val());//金额
        var taxAmount = convertFloat(currentRow.find("input[name='taxAmount']").val());//税额
        noTaxAmount = parseFloat(noTaxAmount.toFixed(_amountFixedLength));
        taxAmount = parseFloat(taxAmount.toFixed(_amountFixedLength));
        if(isRebateRow(currentRow)){
        	totalNoTaxAmount -= noTaxAmount;
            totalTaxAmount -= taxAmount;
        }else{
        	totalNoTaxAmount += noTaxAmount;
            totalTaxAmount += taxAmount;
        }
    });
    $("#totalNoTaxAmount").text(formatMoney(totalNoTaxAmount, 2));
    $("#totalTaxAmount").text(formatMoney(totalTaxAmount, 2));
}

/**
 *  判断是否为空行
 * @param row   需要判断的row
 * @returns {boolean} true:空行，false：非空
 */
function isEmptyRow(row) {
    var nsortNameComb = $.trim($(row).find("input[name='nsortNameComb']").val());
    var spec = $.trim($(row).find("input[name='spec']").val());
    var weight = $.trim($(row).find("input[name='weight']").val());
    var noTaxAmount = $.trim($(row).find("input[name='noTaxAmount']").val());
    var taxAmount = $.trim($(row).find("input[name='taxAmount']").val());
    if (nsortNameComb == "" && 
    		spec == "" && 
    		(weight == ""||weight=="0") && 
    		(noTaxAmount == ""||noTaxAmount=="0") && 
    		(taxAmount == ""||taxAmount=="0") ){
        return true;
    }
    else
        return false;
}
var assignOrderItemsTable = false;
function bindAssignOrderItems(_canceledRelatedOrderItemIds){
	assignOrderItemsTable = $('#relateTable').DataTable({
		ajax : {
			url : Context.PATH + "/invoice/in/assign.html",
			type : "POST",
			data : function(d) {
				d.sellerAccountId = $("#sellerId").val();
				
				var departmentId = $("#selectDepartment option:selected").val();
				if(departmentId)
					d.departmentId = departmentId;
				
				d.nsortName = $("#assign_nsort").val();
				d.materials = $("#assign_materials").val();
				d.contractCode = $("#assign_contract").val();
				d.spec = $("#assign_spec").val();
				d.weight = parseFloat(convertTextToFloat($("#assign_cargoWeight").text()).toFixed(_weightFixedLength));
				d.priceAndTaxAmount = parseFloat(convertTextToFloat($("#assign_priceTaxAmount").text()).toFixed(_amountFixedLength));
				d.orderBeginDate = $("#assign_beginDate").val();
				d.orderEndDate = $("#assign_endDate").val();
				d.purchasePrice = $.trim($("#assign_price").val());
				d.actualWeight = parseFloat(convertTextToFloat($("#assign_weight").val()).toFixed(_weightFixedLength));
				var tgtRow = $("#" + $("#assign_currentRowId").val());
				var hasRel = tgtRow.prop("dbHasReleation");
				hasRel = hasRel && hasRel == 'true';
				var canceledReleation = tgtRow.prop("releationCanceled");
				if(!$("#assign_searchBar").is(":visible") && isInEditModel() && hasRel && ((hasRel+"")=="true") && !canceledReleation){
					d.invoiceDetailId = tgtRow.find("input[name=invoiceDetailId]").val();
				}
				// 数据库没有关联关系，或者数据库有关联关系，但是做过取消操作
				if(!hasRel || canceledReleation){
					var assigned = countAllAssigned();
					for(var i=0;i<assigned.length;i++){
						for(var k in assigned[i]){
							d['assigned[' + i + '].' + k] = assigned[i][k];
						}
					}
				}

				//在客户端被取消的关联（待寄出模块下）
				if(isInReceivedStatus() && _canceledRelatedOrderItemIds){
					for(var i=0;i<_canceledRelatedOrderItemIds.length;i++){
						for(var k in _canceledRelatedOrderItemIds[i]){
							d['cancelAssigned[' + i + '].' + k] = _canceledRelatedOrderItemIds[i][k];
						}
					}
				}


				delete d.columns;
				delete d.order;
				delete d.search;
			}
		},
		columns : [
		    {
		    	data : 'orderItemId',
		    	render : function(data, type, full, meta) {
		    		return '<label class="pos-rel"><input value="'+data+'" type="checkbox" class="ace"><span class="lbl"></span></label>';
		    	}
		    },
		    {
				data : 'orderDateTime',
				render : function(data, type, full, meta) {
					return new Date(data).Format("yyyy-MM-dd");
				}
			},
		    {data : 'orderNumber'},
		    {data : 'contractCode'},
		    {data : 'buyerName'},
		    {data : 'nsortName'},
		    {data : 'materials'},
		    {data : 'spec'},
			{data : 'costPrice'},
		    {data : 'unInvoiceWeight',defaultContent:'0'},
		    {data : 'unInvoiceAmount',defaultContent:'0'},
		    {data : 'increaseWeight',defaultContent:'0'},
		    {data : 'increaseAmount',defaultContent:'0'}
        ],
        fnRowCallback : function(nRow, aData, iDataIndex) {
			$(nRow).attr("alterstatus", aData.alterStatus);
			// 是否显示部门
			if (aData.isShowDept && aData.departmentName != null) {
				$("td", nRow).eq(4).text(aData.buyerName + "【" + aData.departmentName + "】");
			}

        	// 设置每个资源项的原始金额数
        	if(!_orderItemOrginal[aData.orderItemId]){
        		_orderItemOrginal[aData.orderItemId] = {weight:0,amount:0};
        	}
        	_orderItemOrginal[aData.orderItemId].weight = aData.invoiceWeight;
        	_orderItemOrginal[aData.orderItemId].amount = aData.invoiceAmount;
        	
			var cell = _relateTableColumns.getTd(nRow,_relateTableColumns.choose);
			var ckbx = $("input[type=checkbox]",cell);
			if(aData.mathced){
				ckbx.prop("checked",true);
				ckbx.attr("matched",true);
				ckbx.attr("matched_iwt",aData.increaseWeight);
				ckbx.attr("matched_iat",aData.increaseAmount);
			}

			ckbx.attr("orderId",aData.orderId);
			cell.attr("originalAmount",aData.invoiceAmount.toFixed(_amountFixedLength));
			cell.attr("originalWeight",aData.invoiceWeight.toFixed(_weightFixedLength));

			var unInvoiceAmount = aData.unInvoiceAmount;
			var unInvoiceWeight = aData.unInvoiceWeight;
			// 有折让项目或者是重新关联进项票页面的查看关联时，未到票金额 需要加上 折让金额
			if (!$("#allowanceChk").prop("checked") || isInReceivedStatus() || isInSentStatus()) {
				if (aData.allowanceAmount != 0) {
					unInvoiceAmount = aData.unInvoiceAmount + aData.allowanceAmount;
					_relateTableColumns.getTd(nRow, _relateTableColumns.unInvoiceAmount).text(formatMoney(unInvoiceAmount, 2)).addClass("temp-zhe");
				}
			}
			if (aData.allowanceWeight != 0) {
				unInvoiceWeight = aData.unInvoiceWeight + aData.allowanceWeight;
				_relateTableColumns.getTd(nRow, _relateTableColumns.unInvoiceWeight).text(unInvoiceWeight.toFixed(_weightFixedLength)).addClass("temp-zhe");
			}

			// 格式化显示
			_relateTableColumns.getTd(nRow, _relateTableColumns.costPrice).text(aData.costPrice.toFixed(_amountFixedLength));
			_relateTableColumns.getTd(nRow, _relateTableColumns.unInvoiceAmount).text(unInvoiceAmount.toFixed(_amountFixedLength));
			_relateTableColumns.getTd(nRow, _relateTableColumns.unInvoiceWeight).text(unInvoiceWeight.toFixed(_weightFixedLength));
		},
		drawCallback : function(settings){
			if($("#assign_searchBar").is(":visible")){
				$("#relateTableBody tr>td:first").removeClass("none");
				$("#relateTable th:first").removeClass("none");
			}else{
				$("#relateTableBody tr").each(function(){
					$("td:first",this).addClass("none");
				});
				$("#relateTable th:first").addClass("none");
				$("#relateTable_wrapper th:first").addClass("none");
				$("#assign_remainWeight").text("0.000000");
				$("#assign_remainAmount").text("0.00");
			}
			
			// 如果当前行已经选择过ID，则将对应的ID选中，不使用系统推荐值
			var targetRow = $("#" + $("#assign_currentRowId").val());
			var selectedIds = getRelatedOrderItemIds(targetRow);
			if(selectedIds && selectedIds.length > 0){
				$("#assign_cancelSelect").click();//取消所有选择
				for(var i=0;i<selectedIds.length;i++){
					var tgtCheckbox = $("#relateTableBody input[type=checkbox][value="+selectedIds[i].orderitemId+"]");
					var currentRow = tgtCheckbox.closest("tr");
					tgtCheckbox.prop("checked",true);
					_relateTableColumns.getTd(currentRow, _relateTableColumns.increaseWeight).text(selectedIds[i].increaseWeight.toFixed(_weightFixedLength));
					_relateTableColumns.getTd(currentRow, _relateTableColumns.increaseAmount).text(selectedIds[i].increaseAmount.toFixed(_amountFixedLength));
				}
				var hasRel = targetRow.prop("dbHasReleation");
				if(!$("#assign_searchBar").is(":visible") && (!hasRel || hasRel=='false')){
					$("#relateTableBody tr").each(function(){
						if(!$("input[type=checkbox]:first",this).is(":checked")){
							$(this).remove();
						}
					});
				}
			}
			computeRemain();
			if(!$("#assign_searchBar").is(":visible")){
				$("#relateTableBody tr").each(function(){
					var crow = $(this);
					var weight = convertTextToFloat(_relateTableColumns.getTd(crow, _relateTableColumns.increaseWeight).text());
					var amout = convertTextToFloat(_relateTableColumns.getTd(crow, _relateTableColumns.increaseAmount).text());
					if(weight == 0 && amout == 0){
						crow.addClass("none");
					}
				});
			}
		},
        bAutoWidth: false,
        searching:false,
        bPaginate: false,
        paging: false,
        ordering : false
        ,scrollX: true
        
    });
}
var allowanceItemsTable = false;
function bindAllowanceDetailItems(){
	// 已经关联的折让单数据
	var targetRow = $("#allowanceRow");
	var items = $(targetRow).attr("allowanceitems");
	var selectedIds = null;
	if (items != null && items != "") {
		selectedIds = $.parseJSON(items);
	}
	assignOrderItemsTable = $('#allowanceTable').DataTable({
		ajax : {
			url : Context.PATH + "/invoice/in/searchallowance.html",
			type : "POST",
			data : function(d) {
				d.accountId = $("#sellerId").val();
				d.unInvoiceInId=$("#invoiceId").val();
			}
		},
		columns : [
			{
				data : 'detailItemId',
				render : function(data, type, full, meta) {
					return '<label class="pos-rel"><input value="'+data+'" type="checkbox" class="ace"><span class="lbl"></span></label>';
				}
			},
			{data : 'allowanceCode'},
			{
				data : 'orderTime',
				render : function(data, type, full, meta) {
					return new Date(data).Format("yyyy-MM-dd");
				}
			},
			{data: 'accountName'},
			{data: 'buyerName'},
			{data: 'nsortName'},
			{data: 'material'},
			{data: 'spec'},
			{
				data: 'allowanceAmount', defaultContent: '0', render: function (data, type, full, meta) {
				return "<span name='allowanceAmount'>" + formatMoney(data, 2) + "</span>";
			}
			}
		],
		fnRowCallback : function(nRow, aData, iDataIndex) {
			var cell = $("td",nRow).eq(0);
			var ckbx = $("input[type=checkbox]",cell);
			ckbx.attr("allowanceOrderDetailItemId",aData.detailItemId);
			ckbx.attr("allowanceId",aData.id);
			ckbx.attr("allowanceAmount",aData.allowanceAmount.toFixed(_weightFixedLength));
			ckbx.attr("orderItemId",aData.orderDetailId);

			if (aData.isShowDept) {
				$("td", nRow).eq(3).text(aData.accountName + "【" + aData.departmentName + "】");
			}
			if (aData.isShowBuyerDept) {
				$("td", nRow).eq(4).text(aData.buyerName + "【" + aData.buyerDepartmentName + "】");
			}
		},
		drawCallback : function(settings){
			// 如果当前行已经选择过ID，则将对应的ID选中，不使用系统推荐值
			if (selectedIds && selectedIds.length > 0) {
				$("#allowanceCancel").click();//取消所有选择
				for (var i = 0; i < selectedIds.length; i++) {
					var tgtCheckbox = $("#allowanceTable input[type=checkbox][value=" + selectedIds[i].allowanceOrderDetailItemId + "]");
					tgtCheckbox.prop("checked", true);
				}
				$("#allowanceTableBody tr").each(function () {
					if (!$("input[type=checkbox]:first", this).is(":checked")) {
						$(this).remove();
					}
				});
			}
			computeAllowanceRemain();
			if (selectedIds && selectedIds.length > 0) {
				$("#allowanceTableBody tr").each(function () {
					$("td:first", this).addClass("none");
				});
				$("#allowanceTable th:first").addClass("none");
				$("#allowanceCancel,#allowanceEnter").addClass("none");
			}else {
				$("#allowanceTableBody tr>td:first").removeClass("none");
				$("#allowanceTable th:first").removeClass("none");
			}
		},
		bAutoWidth: false,
		searching:false,
		bPaginate: false,
		paging: false,
		ordering : false
	});
}
function countAllAssigned(){
	var res = [];
	var tgtRowId = $("#assign_currentRowId").val();
	$("#dynamic-table-body tr").each(function(){
		var currentRow = $(this);
		if(tgtRowId == currentRow.attr("id")){//排除掉当前行
			return;
		}
		var releatedItems = getRelatedOrderItemIds(currentRow);
		if(releatedItems && releatedItems.length > 0){
			res = res.concat(releatedItems);
		}
	});
	return res;
}

function computeRemain(){//计算剩余量
	// 获取发票重量和价税合计
	var gotWeight = parseFloat(convertTextToFloat($("#assign_cargoWeight").text()).toFixed(_weightFixedLength));
	var gotAmount = parseFloat(convertTextToFloat($("#assign_priceTaxAmount").text()).toFixed(_amountFixedLength));
	var increaseWeightTotal = 0;
	var increaseAmountTotal = 0;
	// 计算选中行的到票量之和
	$("#relateTableBody input[type=checkbox]:checked").each(function(){
		var currentRow = $(this).closest("tr");
		var increaseWeight = _relateTableColumns.getTd(currentRow, _relateTableColumns.increaseWeight);
		var increaseAmount = _relateTableColumns.getTd(currentRow, _relateTableColumns.increaseAmount);
		var iwt = convertTextToFloat(increaseWeight.text()).toFixed(_weightFixedLength);
		var iat = convertTextToFloat(increaseAmount.text()).toFixed(_amountFixedLength);
		increaseWeightTotal += parseFloat(iwt);
		increaseAmountTotal += parseFloat(iat);
	});
	// 设置当前剩余量
	var rmWeight = parseFloat((gotWeight - increaseWeightTotal).toFixed(_weightFixedLength));
	var rmAmount = parseFloat((gotAmount - increaseAmountTotal).toFixed(_amountFixedLength));
	$("#assign_remainWeight").text(rmWeight.toFixed(_weightFixedLength));
	$("#assign_remainAmount").text(rmAmount.toFixed(_amountFixedLength));
	// 如果当前剩余量为0，则将其他复选框设置为不可用
	if(rmWeight == 0 && rmAmount == 0){
		$("#relateTableBody input[type=checkbox]").each(function(){
			if($(this).is(":checked")){
				$(this).removeAttr("disabled");
			}else{
				$(this).attr("disabled","disabled");
			}
		});
	}else{
		$("#relateTableBody input[type=checkbox]").removeAttr("disabled");
	}
}
function computeAllowanceRemain(){//计算折让剩余量
	// 获取折扣价税合计
	var gotAmount = parseFloat(convertTextToFloat($("#allowancePriceTaxAmount").text()).toFixed(_amountFixedLength));
	var increaseAmountTotal = 0;
	// 计算选中行的折扣量之和
	$("#allowanceTableBody input[type=checkbox]:checked").each(function () {
		var currentRow = $(this).closest("tr");
		var iat = convertTextToFloat($("span[name='allowanceAmount']", currentRow).text()).toFixed(_amountFixedLength);
		increaseAmountTotal += parseFloat(iat);
	});
	// 设置当前剩余量
	var rmAmount = parseFloat((gotAmount - increaseAmountTotal).toFixed(_amountFixedLength));
	$("#allowanceRemainAmount").text(rmAmount.toFixed(_amountFixedLength));
	// 如果当前剩余量为0，则将其他复选框设置为不可用
	if(rmAmount == 0){
		$("#allowanceTableBody input[type=checkbox]").each(function(){
			if($(this).is(":checked")){
				$(this).removeAttr("disabled");
			}else{
				$(this).attr("disabled","disabled");
			}
		});
	}else{
		$("#allowanceTableBody input[type=checkbox]").removeAttr("disabled");
	}
}
function convertTextToFloat(txt){
	if(!txt){
		return 0;
	}
	return parseFloat($.trim(txt).replace(/,/g,''));
}
function computeRowIncrease(currentRow){
	if($("input[type=checkbox]:first",currentRow).is(":checked")){
		var unInvoiceWeight = _relateTableColumns.getTd(currentRow, _relateTableColumns.unInvoiceWeight).text();
		var unInvoiceAmount = _relateTableColumns.getTd(currentRow, _relateTableColumns.unInvoiceAmount).text();
		var iwt	= parseFloat(convertTextToFloat(unInvoiceWeight).toFixed(_weightFixedLength));
		var iat	= parseFloat(convertTextToFloat(unInvoiceAmount).toFixed(_amountFixedLength));
		
		var rwt = parseFloat(convertTextToFloat($("#assign_remainWeight").text()).toFixed(_weightFixedLength));
		var rat = parseFloat(convertTextToFloat($("#assign_remainAmount").text()).toFixed(_amountFixedLength));
		_relateTableColumns.getTd(currentRow, _relateTableColumns.increaseWeight).text(Math.min(iwt,rwt).toFixed(_weightFixedLength));
		_relateTableColumns.getTd(currentRow, _relateTableColumns.increaseAmount).text(Math.min(iat,rat).toFixed(_amountFixedLength));
	}else{
		_relateTableColumns.getTd(currentRow, _relateTableColumns.increaseWeight).text("0");
		_relateTableColumns.getTd(currentRow, _relateTableColumns.increaseAmount).text("0");
	}
	
}

function getAllowanceDetail(){
	var invoiceId = $("#invoiceId").val();
	// 加载数据
	$.getJSON(Context.PATH + "/invoice/in/allowancedetail.html",{invoiceId : invoiceId},function(re){
		if(re.success){
			var details = re.data;
			if (details) {
				var ids = [];
				for (var i = 0; i < details.length; i++) {
					ids.push({
						allowanceId: details[i].allowanceId, // 折让id
						orderItemId: details[i].orderItemId, // 订单详情id
						allowanceOrderDetailItemId: details[i].allowanceOrderDetailItemId, // 折让详情id
						allowanceAmount: parseFloat(parseFloat(details[i].allowanceAmount).toFixed(_amountFixedLength))
					});
				}
				$("#allowanceRow").attr("allowanceitems", JSON.stringify(ids));
			}
		}else{
			cbms.alert("读取折让单详情失败！");
		}
	});
}

function doSelectDepartment(){
	$("#assign_doSearch").click();
}


/**
 * 是否处于合同变更的不能操作状态
 * @param alterStatus 合同变更状态
 * @returns {boolean}
 */
function isChangeContract(alterStatus) {
	if (alterStatus == "PENDING_APPROVAL"||alterStatus == "PENDING_RELATE"||alterStatus == "PENDING_APPLY"
		||alterStatus == "PAYED_DISAPPROVE"||alterStatus == "PENDING_APPR_PAY"||alterStatus == "PENDING_PRINT_PAY"
		||alterStatus == "PENDING_CONFIRM_PAY") {
		return true;
	}
	return false;
}