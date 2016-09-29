/**
 * 添加账号基本信息
 * Created by lichaowei on 2016/1/15.
 */
var _verify = false;        // 数据验证是否通过
var _isSeller = false;      // 是否是卖家
var _relId = "threeCard";   // 三证还是五证合一
$().ready(function () {
    $("#formstep1").verifyForm();
    $("#formstep2").verifyForm();

    $("#acountName").blur(function () {
        searchAccountByName($.trim($(this).val()));
    });

    // 卖家显示代理钢厂信息
    $("input[name='nature'][value='2']").click(function () {
        if ($(this).prop("checked")) {
            _isSeller = true;
            $(".orgtd").show();
            $("#factoryInfo").show();
            $("#saveInfoBtn").hide();
            $("#nextStepBtn").text("下一步");
            cardMustInput(_relId);
            $("label[must='true']").text("*");
        }
        else {
            _isSeller = false;
            $(".orgtd").hide();
            $("#factoryInfo").hide();
            $("#saveInfoBtn").show();
            $("#nextStepBtn").text("下一步（完善资料）");
            cardMustInput("");
            $("label[must='true']").text("");
        }
    });

    // 代理钢厂复制行
    $("#factoryInfoTbody").on("click", "a[option='add']", function () {
        var currentRow = $(this).closest("tr");
        var cloneRow = $(currentRow).clone();
        $(cloneRow).removeAttr("id");
        $(cloneRow).find("a[option='del']").show();
        $(cloneRow).find("input").val("");
        $("#factoryInfoTbody").append($(cloneRow));
    });

    // 代理钢厂删除行
    $("#factoryInfoTbody").on("click", "a[option='del']", function () {
        $(this).closest("tr").remove();
    });

    $("#contactName,#contactTel").focus(function () {
        cbms.getDialog("联系人", "#contactInfoLayer");
        $("#inputName,#contactAddDefine").hide();

        var contactTel = $("#contactTel").val();
        if (contactTel != "") {
            $("#inputMobile").val(contactTel).show();
            searchContactByMobile($.trim(contactTel));
        }
    });

    $(document).on("input", "#inputMobile", function () {
        searchContactByMobile($.trim($(this).val()));
    });

    $(document).on("click", "#contactAdd", function () {
        $("#inputName,#contactAddDefine").show();
        $("#contactList,#contactEmpty").hide();
    });

    $(document).on("click", "#contactAddDefine", function () {
        var name = $.trim($("#inputName").val());
        var mobile = $.trim($("#inputMobile").val());
        $("#contactName").val(name).attr("contactid", 0);
        $("#contactTel").val(mobile);
        cbms.closeDialog();
    });

    $(document).on("click", "#contactList li", function () {
            var contactId = $(this).attr("contactid");
            var name = $(this).find("span").text();
            var mobile = $(this).find("label").text();
            $("#contactName").val(name).attr("contactid", contactId);
            $("#contactTel").val(mobile);
            cbms.closeDialog();
        }
    );

    // 提交注册
    $("#saveInfoBtn").click(function () {

        if (!setlistensSave()) return;
        if (!verify() || !_verify)   return;

        save(function () {
            $("#btnBar").hide();

            if ($("#step1").is(":hidden") == false) {
                window.location.href = Context.PATH + "/company/list.html";
            }
            else {
                $("#step1,#step2").hide();
                $("#step3").show();
                $("#progressBar li").eq(0).addClass("cu2");
                $("#progressBar li").eq(1).removeClass("cu1").addClass("cu2");
                $("#progressBar li").eq(2).addClass("cu3");
            }
        });
    });

    // 下一步（完善资料）
    $("#nextStepBtn").click(function () {
        if(!setlistensSave($("#formstep1"))){
            return false;
        }
        var orgId = $("#org").val();
        if (orgId == "" && _isSeller) {
            cbms.alert("请选择归属服务中心！");
            return false;
        }

        $("#step2").show();
        $("#step1").hide();

        $("#prevStepBtn").show();
        $("#nextStepBtn").hide();
        $("#saveInfoBtn").show();

        $("#progressBar li").eq(0).addClass("cu2");
        $("#progressBar li").eq(1).addClass("cu1");

        $("#nameText").text($("#acountName").val());
        $("#businessTypeText").text($("input[name='nature']:checked").parent().text());
        if($("#org").val() != ""){
            $("#orgNameText").show();
            $("#orgNameText label").text($("#org").find("option:selected").text());
        }
        else{
            $("#orgNameText").hide();
        }
        var accountTag = getAccountTag();
        if ((1 & accountTag) == 1) {
            $("#payInfo,#cbmsInfo").hide();
            $("#billingInfo").show();
        }
        if ((2 & accountTag) == 2) {
            $("#billingInfo").hide();
            $("#payInfo,#cbmsInfo").show();
        }
        if ((1 & accountTag) == 1 && (2 & accountTag) == 2) {
            $("#payInfo,#cbmsInfo,#billingInfo").show();
        }
    });
    // 上一步
    $("#prevStepBtn").click(function () {
        $("#step1").show();
        $("#step2").hide();

        $("#prevStepBtn").hide();
        $("#nextStepBtn").show();
        if ($("input[name='nature'][value='2']").prop("checked") == true) {
            $("#saveInfoBtn").hide();
        }
        $("#progressBar li").eq(0).removeClass("cu2").addClass("cu1");
        $("#progressBar li").eq(1).removeClass("cu1");
    });

    // 切换三证/五证
    $("input[name='cardRadio']").click(function () {
        $("#cardInfo").show();
        _relId = $(this).val();
        $("#" + _relId).show().siblings().hide();

        cardMustInput(_relId)
    });

    $(".tableFour font").click(function () {
        $(this).closest("h1").siblings().toggle();
    });
    $(".tableFive h1,.tableSix h1,.tableSeven h1").click(function () {
        $(this).siblings().toggle();
    });
});

