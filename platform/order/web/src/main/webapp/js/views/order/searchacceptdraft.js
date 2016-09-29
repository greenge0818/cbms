/**
 * 根据客户id查下银票票号
 */
var acceptDraftData = null;    // 银票数据

$().ready(function () {
    var accountId = $("#buyerId").val();
    getAcceptDraft(accountId);
});

// 获取买家银票
function getAcceptDraft(accountId) {
    $.ajax({
        type: 'post',
        url: Context.PATH + "/order/getacceptdraft.html",
        data: {
            accountId: accountId
        },
        error: function (s) {
        },
        success: function (result) {
            if (result) {
                var adOptions = "<option value='0' selected='selected'>无</option>";
                if (result.data != null && $(result.data).length > 0) {
                    if ($(result.data).length > 0) {
                        acceptDraftData = $(result.data);
                        for (var i = 0; i < acceptDraftData.length; i++) {
                            adOptions += "<option value='" + acceptDraftData[i].id + "'>" + acceptDraftData[i].code + "</option>";
                        }
                    }
                }
                $("select[name='acceptDraft']").html(adOptions);
            } else {
                cbms.alert("获取数据失败");
            }
        }
    });
}
