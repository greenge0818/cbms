/**
 * @author lixiang
 * @date 201-12-29 09:55
 */
$(function(){
	$(".img-box").click(function () {
		var $img = $(this).next("img"), tit = $img.attr("alt");
		var src = $(this).find("img").attr("src");
		renderImg(src);
	});
	approve();
	decline();
});
	
// 通过
function approve(){
	var imgFlag = false;
	var accountId =$("#account_id").val();
	$("#approval").click(function(){
		cbms.confirm("确定通过审核吗?","",function(){
			cbms.loading();
			$("#picture img").each(function (i,e){
              var imgsrc = $(e).attr("src");
              if(imgsrc) {  			  
  				 imgFlag = true;
  			   }
	        });
			if (!imgFlag) {
            	cbms.closeLoading();
  				cbms.alert("请先上传打款资料或银行开户许可证！");
				return false;				
			}
			$.ajax({
				type : "POST",
				url : Context.PATH + "/order/query/account/" + accountId + "/approve.html",
				data : {
					
				},
				success : function(result) {
					cbms.closeLoading();
					if (result && result.success) {
						cbms.alert(result.data,function(){
							location = Context.PATH + "/order/query/bankcodeverify.html";
						});
					} else {
						cbms.alert("审核失败！");
					}
				},
				error : function(xhr, textStatus, errorThrown) {
					cbms.closeLoading();
				}
			});
		})
	});
}
// 不通过
function decline(){
	$("#decline").click(function() {
     var html="<form id='form-horizontal'><div class='dialog-m'><textarea  name='cause' class='textarea' must='1' style='width:320px;height:100px;' placeholder='请填写不通过 的理由！'></textarea><br>"+
        "<div class='btn-bar text-center'><button type='button'  class='btn btn-default btn-sm' id='btnClose'>取消</button>&nbsp;"+
        "<button type='button'  class='btn btn-info btn-sm' id='btncommit'>确定</button></div> </div></form>";
		cbms.getDialog("不通过审核", html);
	});
	$(document).on("click", "#btnClose",function () {
        cbms.closeDialog();
    });
	$(document).on('click', '#btncommit', function() {
		if(!setlistensSave("#form-horizontal")){
			return;		
		}
		var accountId =$("#account_id").val();
		$.ajax({
			type : "POST",
			url : Context.PATH +"/order/query/account/" + accountId + "/decline.html",
			data: {reason: $("textarea[name='cause']").val()},
			success : function(result) {
				cbms.closeLoading();
				if (result && result.success) {
					cbms.alert(result.data,function(){
						location = Context.PATH + "/order/query/bankcodeverify.html";
					});
				} else {
					if(result.data != null){
						cbms.alert(result.data);
					}else {
						cbms.alert("提交失败！");
					}
				}
			},
			error : function(xhr, textStatus, errorThrown) {
				cbms.closeLoading();
			}
		});
	});
}
