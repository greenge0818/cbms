/**
 * 上传图片
 * Created by lichaowei on 2016/1/20.
 */
var _allowExt = ".jpeg|.jpg|.png|.gif|.bmp|"; //允许上传的文件类型 ?为无限制 每个扩展名后边要加一个"|" 小写字母表示
var _allowImgFileSize = 2048;    //允许上传图片文件的大小 0为无限制 单位：KB

$().ready(function () {
    //loadImages();

    $(document).off("change", "#uploadFile");
    $(document).on("change", "#uploadFile", function () {
        uploadImages(this);
    });

    $(document).off("click", "#btnSubmit");
    $(document).on("click", "#btnSubmit", function () {
        var imgUrls = "";
        $("#imageview img").each(function () {
            var tempUrl = $.trim($(this).attr("url"));
            imgUrls += imgUrls == "" ? tempUrl : "|" + tempUrl;
        });
        $("#" + $("#fileUrl").val()).attr("url", imgUrls);

        if(imgUrls){
            saveUpload();
            cbms.closeDialog();
        }else{
            alert("请选择上传图片！");
        }

    });
    $(document).off("click", "#btnCancel");
    $(document).on("click", "#btnCancel", function () {
        cbms.closeDialog();
    });

    $(document).off("click", ".close-box");
    $(document).on("click", ".close-box", function () {
        var $img = $(this).closest(".img-ctrl");
        var attachmentId = $img.attr("cid");
        if (!isNaN(attachmentId)) {
            var url = Context.PATH + '/company/deleteattachment.html';
            $.post(url, {id: attachmentId}, function (re) {
                if (re.success) {
                    $img.remove();
                }
                else {
                    cbms.gritter("删除失败！",false);
                }
            });
        }
        else {
            $img.remove();
        }
    });
});


// 检测图像属性
function checkProperty(file) {
    var ErrMsg = "";
    var ImgFileSize = Math.round(file.size / 1024 * 100) / 100;//取得图片文件的大小
    if (_allowImgFileSize != 0 && _allowImgFileSize < ImgFileSize) {
        ErrMsg = "请上传小于" + _allowImgFileSize + "KB的文件";
        gritter(ErrMsg);
        return false;
    }
    else
        return true;
}
function checkExt(file) {
    var ErrMsg = "";
    if (file.name == "") return false;
    var FileExt = file.name.substr(file.name.lastIndexOf(".")).toLowerCase();
    if (_allowExt != 0 && _allowExt.indexOf(FileExt + "|") == -1) //判断文件类型是否允许上传
    {
        ErrMsg = "不允许上传的文件类型" + FileExt;
        gritter(ErrMsg);
        return false;
    }
    else
        return true;
}

/**
 * 上传文件
 * @param fileElement 上传文件控件
 */
function uploadImages(fileElement) {
    if (fileElement.files.length > 0) {
        if (checkExt(fileElement.files[0]) && checkProperty(fileElement.files[0])) {
            var options = {
                type: "POST",
                success: function (re) {
                    var loadingspan = $("#imageview span[loading=true]");
                    if (re) {
                        if (re.success) {
                            $(fileElement).val("");
                            var rootbase = Context.PATH + '/common/getfile.html?key=';
                            loadingspan.removeAttr("loading");
                            loadingspan.find("img").attr("src", rootbase + re.data);
                            loadingspan.find("img").attr("url", re.data);
                            loadingspan.find("a").removeClass("none");
                        }
                        else {
                            gritter(re.data);
                        }
                    }
                    else {
                        gritter("上传失败，服务器异常");
                    }
                },
                error: function (re) {
                    gritter("上传失败，服务器异常");
                }
            };
            $("#imageview").append('<span class="img-ctrl pos-rel" loading="true"><img src="" class="imgload" style="width:70px;height:70px" /><a class="close-box pos-abs none"><i class="fa fa-close red"></i></a></span>');
            $("#uploadForm").ajaxSubmit(options);
        }
    }
}

function loadImages() {
    var urls = $("#" + $("#fileUrl").val()).attr("url");
    if (urls != "") {
        var imgArray = urls.split("|");
        for (var j = 0; j < imgArray.length; j++) {
            var src = Context.PATH + '/common/getfile.html?key=' + imgArray[j];
            $("#imageview").append('<span class="img-ctrl pos-rel"><img url="' + imgArray[j] + '" src="' + src + '" style="width:70px;height:70px"/><a class="close-box pos-abs"><i class="fa fa-close red"></i></a></span>');
        }
    }
}


/**
 * 保存
 */
function saveUpload() {
    var accountId = $("#accountId").val();
    var accountData = {
        "accountExt.custAccountId" :  accountId
    };

    var attachmentIndex = 0;
    // 买家年度采购协议
    if("buyerUpload" == $("#fileUrl").val()){
        var purchaseAgreementImg = $("#buyerUpload").attr("url");
        if (purchaseAgreementImg != undefined && purchaseAgreementImg != "") {
            var purchaseImgArray = purchaseAgreementImg.split("|");
            for (var j = 0; j < purchaseImgArray.length; j++) {
                var url = purchaseImgArray[j];
                if (url && url != "") {
                    var prefix = 'attachmentList[' + attachmentIndex + '].';
                    accountData[prefix + 'type'] = "purchase_agreement";
                    accountData[prefix + 'url'] = url;
                    accountData[prefix + 'accountId'] = accountId;
                    attachmentIndex++;
                }
            }
        }
    }
    //卖家代运营协议
    else{
        var consignContractImg = $("#sellerUpload").attr("url");
        if (consignContractImg != undefined && consignContractImg != "") {
            var consignImg = consignContractImg.split("|");
            for (var j = 0; j < consignImg.length; j++) {
                var url = consignImg[j];
                if (url && url != "") {
                    var prefix = 'attachmentList[' + attachmentIndex + '].';
                    accountData[prefix + 'type'] = "consign_contract";
                    accountData[prefix + 'url'] = url;
                    accountData[prefix + 'accountId'] = accountId;
                    attachmentIndex++;
                }
            }
        }
    }

    $.ajax({
        type: 'post',
        url: Context.PATH + "/company/saveagreementuploadfiles.html",
        data: accountData,
        error: function (s) {
            cbms.closeLoading();
        }
        , success: function (result) {
            cbms.closeLoading();
            if (result) {
                if (result.success) {
                    cbms.gritter('上传图片成功！', true);
                    window.location.reload(true);
                } else {
                    cbms.alert(result.data);
                }
            } else {
                cbms.alert("上传图片失败");
            }
        }
    });
}