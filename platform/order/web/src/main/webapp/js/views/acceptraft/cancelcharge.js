/**
 * 银票审核取消充值页面（待审核状态）
 * Created by Rabbit on 2015-11-13 11:56:47.
 */
var isPass;
var message;
$().ready(function () {
    $(document).on("click", "#rollback", function () {
        isPass = true;
        message = '确定通过取消充值申请吗？';
        aduit();
    });
    $(document).on("click", "#refuse", function () {
        isPass = false;
        message = '确定不通过取消充值申请吗？';
        aduit();
    });
});

function aduit() {
    var id = $("#acceptDraftId").val();
    $(this).prop("disabled", true);
    cbms.confirm(message, null, function () {
        cbms.loading();
        $.ajax({
            type: 'post',
            url: Context.PATH + "/acceptdraft/audit.html",
            data: {
                id: id,
                isPass: isPass
            },
            error: function (s) {
                cbms.closeLoading();
            }
            , success: function (result) {
                cbms.closeLoading();
                if (result.success) {
                    cbms.alert("审核成功！", function () {
                        location.href = Context.PATH + '/acceptdraft/list.html';
                    });
                }
                else {
                    cbms.alert(result.data);
                }
            }
        });
    });
}