/**
 *  根据名称查询公司
 * @param name 名称
 */
function searchAccountByName(name) {
    if (name == "")return;
    var accountTag = 1; // 默认买家
    $("#view").hide();
    $.ajax({
        type: "POST",
        url: Context.PATH + "/account/searchaccountbyname.html",
        data: {
            "name": name
        },
        success: function (result) {
            if (result.success) {
                $("#view a").attr("href", Context.PATH + "/company/" + result.data.id + "/credentialsinfo.html");
                $("#view").show();
                accountTag = result.data.accountTag;
            }
            else {
                _verify = true;
            }
            //错误 #9065 解决添加我的客户信息时，先选客户性质再填写公司名称，之前选的客户性质数据会丢失。 afeng 2016/5/16
//            setAccountTag(accountTag);
//            if ((accountTag & 2) != 2) {
//                _isSeller = false;
//                $("#org").val("");
//                $(".orgtd").hide();
//
//                $("#factoryInfo").hide();
//                $("#saveInfoBtn").show();
//                cardMustInput("");
//            }
        },
        error: function (xhr, textStatus, errorThrown) {
        }
    });
}

/**
 * 获取AccountTag
 * @returns {number}
 */
function getAccountTag() {
    var tag = 0;
    $("input[name='nature']:checked").each(function () {
        var tempTag = parseInt($(this).val(), 10);
        tag = tag | tempTag;
    });
    return tag;
}
/**
 * 设置AccountTag
 * @param accountTag
 */
function setAccountTag(accountTag) {
    $("input[name='nature']").prop("checked", false);
    $("input[name='nature']").each(function () {
        var val = parseInt($(this).val());
        if ((accountTag & val) == val) {
            $(this).prop("checked", true);
        }
    });
}

/**
 * 根据电话查询联系人
 * @param mobile
 */
