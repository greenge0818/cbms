/**
 * 变更记录交易单详情
 * Created by chengui on 2016/8/30.
 */

var _weightFixedLength = 6;
var _amountFixedLength = 2;
$().ready(function () {
    getChangeRecord();

    $(".viewDetail").click(function () {
        var currentRow = $(this).closest("tr");
        var text = $(this).text();
        if (text == "^") {
            $(this).text("v");
            $(currentRow).next().remove();
        }
        else {
            var cloneRow = $(currentRow).clone();       // 复制当前行
            $(cloneRow).html("<td colspan='" + $(currentRow).find("td").size() + "'></td>");
            $(this).text("^");
            var table = $("#recordTable").clone();
            $(cloneRow).find("td").append(table);
            // 追加在当前行后面
            $(currentRow).after($(cloneRow));
            changeItemsRecordTable = false;
            getChangeRecord($(currentRow).attr("orderitemid"), $(cloneRow).find("table"));
        }
    });
});

var changeItemsRecordTable = false;
/**
 * 获取变更记录
 */
function getChangeRecord(orderItemId,table) {
    var itemIndex = 0;
    changeItemsRecordTable = $(table).DataTable({
        ajax: {
            url: Context.PATH + "/order/changecontract/getchangerecorddetail.html",
            type: "POST",
            data: function (d) {
                d.orderItemId = orderItemId;
                delete d.columns;
                delete d.order;
                delete d.search;
            }
        },
        columns: [
            {
                data: 'orderItemId',
                render: function (data, type, full, meta) {
                    return '<label class="pos-rel"><input value="' + data + '" type="checkbox" class="ace"><span class="lbl"></span></label>';
                }
            },
            {
                data: 'created',
                render: function (data, type, full, meta) {
                    return new Date(data).Format("yyyy-MM-dd hh:mm:ss");
                }
            },
            {
                data: 'status',
                render: function (data, type, full, meta) {
                    return "变更成功";
                }
            },
            {data: 'createdBy'},
            {data: 'nsortName', defaultContent: '-'},
            {data: 'material', defaultContent: '-'},
            {data: 'spec', defaultContent: '-'},
            {data: 'factory', defaultContent: '-'},
            {data: 'city', defaultContent: '-'},
            {data: 'warehouse', defaultContent: '-'},
            {data: 'quantity', defaultContent: '-'},
            {data: 'weight', defaultContent: '-'},
            {data: 'weightConcept', defaultContent: '-'},
            {data: 'dealPrice', defaultContent: '-'},
            {data: 'costPrice', defaultContent: '-'},
            {data: 'saleAmount', defaultContent: '-'},
            {data: 'purchaseAmount', defaultContent: '-'},
            {data: 'strappingNum', defaultContent: '-'}
        ],
        fnRowCallback: function (nRow, aData, iDataIndex) {
            if (itemIndex == 0) {
                if(aData.type == 'ADD'){
                    itemIndex++;
                    $("td", nRow).eq(0).html("<span style='color: #ff0000;'>" + itemIndex + "（新增资源明细）</span>");
                }else{
                    $("td", nRow).eq(0).text("原订单");
                    $("td", nRow).eq(2).text("-");
                    $("td", nRow).eq(3).text("-");
                }
            } else {
                $("td", nRow).eq(0).text(itemIndex);
            }
            itemIndex++;

            if(aData.type == 'DEL'){
                $("td", nRow).eq(2).html("<span style='color: #ff0000'>已删除</span>");
                for (var i = 4; i <= 16; i++) {
                    $("td", nRow).eq(i).text('-');
                }
            }else{
                if (aData.weight != null) {
                    $("td", nRow).eq(11).text(formatMoney(aData.weight, 6));
                }
                if (aData.dealPrice != null) {
                    $("td", nRow).eq(13).text(formatMoney(aData.dealPrice, 2));
                }
                // 是否银票支付
                if (aData.isPayedByAcceptDraft && aData.acceptDraftId > 0) {
                    $("td", nRow).eq(13).html(formatMoney(aData.dealPrice, 2)
                        + "<span style='background: #ff0000'>银票支付</span>");
                }
                if (aData.costPrice != null) {
                    $("td", nRow).eq(14).text(formatMoney(aData.costPrice, 2));
                }
                if (aData.saleAmount != null) {
                    $("td", nRow).eq(15).text(formatMoney(aData.saleAmount, 2));
                }
                if (aData.purchaseAmount != null) {
                    $("td", nRow).eq(16).text(formatMoney(aData.purchaseAmount, 2));
                }
            }

        },
        drawCallback: function (settings) {

        },
        bAutoWidth: false,
        searching: false,
        bPaginate: false,
        paging: false,
        ordering: false
        , scrollX: true
    });
}

