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
    var url = Context.PATH + "/order/changecontract/loadOrderData.html";
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
                d.alterStatus =  $("#alterStatus").val();
                d.sellerName = $("#sellerName").val();
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
                    return showOperationalByStatus(full.id,full.orderId,full.alterStatus,full.payStatus);
                }
            }
        ]
        ,"scrollY": _tableHeight
    });
}

function renderAlterStatus(f) {
    var statusShow;
    if (f == 'PENDING_CHANGED') {
        return "待变更";
    } else if (f == 'PENDING_APPROVAL') {
        return "已变更待审核";
    } else if (f == 'DISAPPROVE') {
        return "变更审核不通过";
    } else if (f == 'CHANGED_SUCCESS1') {
        return "变更成功";
    } else if (f == 'CHANGED_SUCCESS2') {
        return "变更成功";
    } else if (f == 'CHANGED_SUCCESS3') {
        return "变更成功";
    } else if (f == 'PENDING_RELATE') {
        return "待关联变更交易单";
    } else if (f == 'PENDING_APPLY') {
        return "待申请付款变更交易单";
    } else if (f == 'CLOSED') {
        return "已关闭本次变更";
    } else if (f == 'PAYED_DISAPPROVE') {
        return "付款申请审核不通过";
    } else if (f == 'PENDING_APPR_PAY') {
        return "待审核付款";
    } else if (f == 'PENDING_PRINT_PAY') {
        return "待打印付款申请单";
    } else if (f == 'PENDING_CONFIRM_PAY') {
        return "待确认付款";
    } else {
        return "";
    }
}



    /**
     * 根据状态显示操作
     * @param orderId ,f,payStatus
     * @returns {*}
     */
    function showOperationalByStatus(id,orderId,f,payStatus){

        var s = "";
        var alterURL = Context.PATH + "/order/changecontract/"+ id +"/change.html"; //变更申请
        var viewURL = Context.PATH + "/order/changecontract/"+ orderId + "/view.html";//变更未完成的订单详情
        var changeURL = Context.PATH + "/order/changecontract/"+ id + "/changeddetail.html";//已变更后的订单详情
        //var viewURL = Context.PATH + "/order/changecontract/"+ orderId + "/changeddetail.html";

        if (f == 'PENDING_CHANGED' || f == 'DISAPPROVE' || f == 'CHANGED_SUCCESS1' || f == 'CHANGED_SUCCESS2' || f == 'CHANGED_SUCCESS3' || f == 'CLOSED') {
            if ($("#permission_change").val() == 'true'){
                s += '<a class="changeorder" orderId="' + orderId + '" type = "' + payStatus + '"href="javascript:;">变更</a>';
             }
        }
        if (f == 'PENDING_APPROVAL') {

            s += createAele(viewURL,"查看详情");
        } else if (f == 'DISAPPROVE') {
            s +="<br/>";
            s += createAele(changeURL,"查看详情");
        } else if (f == 'CHANGED_SUCCESS1') {
            s +="<br/>";
            s += createAele(changeURL,"查看详情");
        } else if (f == 'CHANGED_SUCCESS2') {
            s +="<br/>";
            s += createAele(changeURL,"查看详情");
        } else if (f == 'CHANGED_SUCCESS3') {
            s +="<br/>";
            s += createAele(changeURL,"查看详情");
        } else if (f == 'PENDING_RELATE') {
            s += createAele(viewURL,"查看详情");
        } else if (f == 'PENDING_APPLY') {
            s += createAele(viewURL,"查看详情");
        } else if (f == 'CLOSED') {
            s +="<br/>";
            s += createAele(changeURL,"查看详情");
        } else if (f == 'PAYED_DISAPPROVE') {
            s += createAele(viewURL,"查看详情");
        } else if (f == 'PENDING_APPR_PAY') {
            s += createAele(viewURL,"查看详情");
        } else if (f == 'PENDING_PRINT_PAY') {
            s += createAele(viewURL,"查看详情");
        } else if (f == 'PENDING_CONFIRM_PAY') {
            s += createAele(viewURL,"查看详情");
        }
        return s;
    }

//该订单尚未完成付款，请完成付款后再变更

$(document).on("click", ".changeorder", function () {
    var type = $(this).attr("type");
    var orderId = $(this).attr("orderId");
    if(type == "REQUESTED" || type == "APPROVED" || type=="APPLYPRINTED"){
        cbms.alert("该订单尚未完成付款，请完成付款后再变更！");
    }else{
        var url =  Context.PATH + '/order/changecontract/queryorderpaystatus.html';
        $.post(url, {orderId: orderId}, function (re) {
            if (re.success) {
                if(re.data == "REQUESTED" || re.data == "APPROVED" || re.data=="APPLYPRINTED"){
                    cbms.alert("该订单尚未完成付款，请完成付款后再变更！");
                }else{
                    window.location.href= Context.PATH + "/order/changecontract/"+ orderId +"/change.html"; //变更申请; //变更申请
                }
            }else{
                cbms.alert("查询订单信息报错！");
            }

        });

    }
});



