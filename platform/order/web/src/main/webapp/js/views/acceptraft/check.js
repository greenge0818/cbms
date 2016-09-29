/**
 * 银票充值审核
 * Created by lcw on 2015/11/12.
 */

$().ready(function () {
    $("#form1").verifyForm();
    /**
     * 提交确认
     */
    $("#submit").click(function () {
        if (!setlistensSave("#form1"))return;
        cbms.loading();
        $('#form1').ajaxSubmit({
            error: function (s) {
                $("#submit").prop("disabled", false);
                cbms.closeLoading();
            }
            , success: function (result) {
                cbms.closeLoading();
                if (result) {
                    if (result.success) {
                        cbms.alert("双敲匹配成功，已完成该银票充值！");
                        setTimeout('location.href="' + Context.PATH + '/acceptdraft/list.html"', 1000);
                    }
                    else {
                        $("#submit").prop("disabled", false);
                        cbms.alert(result.data);
                    }
                } else {
                    $("#submit").prop("disabled", false);
                    cbms.alert("确认充值失败！");
                }
            }
        });

    });
});