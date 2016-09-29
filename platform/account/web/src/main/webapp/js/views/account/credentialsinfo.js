/**
 * 企业证件资料
 * Created by lichaowei on 2016/3/4.
 */

$(".input").hide();
var _relId = "threeCard";   // 三证还是五证合一
$().ready(function () {
    if (_isSeller) {
        cardMustInput(_relId);
    }
    $("#saveForm").verifyForm();

    $(".imgbigger").each(function () {
        $(this).closest("a[rel='lightbox']").attr("href", $(this).attr("src"));
    });

    getPurchaseStatus($("#purchaseStatus").text());
    getConsignContractStatus($("#consignContractStatus").text());

    $("#invoiceSpeedText").text(getInvoiceSpeedText($("#invoiceSpeed").val()));
    $("input[name='invoiceSpeed'][value='" + $("#invoiceSpeed").val() + "']").prop("checked", true);

    $("#invoiceTypeText").text(getInvoiceTypeText($("#invoiceType").val()));
    $("input[name='invoiceType'][value='" + $("#invoiceType").val() + "']").prop("checked", true);

    // 切换三证/五证
    $("input[name='cardRadio']").click(function () {
        _relId = $(this).val();
        $("#" + _relId).show().siblings().hide();
        cardMustInput(_relId);
    });

    // 切换编辑
    $("#editInfoBtn").click(function () {
        $(this).hide();
        $(".view").hide();
        $(".input,#saveInfoBtn").show();
    });

    // 弹窗上传
    $("#buyerUpload").click(function () {
        var uploadurl = Context.PATH + '/company/imagesupload.html?accountId=' + $("#accountId").val() + '&type=purchase_agreement&fileUrl=buyerUpload&';
        cbms.getDialog("买家年度采购协议", uploadurl);
    });
    $("#sellerUpload").click(function () {
        var uploadurl = Context.PATH + '/company/imagesupload.html?accountId=' + $("#accountId").val() + '&type=consign_contract&&fileUrl=sellerUpload&';
        cbms.getDialog("卖家代运营协议", uploadurl);
    });

    // 代运营协议状态修改
    $("#sellerStop,#sellerReOpen").click(function () {
        var status = $(this).attr("status");
        var accountExt = {
            'custAccountId': $("#accountId").val(),
            'sellerConsignAgreementStatus': status
        };
        var confirmText = '';
        var successText = '';
        var failedText = '';
        if (status == 'Terminate') {
            confirmText = '确定终止该用户代运营协议？';
            successText = '代运营协议已终止！';
            failedText = '代运营协议终止失败！';
        }
        else {
            confirmText = '确定重新开启该用户代运营协议？';
            successText = '代运营协议已重新开启！';
            failedText = '代运营协议重新开启失败！';
        }
        cbms.confirm(confirmText, null, function () {
            cbms.loading();
            $.ajax({
                type: 'post',
                url: Context.PATH + "/company/updatesellerconsignstatus.html",
                data: accountExt,
                error: function (s) {
                    cbms.closeLoading();
                }
                , success: function (result) {
                    cbms.closeLoading();
                    if (result) {
                        if (result.success) {
                            cbms.gritter(successText, true);
                            if (status == "Approved") {
                                getConsignContractStatus("审核通过");
                                $("#consignContractStatus").text("审核通过");
                            }
                            else {
                                getConsignContractStatus("已终止");
                                $("#consignContractStatus").text("已终止");
                            }
                        } else {
                            cbms.gritter(result.data, false);
                        }
                    } else {
                        cbms.gritter(failedText, false);
                    }
                }
            });
        });
    });

    // 保存
    $("#saveInfoBtn").click(function () {
        if (!setlistensSave()) return;
        if (!verify()) return;
        save(function () {
            window.location.reload(true);
        });
    });
});

