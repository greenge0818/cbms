/**
 * Created by Rabbit Mao on 2015/7/20.
 */
var invoiceIds;           // 发票ID集合
var dt;
function fillDataTable() {
    dt = $('#dynamicTable').DataTable({
        ajax: {
            url: Context.PATH + '/invoice/out/confirmedList.html',
            type: "POST",
            data: function (d) {
                return $.extend({}, d, {
                    sbuyerName: $("#sbuyerName").val(),
                    sexpressName: $("#sexpressName").val(),
                    stimeFrom: $("#stimefrom").val(),
                    stimeTo: $("#stimeto").val()
                });
            }
        },
        searching: false,
        processing: true,
        serverSide: true,
        ordering: false,
        bLengthChange: false,
        columns: [
            {defaultContent: ""},
            {data: 'invoiceOut.code'},   //销项发票号
            {data: 'invoiceOut.buyerName'},  //买家全称
            {data: 'invoiceOut.amount'},  //发票金额
            {data: 'invoiceOut.inputUserName'},  //录入人员
            {data: 'invoiceOut.inputUserMobil'},  //联系电话
            {data: 'express.sendTime'},    //寄出时间
            {data: 'invoiceOut.checkUserName'},   //确认人员
            {data: 'invoiceOut.checkDate'},    //确认时间
            {data: 'invoiceOut.status'}   //发票状态
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
                "data": "invoiceOut.id",
                "render": renderInput
            },
            {
                "targets": 3, //第几列 从0开始
                "data": "invoiceOut.amount",
                "render": renderAmount
            },
            {
                "targets": 6, //第几列 从0开始
                "data": "express.sendTime",
                "render": renderTime
            },
            {
                "targets": 8, //第几列 从0开始
                "data": "invoiceOut.checkDate",
                "render": renderTime
            },
            {
                "targets": -1, //第几列 从0开始
                "data": "invoiceOut.status",
                "render": getBankTransactionType
            }
        ]
    });
}

function renderInput(data){
    return "<input type='checkbox' name='check' value='" + data + "'>";
};

function getBankTransactionType(data, type, full, meta){
    var status = '--';
    if(data == '3') status='<span>已确认</span>';
    return status;
};


function renderTime(data, type, full, meta){
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

// 全选/全不选
$("#allCheck").click(function () {
    var checked = $(this).is(':checked');
    // 取消全选
    if (!checked) {
        $("input[name='check']").removeAttr("checked");
        $(this).removeAttr("checked");
    }
    else {
        $("input[name='check']").prop('checked', true);
        $(this).prop('checked', true);
    }

    invoiceTotal();
});

// 单选
$("body").on("click", "input[name='check']", function () {
    var checked = $(this).is(':checked');
    if (!checked) {
        $(this).removeAttr("checked");
        $("#allCheck").removeAttr("checked");   // 取消全选
    }
    else {
        $(this).prop('checked', true);
    }

    // 如果全部选中，那么全选checkbox选中
    var checkCount = $("input[name='check']").length;
    var checkedCount = $("input[name='check']:checked").length;
    if (checkCount == checkedCount) {
        $("#allCheck").prop('checked', true);
    }

    invoiceTotal();
});

// 统计选中的发票
function invoiceTotal() {
    invoiceIds = new Array();
    var checked = $("input[name='check']:checked");
    $(checked).each(function () {
        var id = $(this).val();
        invoiceIds.push(id);
    });
}

jQuery(function($) {

    fillDataTable();

    $("#searchForm").on("click","#search", function() {
        dt.ajax.reload();
        $("#allCheck").removeAttr("checked");   // 取消全选
    });

    $(document).on("click", "#print", function(){
        $.ajax({
            type: "POST",
            url: Context.PATH + '/invoice/out/getPrintTableData.html',
            data: {
                invoiceIds: invoiceIds.toString()
            },
            dataType: "json",
            success: function (response, textStatus, xhr) {
                if (response.success) {
                    var datas = response.data;
                    $("#printTable tbody").empty();
                    for(var i in datas){
                        $("#printTable tbody").append("<tr><td>"+datas[i].buyerName+"</td><td>"+datas[i].code+"</td><td>"+datas[i].amount+"</td></tr>");
                    }
                    $( "#printTable" ).print();
                    dt.ajax.reload();
                    $("#allCheck").removeAttr("checked");   // 取消全选
                } else {
                    cbms.alert("获取打印数据失败，请重试");
                }
            }
        });

    });
});

