/**
 * Created by lixiang on 2015/8/4.
 */

var dt = "";
function fillDataTable() {
    dt = $('#dynamic-table').DataTable({
        ajax: {
            url: Context.PATH + '/invoice/out/findwaitinginvoice.html',
            type: "POST", 
            data: function (d) {
                return $.extend({}, d, {
                	orgName:$("#sorganization").val(),//服务中心
                    buyerName:$("#buyer_name").val()//买家全称
                });
            }
        },
        searching: false,
        "processing": true,
        "serverSide": true,
        "bLengthChange":false,
        fnRowCallback: function (nRow, aData, iDataIndex) {
        	var link = '<a href="' + Context.PATH + '/invoice/out/waitinginvoicedetail.html?id='+aData.id+'">查看详情</a>';
        	var totalWeight = aData.totalWeight.toFixed(4);
        	var totalAmount = "<input type='hidden' name='je' value="+aData.totalAmount+">"+formatMoney(aData.totalAmount,2);
            $('td:eq(-1)', nRow).html(link);
            //判断应收款，如果小于零显示其本身，大于零显示为0.00
            var balanceSecondSettlement = 0.00;
            if(aData.balanceSecondSettlement < 0){
        		balanceSecondSettlement ="<input type='hidden' name='ys' value="+aData.balanceSecondSettlement+">"+formatMoney(aData.balanceSecondSettlement,2);//应收款
        	}else{
        		balanceSecondSettlement = "0.00";
        	}
            $('td:eq(1)', nRow).html(balanceSecondSettlement).addClass("text-right");
            $('td:eq(2)', nRow).html(totalWeight).addClass("text-left");
            $('td:eq(3)', nRow).html(totalAmount).addClass("text-right");
             return nRow;
        },
        columns: [
            {data: 'buyerName'},  //买家全称
            {defaultContent: ''},  //结算应收款
            {defaultContent: ''},    //本次开票重量
            {defaultContent: ''},   //本次开票金额
            {data: 'code'},   //发票数量
            {data: 'orgName'},  //服务中心
            {data: 'created'},//提交时间
            {defaultContent: ''} //操作
        ],
        "oLanguage": {                          //汉化
            "sLengthMenu": "每页显示 _MENU_ 条记录",
            "sZeroRecords": "没有检索到数据",
            "sInfo": "当前数据为从第 _START_ 到第 _END_ 条数据；总共有 _TOTAL_ 条记录",
            "sInfoEmtpy": "没有数据",
            "sProcessing": "正在加载数据...",
            "oPaginate": {
                "sFirst": "首页",
                "sPrevious": "前页",
                "sNext": "后页",
                "sLast": "尾页"
            }
        },
        columnDefs: [
            {
                "targets": 6, //第几列 从0开始
                "data": "created",
                "render": renderTime
            }
        ]
    });
    
}
  
    
    
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

jQuery(function($) {
	
    fillDataTable();
    window.setTimeout(setTotalRow,500);
    $("#ser").on("click","#button", function() {
        dt.ajax.reload();
        window.setTimeout(setTotalRow,500);
    });
});