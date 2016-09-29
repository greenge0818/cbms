/**
 * Created by caochao on 2015/8/4.
 */
var picNum = $("#picNum").val();
jQuery(function ($) {
    $("#uploadselect").change(function () {
        if (this.files.length > 0  && checkUploadPic(this.files)) {
            var imgEle = "", loadingspan1 = "";
            var options = {
                type: "POST",
                success: function (re) {
                    var loadingspan = $("#img_pics span[loading=true]");

                    var imglength = $("#img_pics img").length;


                    if (re) {
                        if (re.success) {
                            var appendImg = "";
                            var dataId = ""
                            var rootbase = Context.PATH + '/common/getfile.html?key=';
                            var data = re.data;
                            picNum = parseInt(picNum) + data.length;
                            for (var i = imglength; i < data.length; i++) {
                                dataId = data[i].id;
                                appendImg = '<div id="img_append' + data[i].id + '"><div/>';
                                // loadingspan.attr("cid", data[i].id).removeAttr("loading");
                                loadingspan1 += '<span class="pull-left pos-rel imgload" style="margin-right:10px;margin-top:10px;width:100px;height:70px" loading="true"><a href="javascript:;"  class="img-box"><img src="" class="imgloaddetail" width="100px" height="70px" style="border: 2px solid rgb(51, 122, 183);" /></a><a href="javascript:;" class="pos-abs fa fa-lg fa-close red" style="right:-5px;top:-5px;z-index: 99;"></a></span>';

                                imgEle += '<span class="pull-left pos-rel" style="margin-right:10px;margin-top:10px" loading="true"><a href="javascript:;"  class="img-box"><img src="' + rootbase + data[i].fileUrl + '" class="imgloaddetail" width="100px" height="70px" style="border: 2px solid rgb(51, 122, 183);" /></a><a href="javascript:;" id="' + data[i].id + '" class="pos-abs fa fa-lg fa-close red" style="right:-5px;top:-5px;z-index: 99;"></a></span>'
                            }
                            $("#img_pics").append(appendImg);
                            $("#img_append" + dataId).append(loadingspan1);

                            setTimeout(function () {
                                $("#img_append" + dataId).html(imgEle);
                            }, 250);
                        }
                        else {
                            loadingspan.remove();
                            cbms.alert("上传失败:" + re.data);
                        }
                    }
                    else {
                        loadingspan.remove();
                        cbms.alert("上传失败，服务器异常");
                    }
                },
                error: function (re) {
                    var loadingspan = $("#img_pics span[loading=true]");
                    loadingspan.remove();
                    cbms.alert("上传失败，服务器异常");
                }
            };

            $("#uploadForm").ajaxSubmit(options);
           // }
        }

        //ShowMsg("");
        //$("#imageview").html("");
        //if (this.files.length > 0) {
        //    for (var i = 0; i < this.files.length; i++) {
        //        if (!CheckExt(this.files[i]) || !CheckProperty(this.files[i]))
        //            return false;
        //        else {
        //            var reader = new FileReader();
        //            reader.onload = function (evt) {
        //                $("#imageview").append('<span class="img-ctrl pos-rel"><img src="' + evt.target.result + '" style="width:70px;height:70px" /><a class="close-box pos-abs"><i class="fa fa-close red"></i></a></span>');
        //            }
        //            reader.readAsDataURL(this.files[i]);
        //        }
        //    }
        //}
        //else {
        //    $("#imageview").html("图片预览区");
        //}
    });
    $("#btnsubmit").click(function () {
        var url = Context.PATH + '/order/contractupload.html';
        var contractId = $("#contract_id").val();
        var contractNo = $("#contract_no").val();
        var contractType = $("#contractType").val();

        /*if($.trim(contractNo)=='')
        {
            ShowMsg("请填写合同编号！");
            return;
         }*/

        //验证合同编号:不能为空、格式
        if (!setlistensSave("#uploadForm")) return;

        if(picNum<=0)
        {
            ShowMsg("请上传合同文件！");
            return ;
        }

        $("#btnsubmit").prop("disabled", true)
        $.post(url, {contractid: contractId, contractno: contractNo,contractType:contractType}, function (re) {
            $("#btnsubmit").removeAttr("disabled");
            if (re.success) {
                ShowMsg("保存成功！");
                setTimeout(function () {
                    parent.cbms.closeDialog();
                    // parent.location.reload(true);
                    //做成局部刷新
                    //add lixiang 合同变更申请付款改变状态栏值
                    var apply_span = $("#payment_table tr td:eq(11) span[style]");
                    if (apply_span.length > 0) {
                    	var content = '<td><span style="style">未申请付款</span><br/>合同已上传</td>';
                        $("#payment_table tr td:eq(11)").html(content);//给状态赋值
                    }
                    var html = '<a href="javascript:void(0)" class="applypay_contract_view  btn-info btn-sm" >查看合同</a>&nbsp; <a href="javascript:void(0)" class="applypay_contract_upload  btn-info btn-sm" > 重新上传 </a>';
                    parent.$("#payment_table tr[contractid=" + contractId + "] td:eq(-1)").html(html);
                }, 500);
            }
            else {
                ShowMsg("保存失败:"+re.data);
            }
        });
    });

    $(document).off("click",".fa-close");
    //删除图片
    $(document).on("click", ".fa-close", function () {
        var attachmentid =  $(this).attr("id");
        if (!isNaN(attachmentid)) {
            var url = Context.PATH + '/order/deleteattachment.html';
            var orderId = $("#order_id").val();
            var contractId = $("#contract_id").val();
            var contractType = $("#contractType").val();
            $.post(url, {orderid: orderId, contractid: contractId, attachmentid: attachmentid,contractType:contractType}, function (re) {
                if (re.success) {
                    picNum =picNum -1;
                    showImgs(re,"img_pics");
                    if(picNum <= 0){
                        var html = '<a href="javascript:void(0)" class="applypay_contract_upload  btn-info btn-sm" >上传合同</a>';
                        parent.$("#payment_table tr[contractid=" + contractId + "] td:eq(-1)").html(html);
                    }
                }
                else {
                    ShowMsg("删除失败:"+re.data);
                }
            });
        }else {
        ShowMsg("删除失败！");
    }
    });
    /*$(document).on("click",".close-box", function () {
       // var $img = $(this).closest(".img-ctrl");
        var attachmentid = $img.attr("cid");
        if (!isNaN(attachmentid)) {
            var url = Context.PATH + '/order/deleteattachment.html';
            var orderId = $("#order_id").val();
            var contractId = $("#contract_id").val();
            $.post(url, {orderid: orderId, contractid: contractId, attachmentid: attachmentid}, function (re) {
                if (re.success) {
                    picNum =picNum -1;
                    showImgs(re,"img_pics");
                }
                else {
                    ShowMsg("删除失败:"+re.data);
                }
            });
        }
        else {
            ShowMsg("删除失败！");
        }
    })*/
//弹出页面查看图片
  /*  $(document).on("click", ".img-box", function () {
        var $img = $(this).next("img"), tit = $img.attr("alt");
        var src = $(this).find("img").attr("src");
        renderImg(src);
    });*/
});
/**
 * 将图片显示到浏览器上
 */
