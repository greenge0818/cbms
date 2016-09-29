/**
 * Created by lx on 2015/12/15.
 */
 var table;
$(document).ready(function () {
	$("#dynamic-table").on("click",".link",function(){
		var orderId=$(this).attr("orderId");
		var url=Context.PATH + '/order/query/detail.html';
		location.href=url+"?orderid="+orderId;
	});
    table = $('#dynamic-table').DataTable({
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ordering": false,
        "bLengthChange": false,
        "ajax": {
            url: Context.PATH + '/report/business/query/ticketlist.html',
            type: 'POST',
            data: function (d) {
            	d.orgName=$("#sorganization").val(),
            	d.accountName=$("#buyer_name").val(),
            	d.startTime=$("#startTime").val(),
            	d.code=$("#code").val(),
            	d.endTime=$("#endTime").val(),
            	d.allStatus=function(){
            		var values = [];
            		var isAll = false;
            		$("#orderStatusList li input[type='checkbox']").each(function(){
            		    if(this.checked){
            		    	var value = $(this).val();
            		    	if(value==0)
            		    		isAll = true;
            		    	values.push(value);
            		    }
            		}); 
            		return isAll?"":values.toString();
            	}
            }
        },
        "fnRowCallback": function (nRow, aData, iDataIndex) {
        	var link = '<a class="link" href="javascript:void(0);" orderId='+aData.orderId+'>'+aData.code+'</a>';
        	$('td:eq(1)', nRow).html(link);
        	
        	var custName = aData.accountName;
        	if (aData.departmentCount > 1) {
        		custName = custName+"【"+aData.departmentName+"】";
        	}
        	$('td:eq(2)', nRow).html(custName);
        	
        	var actualPickWeightServer = formatMoney(aData.actualPickWeightServer,6);//实提重量
        	$('td:eq(6)', nRow).html(actualPickWeightServer).addClass("text-left");
        	
        	var usedWeight = formatMoney(aData.usedWeight,6);//已开销项票重量
        	$('td:eq(7)', nRow).html(usedWeight).addClass("text-left");
        	
        	var notOpenWeight = formatMoney(aData.notOpenWeight,6);//未开销项票重量
        	$('td:eq(8)', nRow).html(notOpenWeight).addClass("text-left");
        	
        	var allowanceWeight = formatMoney(aData.allowanceWeight,6);//折让重量
        	$('td:eq(9)', nRow).html(allowanceWeight).addClass("text-left");
        	
        	var bringamount =  formatMoney(aData.bringamount,2);//实提金额
        	$('td:eq(10)', nRow).html(bringamount).addClass("text-right");
        	
        	var usedAmount =  formatMoney(aData.usedAmount,2);//已开销项票金额
        	$('td:eq(11)', nRow).html(usedAmount).addClass("text-right");
        	
        	var notOpenAmount =  formatMoney(aData.notOpenAmount,2);//未开销项票金额
        	$('td:eq(12)', nRow).html(notOpenAmount).addClass("text-right");
        	
        	var allowanceBuyerAmount = formatMoney(aData.allowanceBuyerAmount,2);//折让买家金额
        	$('td:eq(13)', nRow).html(allowanceBuyerAmount).addClass("text-right");
        	
            if (aData.usedWeight == aData.actualPickWeightServer && aData.usedWeight != 0) {
            	$('td:eq(14)', nRow).html("已开具");
            } else if (aData.usedWeight < aData.actualPickWeightServer && aData.usedWeight != 0) {
            	$('td:eq(14)', nRow).html("部分开具");
            } else if (aData.usedWeight == 0) {
            	$('td:eq(14)', nRow).html("未开具");
            } 
            return nRow;
        },
        columns: [
			{data: 'creationTime'},
			{defaultContent: ''},
			{defaultContent: ''},
            {data: 'nsortName'},
            {data: 'material'},
            {data: 'spec'},
            {defaultContent: ''},
            {defaultContent: ''},
            {defaultContent: ''},
            {defaultContent: ''},
            {defaultContent: ''},
            {defaultContent: ''},
            {defaultContent: ''},
            {defaultContent: ''},
            {defaultContent: ''}
        ],
        columnDefs: [
             {
                 "targets": 0, //第几列 从0开始
                 "data": "creationTime",
                 "render": renderTime
             }
        ]
        
    });
    $("#searchForm").on("click","#btn", function() {
    	table.ajax.reload();
    	totalAmount();
    });
});

