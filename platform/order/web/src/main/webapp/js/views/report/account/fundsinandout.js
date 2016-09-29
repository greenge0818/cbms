/**
 * Created by caochao on 2015/8/19.
 * 资金流水报表
 */

var dt;
//数据来源，0订单，1浦发，2工行
var order = 0, spd = 1, icbc = 2;

jQuery(function ($) {
    initTable();
    //搜索事件
    $("#queryBtn").click(function () {
        dt.ajax.reload();
    });
    $("#exportexcel").click(function () {
        exportToExcel();
    });
});

function initTable() {
    var url = Context.PATH + "/report/account/inandoutdata.html";
    dt = $("#dynamic-table").DataTable({
        ajax: {
            url: url,
            type: "POST",
            data: function (d) {
                d.accountName = $.trim($("#account_name").val());
                d.sdate = $("#startTime").val();
                d.edate = $("#endTime").val();
            }
        },
        columns: [
            {data: 'accountName'},
            {data: 'createTime'},
            {defaultContent: '-'},
            {defaultContent: '-'},
            {defaultContent: '-'},
            {defaultContent: '-'},
            {defaultContent: '-'},
            {defaultContent: '-'},
            {defaultContent: '-'},
            {defaultContent: '-'}
        ],
        columnDefs: [
            {
                "targets": 0, //第几列 从0开始
                "orderable": false
            },
            {
                "targets": 1, //第几列 从0开始
                "data": "createTime",
                "render": function (data, type, full, meta) {
                    return dateFormat(new Date(data), "yyyy-MM-dd hh:mm:ss");
                }
            },
            {
                "targets": 2, //第几列 从0开始
                "data": "receiptAmount",
                "orderable": false,
                "render": function (data, type, full, meta) {
                    if (full.transType == spd && parseFloat(data) > 0) {
                        return formatMoney(data);
                    }
                    else {
                        return '-';
                    }
                }
            },
            {
                "targets": 3, //第几列 从0开始
                "data": "paymentAmount",
                "orderable": false,
                "render": function (data, type, full, meta) {
                    if (full.transType == order && full.paymentBank=='SPD' && parseFloat(data) > 0) {
                        return formatMoney(data);
                    }
                    else {
                        return '-';
                    }
                }
            },
            {
                "targets": 4, //第几列 从0开始
                "orderable": false,
                "data": "dealAmount",
                "render": function (data, type, full, meta) {
                    if (full.transType == spd && parseFloat(data) > 0) {
                        return formatMoney(data);
                    }
                    else {
                        return '-';
                    }
                }
            },
            {
                "targets": 5, //第几列 从0开始
                "orderable": false
            },
            {
                "targets": 6, //第几列 从0开始
                "orderable": false,
                "data": "receiptAmount",
                "render": function (data, type, full, meta) {
                    if (full.transType == icbc && parseFloat(data) > 0) {
                        return formatMoney(data);
                    }
                    else {
                        return '-';
                    }
                }
            },
            {
            	"targets": 7, //第几列 从0开始
                "data": "paymentAmount",
                "orderable": false,
                "render": function (data, type, full, meta) {
                    if (full.transType == order && full.paymentBank=='ICBC' && parseFloat(data) > 0) {
                        return formatMoney(data);
                    }
                    else {
                        return '-';
                    }
                }
            },
            {
                "targets": 8, //第几列 从0开始
                "orderable": false,
                "data": "dealAmount",
                "render": function (data, type, full, meta) {
                    if (full.transType == icbc && parseFloat(data) > 0) {
                        return formatMoney(data);
                    }
                    else {
                        return '-';
                    }
                }
            },
            {
                "targets": 9, //第几列 从0开始
                "orderable": false
            }
        ],
        aaSorting: [
            [1, "asc"]
        ],
        aLengthMenu: [50, 100, 150],
        serverSide: true, //服务模式
        processing: true,//显示数据加载进度
        searching: false, //是否启用搜索
        ordering: true, //是否启用排序
        bFilter: false,
        bLengthChange: true, //不显示每页长度的选择条
        fnDrawCallback:function(){
            getInAndOutTotal();
        }
    });
}

