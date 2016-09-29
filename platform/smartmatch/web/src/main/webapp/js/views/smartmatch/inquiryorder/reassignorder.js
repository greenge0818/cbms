$(document).ready(function () {

    $(document).on("change", "#assign_reason_select", function () {
        var select = $(this).val();
        if (select != '其他') {
            $("#assign_reason_panel").hide();
        } else {
            $("#assign_reason_panel").show();
        }
    });

    $(document).on("click", "#btnreassign", function () {
        var reason = "";
        var checkReason = $("#assign_reason_select").val();
        if (checkReason == '其他') {
            reason = $("#reassign_reason_other_text").val();
            if (reason == '') {
                cbms.alert("请填写改派理由！");
                return false;
            }
        }
        else {
            reason = checkReason;
        }
        var order_id = $("#reassign_purchase_orderid").val();
        var url = Context.PATH + '/smartmatch/purchaseorder/orderReassign.html';
        $.post(url, {id: order_id, remark: reason}, function (re) {
            if (re.success) {
                cbms.closeDialog();
                cbms.gritter('改派成功！', true, function () {
                    if (typeof(PurchasePage) != 'undefined' && PurchasePage.dt) {
                        PurchasePage.dt.ajax.reload();
                    } else {
                        location.href = Context.PATH + '/smartmatch/purchaseorder/list.html';
                    }
                });
            }
            else {
                cbms.alert(re.data);
            }
        });
    });

    $(document).on("click", "#reassign_cancel", function () {
        cbms.closeDialog();
    });
});