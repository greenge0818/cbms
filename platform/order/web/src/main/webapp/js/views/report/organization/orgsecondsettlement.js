/**
 * Created by lixiang on 2015/7/31.
 */

var dt = "";
function fillDataTable() {
	
	    dt = $('#dynamic-table').DataTable({
        ajax: {
            url: Context.PATH + '/report/organization/org/secondsettlement.html',
            type: "POST", 
            data: function (d) {
                return $.extend({}, d, {
                	serialTimeStr:$("#dateTime").val(),//时间
                	
                });
            }
        },
        searching: false,
        "processing": true,
        "serverSide": true,
        "bLengthChange":false,
        fnRowCallback: function (nRow, aData, iDataIndex) {
        	 var creditLimit = formatMoney(aData.creditLimit,2);//储备金总额
        	 var yesterdayAmount = "<input type='hidden' name='ys' value="+aData.yesterdayAmount+">"+formatMoney(aData.yesterdayAmount,2);//昨日余额
        	 var amountAdd = "<input type='hidden' name='tdd' value="+aData.amountAdd+">"+formatMoney(aData.amountAdd,2);//当日金额增加
        	 var amountReduce = "<input type='hidden' name='trr' value="+aData.amountReduce+">"+formatMoney(Math.abs(aData.amountReduce),2);//当日金额减少
        	 var todayAmount = "<input type='hidden' name='ts' value="+aData.todayAmount+">"+formatMoney(aData.todayAmount,2);//当日金额
        	 $('td:eq(1)', nRow).html(creditLimit).addClass("text-right");
        	 $('td:eq(2)', nRow).html(yesterdayAmount).addClass("text-right");
        	 $('td:eq(3)', nRow).html(amountAdd).addClass("text-right");
        	 $('td:eq(4)', nRow).html(amountReduce).addClass("text-right");
        	 $('td:eq(5)', nRow).html(todayAmount).addClass("text-right");
             return nRow;
        },
        drawCallback: function(){
            setTotalRow();
        },
        columns: [
            {data: 'name'},   //服务中心
            {defaultContent: ''},  //储备金总额(元)
            {defaultContent: ''}, //昨日余额
            {defaultContent: ''},  //当日增加
            {defaultContent: ''}, //当日减少
            {defaultContent: ''} //当日余额
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
			
        ]
    });
    
}


jQuery(function($) {
	
    fillDataTable();
    window.setTimeout(setTotalRow,500);
    $("#searchForm").on("click","#queryBtn", function() {
    	if($("#dateTime").val()==''){
    		cbms.alert("请选择查询的日期！");
    		return;
    	}
        dt.ajax.reload();
        window.setTimeout(setTotalRow,500);
    });
});