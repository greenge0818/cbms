/**
 * Created by lixiang on 2015/8/4.
 */

var dt = "";
function fillDataTable() {
	var dateStart = $("#startTime").val();
	var dateEnd = $("#startTime").val();
    dt = $('#dynamic-table').DataTable({
        ajax: {
            url: Context.PATH + '/invoice/out/findinvoicedetail.html',
            type: "POST", 
            data: function (d) {
                return $.extend({}, d, {
                	id:$("#invoicoutMainId").val(),
                	dateStart:dateStart,
                	dateEnd:dateEnd
                });
            }
        },
        searching: false,
        "processing": true,
        "serverSide": true,
        "bLengthChange":false,
        fnRowCallback: function (nRow, aData, iDataIndex) {

            var weight = aData.weight.toFixed(4);
            var price = formatMoney(aData.price,2);
            var noTaxAmount = formatMoney(aData.noTaxAmount,2);
            var taxAmount = formatMoney(aData.taxAmount,2);
            var amount = formatMoney(aData.amount,2);
            $('td:eq(4)', nRow).html(weight).addClass("text-left");
            $('td:eq(5)', nRow).html(price).addClass("text-right");
            $('td:eq(6)', nRow).html(noTaxAmount).addClass("text-right");
            $('td:eq(7)', nRow).html(taxAmount).addClass("text-right");
            $('td:eq(8)', nRow).html(amount).addClass("text-right");
            return nRow;
        },
        columns: [
            {data: 'code'},  //编号
            {data: 'nsortName'},  //品名
            {data: 'spec'},    //规格
            {data: 'material'},  //材质
            {defaultContent: ''},  //重量
            {defaultContent: ''},  //单价
            {defaultContent: ''},//金额
            {defaultContent: ''},//税额
            {defaultContent: ''},//价税合计
            {data: 'orgName'},//服务中心
            {data: 'created'}//提交时间
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
                "targets": -1, //第几列 从0开始
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
    $("#ser").on("click","#button", function() {
        dt.ajax.reload();
    });
});