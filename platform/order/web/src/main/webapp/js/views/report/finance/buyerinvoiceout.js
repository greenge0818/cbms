/**
 * Created by tuxianming on 2015/12/21.
 * 进项发票清单
 */
var _first,  //期初余额（元） html 
	_last, 	//期末余额（元） html
	_isClickSearch=false, //点击搜索按钮
	_table;	//DateTable

$(document).ready(function(){
	initTable();
	initEvent();
});

function initTable() {
	_table = $("#dynamic-table").DataTable({
		"scrollY": ($(window).height()-(300)<300?300:$(window).height()-(300))+"px",  //300: 大约为页头与页尾的高度
		"scrollX":true,
		"paging": "false",
        "pageLength": 50, //每页记录数
        "processing": true,//显示数据加载进度
        "serverSide": true, //服务模式
        "searching": false, //是否启用搜索
        "ordering": false, //是否启用排序
        "bAutoWidth": true, //是否自动计算表格各列宽度
        "aLengthMenu": [50,100,150], //这个为选择每页的条目数default [10, 25, 50, 100]
        "lengthChange": true, //是否显示pageSize的下拉框
        "ajax": {
            "url": Context.PATH + "/report/finance/loadbuyerinvoiceout.html"
            , "type": "POST"
            , data: function (d) {
            	var buyerId = $("#buyer_id").val();
            	var startTime =  $("#startTime").val();
            	var endTime = $("#endTime").val();
            	
            	if(buyerId) d.buyerId = buyerId;
            	if(startTime) d.startTime = startTime;
            	if(endTime) d.endTime = endTime;
            	
            	return d;
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: "dateStr"},
            {data: 'contractCode'},
            {data: 'orderCode'},
            {data: 'orderAmount'},
            {data: 'invoiceOutAmount'},
            {data: 'invoiceOutBalance'},
            {data: 'remark'}
        ]
        , fnRowCallback: function (nRow, aData, iDataIndex) { //表格加载完以后
        	
        	 $('td:eq(6)', nRow).css("minWidth", "80px");
        },columnDefs: [
             {
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
        ], "footerCallback": function (row, data, start, end, display) {
    		if(!_isClickSearch && _first && _last){ //此次页面加载，是不是通过点击搜索按钮加载，如果不是，则不重新发送请求，
    			$("#dynamic-table tbody").find("tr").first().before(_first);
    			$("#dynamic-table tbody").find("tr").last().after(_last);

    		}else{
    			var param = {};
    			
    			var buyerId = $("#buyer_id").val();
            	var startTime =  $("#startTime").val();
            	var endTime = $("#endTime").val();
            	
            	if(buyerId) param.buyerId = buyerId;
            	if(startTime) param.startTime = startTime;
            	if(endTime) param.endTime = endTime;
    			
    			$.ajax({
                    type: 'post',
                    url: Context.PATH + "/report/finance/loadbuyerinvoiceoutsum.html",
                    data: param,
                    success: function (result) {
                    	//var dto = $.parseJSON(result);
                		_first = "<tr><td><b>期初余额（元）</b></td><td></td><td></td><td></td><td></td><td>"+renderTotalAmount(result.startAmount)+"</td><td></td></tr>";
            			_last = "<tr><td><b>期末余额（元）</b></td><td></td><td></td><td>"+renderTotalAmount(result.actualOcurAmount)+"</td><td>"+renderTotalAmount(result.makeOutAmount)+"</td><td>"+renderTotalAmount(result.unmakeOutAmount)+"</td><td></td></tr>";
            			
            			$("#dynamic-table tbody").find("tr").first().before(_first);
            			$("#dynamic-table tbody").find("tr").last().after(_last);
            			_isClickSearch = false;
                    }
                });
    		}
        }
    });
}

function renderAmount(data, type, full, meta) {
    if (data) {
        return formatMoney(data, 2);
    }
    return "-";
}

function renderTotalAmount(data) {
    if (data) {
        return formatMoney(data, 2);
    }
    return "-";
}

function initEvent() {

    $("#export_to_excel").click(function () {
    	exportExcle();
    });

    $("#queryBtn").click(function(){
    	_isClickSearch = true;
    	_table.ajax.reload();
    });
    
}

function exportExcle(){
	var form = $("<form>")
		.css("display", "none")
		.attr("method", "post")
		.attr("enctype", "multipart/form-data")//防止提交数据乱码
		.attr("action", Context.PATH + "/report/finance/exportbuyerinvoiceoutexcel.html");

	var buyerId = $("#buyer_id").val();
	var startTime =  $("#startTime").val();
	var endTime = $("#endTime").val();

	if(buyerId) {
		form.append("<input type='hideen' name='buyerId' value='"+buyerId+"'/>");
	}
	if(startTime) {
		form.append("<input type='hideen' name='startTime' value='"+startTime+"'/>");
	}
	if(endTime){
		form.append("<input type='hideen' name='endTime' value='"+endTime+"'/>");
	}
	
	$('body').append(form);

	form.submit();
	form.remove();
}
