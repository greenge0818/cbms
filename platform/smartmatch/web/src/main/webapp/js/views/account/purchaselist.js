/**
 * 采购记录
 * Created by dengxiyan on 2015/8/1.
 */
var tradeTable;
jQuery(function ($) {
    initTable();
    initClickEvent();
});

function initTable() {
    tradeTable = jQuery("#dynamic-table").DataTable({
        "ajax": {
            "url": Context.PATH + "/account/loadPurchaseData.html"
            , "type": "POST"
            , data: function (d) {
                d.nsortName = $("#nsortName").val();
                d.sellerName = $("#sellerName").val();
                d.createdStartTime = $("#startTime").val();
                d.createdEndTime = $("#endTime").val();
                d.selectedStatus = $("#status").val();
                d.accountId = $("#accountId").val();
            }
            //操作服务器返回的数据
            , "dataSrc": function (result) {
                //设置交易笔数
                $("#total").text(result.recordsFiltered);
                return result.data;//返回data
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
        ]

        , columnDefs: [
            {
                "targets": 0, //第几列 从0开始
                "data": "code",
                "render": function (data, type, full, meta) {
                    //查看订单详情
                    var url = Context.PATH + "/order/query/detail.html?orderid=" + full.id + "&op=pickup";
                    return createAele(url, data);
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
                "render": function (data, type, full, meta) {

                    //订单状态显示
                    if (full.status == '1' || full.status == '2' || full.status == '6' || full.status == '10') {
                        return renderStatus(full.status);//1:待审核 2:待关联合同 6:待二次结算 10:交易完成
                    }
                    if (full.status == '4') {//已关联合同
                        if (full.fillupStatus == '1') {
                            return renderStatus("5");//放货单已全打印
                        }
                        return renderStatus(full.status);//待出库
                    }
                    if (full.status == '7') {
                        return "待开票申请";
                    }
                    if (full.status == '8') {
                        return "待开票";
                    }
                    if (full.status == '3' || full.status == '5' || full.status == '-5' || full.status == '-6') {
                        return renderStatus("9");//已申请关闭
                    }
                    if (full.status == '-1' || full.status == '-2' || full.status == '-3' || full.status == '-4' || full.status == '-7'
                        || full.status == '-8') {
                        return renderStatus("0");//交易关闭
                    }
                    return "";
                }
            }
        ]
        //生成footer
        , "footerCallback": function (row, data, start, end, display) {
            var api = this.api();

            //列索引:总重量、实提总重量、总金额、实提总金额
            var weightColIndex = 6, pickupWeightColIndex = 7, amountColIndex = 8, pickupAmountColIndex = 9
                , totalWeight = cbms.convertFloat(totalColumn(api, weightColIndex))
                , totalPickupWeight = cbms.convertFloat(totalColumn(api, pickupWeightColIndex))
                , totalAmount = formatMoney(totalColumn(api, amountColIndex), 2)
                , totalPickupAmount = formatMoney(totalColumn(api, pickupAmountColIndex), 2);

            //更新所有页的总数
            $("#totalWeight").text(totalWeight + "吨");
            $("#totalPickupWeight").text(totalPickupWeight + "吨");
            $("#totalAmount").text(totalAmount + "元");
            $("#totalPickupAmount").text(totalPickupAmount + "元");
        }
    });
}

function initClickEvent() {
    //搜索事件
    $("#queryBtn").click(function () {
        tradeTable.ajax.reload();
    });
}