function renderTime(data){
    return date2String(new Date(data));
}
//js将Date类型转换为String类型：
function date2String(aDate){
    var year=aDate.getFullYear();
    var month=aDate.getMonth();
    month++;
    var mydate=aDate.getDate();
    var hour=aDate.getHours();
    var minute=aDate.getMinutes();
    var mytime=year+"-"+(month<10?"0":"")+month+"-"+(mydate<10?"0":"")+mydate+" "+(hour<10?"0":"")+hour+":"+(minute<10?"0":"")+minute;
    return mytime;
}


$("#orderStatusBtn").click(showSelectOptionsBox);

function showSelectOptionsBox(){
	var optionbox = $("#orderStatusList");
	if(optionbox.css("display") == "none"){
		optionbox.show();
		$("#search").on("click","#yes", function() {
	    	optionbox.hide();//隐藏checkbox
			//请求后台方法
			table.ajax.reload();//刷新列表
			totalAmount();
		});	
	}else{
		optionbox.hide();
	}
}

$("#exportexcel").click(function () {
    exportExcel();
});

//金额合计统计
function totalAmount() {
	$.ajax({
		type : "POST",
		url : Context.PATH + "/report/business/query/total.html",
		data : {
			"orgName":$("#sorganization").val(),
	    	"accountName":$("#buyer_name").val(),
	    	"startTime":$("#startTime").val(),
	    	"code":$("#code").val(),
	    	"endTime":$("#endTime").val(),
	    	"allStatus": function(){
	    		var values = [];
	    		var isAll = false;
	    		$("#orderStatusList li input[type='checkbox']").each(function(){
	    		    if(this.checked){
	    		    	var value = $(this).val();
	    		    	if(value==0)
	    		    		isAll = true;
	    		    	values.push(value);
	    		    }
	    		}); 
	    		return isAll?"":values.toString();
	    	}
		},
	    success: function (result) {
	    	if(result.success){
	    		if (result.data == null) {
	    			$("#totalAmount").text("0.00");
	    			$("#totalWeight").text("0.00");
	    		}
	    		$("#totalAmount").text(formatMoney(result.data.notOpenAmount,2));
				$("#totalWeight").text(formatMoney(result.data.notOpenWeight,6));
	    	}
	    }
	});
}
//导出报表
function exportExcel() {
    var form = $("<form>");
    form.attr('style', 'display:none');
    form.attr('target', '');
    form.attr('method', 'post');
    form.attr('action', Context.PATH + "/report/business/pooloutexcel.html");
    form.attr("enctype","multipart/form-data");//防止提交数据乱码
    
    if ($("#sorganizationHidden").length > 0) {
        var input0 = $('<input>');
        input0.attr('type', 'hidden');
        input0.attr('name', 'orgId');
        input0.attr('value', $("#sorganizationHidden").val());
        form.append(input0);
    }
    
    if ($("#buyer_name").length > 0) {
	    var input1 = $('<input>');
	    input1.attr('type', 'hidden');
	    input1.attr('name', 'accountName');
	    input1.attr('value', $.trim($("#buyer_name").val()));
    }
    
    var input2 = $('<input>');
    input2.attr('type', 'hidden');
    input2.attr('name', 'code');
    input2.attr('value', $.trim($("#code").val()));

    var input3 = $('<input>');
    input3.attr('type', 'hidden');
    input3.attr('name', 'startTime');
    input3.attr('value', $("#startTime").val());

    var input4 = $('<input>');
    input4.attr('type', 'hidden');
    input4.attr('name', 'endTime');
    input4.attr('value', $("#endTime").val());

    form.append(input1);
    form.append(input2);
    form.append(input3);
    form.append(input4);
    
    $('body').append(form);
    form.submit();
    form.remove();
}

