/**
 * Created by lixiang on 2015/7/29.
 */

var dt = "";
function fillDataTable() {
    	
	var accountId =$("#account_id").val();	
    dt = $('#dynamic-table').DataTable({
        ajax: {
            url: Context.PATH + '/order/query/secondpayauditskipother.html',
            type: "POST", 
            data: function (d) {
                return $.extend({}, d, {
                	accountId:accountId,
                    dateStart:$("#startTime").val(),
                    dateEnd:$("#endTime").val()
                });
            }
        },
        searching: false,
        "processing": true,
        "serverSide": true,
        fnRowCallback: function (nRow, aData, iDataIndex) {
        	var amount = formatMoney(aData.amount,2);
        	var currentBalance = formatMoney(aData.currentBalance,2);
        	$('td:eq(4)', nRow).html(amount).addClass("text-right");
            $('td:eq(5)', nRow).html(currentBalance).addClass("text-right");
        },
        columns: [
            {data: 'serialTime'},   //结算时间
            {data: 'associationType'},  //关联类型
            {data: 'consignOrderCode'},  //关联单号
            {data: 'applyType'},  //类型
            {defaultContent: ''},  //二次结算账户发生额
            {defaultContent: ''},    //二次结算账户余额
            {data: 'applyerName'},   //交易员
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
                "targets": 0, //第几列 从0开始
                "data": "serialTime",
                "render": renderTime
            },
            {
                "targets": 1, //第几列 从0开始
                "data": "associationType",
                "render": getType
            },
            {
                "targets": 3, //第几列 从0开始
                "data": "applyType",
                "render": getApplyType
            },
        ]
    });
    
}
function getType(data, type, full, meta){
    var status = '--';
    if(data == '1') status='<span>付款单号</span>';
    else if(data == '2') status='<span>交易单号</span>';
    else if(data == '3') status='<span>银行流水号 </span>';
    else if(data == '4') status='<span>还款流水号 </span>';
    return status;
};
   
function getApplyType(data, type, full, meta){
    var status = '--';
    if(data == '1') status='<span>充值到资金账户</span>';
    else if(data == '2') status='<span>支付合同货款</span>';
    else if(data == '3') status='<span>二次结算 </span>';
    else if(data == '4') status='<span>资金账户转出 </span>';
    else if(data == '5') status='<span>抵扣二次结算账户欠款 </span>';
    else if(data == '6') status='<span>二次结算账户余额转入资金账户 </span>';
    else if(data == '7') status='<span>抵扣二次结算账户欠款 </span>';
    else if(data == '7') status='<span>合同款到账 </span>';
    else if(data == '7') status='<span>二次结算账户余额转入资金账户 </span>';
    return status;
};    
    
    
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

    $("#createForm").on("click","#dateBtn", function() {
        dt.ajax.reload();
    });

    //var limitAmount = $("#limitAmount").val();//服务中心可用提现金额
    //var totalAmount = $("#total_amount").val();//申请提现的金额
    //var residueLimit = $("#residueLimit").val();//服务中心剩余额度
    //if(totalAmount > limitAmount){
    //	 $("#payBtnPass").prop("disabled", true)
    //}else{
    //	 $("#payBtnPass").removeAttr("disabled");
    //}
    //if(totalAmount > residueLimit){
    //	 $("#payBtnPass").prop("disabled", true)
    //}else{
    //	 $("#payBtnPass").removeAttr("disabled");
    //}
});
