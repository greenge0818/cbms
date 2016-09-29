/**
 * Created by lx on 2016/02/24.
 * 初次付款信息报表
 */

var dt;
$(document).ready(function () {
	    dt = $('#dynamic-table').DataTable({
        ajax: {
            url: Context.PATH + '/report/finance/select/initialpayment.html',
            type: "POST", 
            data: function (d) {
            	d.orgName = $("#sorganization").val() == "无"?"":$("#sorganization").val(),//服务中心
            	d.sellerName = $("#seller_name").val();
            	d.code = $("#code").val();
            	d.accounting = $("#accounting").val();
            	d.startTime = $("#startTime").val();//起始时间
            	d.endTime = $("#endTime").val();//终止时间
            }
        },
        aLengthMenu: [50, 100, 150],
        serverSide: true, //服务模式
        processing: true,//显示数据加载进度
        searching: false, //是否启用搜索
        ordering: false, //是否启用排序
        bFilter: false,
        bLengthChange: true, //不显示每页长度的选择条
        fnRowCallback: function (nRow, aData, iDataIndex) {
             $('td:eq(3)', nRow).html(formatMoney(aData.payAmount,2)).addClass("text-right");
             return nRow;
        },
        columns: [
            {data: 'buyerName'},   //
            {data: 'sellerName'}, //卖家全称
            {data: 'code'},  //
            {defaultContent: ''},  //初次付款金额
            {data: 'firstApplyTime'}, //初次申请时间
            {data: 'firstPayTime'}, //初次付款时间
            {data: 'status'},// 状态
            {data: 'accounting'},//核算会计
            {data: 'buyerOrgName'},//发票状态
            {data: 'paymentBank'}//出款银行
        ],
        "oLanguage": {                          //汉化
            "sLengthMenu": "每页显示 _MENU_ 条记录",
            "sZeroRecords": "没有检索到数据",
            "sInfo": "当前数据为从第 _START_ 到第 _END_ 条数据；总共有 _TOTAL_ 条记录",
            "sInfoEmtpy": "没有数据",
            "sProcessing": "正在加载数据...",
            "oPaginate": {
                "sFirst": "首页",
		        "sPrevious": "上一页",
		        "sNext": "下一页",
                "sLast": "尾页"
            }
        },
        columnDefs: [
             {
                 "targets": 4, //第几列 从0开始
                 "data": "firstApplyTime",
                 "render": formatDay
             }
             ,{
                  "targets": 5, //第几列 从0开始
                  "data": "firstPayTime",
                  "render": formatDay
              }
             ,{
                 "targets": 9, //第几列 从0开始
                 "data": "paymentBank",
                 "render": getBank
             }
             ,{
                 "targets": 6, //第几列 从0开始
                 "data": "status",
                 "render": toStatus
             }
        ]
    });
	    
    function formatDay(data) {
    	var dt = new Date(data);
    	var time = dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate() + " " + 
		((dt.getHours()/1)<10?("0"+dt.getHours()):dt.getHours()) + ":" + ((dt.getMinutes()/1)<10?("0"+dt.getMinutes()):dt.getMinutes()) + ":" 
		+ ((dt.getSeconds()/1)<10?("0"+dt.getSeconds()):dt.getSeconds());
    	return time;
    }
    
    function getBank(data) {
    	var bank = "";
    	if (data == "SPD") {
    		bank = "浦发银行";
    	} else if (data == "ICBC") {
    		bank = "工商银行";
    	}
    	return bank;
    }
    
    function toStatus(data) {
    	var status ="";
    	if(data == "4") {
    		status = "已关联";
    	} else if (data == "5") {
    		status = "请求关闭";
    	} else if (data == "6") {
    		status = "待二次结算";
    	} else if (data == "7") {
    		status = "待开票申请";
    	} else if (data == "8") {
    		status = "待开票";
    	} else if (data == "10") {
    		status = "交易完成";
    	} else if (data == "-3") {
    		status = "订单关闭-5点半未关联（待关联）订单";
    	} else if (data == "-4") {
    		status = "订单关闭-确认付款关闭";
    	} else if (data == "-5") {
    		status = "申请关闭订单";
    	} else if (data == "-6") {
    		status = "总经理审核通过关闭订单";
    	} else if (data == "-7") {
    		status = "订单关闭-财务审核通过关闭订单";
    	} else if (data == "-8") {
    		status = "订单关闭-财务审核通过付款后关闭订单";
    	} 
    	return status;
    }
});

$("#searchForm").on("click","#queryBtn", function() {
	dt.ajax.reload();
	$.ajax({
		type : "POST",
		url : Context.PATH + '/report/finance/total.html',
		data : {
			"orgName" : $("#sorganization").val() == "无"?"":$("#sorganization").val(),//服务中心
        	"sellerName" : $("#seller_name").val(),
        	"code" : $("#code").val(),
        	"accounting" : $("#accounting").val(),
        	"startTime" : $("#startTime").val(),//起始时间
        	"endTime" : $("#endTime").val()//终止时间
		},
	    success: function (result) {
	    	if(result.success){
	    		if (result.data == null) {
	    			$("#pay_amount").text("0.00");
	    		}
				$("#pay_amount").text(formatMoney(result.data.payAmount,2));
	    	}
	    }
	});
});


$("#exportexcel").click(function () {
    exportExcel();
});

//导出报表
function exportExcel() {
    var form = $("<form>");
    form.attr('style', 'display:none');
    form.attr('target', '');
    form.attr('method', 'post');
    form.attr('action', Context.PATH + "/report/finance/initialpaymentexcel.html");
    form.attr("enctype","multipart/form-data");//防止提交数据乱码
    
    if ($("#seller_name").val()) {
	    var input1 = $('<input>');
	    input1.attr('type', 'hidden');
	    input1.attr('name', 'sellerName');
	    input1.attr('value', $("#seller_name").val());
    }
    var input2 = $('<input>');
    input2.attr('type', 'hidden');
    input2.attr('name', 'startTime');
    input2.attr('value', $("#startTime").val());
    
    var input3 = $('<input>');
    input3.attr('type', 'hidden');
    input3.attr('name', 'endTime');
    input3.attr('value', $("#endTime").val());
    
    if ($("#sorganization").val()) {
	    var input4 = $('<input>');
	    input4.attr('type', 'hidden');
	    input4.attr('name', 'orgName');
	    input4.attr('value', $("#sorganization").val())
    }
    
    if ($("#code").val()) {
	    var input5 = $('<input>');
	    input5.attr('type', 'hidden');
	    input5.attr('name', 'code');
	    input5.attr('value', $("#code").val())
    }
    
    if ($("#accounting").val()) {
	    var input6 = $('<input>');
	    input6.attr('type', 'hidden');
	    input6.attr('name', 'accounting');
	    input6.attr('value', $("#accounting").val())
    }

    form.append(input1);
    form.append(input2);
    form.append(input3);
    form.append(input4);
    form.append(input5);
    form.append(input6);
    
    $('body').append(form);
    form.submit();
    form.remove();
}

