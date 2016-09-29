

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

/**
 * 显示审核意见输入框
 * @param isShow
 */
function isShowAuditDesc(isShow,id){
	if(isShow){
		$("#auditDesc_"+id).css('display','');
		$("#disagree_"+id).attr('isSelect',true); 
	}else{
		$("#auditDesc_"+id).css('display','none');
		$("#auditDesc_"+id).val("");
		$("#disagree_"+id).attr('isSelect',false); 
	}
}

/**
 * 验证所有审核意见是否输入
 * @returns {Boolean}
 */
function validation(){
	var bankId ="";
	var auditDesc = "";
	var result = true;
	$('input[id*="disagree"]').each(function(){
		if($(this).attr("isSelect") == "true"){
			bankId = $(this).attr("bankId");
			var inputName ="#auditDesc_"+bankId;
			auditDesc = $.trim($(inputName).val());
			if(!auditDesc){
				 $(inputName).attr("title", "请输入不通过理由");
			     $(inputName).attr("data-original-title", "请输入不通过理由");
			     $(inputName).tooltip({
			         trigger: 'focus'
			     });
				$(inputName).focus();
				$(inputName).click();
				result = false;
				return;
			}
		}
	});
	return result;
}
/**
 * 审核
 * @returns {Boolean}
 */
function doAudit(){
	if(validation()){
		var data = $("#bankForm").serialize();
		cbms.confirm("确定提交审核结果吗？",null,function(){
			$.ajax({
				url: Context.PATH + "/flow/bank/doauditbank.html",
				type:"POST",
				data:data,
				success: function (result) {
	            	if (result.success) {
	            		cbms.gritter("审核成功");
	            		window.location.href =Context.PATH + "/flow/bank/list.html";
	            	}else{
	            		cbms.confirm("该客户打款资料已经被审核,请审核其他客户打垮资料",null,function(){
	            			window.location.href =Context.PATH + "/flow/bank/list.html";
	            		});
	            	}
				}
			});
		});
	}
}