function showImgs(result,targetId) {
    var imgPre = document.getElementById(targetId);
    var htmlStr = '';
    var rootbase = Context.PATH + '/common/getfile.html?key=';
    for(var i=0;i<result.data.length;i++){
        htmlStr += '<span class="pull-left pos-rel" style="margin-right:10px;margin-top:10px"><a href="javascript:;" class="img-box"><img src="'+rootbase+result.data[i].fileUrl+'" alt="回单" width="100px" height="70px" style="border: 2px solid rgb(51, 122, 183);"/></a>';
        htmlStr += '<a href="javascript:;" class="pos-abs fa fa-lg fa-close red" style="right:-5px;top:-5px;z-index: 99;" id="'+result.data[i].id+'"></a></span>';

    }
    $(imgPre).html(htmlStr);
}
var AllowExt = ".jpeg|.jpg|.png|.gif|.bmp|" //允许上传的文件类型 ?为无限制 每个扩展名后边要加一个"|" 小写字母表示
var AllowImgFileSize = 2048;    //允许上传图片文件的大小 0为无限制 单位：KB

function CheckProperty(file)    //检测图像属性
{
    var ErrMsg = "";
    var ImgFileSize = Math.round(file.size / 1024 * 100) / 100;//取得图片文件的大小
    if (AllowImgFileSize != 0 && AllowImgFileSize < ImgFileSize) {
        ErrMsg = "请上传小于" + AllowImgFileSize + "KB的文件";
        ShowMsg(ErrMsg);
        return false;
    }
    else
        return true;
}
function CheckExt(file) {
    var ErrMsg = "";
    if (file.name == "") return false;
    var FileExt = file.name.substr(file.name.lastIndexOf(".")).toLowerCase();
    if (AllowExt != 0 && AllowExt.indexOf(FileExt + "|") == -1) //判断文件类型是否允许上传
    {
        ErrMsg = "不允许上传的文件类型" + FileExt;
        ShowMsg(ErrMsg);
        return false;
    }
    else
        return true;
}

function ShowMsg(msg) //显示提示信息 tf=true 显示文件信息 tf=false 显示错误信息 msg-信息内容
{
    $("#uploadmsg").html(msg);
}
function checkUploadPic(files){
    for(var i =0;i<files.length;i++){
        if (!CheckExt(files[i]) || !CheckProperty(files[i])) {
            return false;
        }
    }
    return true;

}

