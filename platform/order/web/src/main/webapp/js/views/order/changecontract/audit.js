/**
 * 审核变更
 * Created by lichaowei on 2016/8/18.
 */

var _weightFixedLength = 6;
var _amountFixedLength = 2;
$().ready(function () {

    // 审核通过
    $("#pass").click(function () {
        cbms.confirm("确定通过审核吗？", null, function () {
            submitAudit(true, $(this));
        });
    });

    // 审核不通过
    $("#unpass").click(function () {
        cbms.getDialog("查看关联","#unpassDialog");
    });

    // 确定审核不通过
    $(document).on("click", "#btnConfirm", function(){
        var note = $.trim($("#txtNote").val());
        if(note==""){
            cbms.alert("请填写不通过原因！");
            return;
        }
        submitAudit(false, $(this),note);
    });

    // 取消审核不通过
    $(document).on("click", "#btnCancel", function(){
        cbms.closeDialog();
    });

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
            var table = $("#recordTable").clone().removeAttr("id");
            $(cloneRow).find("td").append(table);
            // 追加在当前行后面
            $(currentRow).after($(cloneRow));
            changeItemsRecordTable = false;
            getChangeRecord($(currentRow).attr("orderitemid"),$(currentRow).attr("itemchangeid"), $(cloneRow).find("table"));
        }
    });
});

var changeItemsRecordTable = false;
/**
 * 获取变更记录
 */
function getChangeRecord(orderItemId, itemChangeId, table) {
    var itemIndex = 0;
    changeItemsRecordTable = $(table).DataTable({
        ajax: {
            url: Context.PATH + "/order/changecontract/getchangerecord.html",
            type: "POST",
            data: function (d) {
                d.orderItemId = orderItemId;
                d.itemChangeId = itemChangeId;
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
            {data: 'statusName'},
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
            if (aData.type == "ADD") {
                itemIndex++;
                $("td", nRow).eq(0).html("<span style='color: #ff0000;'>" + itemIndex + "（新增资源明细）</span>");
                itemIndex++;
            }
            else {
                if (itemIndex == 0) {
                    $("td", nRow).eq(0).text("原资源明细");
                } else {
                    $("td", nRow).eq(0).text(itemIndex);
                }
                itemIndex++;
            }
            var isDel = false;
            if (aData.statusName.toString().indexOf("已删除") == 0) {
                $("td", nRow).eq(2).html("<span style='color: #ff0000'>" + aData.statusName + "</span>");
                isDel = true;
            }
            else if (aData.statusName == "原订单") {
                $("td", nRow).eq(2).html("-");
                $("td", nRow).eq(3).html("-");
            }
            if(isDel){
                for (var i = 4; i <= 17; i++) {
                    $("td", nRow).eq(i).text('-');
                }
            }
            else {
                if (aData.weight != null) {
                    $("td", nRow).eq(11).text(formatMoney(aData.weight, 6));
                }
                if (aData.dealPrice != null) {
                    $("td", nRow).eq(13).text(formatMoney(aData.dealPrice, 2));
                }
                // 是否银票支付
                if (aData.isPayedByAcceptDraft && aData.acceptDraftId > 0) {
                    $("td", nRow).eq(13).html(formatMoney(aData.dealPrice, 2)
                    + "<br/><span style='background: #ff0000'>银票支付</span>");
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
        ordering: false,
        bInfo: false,
        scrollX: true
    });
}

/**
 * 提交审核状态
 * @param auditStatus  true：审核通过， false：审核不通过
 * @param btn          提交按钮
 * @param note         不通过理由
 */
function submitAudit(auditStatus, btn, note) {
    $(btn).prop("disabled", true);
    cbms.loading();
    $.ajax({
        type: 'post',
        url: Context.PATH + "/order/changecontract/submitaudit.html",
        data: {
            orderChangeId: $("#orderChangeId").val(),
            auditStatus: auditStatus,
            note: note
        },
        error: function (s) {
            $(btn).prop("disabled", false);
            cbms.closeLoading();
        }
        , success: function (result) {
            if(!auditStatus){
                cbms.closeDialog();
            }
            cbms.closeLoading();
            if (result) {
                if (result.success) {
                    cbms.gritter("审核成功！", true, function () {
                        window.location.href = Context.PATH + "/order/changecontract/auditlist.html";
                    })
                }
                else {
                    $(btn).prop("disabled", false);
                    cbms.alert(result.data);
                }
            } else {
                $(btn).prop("disabled", false);
                cbms.alert("审核失败");
            }
        }
    });
}