/**
 * tuxianming on 20160614.
 * 应收应付发票报表
 */
var intable, 
	outtable,	
	type='in';  //in: 应收发票报表 | out: 应付发票报表
var _tableHeight = null;

jQuery(function ($) {
	_tableHeight = ($(window).height()-(300)<300?300:$(window).height()-(300))+"px"
	initInTable();
    initEvent();
    
});

function initInTable() {
	intable = $("#in-dynamic-table").DataTable({
        "pageLength": 50, //每页记录数
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "aLengthMenu": [50,100,150], //这个为选择每页的条目数default [10, 25, 50, 100]
        "lengthChange": true, //是否显示pageSize的下拉框
        "oLanguage": {sUrl: Context.PATH + "/js/DT_zh.txt"},
        "scrollY": _tableHeight,
        "scrollX": true,
        "ajax": {
            "url": Context.PATH + "/report/finance/loadinvoicein.html"
            , "type": "POST"
            , data: function (d) {
                setQueryParam(d);
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'accountName'},
            {data: 'initAmount'},
            {data: 'invoiceAmount'},
            {data: 'actInvoiceAmount'},
            {data: 'finalAmount'},
            {data: 'accountId'}
        ],
        columnDefs: [
               {
                   "targets": 1, //第几列 从0开始
                   "data": "initAmount",
                   "render": renderAmount
               }
               ,{
                   "targets": 2, //第几列 从0开始
                   "data": "invoiceAmount",
                   "render": renderAmount
               }
               ,{
                   "targets": 3, //第几列 从0开始
                   "data": "actInvoiceAmount",
                   "render": renderAmount
               }
               ,{
            	   "targets": 4, //第几列 从0开始
            	   "data": "finalAmount",
            	   "render": renderAmount
               }
               ,{
                   "targets": 5, //第几列 从0开始
                   "data": "accountId",
                   "render": renderInCtrl
               }
         ]
        , fnRowCallback: function (nRow, aData, iDataIndex) {
        	
       	 $('td:eq(2), td:eq(3), td:eq(4), td:eq(5)', nRow).css("text-align", "right");
       }
    });
}

function initOutTable() {
	outtable = $("#out-dynamic-table").DataTable({
        "pageLength": 50, //每页记录数
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "aLengthMenu": [50,100,150], //这个为选择每页的条目数default [10, 25, 50, 100]
        "lengthChange": true, //是否显示pageSize的下拉框
        "oLanguage": {sUrl: Context.PATH + "/js/DT_zh.txt"},
        "scrollY": _tableHeight,
        "scrollX": true,
        "ajax": {
            "url": Context.PATH + "/report/finance/loadinvoiceout.html"
            , "type": "POST"
            , data: function (d) {
                setQueryParam(d);
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'accountName'},
            {data: 'initAmount'},
            {data: 'invoiceAmount'},
            {data: 'actInvoiceAmount'},
            {data: 'finalAmount'},
            {data: 'accountId'}
        ],
        columnDefs: [
               {
                   "targets": 1, //第几列 从0开始
                   "data": "initAmount",
                   "render": renderAmount
               }
               ,{
                   "targets": 2, //第几列 从0开始
                   "data": "invoiceAmount",
                   "render": renderAmount
               }
               ,{
            	   "targets": 4, //第几列 从0开始
            	   "data": "finalAmount",
            	   "render": renderAmount
               }
               ,{
                   "targets": 5, //第几列 从0开始
                   "data": "accountId",
                   "render": renderInCtrl
               }
         ]
        , fnRowCallback: function (nRow, aData, iDataIndex) {
        	
        	 $('td:eq(2), td:eq(3), td:eq(4), td:eq(5)', nRow).css("text-align", "right");
          }
    });
}

function renderAmount(data, type, full, meta) {
    if (data) {
        return formatMoney(data, 2);
    }
    return "0.00";
}

function renderInCtrl(data, type, full, meta) {
    //return '<a href="'+Context.PATH +'/report/finance/xx.html">查看详情</a>';
	return '';
}

function initEvent() {
	
	$(document).on("click", "#report-switch li", function(){
		var _this = $(this);
		type = _this.attr("ctype");
		$("#report-switch li").removeClass("active");
		_this.addClass("active");
		
		$("#toast-text").text(_this.find("a").text());
		
		if(type=='in'){
			$("#seller-input").show();
        	$("#buyer-input").hide();
			$("#in-table-wrapper").show();
			$("#out-table-wrapper").hide();
			
			$("#accountId").attr("accounttype", "seller");
			accounttype
			
			if(!intable)
				initInTable();
		}else if(type == 'out'){
			$("#accountId").attr("accounttype", "buyer");
			$("#out-table-wrapper").show();
			$("#in-table-wrapper").hide();
			$("#seller-input").hide();
        	$("#buyer-input").show();
			if(!outtable)
				initOutTable();
		}
			
		
	});
	
    $("#queryBtn").click(function () {
        if(type == 'in'){
        	if(intable)
        		intable.ajax.reload();	
        	else
        		initInTable();
        }else if(type=='out'){
        	if(outtable)
        		outtable.ajax.reload();
        	else
        		initOutTable();
        	
        }	
    });

    $("#output").click(function () {
        exportExcle();
    });

}

function exportExcle() {
    var form = $("<form>");
    form.attr('style', 'display:none');
    form.attr('target', '');
    form.attr('method', 'post');
    
    if(type=='in')
    	form.attr('action', Context.PATH + "/report/finance/exportinvoicein.html");
    else
    	form.attr('action', Context.PATH + "/report/finance/exportinvoiceout.html");

    form.attr("enctype", "multipart/form-data");//防止提交数据乱码

    $('body').append(form);

    var params = setQueryParam({});
    for(var p in params){
    	var input = $('<input>');
    	input.attr('type', 'hidden');
    	input.attr('name', p);
    	input.attr('value', params[p]);
        form.append(input);
    }

    form.submit();
    form.remove();
}

function setQueryParam(d) {
	if(d==null) 
		d = {};
	
	if(type=='in'){
		var accountId = $("#seller-input").attr("accountid");
		if(accountId){
	    	d.accountId = accountId;
	    }
	}else{
		var accountId = $("#buyer-input").attr("accountid");
		if(accountId){
	    	d.accountId = accountId;
	    }
	}
    var accountId = $("#accountId").attr("accountid");
    
    
    var startTime = $("#startTime").val();
    if(startTime){
    	d.startTime = startTime;
    }
    
    var endTime = $("#endTime").val();
    if(endTime){
    	d.endTime = endTime;
    }
    return d;
}




