/**
 * create by wangxianjun
 * 可变更交易单
 */
var tradeTable;
var _tableHeight = "";
jQuery(function ($) {
    _tableHeight = ($(window).height()-(300)<300?300:$(window).height()-(300))+"px";
    initTable();
    //搜索事件
    $("#queryBtn").click(function () {
        tradeTable.ajax.reload();
    });
});

function initTable() {
    var url = Context.PATH + "/order/changecontract/queryChangeOrderData.html";
    tradeTable = jQuery("#dynamic-table").DataTable({
        "ajax": {
            "processing": true,//显示数据加载进度
            "serverSide": true, //服务模式
            "searching": false, //是否启用搜索
            "ordering": false, //是否启用排序
            "lengthChange": true, //显示pageSize的下拉框 50 100 150
            "url": url
            , "type": "POST"
            , data: function (d) {
                d.code = $("#code").val();
                d.accountName = $("#accountName").val();
                d.ownerName = $("#ownerName").val();
                d.startTime = $("#startTime").val();
                d.endTime = $("#endTime").val();
                d.alterStatus = 'PENDING_RELATE';//已变更待关联
                d.sellerName = $("#sellerName").val();
                //d.isPayedByAcceptDraft   =$("#select_pay_style").val();
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'code'},
            {data: 'createTime'},
            {data: 'accountName'},
            {data: 'ownerName'}
            , {data: 'sellerName'}
            , {data: 'totalQuantity', "sClass": "text-right"}
            , {data: 'totalWeight', "sClass": "text-right"}
            , {data: 'totalAmount', "sClass": "text-right"}
            , {data: 'alterStatus'}
            , {defaultContent: ''}
        ]
        , columnDefs: [
            {
                "targets": 0, //第几列 从0开始
                "data": "code",
                "render": renderCode
            }
            , {
                "targets": 6, //第几列 从0开始
                "data": "totalWeight",
                "render": renderWeight
            }
            , {
                "targets": 7, //第几列 从0开始
                "data": "totalAmount",
                "render": renderAmount
            }
            ,
            {
                "targets": 8, //第几列 从0开始
                "data": "alterStatus",
                "render": function (data, type, full, meta) {
                    var status = renderAlterStatus(full.alterStatus);
                    return status;
                }
            }
            , {
                "targets": 9, //第几列 从0开始
                "data": "alterStatus",
                "render": function (data, type, full, meta) {
                    return showOperationalByStatus(full.orderId);
                }
            }
        ]
        ,"scrollY": _tableHeight
    });
}




function renderAlterStatus(f) {
    var statusShow;
   if (f == 'PENDING_RELATE') {
        return "待关联变更交易单";
    } else {
        return "";
    }
}


/**
 * 根据状态显示操作
 * @param orderId
 * @returns {*}
 */
function showOperationalByStatus(orderId){
    var relateURL = Context.PATH + "/order/changecontract/changeassociatedetail.html?id=" + orderId; //关联订单
    var printURL =  Context.PATH + "/order/changecontract/print/contract.html?orderChangeId="+ orderId; //打印变更合同
    var s = "";
    if( $("#permission_relate").val() == 'true')
    {
        s += createAele(relateURL, "处理");
    }
    if( $("#permission_print").val() == 'true'){
        s +="<br/>";
        s += createAele(printURL,"打印合同");
    }
    return s;
}




