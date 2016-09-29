/**
 * create by wangxianjun
 *
 */
var tradeTable;
var uploadNum = $("#uploadNum").val();
jQuery(function ($) {
    //弹出页面查看图片
    $(document).on("click", ".img-box", function () {
        var $img = $(this).next("img"), tit = $img.attr("alt");
        var src = $(this).find("img").attr("src");
        renderImg(src);
    });
    //删除图片
    $(document).on("click", ".fa-close", function () {
        var attachmentid =  $(this).attr("id");
        if (!isNaN(attachmentid)) {
            deleteImg(attachmentid, $("#certid").val());
        }
    });

});

/**
 * 从 file 域获取 本地图片 url
 */
function getFileUrl(sourceId) {
    var url;
    if (navigator.userAgent.indexOf("MSIE")>=1) { // IE
        url = document.getElementById(sourceId).value;
    } else if(navigator.userAgent.indexOf("Firefox")>0) { // Firefox
        url = document.getElementById(sourceId).files;
    } else if(navigator.userAgent.indexOf("Chrome")>0) { // Chrome
        // url = window.URL.createObjectURL(document.getElementById(sourceId).files.item(0));
        url = document.getElementById(sourceId).files;
    }

    return url;
}
/**
 * 上传图片 显示到浏览器上
 */

