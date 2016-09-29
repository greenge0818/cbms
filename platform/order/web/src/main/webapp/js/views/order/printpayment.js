/**
 * Created by chengui on 2016/3/10.
 */
var table;
$(document).ready(function () {
    table = $('#dynamic-table').dataTable({
    	ajax: {
			url: Context.PATH + '/order/query/printpaymentquery.htmll',
			type: "POST", 
			data: function (d) {
				d.requesterName = $("#owner").val();//客户名称
				d.startTime = $("#startTime").val();//起始时间
				d.endTime = $("#endTime").val()//终止时间
				d.status = $("#status").val();
			}
    	},
        aLengthMenu: [10, 50, 100],
        serverSide: true, //服务模式
        processing: true,//显示数据加载进度
        searching: false, //是否启用搜索
        ordering: false, //是否启用排序
        bFilter: false,
        bLengthChange: true, //不显示每页长度的选择条
        fnRowCallback: function (nRow, aData, iDataIndex) {
            $('td:eq(4)', nRow).html(formatMoney(aData.totalAmount,2)).addClass("text-right");
            var link = '<a href="'+Context.PATH+'/order/query/'+aData.id+'/requestdetail.html">审核</a>';
            $('td:eq(-1)', nRow).html(link);
            return nRow;
       },
       columns: [
           {data: 'payCode'},   //付款申请单号
           {data: 'created'}, //申请时间
           {data: 'buyerName'},  //公司全称
           {data: 'requesterName'},  //钢为交易员
           {defaultContent: ''}, //本次申请付款金额
           {data: 'status'},// 付款申请类型
           {defaultContent: ''}//操作
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
                "targets": 1, //第几列 从0开始
                "data": "created",
                "render": formatDay
            }
            ,{
                "targets": 5, //第几列 从0开始
                "data": "status",
                "render": toStatus
            }
       ]
    });

    $("#queryBtn").on("click", function () {
        table.fnDraw();
    });
    
    
	$(document).on("click","#showPrinted",function(){
		table.fnDraw();
	});
	
	$("table thead th:lt(5)").click(function(){
		var orderBy = $(this).attr("orderBy");
		var order=$(this).attr("order");
		if(order==undefined){
			order = "";
		}
		if(order==""||order=="asc"){
			$(this).attr("order","desc");
		}else{
			$(this).attr("order","");
		}
		$("#orderBy").val(orderBy);
		$("#order").val(order);
	});
    
});

