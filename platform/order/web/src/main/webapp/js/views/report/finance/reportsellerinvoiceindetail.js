/**
 * Created by dq on 2016/01/04.
 * 供应商进项票详情报表
 */

$(document).ready(function(){
	
	initTable();
	
    $("#queryBtn").click(function(){
    	_table.ajax.reload();
    });
    
    $("#output").click(function () {
        exportExcel();
    });
    
});

function initTable() {
	_table = $("#dynamic-table").DataTable({
        "pageLength": 50, //每页记录数
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "bAutoWidth": true, //是否自动计算表格各列宽度
        "aLengthMenu": [50,100,150], //这个为选择每页的条目数default [10, 25, 50, 100]
        "lengthChange": true, //是否显示pageSize的下拉框
        "ajax": {
            "url": Context.PATH + "/report/finance/reportsellerinvoiceindetaildata.html"
            , "type": "POST"
            , data: function (d) {
            	var sellerId = $("#sellerId").val();
            	var startTime =  $("#startTime").val();
            	var endTime = $("#endTime").val();
            	
            	if(sellerId) d.sellerId = sellerId;
            	if(startTime) d.startTime = startTime;
            	if(endTime) d.endTime = endTime;
            	
            	return d;
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: "happenTime"},
            {data: 'contractCode'},
            {data: 'orderCode'},
            {data: 'orderAmount'},
            {data: 'invoiceInAmount'},
            {data: 'invoiceInBalance'},
            {data: 'remark'}
        ]
        , fnRowCallback: function (nRow, aData, iDataIndex) { //表格加载完以后
        	 $('td:eq(6)', nRow).css("minWidth", "80px");
        },columnDefs: [
             {
                 "targets": 0, //第几列 从0开始
                 "data": "happenTime",
                 "render": formatDay
             }
             ,{
                  "targets": 3, //第几列 从0开始
                  "data": "orderAmount",
                  "render": renderAmount
              }
              ,{
                  "targets": 4, //第几列 从0开始
                  "data": "invoiceOutAmount",
                  "render": renderAmount
              }
              ,{
                  "targets": 5, //第几列 从0开始
                  "data": "invoiceOutBalance",
                  "render": renderAmount
              }
        ], fnDrawCallback: function (row, data, start, end, display) {
        	initRangeAmount();
        }
        ,"scrollY": $(document.body).height() -10,
        "scrollX":true
    });
}

function initRangeAmount() {
	var param = {};
	
	var sellerId = $("#sellerId").val();
	var startTime =  $("#startTime").val();
	var endTime = $("#endTime").val();
	
	if(sellerId) param.sellerId = sellerId;
	if(startTime) param.startTime = startTime;
	if(endTime) param.endTime = endTime;
	$.ajax({
        type: 'post',
        url: Context.PATH + "/report/finance/reportsellerinvoiceinrangeamount.html",
        data: param,
        success: function (result) {
        	var data = result.data;
        	var primeBalance = amountIsNull(data.primeBalance);
        	var terminalBalance = amountIsNull(data.terminalBalance);
        	var actualAmount = amountIsNull(data.actualAmount);
        	var invoiceInAmount = amountIsNull(data.invoiceInAmount);
        	
        	_first = "<tr><td><b>期初余额（元）</b></td><td></td><td></td><td></td><td></td><td>"+primeBalance+"</td><td></td></tr>";
        	$("#dynamic-tbody").find("tr").first().before(_first);
    		_last = "<tr><td><b>期末余额（元）</b></td><td></td><td></td><td>"+actualAmount+"</td><td>"+invoiceInAmount+"</td><td>"+terminalBalance+"</td><td></td></tr>";
    		$("#dynamic-tbody").find("tr").last().after(_last);
        }
    });
}

function amountIsNull(amount){
	if(!amount || "null" == amount || null == amount) {
		return "-";
	}else {
		return formatMoney(amount, 2);
	}
}

function renderAmount(data, type, full, meta) {
    if (data) {
        return formatMoney(data, 2);
    }
    return "-";
}
function formatDay(data) {
	var dt = new Date(data);
	var time = dt.getFullYear() + "-" + ((dt.getMonth() + 1)/1<10?("0"+(dt.getMonth() + 1)):dt.getMonth() + 1) + "-" + ((dt.getDate())/1<10?("0"+dt.getDate()):dt.getDate()) + " " + 
		((dt.getHours()/1)<10?("0"+dt.getHours()):dt.getHours()) + ":" + ((dt.getMinutes()/1)<10?("0"+dt.getMinutes()):dt.getMinutes()) + ":" + ((dt.getSeconds()/1)<10?("0"+dt.getSeconds()):dt.getSeconds());
	return time;
}

function exportExcel() {
    var form = $("<form>");
    form.attr('style', 'display:none');
    form.attr('target', '');
    form.attr('method', 'post');
    form.attr('enctype', 'multipart/form-data');
    form.attr('action', Context.PATH + "/report/finance/reportsellerinvoiceindetailexcel.html");
    
    if ($("#sellerId").val().length > 0) {
		var input1 = $('<input>');
	    input1.attr('type', 'hidden');
	    input1.attr('name', 'sellerId');
	    input1.attr('value', $("#sellerId").val());
	    form.append(input1); 
    }
	var input2 = $('<input>');
    input2.attr('type', 'hidden');
    input2.attr('name', 'startTime');
    input2.attr('value', $("#startTime").val());
    form.append(input2);
    
    var input3 = $('<input>');
    input3.attr('type', 'hidden');
    input3.attr('name', 'endTime');
    input3.attr('value', $("#endTime").val());
    form.append(input3);

    $('body').append(form);
    form.submit();
    form.remove();
}