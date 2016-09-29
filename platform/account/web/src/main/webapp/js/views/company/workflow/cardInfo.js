

$().ready(function () {
	$("#auditDesc").attr("title", "请输入不通过理由");
    $("#auditDesc").attr("data-original-title", "请输入不通过理由");
    $("#auditDesc").tooltip({
         trigger: 'focus'
    });
    $("#disagree").attr('select',false);
    //点击图片 放大
    $(".imgbigger").each(function () {
        $(this).closest("a[rel='lightbox']").attr("href", $(this).attr("src"));
    });
});

function showCardInfo(type){
	if(3== type){
		$("#threeCard").css('display','');
		$("#fiveCard").css('display','none');
	}else{
		$("#fiveCard").css('display','');
		$("#threeCard").css('display','none');
	}
}
/**
 * 显示审核意见输入框
 * @param isShow
 */
function isShowAuditDesc(isShow){
	if(isShow){
		$("#auditDesc").css('display','');
		$("#disagree").attr('select',true); 
	}else{
		$("#auditDesc").css('display','none');
		$("#auditDesc").val("");
		$("#disagree").attr('select',false); 
	}
}
/**
 * 审核
 * @returns {Boolean}
 */
function doAudit(accountId,extId){
	var cardInfoStatus = "Approved";
	var isSelect = $("#disagree").attr('select');
	var auditDesc = "";
	if(isSelect == "true"){
		cardInfoStatus = "Declined";
		var auditDesc = $.trim($("#auditDesc").val());
		if(!auditDesc){
			$("#auditDesc").focus();
			$("#auditDesc").click();
			return false;
		}
	}
	cbms.confirm("确定提交审核结果吗？",null,function(){
		$.ajax({
			url: Context.PATH + "/flow/cardinfo/doauditdoc.html",
			type:"POST",
			data:{
				"id":accountId,
				"extId":extId,
				"cardInfoStatus":cardInfoStatus,
				"disagreeDesc":auditDesc},
			success: function (result) {
            	if (result.success) {
            		cbms.gritter("审核成功");
            		window.location.href =Context.PATH + "/flow/cardinfo/list.html";
            	}else{
            		cbms.confirm("该客户证件资料已经被审核,请审核其他客户证件资料",null,function(){
            			window.location.href =Context.PATH + "/flow/cardinfo/list.html";
            		});
            	}
			}
		});
	});
}