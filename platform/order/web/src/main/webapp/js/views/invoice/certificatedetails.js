/**
 * Created by lixiang on 2016/02/17.
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

            $(".ystep4").setStep(stepindexmain);
//            for (var i in re.data) {
//            	if (re.data[i].invoiceInStatus == "ROLLBACK") {
//            		if (re.data[i].created != null) {
//            			$(".ystep2").setStep(-2);
//            		}
//            	}
//	            if (re.data[i].invoiceInStatus == "ALREADY" && re.data[i].invoiceInStatus == "ROLLBACK") {
//	            	if (re.data[i].created != null) {
//	            		$(".ystep2").setStep(-1);
//	            	}
//	            }
//            }
		}
	});
});

var _weightFixedLength = 6;
var _amountFixedLength = 2;
var _relateTableColumns = {
    getTd: function (row, column) {
        return $(column.filter, row);
    },
    orderDateTime: {
        index: 0,
        filter: 'td:eq(0)'
    },
    orderNumber: {
        index: 1,
        filter: 'td:eq(1)'
    },
    contractCode: {
        index: 2,
        filter: 'td:eq(2)'
    },
    buyerName: {
        index: 3,
        filter: 'td:eq(3)'
    },
    nsortName: {
        index: 4,
        filter: 'td:eq(4)'
    },
    materials: {
        index: 5,
        filter: 'td:eq(5)'
    },
    spec: {
        index: 6,
        filter: 'td:eq(6)'
    },
    costPrice: {
        index: 7,
        filter: 'td:eq(7)'
    },
    unInvoiceWeight: {
        index: 8,
        filter: 'td:eq(8)'
    },
    unInvoiceAmount: {
        index: 9,
        filter: 'td:eq(9)'
    },
    increaseWeight: {
        index: 10,
        filter: 'td:eq(10)'
    },
    increaseAmount: {
        index: 11,
        filter: 'td:eq(11)'
    }
};

var _detailTableColumns = {
    getTd: function (row, column) {
        return $(column.filter, row);
    },
    assign_cargoName: {
        index: 0,
        filter: 'td:eq(0)'
    },
    assign_cargoSpec: {
        index: 1,
        filter: 'td:eq(1)'
    },
    assign_cargoWeight: {
        index: 3,
        filter: 'td:eq(3)'
    },
    assign_priceTaxAmount: {
        index: 8,
        filter: 'td:eq(8)'
    }
};

jQuery(function ($) {
    $("#dynamic-table").on("click", "button[name='viewRelate']", function () {
        var currentRow = $(this).closest("tr");

        cbms.getDialog("查看关联", "#assignDialog");

        copyRowToRelateContainer(currentRow);

        bindAssignOrderItems($(this).attr("detailId"));


    })
    
    $("#dynamic-table").on("click", "button[name='allowanceViewRelate']", function () {
        cbms.getDialog("查看关联", "#allowanceDialog");
    })
    
});


function bindAssignOrderItems(invoiceDetailId) {
    $('#relateTable').DataTable({
        ajax: {
            url: Context.PATH + "/invoice/in/assign.html",
            type: "POST",
            data: function (d) {

                //满足后台检查，invoiceDetailId
                d.sellerAccountId = $("#sellerId").val();
                d.nsortName = '';
                d.materials = '';
                d.spec = '';
                d.weight = parseFloat(convertTextToFloat($("#assign_cargoWeight").text()).toFixed(_weightFixedLength));
                d.priceAndTaxAmount = parseFloat(convertTextToFloat($("#assign_priceTaxAmount").text()).toFixed(_amountFixedLength));
                d.invoiceDetailId = invoiceDetailId;

                delete d.columns;
                delete d.order;
                delete d.search;
            }
        },
        columns: [
            {
                data: 'orderDateTime',
                render: function (data, type, full, meta) {
                    return new Date(data).Format("yyyy-MM-dd");
                }
            },
            {data: 'orderNumber'},
            {data: 'contractCode'},
            {data: 'buyerName'},
            {data: 'nsortName'},
            {data: 'materials'},
            {data: 'spec'},
            {data:'costPrice', defaultContent: '0', render: function (data, type, full, meta) {
                return renderRedColor1(data, _amountFixedLength)
            }
            },
            {
                data: 'unInvoiceWeight', defaultContent: '0', render: function (data, type, full, meta) {
                var value = data || 0;
                return value.toFixed(_weightFixedLength);
            }
            },
            {
                data: 'unInvoiceAmount', defaultContent: '0', render: function (data, type, full, meta) {
                var value = data || 0;
                
                if(full.allowanceAmount){
                	value += full.allowanceAmount;
                }
                
                return value.toFixed(_amountFixedLength);
            }
            },
            {
                data: 'increaseWeight', defaultContent: '0', render: function (data, type, full, meta) {
                return renderRedColor(data, _weightFixedLength)
            }
            },
            {
                data: 'increaseAmount', defaultContent: '0', render: function (data, type, full, meta) {
                	return renderRedColor(data, _amountFixedLength)
                }
            }
            ,{
                data: 'allowanceAmount', defaultContent: '0', render: function (data, type, full, meta) {
                	return '<input name="allowanceAmount" value="'+data+'"/>'; 
                },
                sClass: "none"
            }
        ],
        fnRowCallback: function (nRow, aData, iDataIndex) {

        },
        drawCallback: function (settings) {
            computeRemain();
        },
        bAutoWidth: false,
        searching: false,
        bPaginate: false,
        paging: false,
        ordering: false
    });
}

function copyRowToRelateContainer(currentRow) {
    $("#assign_invoiceDate").text($("#detail_invoiceDate").text());
    $("#assign_sellerName").text($("#sellerName").text());
    $("#assign_cargoName").text(_detailTableColumns.getTd(currentRow, _detailTableColumns.assign_cargoName).text());
    $("#assign_cargoSpec").text(_detailTableColumns.getTd(currentRow, _detailTableColumns.assign_cargoSpec).text());
    var cargoWeight = parseFloat(_detailTableColumns.getTd(currentRow, _detailTableColumns.assign_cargoWeight).text()).toFixed(_weightFixedLength);
    var priceTaxAmount = parseFloat(_detailTableColumns.getTd(currentRow, _detailTableColumns.assign_priceTaxAmount).text()).toFixed(_amountFixedLength);
    //计算发票单价 价税合计/重量
    var invoicePrice =  parseFloat(priceTaxAmount/cargoWeight).toFixed(_amountFixedLength);
    $("#assign_invoicePrice").text(invoicePrice);
    $("#assign_cargoWeight").text(cargoWeight);
    $("#assign_priceTaxAmount").text(priceTaxAmount);
}

//计算剩余量
function computeRemain() {
    // 获取发票重量和价税合计
    var gotWeight = parseFloat(convertTextToFloat($("#assign_cargoWeight").text()).toFixed(_weightFixedLength));
    var gotAmount = parseFloat(convertTextToFloat($("#assign_priceTaxAmount").text()).toFixed(_amountFixedLength));
    var increaseWeightTotal = 0;
    var increaseAmountTotal = 0;
    // 计算所有行的到票量之和
    $("#relateTableBody tr").each(function () {
        var currentRow = $(this);
        var increaseWeight = _relateTableColumns.getTd(currentRow, _relateTableColumns.increaseWeight);
        var increaseAmount = _relateTableColumns.getTd(currentRow, _relateTableColumns.increaseAmount);
        var iwt = convertTextToFloat(increaseWeight.text()).toFixed(_weightFixedLength);
        var iat = convertTextToFloat(increaseAmount.text()).toFixed(_amountFixedLength);
       
        var allwoanceAmount = convertTextToFloat(currentRow.find('input[name="allowanceAmount"]').val());
        
        increaseWeightTotal += parseFloat(iwt);
        increaseAmountTotal += parseFloat(iat) - allwoanceAmount;
        
    });
    // 设置当前剩余量
    var rmWeight = parseFloat((gotWeight - increaseWeightTotal).toFixed(_weightFixedLength));
    var rmAmount = parseFloat((gotAmount - increaseAmountTotal).toFixed(_amountFixedLength));
    $("#assign_remainWeight").text(rmWeight.toFixed(_weightFixedLength));
    $("#assign_remainAmount").text(rmAmount.toFixed(_amountFixedLength));
}

function convertTextToFloat(txt) {
    if (!txt) {
        return 0;
    }
    return parseFloat($.trim(txt).replace(/,/g, ''));
}

function renderRedColor(data, scale) {
    var value = data || 0;
    return '<em class="red">' + (value.toFixed(scale)) + '</em>';
}

function renderRedColor1(data, scale) {
    var value = data || 0;
    return (value.toFixed(scale));
}


// 通过认证
$("#pass").click(function () {
    cbms.confirm("确定认证通过选中的发票吗",null,function(){
    	inauthentication("pass");
    });
});

// 未通过认证
$("#refuse").click(function () {
	cbms.confirm("确定作废掉选中的发票吗",null,function(){
		inauthentication("refuse");
	});
});

$("#roollback").click(function(){cbms.confirm("确定打回选中的发票重新关联吗",null,function(){
		inauthentication("rollback");
	});   
});

//进行认证发票
function inauthentication(flag) {
	var invoiceIds = [];
	invoiceIds.push($("#invoiceId").val());
	var paramsJson = {
		flag: flag,
        invoiceIds: invoiceIds
    }
	cbms.loading();
    $.ajax({
        type: 'post',
        url: Context.PATH + "/invoice/in/inauthentication.html",
        data: {
        	paramsJson: JSON.stringify(paramsJson)
        },
        error: function (s) {
            cbms.closeLoading();
        }
        , success: function (result) {
            cbms.closeLoading();
            if (result) {
                if (result.success) {
                	cbms.alert("操作成功！", function() {
						location.href = Context.PATH + "/invoice/in/authentication.html";
					});
                }
                else {
                	var msg = "操作失败，请联系系统管理员";
                	if(result.message){
                		msg = result.message;
                	}
                    cbms.alert(msg);
                    if(result.data)
                    {
                        for (var i = 0; i < result.data.length; i++) {
                            var label = $("#dynamic-table input[value='" + result.data[i] + "']").parent();
                            if(label.find(".bg-circle").length<=0)
                            {
                                label.append("<span class='bg-circle'></span>");
                            }
                        }
                    }
                }
            } else {
                cbms.alert("认证失败！");
            }
        }
    });
}

//取消认证发票
$("#cancel").click(function () {
    outauthentication();
});
//取消认证发票
function outauthentication() {
	var invoiceIds = [];
	invoiceIds.push($("#invoiceId").val());
	var idsJson = {
		invoiceIds: invoiceIds
    }
	cbms.loading();
    $.ajax({
        type: 'post',
        url: Context.PATH + "/invoice/in/outauthentication.html",
        data: {
        	idsJson: JSON.stringify(idsJson)
        },
        error: function (s) {
            cbms.closeLoading();
        }
        , success: function (result) {
            cbms.closeLoading();
            if (result) {
                if (result.success) {
                	cbms.alert("操作成功！", function() {
						location.href = Context.PATH + "/invoice/in/authenticationed.html";
					});
                }
                else {
                    var msg = "操作失败，请联系系统管理员";
                    if(result.message){
                        msg = result.message;
                    }
                    cbms.alert(msg);
                    if(result.data)
                    {
                        for (var i = 0; i < result.data.length; i++) {
                            var label = $("#dynamic-table input[value='" + result.data[i] + "']").parent();
                            if(label.find(".bg-circle").length<=0)
                            {
                                label.append("<span class='bg-circle'></span>");
                            }
                        }
                    }
                }
            } else {
                cbms.alert("取消认证失败");
            }
        }
    });
}


