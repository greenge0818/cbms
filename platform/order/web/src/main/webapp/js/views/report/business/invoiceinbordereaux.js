/**
 * Created by xq on 2015/12/15.
 * 进行票清单报表
 */

var dt;
var weightScale = 6;   //重量保留6位小数
var moneyScale = 2;
var _status = '0';           // 状态集合
jQuery(function ($) {

    initTable();
    initSum();

    $("#queryBtn").click(function () {
        dt.ajax.reload();
        initSum();
    });

    $("#output").click(function () {
        exportExcel();
    });

    $("#orderStatusBtn").click(function() {
    	var optionbox = $("#orderStatusList");
    	if(optionbox.css("display") == "none"){
    		optionbox.show();
    	}else{
    		optionbox.hide();
    	}
    });
    
    $("#yes").click(function() {
    	var optionbox = $("#orderStatusList");
		optionbox.hide();
		//请求后台方法
		dt.ajax.reload();
		initSum();
	});
	
    // 单选
    $(".ace").click(function() {
        setStatus();
    });
    
});

//统计选中的订单
function setStatus() {
	var status = [];
	$("#orderStatusList li input[type='checkbox']").each(function(){
	    if(this.checked){
	    	var value = $(this).val();
	    	status.push(value);
	    }
	}); 
	_status = status.toString();
	if(_status == '') {
		_status = '0';
	}
}


function initSum() {
	$.ajax({
		url : Context.PATH + "/report/business/invoiceInBordereauxsum.html",
		type : "POST",
		data: {
            orgId : $("#sorganizationHidden").val(),
            sellerName : $("#sellerName").val(),
    		orderCode : $("#orderCode").val(),
        	startTime : $("#startTime").val(),
            endTime : $("#endTime").val(),
	    	status : _status
        },
	    success: function (result) {
	    	if(result.success){
	    		if (result.data == null) {
	    			$("#totalNoWeight").text("0.000000");
	    			$("#totalNoAmount").text("0.00");
	    		}
	    		$("#totalNoWeight").text(formatMoney(result.data.totalNoWeight,weightScale));
				$("#totalNoAmount").text(formatMoney(result.data.totalNoAmount,moneyScale));
	    	}
	    }
	});
}

function initTable() {
    dt = $("#dynamic-table").DataTable({
        "pageLength": 50, //每页记录数
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "lengthChange": false, //不显示pageSize的下拉框
        "oLanguage": {sUrl: Context.PATH + "/js/DT_zh.txt"},
        "ajax": {
            "url": Context.PATH + "/report/business/invoiceinbordereauxdata.html"
            , "type": "POST"
            , data: function (d) {
            	if ($("#sorganizationHidden").length > 0) {
                    d.orgId = $("#sorganizationHidden").val();
                }
            	if ($("#sellerName").val().length > 0) {
                    d.sellerName = $("#sellerName").val();
                }
            	if ($("#orderCode").val().length > 0) {
            		d.orderCode = $("#orderCode").val();
                }
                d.startTime = $("#startTime").val();
                d.endTime = $("#endTime").val();
                d.status=_status;
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'orderTime'},
            {data: 'orderCode'},
            {data: 'sellerName'},
            {data: 'nsortName'},
            {data: 'material'},
            {data: 'spec'},
            {data: 'actualWeight',name:"actualWeight", "sClass": "text-right"},
            {data: 'actualAmount',name:"actualAmount", "sClass": "text-right"},
            {data: 'allowanceWeight',name: 'allowanceWeight', "sClass": "text-right"},
            {data: 'allowanceAmount',name: 'allowanceAmount', "sClass": "text-right"},
            {data: 'weight',name: 'weight', "sClass": "text-right"},
            {data: 'amount',name: 'amount', "sClass": "text-right"},
            {data: 'unWeight',name: 'unWeight', "sClass": "text-right"},
            {data: 'unAmount',name: 'unAmount', "sClass": "text-right"},
            {data: 'status'}
        ]
        , columnDefs: [
            {
                "targets": 0,
                "data": "orderTime",
                "render": function (data) {
                    return formatDay(data);
                }
            }, {
                "targets": 1,
                "data": "orderCode",
                "render": function (data, type, full, meta) {
                	var href = Context.PATH + "/order/query/detail.html?orderid=" + full.orderId;
                	return '<a class="getDataList" href="' + href + '" type="buyer">' + data + '</a><span class="pos-rel"></span>';
                }
            }, {
                "targets": 6, 
                "data": "actualWeight",
                "render": function (data) {
                    return formatWeight(data,weightScale);
                }
            }, {
                "targets": 7,
                "data": "actualAmount",
                "render": function (data) {
                    return renderMoney(data,moneyScale);
                }
            }, {
                "targets": 8,
                "data": "allowanceWeight",
                "render": function (data) {
                    return formatWeight(data,weightScale);
                }
            }
            , {
                "targets": 9,
                "data": "allowanceAmount",
                "render": function (data) {
                    return renderMoney(data,moneyScale);
                }
            }, {
                "targets": 10,
                "data": "weight",
                "render": function (data) {
                    return formatWeight(data,weightScale);
                }
            }
            , {
                "targets": 11,
                "data": "amount",
                "render": function (data) {
                    return renderMoney(data,moneyScale);
                }
            }, {
                "targets": 12,
                "data": "unWeight",
                "render": function (data) {
                    return formatWeight(data,weightScale);
                }
            }
            , {
                "targets": 13,
                "data": "unAmount",
                "render": function (data) {
                    return renderMoney(data,moneyScale);
                }
            }
            , {
                "targets": 2,
                "data": "unAmount",
                "render": function (data, type, full, meta) {
                    return getDepartmentName(data, full);
                }
            }
        ]
    });
}

