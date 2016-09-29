/**
 * 银票充值页面（待审核状态）
 * Created by lcw on 2015/11/11.
 */

$().ready(function () {
    /**
     * 撤回充值申请
     */
    $("#withdrawaudit").click(function () {
        var id = $("#acceptDraftId").val();
        $(this).prop("disabled", true);
        cbms.confirm("确定撤回充值申请吗？", null, function () {
            cbms.loading();
            $.ajax({
                type: 'post',
                url: Context.PATH + "/acceptdraft/withdrawaudit.html",
                data: {
                    id: id
                },
                error: function (s) {
                    $("#withdrawaudit").prop("disabled", false);
                    cbms.closeLoading();
                }
                , success: function (result) {
                    cbms.closeLoading();
                    if (result) {
                        if (result.success) {
                            cbms.alert("撤回充值申请成功！");
                            setTimeout('location.href="' + Context.PATH + '/acceptdraft/list.html"', 1000);
                        }
                        else {
                            $("#withdrawaudit").prop("disabled", false);
                            cbms.alert(result.data);
                        }
                    } else {
                        $("#withdrawaudit").prop("disabled", false);
                        cbms.alert("撤回充值申请失败！");
                    }
                }
            });
        });
    });
});