function preImg() {
    var imgEle = "", loadingspan1 = "";
    if($("#certid").val() == '' || ($("#printNum").val() == 0 && $("#printCodeNum").val() == 0)){
        cbms.alert("请先打印凭证!");
        return;
    }
    if(uploadNum == 0){
    	//第一次上传更新凭证名称
    	$("#credentName").val($("#credent_name").val());
    }else{
    	$("#credentName").val('');
    }
    	
        var options = {
        type: "POST",
        success: function (re) {
            var loadingspan = $("#img_pics span[loading=true]");

            var imglength = $("#img_pics img").length;


            if (re) {
                if (re.success) {
                    var appendImg = "";
                    var dataId = "";
                    var rootbase = Context.PATH + '/common/getfile.html?key=';
                    var data = re.data;
                    for (var i = imglength; i < data.length; i++) {
                        dataId = data[i].id;
                        appendImg = '<div id="img_append' + data[i].id + '"><div/>';
                        // loadingspan.attr("cid", data[i].id).removeAttr("loading");
                        loadingspan1 += '<span class="pull-left pos-rel imgload" style="margin-right:10px;margin-top:10px;width:100px;height:70px" loading="true"><a href="javascript:;"  class="img-box"><img src="" class="imgloaddetail" width="100px" height="70px" style="border: 2px solid rgb(51, 122, 183);" /></a><a href="javascript:;" class="pos-abs fa fa-lg fa-close red" style="right:-5px;top:-5px;z-index: 99;"></a></span>';

                        imgEle += '<span class="pull-left pos-rel" style="margin-right:10px;margin-top:10px" loading="true"><a href="javascript:;"  class="img-box"><img src="' + rootbase + data[i].fileUrl + '" class="imgloaddetail" width="100px" height="70px" style="border: 2px solid rgb(51, 122, 183);" /></a><a href="javascript:;" id="' + data[i].id + '" class="pos-abs fa fa-lg fa-close red" style="right:-5px;top:-5px;z-index: 99;"></a></span>'
                    }
                    uploadNum = data.length;
                    if(uploadNum > 0){
                        //上传凭证后，不能再修改凭证名称
                        $("#credent_name").attr("disabled",true);
                    }else{
                        $("#credent_name").attr("disabled",false);
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

    $("#orderForm").ajaxSubmit(options);

}

function refreshTable(){
	
	   $.ajax({
	        type: 'post',
	        url: Context.PATH + "/order/certificate/getorderattachment.html",
	        async : false,
	        data: {
	        	id :$("#certid").val(),
	        	code:$("#certcode").val(),
                orderByPageNumber:"orderByPageNumber"
	        },
	        error: function (s) {
	        }
	        , success: function (result) {
        		if(result && result.success){
        			
        			var imglength = $("#img_pics img").length;
        			var imgEle = "", loadingspan1 = "";
        			
        			var appendImg = "";
        		    var dataId = "";
        		    var rootbase = Context.PATH + '/common/getfile.html?key=';
        		    for (var i = 0; i < result.data.length; i++) {
        		    	try {
        					dataId = result.data[i].id;
        			     	//appendImg = '<div id="img_append' + result.data[i].id + '"><div/>';
        			     	// loadingspan.attr("cid", data[i].id).removeAttr("loading");
        			     	//loadingspan1 += '<span class="pull-left pos-rel imgload" style="margin-right:10px;margin-top:10px;width:100px;height:70px" loading="true"><a href="javascript:;"  class="img-box"><img src="" class="imgloaddetail" width="100px" height="70px" style="border: 2px solid rgb(51, 122, 183);" /></a><a href="javascript:;" class="pos-abs fa fa-lg fa-close red" style="right:-5px;top:-5px;z-index: 99;"></a></span>';
        			
        			     	imgEle += '<span class="pull-left pos-rel" style="margin-right:10px;margin-top:10px" loading="true"><a href="javascript:;"  class="img-box"><img src="' + rootbase + result.data[i].fileUrl + '" class="imgloaddetail" width="100px" height="70px" style="border: 2px solid rgb(51, 122, 183);" /></a>'+
        			     	'<br><span>第' + result.data[i].pageNumber + '页</span><a href="javascript:;" id="' + result.data[i].id + '" class="pos-abs fa fa-lg fa-close red" style="right:-5px;top:-5px;z-index: 99;"></a></span>'
        				} catch(err) {
        					console.log(err);
        				} 
        		    }
        		    uploadNum = result.data.length;
        		    if(uploadNum > 0){
        		        //上传凭证后，不能再修改凭证名称
        		        $("#credent_name").attr("disabled",true);
        		    }else{
        		        $("#credent_name").attr("disabled",false);
        		    }
        		    $("#img_pics").html(imgEle);
        		    //$("#img_append" + dataId).append(loadingspan1);

        		    //setTimeout(function () {
        		    //    $("#img_append" + dataId).html(imgEle);
        		    //}, 250);
        		}else{
	                 
        		}
	        }
	        

	    });
}

/**
 * 删除选中的图片，并将剩余的图片显示到浏览器上
 */
function deleteImg(imgId,certid) {
    $.ajax({
        type: "POST",
        url: Context.PATH + '/order/query/deletePic.html',
        data: {
            imgId : imgId,
            certid : certid,
            orderByPageNumber:"orderByPageNumber"
        },

        success:function(re){
            if (re) {
                if (re.success) {
                    showImgs(re,"img_pics");
                }
                else {
                    cbms.alert("删除失败:"+re.data);
                }
            }
            else {
                cbms.alert("删除失败，服务器异常");
            }
        }
        ,
        error: function (re) {
            cbms.alert("删除失败，服务器异常");
        }
    });
}

/**
 * 将图片显示到浏览器上
 */
function showImgs(result,targetId) {
    var imgPre = document.getElementById(targetId);
    var htmlStr = '';
    var rootbase = Context.PATH + '/common/getfile.html?key=';
    for(var i=0;i<result.data.length;i++){
        htmlStr += '<span class="pull-left pos-rel" style="margin-right:10px;margin-top:10px"><a href="javascript:;" class="img-box"><img src="'+rootbase+result.data[i].fileUrl+'" alt="回单" width="100px" height="70px" style="border: 2px solid rgb(51, 122, 183);"/></a>';
        htmlStr += '<br><span>第' + result.data[i].pageNumber + '页</span>';
        htmlStr += '<a href="javascript:;" class="pos-abs fa fa-lg fa-close red" style="right:-5px;top:-5px;z-index: 99;" id="'+result.data[i].id+'"></a></span>';

    }
    uploadNum = result.data.length;
    if(uploadNum > 0){
        //只要有凭证，不能再修改凭证名称
        $("#credent_name").attr("disabled",true);
    }else{
        $("#credent_name").attr("disabled",false);
    }
    $(imgPre).html(htmlStr);
}
//提交审核
$("#submit-seller-certi").click(function(){
    if($("#printNum").val() == 0 && $("#printCodeNum").val() == 0){
        cbms.alert("请先打印凭证!");
        return;
    }
    if($(".img-box").length == 0){
        cbms.alert("请先上传凭证!");
        return;
    }

    $.ajax({
        type: "POST",
        url: Context.PATH + '/order/certificate/submitCert.html',
        data: {
            status : 'PENDING_APPROVAL',
            certid : $("#certid").val()
        },

        success:function(re){
            if (re) {
                if (re.success) {
                    cbms.alert(re.data);
                    //提交审核成功，就隐藏提交按钮
                    $("#printing").hide();
                    $(".edit").hide();
                    $("#submit-seller-certi").hide();
                    $("#print-seller-certi").hide();
                    $("#print-seller-code").hide();
                    $("#upload-hint").hide();
                    $(".browse-files").hide();
                    $("#pic").removeClass("inline").hide();
                    $(".fa-close").each(function(i){
                        var attamentId =  $(this).attr("id");
                        $("#" + attamentId).hide();
                    });
                    $("#print-code-info").hide();
                }
                else {
                    cbms.alert(re.data);
                }
            }
            else {
                cbms.alert("服务器异常");
            }
        }
        ,
        error: function (re) {
            cbms.alert("服务器异常");
        }
    });

});

//卖家审核通过
$("#audit-seller-certi").click(function(){
    $.ajax({
        type: "POST",
        url: Context.PATH + '/order/certificate/submitCert.html',
        data: {
            status : 'APPROVED',
            certid : $("#certid").val()
        },

        success:function(re){
            if (re) {
                if (re.success) {
                    cbms.alert(re.data);
                    $("#audit-seller-certi").hide();
                    $("#noaudit-seller-certi").hide();
                    $("#cancel-seller-certi").show();
                }
                else {
                    cbms.alert(re.data);
                }
            }
            else {
                cbms.alert("服务器异常");
            }
        }
        ,
        error: function (re) {
            cbms.alert("服务器异常");
        }
    });

});


//卖家审核不通过
//卖家审核不通过
$(document).on("click","#cert_pay_shut",function(){
	if (!setlistensSave("#cert_shut_editForm")) return;
	  $.ajax({
	        type: "POST",
	        url: Context.PATH + '/order/certificate/submitCert.html',
	        data: {
	            status : 'DISAPPROVE',
	            certid : $("#certid").val(),
	            cause:$("textarea[name='cause']").val()
	        },

	        success:function(re){
	            if (re) {
	            	 cbms.alert(re.data,function(){
                		 //跳转到风控管理列表
                         if (re.success) {
                             location.href = Context.PATH + "/order/certificate/checklist.html?isAudit=" + $("#isAudit").val();
                         }
                     });
	            }
	            else {
	                cbms.alert("服务器异常");
	            }
	        }
	        ,
	        error: function (re) {
	            cbms.alert("服务器异常");
	        }
	    });
});
$("#noaudit-seller-certi").click(function(){
          var html="<form id='cert_shut_editForm'><div class='dialog-m'><textarea name='cause' class='textarea' must='1' style='width:320px;height:100px;'placeholder='请填写关闭订单的理由！'></textarea><br>"+
              "<div class='btn-bar text-center'><button type='button'  class='btn btn-default btn-sm' id='window_close'>取消</button>&nbsp;"+
              "<button type='button'  class='btn btn-info btn-sm' id='cert_pay_shut'>确定</button></div> </div></form>";
          cbms.getDialog("不通过审核",html);
});


//卖家取消审核通过
$("#cancel-seller-certi").click(function(){
    $.ajax({
        type: "POST",
        url: Context.PATH + '/order/certificate/submitCert.html',
        data: {
            status : 'CANCEL',
            certid : $("#certid").val()
        },

        success:function(re){
            if (re) {
            	 cbms.alert(re.data,function(){
            		 //跳转到风控管理列表
                     if (re.success) {
                         location.href = Context.PATH + "/order/certificate/checklist.html?isAudit=" + $("#isAudit").val();
                     }
                 });
            }
            else {
                cbms.alert("服务器异常");
            }
        }
        ,
        error: function (re) {
            cbms.alert("服务器异常");
        }
    });

});

