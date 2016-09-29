/**
 * Created by tuxianming on 2015/12/21.
 * 进项发票清单
 */
var _table;

jQuery(function ($) {
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
            "url": Context.PATH + "/report/finance/loadbuyerinvoiceoutlist.html"
            , "type": "POST"
            , data: function (d) {
            	
            	var buyerName = $("#buyer_name").val();
            	var startTime =  $("#startTime").val();
            	var endTime = $("#endTime").val();
            	
            	if(buyerName) d.buyName = buyerName;
            	if(startTime && endTime) {
            		d.startTime = startTime;
            		d.endTime = endTime;
            	}
            	
            	return d;
            }
//            //操作服务器返回的数据
//            , "dataSrc": function (result) {
//                //设置总发票张数
//                $("#total").text(result.recordsFiltered);
//                return result.data;//返回data
//            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
        //    {defaultContent: ""},
            {data: 'buyName'},
            {data: 'startAmount'},
            {data: 'actualOcurAmount'},
            {data: 'makeOutAmount'},
            {data: 'unmakeOutAmount'},
            {defaultContent: ''},
        ]
        , fnRowCallback: function (nRow, aData, iDataIndex) {
//        	var inputHtml = "<label class='checkbox'><input class='ace select_item' type='checkbox' value='"+aData.buyerId+"' name='status'><span class='lbl'></span></label>";
//        	$('td:eq(0)', nRow).html(inputHtml); //css("width", "20px")
        	
        	var param = "&buyerId="+aData.buyerId+"&buyName="+aData.buyName+"&";
        	
        	var stime = $("#startTime").val();
        	var etime = $("#endTime").val();
        	if(stime && etime)
        		param += "startTime="+stime+"&endTime="+etime+"&";
        	
        	var editLink = "&nbsp;&nbsp;<a href='buyerinvoiceout.html?"+encodeURI(param)+"' " +
        			"class='editBtn' name='edit'>查看明细</a>";
            $('td:eq(5)', nRow).html(editLink);
        	
        }
        ,columnDefs: [
              {
                  "targets": 1, //第几列 从0开始
                  "data": "startAmount",
                  "render": renderAmount
              }
              ,{
                  "targets": 2, //第几列 从0开始
                  "data": "actualOcurAmount",
                  "render": renderAmount
              }
              ,{
                  "targets": 3, //第几列 从0开始
                  "data": "makeOutAmount",
                  "render": renderAmount
              }
              ,{
                  "targets": 4, //第几列 从0开始
                  "data": "unmakeOutAmount",
                  "render": renderAmount
              }
        ]
    });
}

function renderAmount(data, type, full, meta) {
    if (data) {
        return formatMoney(data, 2);
    }
    return "-";
}

function initEvent() {

    $("#export_to_excel").click(function () {
        exportExcle();
    });
    
    // 全选/全不选(当前页)
//    $("#selectAll").click(function () {
//    	var checkbox = $(this);
//    	if(checkbox.prop("checked")){
//    		$(".select_item").prop("checked", "false");
//    	}else{
//    		$(".select_item").removeAttr("checked");
//    	}
//    });
    
    $("#queryBtn").click(function(){
    	_table.ajax.reload();
    });
    
}


function exportExcle() {
	var form = $("<form>")
		.css("display", "none")
		.attr("method", "post")
		.attr("enctype", "multipart/form-data")//防止提交数据乱码
		.attr("action", Context.PATH + "/report/finance/exportbuyerinvoiceoutlistexcel.html");
	
	var buyerName = $("#buyer_name").val();
	var startTime =  $("#startTime").val();
	var endTime = $("#endTime").val();
	
	if(buyerName) {
		form.append("<input type='hideen' name='buyName' value='"+buyerName+"'/>");
	}
	if(startTime && endTime) {
		form.append("<input type='hideen' name='startTime' value='"+startTime+"'/>");
		form.append("<input type='hideen' name='endTime' value='"+endTime+"'/>");
	}
	
	$('body').append(form);
	
	form.submit();
	form.remove();
}