//买家/卖家全称显示，如果有多个部门则显示部门，否则不显示部门
function getDepartmentName(data, full) {
	if (full.departmentCount > 1) {
		data = data +"【"+full.departmentName+"】";
	}
	return data;
}


/**
 * 去掉格式化
 * @param i
 * @returns {number}
 */
function numberVal(i) {
    return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1 : typeof i === 'number' ? i : 0;
}

function exportExcel() {
    var form = $("<form>");
    form.attr('style', 'display:none');
    form.attr('target', '');
    form.attr('method', 'post');
    form.attr('enctype', 'multipart/form-data');
    form.attr('action', Context.PATH + "/report/business/invoiceinbordereauxdataexcel.html");
    
    if ($("#sorganizationHidden").length > 0) {
        var input0 = $('<input>');
        input0.attr('type', 'hidden');
        input0.attr('name', 'orgId');
        input0.attr('value', $("#sorganizationHidden").val());
        form.append(input0);
    }
	if ($("#sellerName").val().length > 0) {
		var input1 = $('<input>');
	    input1.attr('type', 'hidden');
	    input1.attr('name', 'sellerName');
	    input1.attr('value', $("#sellerName").val());
	    form.append(input1); 
    }
	if ($("#orderCode").val().length > 0) {
		var input2 = $('<input>');
	    input2.attr('type', 'hidden');
	    input2.attr('name', 'orderCode');
	    input2.attr('value', $("#orderCode").val());
	    form.append(input2);
    }
    
    var input3 = $('<input>');
    input3.attr('type', 'hidden');
    input3.attr('name', 'startTime');
    input3.attr('value', $("#startTime").val());
    form.append(input3);

    var input4 = $('<input>');
    input4.attr('type', 'hidden');
    input4.attr('name', 'endTime');
    input4.attr('value', $("#endTime").val());
    form.append(input4);
    
    var input5 = $('<input>');
    input5.attr('type', 'hidden');
    input5.attr('name', 'status');
    input5.attr('value',_status);
    form.append(input5);
    
    $('body').append(form);
    form.submit();
    form.remove();
}

function formatWeight(data,scale) {
    if (data) {
        return parseFloat((data + "").replace(/[^\d\.-]/g, "")).toFixed(scale) + "";
    }
    return "-";
}

function renderMoney(data,scale) {
    if (data) {
        return formatMoney(data, scale);
    }
    return "-";
}
function formatDay(data) {
	var dt = new Date(data);
	var time = dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate();
	return time;
}

