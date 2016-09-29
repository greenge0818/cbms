/**
 * 银票已充值界面
 */
jQuery(function ($) {



    $(document).on("click", "#cancleCharge", function () {
        var ele = '<form id="reasonForm"><div class="dialog-m" id="dialog">' +
            '<p><label>申请取消充值的原因：</label><select id="reasonSelect"><option value="0">客户名称输入错误</option><option value="1">其他原因</option></select></p>' +
            '<p id="reasonDiv"><label>输入其他原因：</label><textarea id="reason" style="overflow-x:hidden" must="1">客户名称输入错误</textarea></p>' +
            '<div class="btn-bar text-center"><button id="commit" type="button" class="btn btn-primary btn-sm">确定</button>&nbsp;<button type="button" class="btn btn-default btn-sm" id="cancel">取消</button></div></div></form>';
        cbms.getDialog("提示信息", ele);

        $("#reasonForm").verifyForm();

        $("#reasonSelect").change(function () {
            var select = $('#reasonSelect').children('option:selected').val();
            if (select == 0) {
                $("#reason").val("客户名称输入错误");
            } else if (select == 1) {
                $("#reason").val("");
            }
        });

        $("#dialog").on("click", "#cancel", function () {
            cbms.closeDialog();
        });

        $("#dialog").on("click", "#commit", function () {
            if (!setlistensSave("#reasonForm")) {
                return;
            }
            var id = $("#acceptDraftId").val();
            var reason = $("#reason").val();
            $("#commit").attr("disabled", true);
            $.ajax({
                type: 'post',
                url: Context.PATH + "/acceptdraft/" + id + "/applycanclecharged.html",
                data: {
                    reason: reason
                },
                error: function (s) {
                },
                success: function (result) {
                    $("#commit").removeAttr("disabled");
                    if (result) {
                        if (result.success) {
                            cbms.alert("已成功提交取消充值申请，待核算会计审核", function(){
                                location.href= Context.PATH + '/acceptdraft/list.html';
                            });
                        }
                        else {
                            cbms.alert(result.data);
                        }
                    } else {
                        cbms.alert("申请取消充值失败！");
                    }
                }
            });
        });
    });
});

