/**
 * Created by lx on 2016/01/13.
 * 进行票记账报表
 */

var dt;
$(document).ready(function () {
	    dt = $('#dynamic-table').DataTable({
        ajax: {
            url: Context.PATH + '/report/business/invoicekeepingdata.html',
            type: "POST", 
            data: function (d) {
            	var checkUserId = $("#userId_select").val();
            	d.startDate = $("#startTime").val();//起始时间
            	d.endDate = $("#endTime").val();//终止时间
            	if(checkUserId){
            		d.checkUserId = checkUserId;
            	}
            	var invoiceNo = $("#invoiceNo").val();
            	if(invoiceNo){
            		d.invoiceNo = invoiceNo;
            	}
            	
            	var category = $("#category").val();
            	if(category){
            		d.category = category;
            	}
            	
            	var seller = $("#seller").val();
            	if(seller){
            		 d.seller = seller;
            	}
            	
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
             $('td:eq(3)', nRow).html(formatMoney(aData.noTaxAmount,2)).addClass("text-right");
             $('td:eq(4)', nRow).html(formatMoney(aData.taxAmount,2)).addClass("text-right");
             $('td:eq(5)', nRow).html(formatMoney(aData.amount,2)).addClass("text-right");
             return nRow;
        },
        columns: [
            {data: 'invoiceDate'},   //开票时间
            {data: 'code'},  //进项发票号
            {data: 'sellerName'}, //卖家全称
            {defaultContent: ''},  //发票金额
            {defaultContent: ''}, //税额
            {defaultContent: ''}, //价税合计
            {data: 'checkUserName'},// 确认人员
            {data: 'checkDate'},//确认时间
            {data: 'status'},//发票状态
            {data: 'name'}//大类
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
                 "targets": 0, //第几列 从0开始
                 "data": "invoiceDate",
                 "render": formatDay
             }
             ,{
                  "targets": 7, //第几列 从0开始
                  "data": "checkDate",
                  "render": formatDay
              }
             ,{
                 "targets": 8, //第几列 从0开始
                 "data": "status",
                 "render": toStatus
             }
        ]
    });
	    
    function formatDay(data) {
    	var dt = new Date(data);
    	var month = ((dt.getMonth() + 1)/1<10?("0"+(dt.getMonth() + 1)):dt.getMonth() + 1);
    	var time = dt.getFullYear() + "-" + month + "-" + ((dt.getDate())/1<10?("0"+dt.getDate()):dt.getDate());
    	return time;
    }
    
    function toStatus(data) {
    	var status ="";
    	if(data == "WAIT") {
    		status = "待认证";
    	} else if (data == "ALREADY") {
    		status = "已认证";
    	}
    	return status;
    }
});

$("#searchForm").on("click","#queryBtn", function() {
	dt.ajax.reload();
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
    form.attr('action', Context.PATH + "/report/business/invoicekeepingexcel.html");
    form.attr("enctype","multipart/form-data");//防止提交数据乱码
    
    if ($("#userId_select").val()) {
	    var input1 = $('<input>');
	    input1.attr('type', 'hidden');
	    input1.attr('name', 'checkUserId');
	    input1.attr('value', $("#userId_select").val());
    }
    var input2 = $('<input>');
    input2.attr('type', 'hidden');
    input2.attr('name', 'startDate');
    input2.attr('value', $("#startTime").val());
    
    var input3 = $('<input>');
    input3.attr('type', 'hidden');
    input3.attr('name', 'endDate');
    input3.attr('value', $("#endTime").val());
    
    if ($("#invoiceNo").val()) {
	    var input4 = $('<input>');
	    input4.attr('type', 'hidden');
	    input4.attr('name', 'invoiceNo');
	    input4.attr('value', $("#invoiceNo").val())
    }
    
    if ($("#category").val()) {
	    var input5 = $('<input>');
	    input5.attr('type', 'hidden');
	    input5.attr('name', 'category');
	    input5.attr('value', $("#category").val())
    }
    
    if ($("#seller").val()) {
	    var input6 = $('<input>');
	    input6.attr('type', 'hidden');
	    input6.attr('name', 'seller');
	    input6.attr('value', $("#seller").val())
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

