/**
 * create by dengxiyan
 * 待提货
 */
var tradeTable;
jQuery(function ($) {
    initTable();
    initClickEvent();
});

function initTable() {
    var url = Context.PATH + "/order/query/loadOrderData.html";
    tradeTable = jQuery("#dynamic-table").DataTable({
        "sScrollY": "400px",
        "bScrollCollapse": true,
        "iDisplayLength": 50,
        "ajax": {
            "url": url
            , "type": "POST"
            , data: function (d) {
                d.code = $("#code").val();
                d.accountName = $("#accountName").val();
                d.ownerName = $.trim($("#ownerId").val());//交易员 唯一匹配
                d.createdStartTime = $("#startTime").val();
                d.createdEndTime = $("#endTime").val();
                d.status = "4";//订单已关联合同
                d.sellerName = $("#sellerName").val();
                
                //支付类型
                var payType = $("#select_pay_style").val();
                if(payType) d.isPayedByAcceptDraft = payType;
            }
        }
        //数据源为数组时，定义数据列的对应
        , columns: [
            {data: 'code'},
            {data: 'createdStr'},
            {data: 'accountName'},
            {data: 'ownerName'}
            , {data: 'sellerName'}
            , {data: 'totalQuantity', "sClass": "text-right"}
            , {data: 'totalWeight', "sClass": "text-right"}
            , {data: 'pickupTotalWeight', "sClass": "text-right"}
            , {data: 'totalAmount', "sClass": "text-right"}
            , {data: 'pickupTotalAmount', "sClass": "text-right"}
            , {data: 'status'}
            , {defaultContent: ''}
        ]
        , columnDefs: [
            {
                "targets": 0, //第几列 从0开始
                "data": "code",
                "render": function(data, type, full, meta){
                    //查看订单详情
                    var url = Context.PATH + "/order/query/detail.html?orderid=" + full.id + "&op=pickup&menu=" + $("#pageTab").val();
                    return createAele(url,data);
                }
            }
            , {
                "targets": 4, //第几列 从0开始
                "data": "sellerName",
                "render": renderSeller
            }
            , {
                "targets": 6, //第几列 从0开始
                "data": "totalWeight",
                "render": renderWeight
            }, {
                "targets": 7, //第几列 从0开始
                "data": "pickupTotalWeight",
                "render": renderWeight
            }
            , {
                "targets": 8, //第几列 从0开始
                "data": "totalAmount",
                "render": renderAmount
            }, {
                "targets": 9, //第几列 从0开始
                "data": "pickupTotalAmount",
                "render": renderPickupAmount
            }
            ,
            {
                "targets": 10, //第几列 从0开始
                "data": "status",
                "render":  function(data, type, full, meta){
                    return renderStatus("4");
                }
            }
            , {
                "targets": 11, //第几列 从0开始
                "data": "status",
                "render": renderOperation
            }
        ]
        //生成footer
        , "footerCallback": function (row, data, start, end, display) {
            var api = this.api(), total;

            //当前页汇总
            total = pageTotalOrder(api);

            // Update footer
            var ele = '<div class=" recordbar col-xs-12">' +
                '<span class="bolder">当前页汇总</span>' +
                '<span>总数量：<span class="bolder">' + total.pageTotalQty + '</span></span>' +
                '<span>总重量：<span class="bolder">' + cbms.convertFloat(total.pageTotalWeight) + '</span>吨</span>' +
                '<span>实提总重量：<span class="bolder">' + cbms.convertFloat(total.pageTotalPickupWeight) + '</span>吨</span>' +
                '<span>总金额：<span class="bolder">' + formatMoney(total.pageTotalAmount, 2) + '</span>元</span>' +
                '<span>实提总金额：<span class="bolder">' + formatMoney(total.pageTotalPickupAmount, 2) + '</span>元</span>' +
                '</div>';
            var footerColumnIndex = 11;//操作列索引
            $(api.column(footerColumnIndex).footer()).html(
                ele
            );

        }
    });
}


/**
 * 统计当前页需要显示总和的列
 * @param api
 * @returns {{pageTotalQty: *, pageTotalWeight: *, pageTotalPickupWeight: *, pageTotalAmount: *, pageTotalPickupAmount: *}}
 */
function pageTotalOrder(api) {
    //列索引:数量、总重量、实提总重量、总金额、实提总金额
    var qtyColIndex = 5, weightColIndex = 6, pickupWeightColIndex = 7, amountColIndex = 8, pickupAmountColIndex = 9;
    var total = {
        pageTotalQty: pageTotalColumn(api, qtyColIndex),
        pageTotalWeight: pageTotalColumn(api, weightColIndex),
        pageTotalPickupWeight: pageTotalColumn(api, pickupWeightColIndex),
        pageTotalAmount: pageTotalColumn(api, amountColIndex),
        pageTotalPickupAmount: pageTotalColumn(api, pickupAmountColIndex)
    }
    return total;
}

