/**
 * Created by dq on 2016/01/04.
 * 供应商进项票报表
 */
var _table;

jQuery(function ($) {
	
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
            "url": Context.PATH + "/report/finance/reportsellerinvoiceindata.html"
            , "type": "POST"
            , data: function (d) {
            	
            	var sellerName = $("#sellerName").val();
            	var startTime =  $("#startTime").val();
            	var endTime = $("#endTime").val();
            	
            	if(sellerName) d.sellerName = sellerName;
            	if(startTime) d.startTime = startTime;
            	if(endTime) d.endTime = endTime;
            	
            	return d;
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'sellerName'},
            {data: 'primeBalance'},
            {data: 'actualAmount'},
            {data: 'invoiceInAmount'},
            {data: 'terminalBalance'},
            {defaultContent: ''},
        ]
        , fnRowCallback: function (nRow, aData, iDataIndex) {
	
        	var param = "sellerId="+aData.sellerId+"&startTime="+$("#startTime").val()+"&endTime="+$("#endTime").val();
        	
        	var editLink = "&nbsp;&nbsp;<a href='reportsellerinvoiceindetail.html?"+encodeURI(param)+"' " +
        			"class='editBtn' name='edit'>查看明细</a>";
            $('td:eq(5)', nRow).html(editLink);
        }
        ,columnDefs: [
              {
                  "targets": 1, //第几列 从0开始
                  "data": "primeBalance",
                  "render": renderAmount
              }
              ,{
                  "targets": 2, //第几列 从0开始
                  "data": "actualAmount",
                  "render": renderAmount
              }
              ,{
                  "targets": 3, //第几列 从0开始
                  "data": "invoiceInAmount",
                  "render": renderAmount
              }
              ,{
                  "targets": 4, //第几列 从0开始
                  "data": "terminalBalance",
                  "render": renderAmount
              }
        ]
        ,"scrollY": $(document.body).height() -10,
        "scrollX":true
    });
}

function renderAmount(data, type, full, meta) {
    if (data) {
        return formatMoney(data, 2);
    }
    return "-";
}

function exportExcel() {
    var form = $("<form>");
    form.attr('style', 'display:none');
    form.attr('target', '');
    form.attr('method', 'post');
    form.attr('enctype', 'multipart/form-data');
    form.attr('action', Context.PATH + "/report/finance/reportsellerinvoiceinexcel.html");
    
	if ($("#sellerName").val().length > 0) {
		var input1 = $('<input>');
	    input1.attr('type', 'hidden');
	    input1.attr('name', 'sellerName');
	    input1.attr('value', $("#sellerName").val());
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




