jQuery(function($) {
	$(".move,.remove").hide();
	$(".clear1,.clear2").hide();
	function huazhuan() {
		var duallist = $('#duallist').val();
		if(duallist==null){
			cbms.alert("请选择要划转的买家联系人！");
			return;
		}
		var managerExIds = "", nameExs = "", accountId = "", contactName = "", type = "";
		$("#duallist option:selected").each(function() {
			if (managerExIds == "") {
				managerExIds = $(this).attr("uid");// 前任ID
				nameExs = $(this).attr("nameex");// 前任名字
				accountId = $(this).attr("accountId");//客户ID
				contactName = $(this).attr("contactName");//联系人
				type = $(this).attr("type");//类型
			} else {
				managerExIds += "," + $(this).attr("uid");
				nameExs += "," + $(this).attr("nameex");
				accountId += "," + $(this).attr("accountId");
				contactName += "," + $(this).attr("contactName");
				type += "," + $(this).attr("type");
			}
		});
		var contactIds = $("#duallist").val().toString();// 联系人ID
		var uid = $("#userId").val(); // 现任ID
		var nameNext = $("#userId option:selected").attr("nameNext");// 现任名字
		if($.trim(uid)==""){
			cbms.alert("请选择要划转到的业务员！");
			return false;
		}
		var nameExStr = [];
		nameExStr = nameExs.split(",");
		for (var int = 0; int < nameExStr.length; int++) {
			var nameEx = nameExStr[int];
			if(nameEx==nameNext){
				cbms.alert("不能划转给自己！");
				return;
			}
		}
		function refreash(uid, nameNext) {
			$("#userId option").each(function() {
				$(this).val(nameNext);
			});
		}
		cbms.loading();
		$.ajax({
			type : "POST",
			url : Context.PATH + "/account/contact/updcontact.html",
			data : {
				"contactIds" : contactIds,
				"uid" : uid,
				"accountIds" : accountId,
				"managerExIds" : managerExIds,
				"nameExs" : nameExs,
				"contactName":contactName,
				"type":type,
				"nameNext" : nameNext
			},
			success : function(result) {
				cbms.closeLoading();
				if (result) {
					if (result.success) {
						cbms.alert("操作成功！",function(){
							location.reload(true);
						});
					} else {
						cbms.alert(result.data);
					}
				}
				
			},
			error : function(xhr, textStatus, errorThrown) {
				cbms.closeLoading();
			}
		});
	}

	$("#trans").click(function() {
		huazhuan();
	})
})