function searchContactByMobile(mobile) {
    $("#contactList").show().empty();
    $("#contactEmpty").hide();
    $("#formMobile").verifyForm();
    if (mobile != null && mobile != "" && mobile.length == 11 ) {
        var regMobile = /^0*(13|14|15|18|17)\d{9}$/;
        if(!regMobile.test(mobile)){
            cbms.gritter("手机号码输入错误");
            return;
        }
        $.ajax({
            type: "POST",
            url: Context.PATH + "/account/searchcontactbymobile.html",
            data: {
                "mobile": mobile
            },
            success: function (result) {
                if (result.success) {
                    var contactHtml = "";
                    if (result.data != null) {
                        contactHtml += "<li contactid='" + result.data.id + "'><span>"
                        + result.data.name + "</span>&nbsp;<label>" + result.data.tel + "</label></li>";
                        $("#contactList").html(contactHtml).show();
                    }
                }
                else { // 没有找到
                    $("#contactEmpty").show();
                    $("#contactList").hide();
                }
            },
            error: function (xhr, textStatus, errorThrown) {
            }
        });
    }
}

/**
 * 三证/五证必填
 * @param tabId
 */
function cardMustInput(tabId) {
    if (tabId == "threeCard") {
        $("#licenseCode").attr("must", 1);
        $("#regAddress").attr("must", 1);
        $("#orgCode").attr("must", 1);
        $("#picLicense").attr("must", 1);
        $("#picOrgCode").attr("must", 1);
        $("#picTaxReg").attr("must", 1);

        $("#creditCode,#picCreditCode").removeAttr("must");
    }
    else if (tabId == "fiveCard") {
        $("#creditCode").attr("must", 1);
        $("#picCreditCode").attr("must", 1);

        $("#licenseCode,#regAddress,#orgCode,#picLicense,#picOrgCode,#picTaxReg").removeAttr("must");
    }
    else {
        $("#licenseCode,#orgCode,#picLicense,#picOrgCode,#picTaxReg").removeAttr("must");
        $("#creditCode,#picCreditCode").removeAttr("must");
    }
}

/**
 * 验证
 */
function verify() {
    var accountTag = getAccountTag();
    if (accountTag == 0) {
        cbms.alert("请选择客户性质！");
        return false;
    }
    var businessType = $("#businessType").val();
    if (businessType == "") {
        cbms.alert("请选择客户类型！");
        return false;
    }

    if (_relId == "threeCard" && _isSeller) {
        if ($.trim($("#licenseCode").val()) == "") {
            cbms.alert("请输入营业执照注册号！");
            return false;
        }
        if ($.trim($("#regAddress").val()) == "") {
            cbms.alert("请输入企业注册地址！");
            return false;
        }
        if ($.trim($("#orgCode").val()) == "") {
            cbms.alert("请输入组织机构代码！");
            return false;
        }
        if ($.trim($("#picLicense").val()) == "") {
            cbms.alert("请上传营业执照！");
            return false;
        }
        if ($.trim($("#picOrgCode").val()) == "") {
            cbms.alert("请上传组织机构代码证！");
            return false;
        }
        if ($.trim($("#picTaxReg").val()) == "") {
            cbms.alert("请上传税务登记证（副本）！");
            return false;
        }
    }
    else if (_relId == "fiveCard" && _isSeller) {
        if ($.trim($("#creditCode").val()) == "") {
            cbms.alert("请输入统一社会信用代码号！");
            return false;
        }
        if ($.trim($("#picCreditCode").val()) == "") {
            cbms.alert("请上传统一社会信用代码！");
            return false;
        }
    }
    var bankNumber = $("#bankNumber").val();
    if (bankNumber != "" && bankNumber.length < 5) {
        cbms.alert("开票资料银行账号太短！");
        return false;
    }
    var accountCode = $("#accountCode").val();
    if (accountCode != "" && accountCode.length < 5) {
        cbms.alert("打款资料银行账号太短！");
        return false;
    }
    return true;
}