function initClickEvent() {
    //搜索事件
    $("#queryBtn").click(search);
}


function search() {
    tradeTable.ajax.reload();
}


function renderOperation(data, type, full, meta) {
    var pickupStatus = full.pickupStatus;
    //var viewURL = Context.PATH + "/order/pickup/order/"+ full.id +".html";
    //var createURL = Context.PATH + "/order/pickup/order/"+ full.id + "/create.html";
    var operation = "",flag;

    //1 未录入(录入提货单) 2 部分录入（查看提货单、录入提货单）3 全录入（查看提货单）
    if(pickupStatus == 1){
        if($("#create").val() === 'true'){
            operation = createChangeAele("printcontract",full.id,full.alterStatus,"录入提货单");
                //createAele(createURL,"录入提货单");
        }
    }
    if(pickupStatus == 2){
        if($("#view").val() === 'true'){
            operation = createChangeAele("changeorder",full.id,full.alterStatus,"查看提货单")
                //createAele(viewURL,"查看提货单");
            flag = true;
        }
        if($("#create").val() === 'true'){
            operation += (flag ? "<br/>" : "") +  createChangeAele("printcontract",full.id,full.alterStatus,"录入提货单");
        }
    }
    if(pickupStatus == 3){
        if($("#view").val() === 'true'){
            operation = createChangeAele("changeorder",full.id,full.alterStatus,"查看提货单");
                //createAele(viewURL,"查看提货单");
        }
    }
    return operation;
}

//该交易单正在进行合同变更，请变更完成后操作

$(document).on("click", ".printcontract", function () {
    var type = $(this).attr("type");
    var orderId = $(this).attr("orderId");
    var createURL = Context.PATH + "/order/pickup/order/"+ orderId + "/create.html";
    if(type == "PENDING_APPROVAL" || type == "PENDING_RELATE" || type=="PENDING_APPLY" || type=="PAYED_DISAPPROVE" ||type=="PENDING_APPR_PAY" || type =="PENDING_PRINT_PAY" || type =="PENDING_CONFIRM_PAY"){
        cbms.alert("该交易单正在进行合同变更，请变更完成后操作！");
    }else{
        var url =  Context.PATH + '/order/changecontract/queryorderpaystatus.html';
        $.post(url, {orderId: orderId,origin:"origin"}, function (re) {
            if (re.success) {
                if(re.data == "PENDING_APPROVAL" || re.data == "PENDING_RELATE" || re.data=="PENDING_APPLY" || re.data=="PAYED_DISAPPROVE" || re.data=="PENDING_APPR_PAY" || re.data =="PENDING_PRINT_PAY" || re.data =="PENDING_CONFIRM_PAY"){
                    cbms.alert("该交易单正在进行合同变更，请变更完成后操作！");
                }else{
                    window.location.href= createURL; //打印合同
                }
            }else{
                cbms.alert("查询订单信息报错！");
            }

        });

    }
});
//该交易单正在进行合同变更，请变更完成后操作

$(document).on("click", ".changeorder", function () {
    var type = $(this).attr("type");
    var orderId = $(this).attr("orderId");
    var viewURL = Context.PATH + "/order/pickup/order/"+ orderId +".html";
    if(type == "PENDING_APPROVAL" || type == "PENDING_RELATE" || type=="PENDING_APPLY" || type=="PAYED_DISAPPROVE" ||type=="PENDING_APPR_PAY" || type =="PENDING_PRINT_PAY" || type =="PENDING_CONFIRM_PAY"){
        cbms.alert("该交易单正在进行合同变更，请变更完成后操作！");
    }else{
        var url =  Context.PATH + '/order/changecontract/queryorderpaystatus.html';
        $.post(url, {orderId: orderId,origin:"origin"}, function (re) {
            if (re.success) {
                if(re.data == "PENDING_APPROVAL" || re.data == "PENDING_RELATE" || re.data=="PENDING_APPLY" || re.data=="PAYED_DISAPPROVE" || re.data=="PENDING_APPR_PAY" || re.data =="PENDING_PRINT_PAY" || re.data =="PENDING_CONFIRM_PAY"){
                    cbms.alert("该交易单正在进行合同变更，请变更完成后操作！");
                }else{
                    window.location.href= viewURL;
                }
            }else{
                cbms.alert("查询订单信息报错！");
            }

        });

    }
});