function getInvoiceSpeedText(invoiceSpeed) {
    var invoiceSpeedText = "正常";
    if (invoiceSpeed == "FAST") {
        invoiceSpeedText = "快";
    } else if (invoiceSpeed == "SLOW") {
        invoiceSpeedText = "慢";
    }
    return invoiceSpeedText;
}

function getInvoiceTypeText(invoiceType) {
    var invoiceTypeText = "增值税普通发票";
    if (invoiceType == "PRIVATE") {
        invoiceTypeText = "增值税专用发票";
    }
    return invoiceTypeText;
}

function getPurchaseStatus(purchaseStatus) {
    $("#buyerEdit,#buyerPrint,#buyerUpload,#purchaseAgreementImg").hide();
    if (purchaseStatus == "待打印") {
        $("#buyerEdit,#buyerPrint,#buyerUpload").show();
    } else if (purchaseStatus == "二审通过") {
        $("#buyerPrint,#buyerUpload").show();
    } else if (purchaseStatus == "审核未通过") {
        $("#buyerEdit,#buyerPrint,#buyerUpload").show();
    } else if (purchaseStatus == "已上传待审核" || purchaseStatus == "审核通过") {
        $("#purchaseAgreementImg").show();
    }
}

function getConsignContractStatus(consignContractStatus) {
    $("#sellerEdit,#sellerPrint,#sellerUpload,#sellerStop,#sellerReOpen,#consignContractImg").hide();
    if (consignContractStatus == "待打印") {
        $("#sellerEdit,#sellerPrint,#sellerUpload").show();
    } else if (consignContractStatus == "二审通过") {
        $("#sellerPrint,#sellerUpload").show();
    } else if (consignContractStatus == "审核未通过") {
        $("#sellerEdit,#sellerPrint,#sellerUpload").show();
    } else if (consignContractStatus == "已上传待审核") {
        $("#consignContractImg").show();
    } else if (consignContractStatus == "审核通过") {
        $("#sellerStop,#consignContractImg").show();
    } else if (consignContractStatus == "已终止") {
        $("#sellerReOpen").show();
    }
}

/**
 * 三证/五证必填
 * @param tabId
 */
function cardMustInput(tabId) {
    if (!_isSeller) return;

    if (tabId == "threeCard") {
        $("#licenseCode").attr("must", 1);
        $("#regAddress").attr("must", 1);
        $("#orgCode").attr("must", 1);

        $("#creditCode").removeAttr("must");
    }
    else if (tabId == "fiveCard") {
        $("#creditCode").attr("must", 1);

        $("#licenseCode,#regAddress,#orgCode").removeAttr("must");
    }
    else {
        $("#licenseCode,#regAddress,#orgCode").removeAttr("must");
        $("#creditCode").removeAttr("must");
    }
}

function verify() {
    if (_isSeller) {
        if (_relId == "threeCard") {
            var oldpicLicense = $("#picLicense").attr("oldUrl");
            var oldpicOrgCode = $("#picOrgCode").attr("oldUrl");
            var oldpicTaxReg = $("#picTaxReg").attr("oldUrl");
            var picLicense = $("#picLicense").attr("url");
            var picOrgCode = $("#picOrgCode").attr("url");
            var picTaxReg = $("#picTaxReg").attr("url");
            if ((oldpicLicense == "" && picLicense == "")||( oldpicOrgCode == "" &&picOrgCode== "")
                ||(oldpicTaxReg == "" && picTaxReg == "")) {
                cbms.gritter("请上传三证图片");
                return false;
            }
        }
        else {
            var oldpicCreditCode = $("#picCreditCode").attr("oldUrl");
            var picCreditCode = $("#picCreditCode").attr("url");
            if (oldpicCreditCode == "" && picCreditCode == "") {
                cbms.gritter("请上传统一社会信用图片");
                return false;
            }
        }
    }
    var bankNumber = $("#bankNumber").val();
    if (bankNumber != "" && bankNumber.length < 5) {
        cbms.alert("开票资料银行账号太短！");
        return false;
    }
    return true;
}