/**
 * 保存
 * @param callback 回调函数
 */
function save(callback) {

    var orgId = $("#org").val();
    //modify by zhoucai@prcsteel.com 如果为非卖家，则重置服务中心
    if(!_isSeller){
        orgId="";
    }
    var accountData = {
        // 公司基本信息
        'account.name': $.trim($("#acountName").val()),
        'account.accountTag': getAccountTag(),

        'account.orgId': orgId == "" ? "0" : orgId,
        'account.orgName': orgId == "" ? "" : $.trim($("#org").find("option:selected").text()),
        'account.businessType': $("#businessType").val(),
        //迁移风控代码，增加5个字段 add by zhoucai@prcsteel.com
        'account.registerCapital':$("#registerCapital").val(),
        'account.supplierLabel': $("#supplierLabel").val(),
        'account.buyerLabel': $("#buyerLabel").val(),
        'account.paymentLabel': $("#paymentLabel").val(),
        'account.registerDate': $("#registerDate").val(),

        'account.sellerSingleTradeWeight':$("#sellerSingleTradeWeight").val(),
        'account.sellerAllTradeQuality':$("#sellerAllTradeQuality").val(),
        'account.sellerAllTradeWeight':$("#sellerAllTradeWeight").val(),
        'account.isSellerPercent':$('input[name="isSellerPercent"]:checked ').val(),



        'accountExt.name': $.trim($("#acountName").val()),
        'accountExt.zip': $.trim($("#zip").val()),
        'accountExt.mailAddr': $.trim($("#mailAddr").val()),
        'accountExt.tel': $.trim($("#tel").val()),
        'accountExt.mobil': $.trim($("#mobile").val()),
        'accountExt.fax': $.trim($("#fax").val()),
        'accountExt.legalPersonName': $.trim($("#legalPersonName").val()),
        'accountExt.webSiteUrl': $.trim($("#webSiteUrl").val()),
        'accountExt.provinceId': $.trim($("#province").val()),
        'accountExt.cityId': $.trim($("#city").val()),
        'accountExt.districtId': $.trim($("#district").val()),



        // 联系人信息
        'contact.id': $("#contactName").attr("contactid"),
        'contact.name': $.trim($("#contactName").val()),
        'contact.email': $.trim($("#contactEmail").val()),
        'contact.tel': $.trim($("#contactTel").val()),
        'contact.note': $.trim($("#contactNote").val()),
        'contact.qq': $.trim($("#contactQq").val()),

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

    // 钢厂信息
    for (var i = 0; i < $("#factoryInfoTbody tr").length; i++) {
        var row = $("#factoryInfoTbody tr").eq(i);
        var preName = 'proxyFactoryList[' + i + '].';
        var factory = $.trim($(row).find("input[name='factory']").val());
        if (factory != "") {
            accountData[preName + 'factory'] = factory;
            accountData[preName + 'nsortName'] = $.trim($(row).find("input[name='nsortName']").val());
            accountData[preName + 'qualification'] = $.trim($(row).find("select[name='qualification']").val());
            accountData[preName + 'quantityUnit'] = $.trim($(row).find("select[name='quantityUnit']").val());
            accountData[preName + 'quantity'] = $.trim($(row).find("input[name='quantity']").val());
            accountData[preName + 'stock'] = $.trim($(row).find("input[name='stock']").val());
        }
    }
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
    cbms.loading();
    $.ajax({
        type: 'post',
        url: Context.PATH + "/account/save.html",
        data: accountData,
        error: function (s) {
            cbms.closeLoading();
        }
        , success: function (result) {
            cbms.closeLoading();
            if (result) {
                if (result.success) {
                    cbms.gritter('添加成功！', true, callback());
                } else {
                    cbms.alert(result.data);
                }
            } else {
                cbms.alert("添加失败");
            }
        }
    });
}

