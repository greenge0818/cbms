/**
 * create by dengxiyan
 * 待审核付款
 */
var tradeTable;
jQuery(function ($) {
    initTable();
    //搜索事件
    $("#queryBtn").click(function () {
        tradeTable.ajax.reload();
    });
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
                d.status = "4,6"; //订单已关联合同/待二次结算
                d.payStatus='REQUESTED';//付款：已申请/待审核
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
                "render": renderCode
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
                    return renderStatus("7");
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


//当交易员已申请过，但是办事处经理暂未审核，交易员这时候看到的按钮变为变为“已申请，请等待审核”，不可点击
function renderOperation(data, type, full, meta) {
    var detailUrl = getDetailURL(full.id);
    var printUrl;
    if(full.alterStatus == 'PENDING_APPR_PAY'){
        //变更流程展示变更合同
        printUrl = getPrintChangeContractURL(full.changeOrderId);
    }else{
        printUrl = getPrintContractURL(full.id);
    }


    var operation="",flag;
    if ($("#print").val() === 'true') {
        operation = createAele(printUrl,"打印合同");
        flag = true;
    }
    if ($("#approval").val() === 'true') {
        operation += (flag ? "<br/>" : "") + createAele(detailUrl,"审核");
    }
    if ($("#show").val() === 'true') {
        operation += (flag ? "<br/>" : "") + "<span class='red'>已申请，请等待审核</span> ";
    }
    return operation;
}