/**
 * 保存
 * @param callback 回调函数
 */
function save(callback) {
    var accountData = {
        'account.id': $("#accountId").val(),
        // 三证
        'accountExt.licenseCode': $.trim($("#licenseCode").val()),
        'accountExt.regAddress': $.trim($("#regAddress").val()),
        'accountExt.orgCode': $.trim($("#orgCode").val()),

        // 五证合一
        'accountExt.creditCode': $.trim($("#creditCode").val()),

        // 代运营资料
        'accountExt.invoiceSpeed': $("input[name='invoiceSpeed']:checked").val(),

        // 开票资料
        'accountExt.taxCode': $.trim($("#taxCode").val()),
        'accountExt.addr': $.trim($("#addr").val()),
        'accountExt.taxTel': $.trim($("#taxTel").val()),
        'accountExt.taxBankNameMain': $.trim($("#taxBankNameMain").val()),
        'accountExt.taxBankNameBranch': $.trim($("#taxBankNameBranch").val()),
        'accountExt.bankNumber': $.trim($("#bankNumber").val()),
        'accountExt.invoiceType': $.trim($("input[name='invoiceType']:checked").val()),

        //打款资料
        'accountExt.bankProvinceId': $.trim($("#bankProvince").val()),
        'accountExt.bankCityId': $.trim($("#bankCity").val()),
        'accountExt.bankNameMain': $.trim($("#bankNameMain").val()),
        'accountExt.bankNameBranch': $.trim($("#bankNameBranch").val()),
        'accountExt.bankCode': $.trim($("#bankCode").val()),
        'accountExt.accountCode': $.trim($("#accountCode").val())
    };
    // 附件集合
    var attachmentArray = $("input[type='file']");
    var attachmentIndex = 0;
    for (var j = 0; j < attachmentArray.length; j++) {
        var url = $(attachmentArray[j]).attr("url");
        if (url && url != "") {
            var prefix = 'attachmentList[' + attachmentIndex + '].';
            accountData[prefix + 'type'] = $(attachmentArray[j]).attr("pictype");
            accountData[prefix + 'url'] = $(attachmentArray[j]).attr("url");
            attachmentIndex++;
        }
    }
    // 买家年度采购协议,卖家代运营协议多张图片特殊处理
    var purchaseAgreementImg = $("#buyerUpload").attr("url");
    if (purchaseAgreementImg != undefined && purchaseAgreementImg != "") {
        var purchaseImgArray = purchaseAgreementImg.split("|");
        for (var j = 0; j < purchaseImgArray.length; j++) {
            var url = purchaseImgArray[j];
            if (url && url != "") {
                var prefix = 'attachmentList[' + attachmentIndex + '].';
                accountData[prefix + 'type'] = "purchase_agreement";
                accountData[prefix + 'url'] = url;
                attachmentIndex++;
            }
        }
    }
    var consignContractImg = $("#sellerUpload").attr("url");
    if (consignContractImg != undefined && consignContractImg != "") {
        var consignImg = consignContractImg.split("|");
        for (var j = 0; j < consignImg.length; j++) {
            var url = consignImg[j];
            if (url && url != "") {
                var prefix = 'attachmentList[' + attachmentIndex + '].';
                accountData[prefix + 'type'] = "consign_contract";
                accountData[prefix + 'url'] = url;
                attachmentIndex++;
            }
        }
    }

    cbms.loading();
    $.ajax({
        type: 'post',
        url: Context.PATH + "/company/savecredentials.html",
        data: accountData,
        error: function (s) {
            cbms.closeLoading();
        }
        , success: function (result) {
            cbms.closeLoading();
            if (result) {
                if (result.success) {
                    cbms.gritter('客户资料修改成功！', true, callback());
                } else {
                    cbms.alert(result.data);
                }
            } else {
                cbms.alert("客户资料修改失败");
            }
        }
    });
}