function getInAndOutTotal() {
    //spd
    $("#spd_totalReceiptAmount").html("0.00");
    $("#spd_totalPaymentAmount").html("0.00");
    $("#spd_totalDealAmount").html("0.00");
    $("#spd_diffAmount").html("0.00");
    //icbc
    $("#icbc_totalReceiptAmount").html("0.00");
    $("#icbc_totalPaymentAmount").html("0.00");
    $("#icbc_totalDealAmount").html("0.00");
    $("#icbc_diffAmount").html("0.00");
    var transTypeColumnName = "transType";
    var url = Context.PATH + "/report/account/inandouttotal.html";
    $.ajax({
        url: url,
        type: "POST",
        data: {
            accountName : $.trim($("#account_name").val()),
            sdate : $("#startTime").val(),
            edate : $("#endTime").val()
        },
        success: function (re) {
            if (re.success) {
                var order_payment = 0,order_payment_SPD = 0, spd_dealTotal = 0, icbc_dealTotal = 0;
                for (var i = 0; i < re.data.length; i++) {
                    if (re.data[i][transTypeColumnName] == spd) {
                    	if (re.data[i].receiptAmount != null) {
                    		$("#spd_totalReceiptAmount").html(formatMoney(re.data[i].receiptAmount));
                    	}
                        if (re.data[i].dealAmount != null) {
                        	$("#spd_totalDealAmount").html(formatMoney(re.data[i].dealAmount));
                        	spd_dealTotal += parseFloat(re.data[i].dealAmount);
                        }
                    }
                    else if (re.data[i][transTypeColumnName] == icbc) {
                    	if (re.data[i].receiptAmount != null) {
                    		$("#icbc_totalReceiptAmount").html(formatMoney(re.data[i].receiptAmount));
                    	}
                    	if (re.data[i].dealAmount != null) {
                    		 $("#icbc_totalDealAmount").html(formatMoney(re.data[i].dealAmount));
                             icbc_dealTotal += parseFloat(re.data[i].dealAmount);
                    	}
                    }
                    else if (re.data[i][transTypeColumnName] == order) {
                    	if (re.data[i].paymentAmount != null) {
                    		if (re.data[i].paymentBank == "SPD") {
                    			order_payment_SPD += parseFloat(re.data[i].paymentAmount);
                    		}
                    		if (re.data[i].paymentBank == "ICBC") {
                    			order_payment += parseFloat(re.data[i].paymentAmount);
                    		}
                    	}
                    }
                }
               
                $("#spd_totalPaymentAmount").html(formatMoney(order_payment_SPD));
                
                $("#icbc_totalPaymentAmount").html(formatMoney(order_payment));
               
                $("#spd_diffAmount").html(formatMoney(order_payment_SPD - spd_dealTotal));
                $("#icbc_diffAmount").html(formatMoney(order_payment - icbc_dealTotal));
            }
            else {
                //spd
                $("#spd_totalReceiptAmount").html("0.00");
                $("#spd_totalPaymentAmount").html("0.00");
                $("#spd_totalDealAmount").html("0.00");
                $("#spd_diffAmount").html("0.00");
                //icbc
                $("#icbc_totalReceiptAmount").html("0.00");
                $("#icbc_totalPaymentAmount").html("0.00");
                $("#icbc_totalDealAmount").html("0.00");
                $("#icbc_diffAmount").html("0.00");
            }
        }
    });

}

function dateFormat(date, fmt) {
    var o = {
        "M+": date.getMonth() + 1,                 //月份
        "d+": date.getDate(),                    //日
        "h+": date.getHours(),                   //小时
        "m+": date.getMinutes(),                 //分
        "s+": date.getSeconds(),                 //秒
        "q+": Math.floor((date.getMonth() + 3) / 3), //季度
        "S": date.getMilliseconds()             //毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

function exportToExcel() {
    var form = $("<form>");
    form.attr('style', 'display:none');
    form.attr('target', '');
    form.attr('method', 'post');
    form.attr('action', Context.PATH + "/report/account/inandouttoexcel.html");
    form.attr("enctype","multipart/form-data");//防止提交数据乱码

    var input1 = $('<input>');
    input1.attr('type', 'hidden');
    input1.attr('name', 'sdate');
    input1.attr('value', $("#startTime").val());

    var input2 = $('<input>');
    input2.attr('type', 'hidden');
    input2.attr('name', 'edate');
    input2.attr('value', $("#endTime").val());

    var input3 = $('<input>');
    input3.attr('type', 'hidden');
    input3.attr('name', 'accountName');
    input3.attr('value', $.trim($("#account_name").val()));

    $('body').append(form);
    form.append(input1);
    form.append(input2);
    form.append(input3);

    form.submit();
    form.remove();
}
