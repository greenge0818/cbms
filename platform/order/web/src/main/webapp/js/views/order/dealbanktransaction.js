/**
 * Created by Rabbit Mao on 2015/7/20.
 */

function setChecked(o) {
    if (!setlistensSave("#chargeForm")) {
        return;
    }
    var name = $("#name").val();
    var tel = $("#tel").val();
    var id = $("#id").val();
    var departmentId = $("#departmentList").size() > 0 ? $("#departmentList").val() : 0;

    $.ajax({
        type: "POST",
        url: Context.PATH + '/order/banktransaction/charge.html',
        data: {
            name: name,
            tel: tel,
            id: id,
            departmentId: departmentId
        },
        dataType: "json",
        success: function (response, textStatus, xhr) {
            if (response.success) {
                window.location.href = Context.PATH + '/order/banktransaction/list.html';
            } else {
                cbms.alert(response.data);
            }
        },
        error: function (xhr, textStatus, errorThrown) {
        }
    });
}


jQuery(function ($) {

    $("#chargeForm").verifyForm();

    $("#refundForm").on("click", "#refund", function () {
        var ele = '<div class="dialog-m" id="refundDialog">' +
            '<p><label>选择退款原因：</label><select id="reasonSelect"><option value="0">客户要求退款，用原公司抬头打款</option><option value="1">其他</option></select></p>' +
            '<p id="reasonDiv"><label>输入其他原因：</label><textarea id="reason" style="overflow-x:hidden">客户要求退款，用原公司抬头打款</textarea></p>' +
            '<div class="btn-bar text-center"><button id="commit" type="button" class="btn btn-primary btn-sm">确定</button>&nbsp;<button type="button" class="btn btn-default btn-sm" id="cancel">取消</button></div></div>';
        var dia = cbms.getDialog("线下退款申请", ele);

        $("#reasonDiv").hide();

        $("#reasonSelect").change(function () {
            var select = $('#reasonSelect').children('option:selected').val();
            if (select == 0) {
                $("#reasonDiv").hide();
                $("#reason").val("客户要求退款，用原公司抬头打款");
            } else if (select == 1) {
                $("#reasonDiv").show();
                $("#reason").val("");
            }
        });

        $("#refundDialog").on("click", "#cancel", function () {
            cbms.closeDialog();
        });

        $("#refundDialog").on("click", "#commit", function () {
            var id = $("#id").val();
            var serialNumber = $("#serialNumber").val();
            var reason = $("#reason").val();
            $.ajax({
                type: "POST",
                url: Context.PATH + '/order/banktransaction/refundTransaction.html',
                data: {
                    id: id,
                    reason: reason
                },
                dataType: "json",
                success: function (response, textStatus, xhr) {
                    if (response.success) {
                        window.location.href = Context.PATH + '/order/banktransaction/refund.html?serialNumber=' + serialNumber;
                    } else {
                        cbms.alert(response.data);
                    }
                },
                error: function (xhr, textStatus, errorThrown) {
                }
            });
        });
    });

    $("#chargeForm").on("click", "#charge", function () {
        cbms.confirm("确定要注册新的企业账号信息并充值吗？", null, setChecked);
    });
});
