var _auditPass = "1";
var _auditRefuse = "0";

$().ready(function () {
    //点击图片 放大
    $(".imgbigger").each(function () {
        $(this).closest("a[rel='lightbox']").attr("href", $(this).attr("src"));
    });

    //通过
    $(document).on("click", "#btnPass", function () {
        cbms.confirm("确定通过审核吗？", null, function () {
            var data = {
                accountId: $("#accountId").val(),
                auditType: $("#auditType").val(),
                oldSellerConsignAgreementStatus: $("#status").val(),
                newSellerConsignAgreementStatus: getAuditPassNewStatusByOldStatus($("#status").val()),
                auditPass: _auditPass
            };
            audit(data);
        });
    });

    //不通过
    $(document).on("click", "#btnRefuse", function () {
        var content = "<div class='page-content dialog-m'>";
        content += "<ul>";
        content += "<li>";
        content += "<span>请填写不通过的理由！</span>";
        content += "</li>";
        content += "<li>";
        content += "<textarea style='width:300px' id='declineReason'></textarea>";
        content += "</li>";
        content += "<li class='table-bar text-center'>";
        content += "<button type='button' id='define' class='btn btn-sm btn-primary'>确定</button>&nbsp;&nbsp;";
        content += "<button type='button' id='cancel' class='btn btn-sm btn-default'>取消</button>";
        content += "</li>";
        content += "</ul>";
        content += "</div>";
        cbms.getDialog("不通过审核", content);

        /**
         * 确认不通过审核
         */
        $(document).on("click", "#define", function () {
            var declineReason = $("#declineReason").val();
            if (declineReason != "") {
                var data = {
                    accountId: $("#accountId").val(),
                    auditType: $("#auditType").val(),
                    oldSellerConsignAgreementStatus: $("#status").val(),
                    newSellerConsignAgreementStatus: getAuditRefuseNewStatusByOldStatus($("#status").val()),
                    sellerConsignAgreementDeclineReason: declineReason,
                    auditPass: _auditRefuse
                };
                //审核
                audit(data);
            }
            else {
                cbms.alert("请输入不通过理由！");
            }
        });

        /**
         * 取消不通过审核
         */
        $(document).on("click", "#cancel", function () {
            cbms.closeDialog();
        });


    });

    //返回
    $(document).on("click", "#btnBack", function () {
        window.location.href = Context.PATH + "/flow/consignagreement/" + $("#auditType").val() + "/list.html";
    });

})
;

function audit(data) {
    $.ajax({
        type: "POST",
        url: Context.PATH + "/flow/consignagreement/doaudit.html",
        data: data,
        dataType: "json",
        success: function (response) {
            if (response.success) {
                cbms.gritter("审核成功", true, function () {
                    window.location.href = Context.PATH + "/flow/consignagreement/" + $("#auditType").val() + "/list.html";
                });

            } else {
        		cbms.confirm(response.data,null,function(){
        			window.location.href = Context.PATH + "/flow/consignagreement/" + $("#auditType").val() + "/list.html";
        		});
            }
        },
        error: function (xhr, textStatus, errorThrown) {
        }
    });
}

function getAuditPassNewStatusByOldStatus(oldStatus) {
    var status = "";
    if (oldStatus == "Requested") {//待审核
        status = "FirstApproved"; //一审通过
    }
    if (oldStatus == "FirstApproved") { //一审通过
        status = "SecondApproved"; //审核通过
    }
    if (oldStatus == "Uploaded") { // 已上传待审核
        status = "Approved"; //审核通过
    }
    return status;
}

function getAuditRefuseNewStatusByOldStatus(oldStatus) {
    var status = "";
    if (oldStatus == "Requested") {//待审核
        status = "FirstDeclined"; //一审未通过
    }
    if (oldStatus == "FirstApproved") { //一审通过
        status = "SecondDeclined"; //二审未通过
    }
    if (oldStatus == "Uploaded") { // 已上传待审核
        status = "Declined"; //审核未通过
    }
    return status;
}
